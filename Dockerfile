FROM adoptopenjdk/openjdk11:alpine-jre
ADD /build/libs/NewsManagement-0.0.1-SNAPSHOT.jar app.jar
CMD java -jar ./app.jar