# twitter-picture-bot
Small java app that publishes a randomly selected image to twitter

# features
1. scan a given directory (and recursive all subdirectories) for image files and select one random image
2. post this image to twitter
3. create empty marker file for used image (ends with ".used.real"), so this image will not be used again until you delete this marker file
4. optional: simulation mode to check what the app would do

# usage
1. (clone repository)
2. copy src/main/resources/twitter4j.properties.tpl to src/main/resources/twitter4j.properties and enter your twitter oauth things
3. run "mvn install"
4. get the jar from the installation folder and copy it e.g. to app.jar
6. for simulation mode run: `java -jar [image directory]`
7. for real mode run: `java -jar [image directory] doit`


I use docker to run the application:

`docker run -i --rm --name jdk17 -v /var/www/twitter-picture-bot:/twitter-picture-bot openjdk:17.0.2-oraclelinux8 java -jar /twitter-picture-bot/app.jar /twitter-picture-bot/images doit`
  
  
