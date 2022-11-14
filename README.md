# mastodon-picture-bot
Small java app that publishes a randomly selected image to twitter<

# features
1. scan a given directory (and recursive all subdirectories) for image files and select one random image
2. post this image to twitter, uses file name as posted text
3. up to four images per tweet
4. some sort of intelligent text replacement (from "1991-10" in the filename it makes "10/1991" in the twitter post)
5. create empty marker file for used image (ends with ".used.real"), so this image will not be used again until you delete this marker file
6. optional: simulation mode to check what the app would do

# usage
1. (clone repository)
2. copy src/main/resources/twitter4j.properties.tpl to src/main/resources/twitter4j.properties and enter your twitter oauth things (get them here: https://developer.twitter.com/en/docs/authentication/oauth-1-0a/obtaining-user-access-tokens )
3. run "mvn install"
4. get the jar from the installation folder and copy it e.g. to app.jar
6. for simulation mode run: `java -jar app.jar [image directory]`
7. for real mode run: `java -jar app.jar [image directory] doit`

# use multiple image for one tweet
If you want to use more than one image in a tweet (e.g. if you have multiple files/pages for one thing) name them (up to four) like this:
1. myfile__1.jpg
2. myfile__2.jpg
3. myfile__3.jpg
4. myfile__4.jpg

# docker

I use docker to run the application:

`docker run -i --rm --name jdk17 -v /var/www/twitter-picture-bot:/twitter-picture-bot openjdk:17.0.2-oraclelinux8 java -jar /twitter-picture-bot/app.jar /twitter-picture-bot/images doit`

# about
This software was developed for usage at the [KultpowerBot](https://twitter.com/KultpowerBot "KultpowerBot")
  
  
