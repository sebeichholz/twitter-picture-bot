package org.sebeichholz;

import java.io.File;
import java.util.Collection;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.UploadedMedia;

public final class App {

    public static void main(String[] args) throws TwitterException {
    	
    	
    	if (args.length==1 || args.length==2) {
    		
    		System.out.println(args[0]);
    		
    		if (new File(args[0]).isDirectory()) {
    			
    			Collection<File> jpgFiles = FileUtils.listFiles(new File(args[0]), new String[]{"jpg"}, true);
    			
    			//jpgFiles = jpgFiles.stream().filter(f -> f.getName().contains("14")).collect(Collectors.toList());
    			
//    			File dir = new File(args[0]);
//    			File[] jpgFiles = dir.listFiles(new FilenameFilter() {
//					@Override
//					public boolean accept(File dir, String name) {
//						return name.endsWith("jpg")
//								//&& name.contains("letzte")
//								;
//					}
//				});
    			//String filename = jpgFiles[new Random().nextInt(jpgFiles.length)].getAbsolutePath();

    			String filename = random(jpgFiles).getAbsolutePath();
    			
    			//System.out.println(filename);
    			
    			if (args.length==2 && "doit".equals(args[1])) {
    				postTweet(filename, true);
    			} else {
        			postTweet(filename, false);
    			}
    			
    		}
    	} 
    	else {
    		System.out.println();
    	}
    	

    }
    
    public static <T> T random(Collection<T> coll) {
        int num = (int) (Math.random() * coll.size());
        for(T t: coll) if (--num < 0) return t;
        throw new AssertionError();
    }    

	private static void postTweet(String filenameWithAbsolutePath, boolean doIt) throws TwitterException {
		
		System.out.println("Verzeichnis = " + filenameWithAbsolutePath);
		
		//String GERMAN_FLAG = "\uE50E";
		String RED_HEART = "\u2764"; //"\uE022";
		
        Twitter twitter = new TwitterFactory().getInstance();
	    File imagefile = new File(filenameWithAbsolutePath);
	    
	    String basename = FilenameUtils.getBaseName(filenameWithAbsolutePath);
	    
	    String regex = "(?<prefix>.*)(?<jahr>[0-9][0-9][0-9][0-9])-(?<monat>[0-9][0-9])(?<postfix>.*)";
	    if (basename.matches(regex)) {
	    	int monat = Integer.parseInt(basename.replaceAll(regex, "${monat}"));
	    	int jahr = Integer.parseInt(basename.replaceAll(regex, "${jahr}"));
	    	String prefix = basename.replaceAll(regex, "${prefix}");
	    	String postfix = basename.replaceAll(regex, "${postfix}");

	    	//System.out.println(basename.replaceAll(regex, "${jahr}-${monat}"));
	    	//System.out.println(basename.replaceAll(regex, monat + "/" + jahr));
	    	basename = prefix + basename.replaceAll(regex, monat + "/" + jahr + postfix);
	    }
	    
	    
	    String tweetText = basename + " " + RED_HEART ;
    	System.out.println("tweetText = " + tweetText);

	    if (doIt) {
		    long[] mediaIds = new long[1];
		    UploadedMedia media = twitter.uploadMedia(imagefile);
		    mediaIds[0] = media.getMediaId();
		    
			StatusUpdate statusUpdate = new StatusUpdate(tweetText
			//+ "\n\nKultpower.de" 
			//+ RED_HEART +"4" + GERMAN_FLAG + " mags"
			);
		    
		    statusUpdate.setMediaIds(mediaIds);
		    
			Status status = twitter.updateStatus(statusUpdate);
			System.out.println("Tweet wurde gepostet:  [" + status.getText() + "].");		
	    }
	    
	    else {
	    	System.out.println("SimulationModus - kein Tweet gepostet");
	    }

	
		
	}

//	private static void post1() throws TwitterException {
//		
//        //--------------------------------------------------
//        // getTweets example
//        //--------------------------------------------------
//        Twitter twitter = new TwitterFactory().getInstance();
//        final TweetsResponse tweets = GetTweetsKt.getTweets(twitter,
//                new long[]{1494714840060993543L},
//                null, null, null, null, null, "");
//        System.out.println("tweets = " + tweets);
//        
//        for (Tweet t: tweets.getTweets()) {
//        	System.out.println(t.getText());
//        }
//        
//	    String text       = "Test post Tweet V2 at " + LocalDateTime.now() + " #TwitterAPI";
//        Status updateStatus = twitter.updateStatus(text);
//        System.out.println(updateStatus.getId());
//
//
////        //--------------------------------------------------
////        // getUsers example
////        //--------------------------------------------------
////        final long twitterDesignId = 87532773L;
////        final UsersResponse users = GetUsersKt.getUsers(twitter, new long[]{twitterDesignId}, null, null, "");
////        System.out.println("users = " + users);
//        
//	}
	
	
}