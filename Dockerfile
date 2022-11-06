FROM openjdk:8-alpine

RUN apk add --no-cache maven

COPY . /usr/src/unobot
WORKDIR /usr/src/unobot
RUN mvn package
CMD java -jar ./target/UnoBot-1.0-SNAPSHOT-jar-with-dependencies.jar
