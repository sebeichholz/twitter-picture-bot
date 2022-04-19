package org.sebeichholz;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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

	//private static final String GERMAN_FLAG = "\uE50E";
	private static final String RED_HEART = "\u2764"; //"\uE022";
	
    public static void main(String[] args) throws TwitterException {
    	
    	
    	if (args.length==1 || args.length==2) {
    		
    		System.out.println(args[0]);
    		
    		if (new File(args[0]).isDirectory()) {
    			
    			Collection<File> jpgFiles = FileUtils.listFiles(new File(args[0]), new String[]{"jpg"}, true);
    			
    			Collection<File> removeList = new ArrayList<File>();
    			
    			for (File f: jpgFiles) {
    				if (f.getAbsolutePath().endsWith("__2.jpg")
    						|| f.getAbsolutePath().endsWith("__3.jpg")
    						|| f.getAbsolutePath().endsWith("__4.jpg")
    						) {
    					removeList.add(f);
    				}
    			}
    			
    			if (!removeList.isEmpty()) {
    				//System.out.println("Anzahl Dateien bisher: " + jpgFiles.size());
    				jpgFiles.removeAll(removeList);
    				//System.out.println("Anzahl Dateien jetzt: " + jpgFiles.size());
    			}
    			
//    			String filter = null;
//    			//filter = "Import";
//    			
//    			final String filter2 = filter;
//    			if (filter2!=null) {
//    				jpgFiles = jpgFiles.stream().filter(f -> f.getName().contains(filter2)).collect(Collectors.toList());
//    			}
    					
    			
    			String filename = random(jpgFiles).getAbsolutePath();
    			
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
		
        Twitter twitter = new TwitterFactory().getInstance();

		System.out.println("filename = " + filenameWithAbsolutePath);
		
		List<String> filenamesToUse = new ArrayList<String>();

		
		if (filenameWithAbsolutePath.endsWith("__1.jpg") 
			|| filenameWithAbsolutePath.endsWith("__2.jpg")
			|| filenameWithAbsolutePath.endsWith("__3.jpg")
			|| filenameWithAbsolutePath.endsWith("__4.jpg")
		) {
			
			String filenameWithoutNumber = filenameWithAbsolutePath.substring(0, filenameWithAbsolutePath.length()-4);
			System.out.println(filenameWithoutNumber);
			
			String filenameWithAbsolutePathWithoutNumber = filenameWithAbsolutePath
    			.replace("__1.jpg", "")
    			.replace("__2.jpg", "")
    			.replace("__3.jpg", "")
    			.replace("__4.jpg", "");
    		
			for (int i=1; i<=4; i++) {
				String a = filenameWithAbsolutePathWithoutNumber + "__" + i + ".jpg";
				if (new File(a).exists()) {
					filenamesToUse.add(new File(a).getAbsolutePath());
				}
			}
			
		} 
		
		else {
			filenamesToUse.add(filenameWithAbsolutePath);
		}
		
		
		
		
		
	    String basename = FilenameUtils.getBaseName(filenamesToUse.get(0)
	    		.replace("__1.jpg", ".jpg")
	    		.replace("__2.jpg", ".jpg")
	    		.replace("__3.jpg", ".jpg")
	    		.replace("__4.jpg", ".jpg")
	    		);
	    
	    
	    basename = basename.replace("_", " ");
	    
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

//	    if (doIt) {
	    	
		    long[] mediaIds = new long[filenamesToUse.size()];
		    
		    for (int filenummer = 0; filenummer < filenamesToUse.size(); filenummer++) {
		    	File imagefile = new File(filenamesToUse.get(filenummer));
		    	if (doIt) {
		    		System.out.println("Lade Datei hoch: " + FilenameUtils.getBaseName(imagefile.getAbsolutePath()));
		    		UploadedMedia media = twitter.uploadMedia(imagefile);
		    		mediaIds[filenummer] = media.getMediaId();
		    	} else {
		    		System.out.println("SIMULIERE Lade Datei hoch: "
		    				+ imagefile.getAbsolutePath()
		    				//+ FilenameUtils.getBaseName(imagefile.getAbsolutePath())
		    				);
		    	}
		    }
		    
	    	System.out.println("tweetText = " + tweetText);
		    
		    
			StatusUpdate statusUpdate = new StatusUpdate(tweetText
			//+ "\n\nKultpower.de" 
			//+ RED_HEART +"4" + GERMAN_FLAG + " mags"
			);
		    
		    statusUpdate.setMediaIds(mediaIds);
		    
		    if (doIt) {
				Status status = twitter.updateStatus(statusUpdate);
				System.out.println("Tweet wurde gepostet:  [" + status.getText() + "].");
		    }
		    else {
		    	System.out.println("SimulationModus - kein Tweet gepostet");
		    }
		    
//	    }
//	    else {
//	    	System.out.println("SimulationModus - kein Tweet gepostet");
//	    }

	
		
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