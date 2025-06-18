rm ./JavaIntroductoryTask.jar
javac -d out ./src/main/java/com/gehtsoft/training/*.java
jar cfm JavaIntroductoryTask.jar ./out/MANIFEST.MF -C out .
