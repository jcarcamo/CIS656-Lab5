# LAB 5 openchord Chat

## Objective

The objective of this project is to give the student hands-on experience building a decentralized peer-to-peer application using distributed hash tables (DHT).  

## Setup

Include the openchord library to your local maven repository. [How to include custom library into maven local repository?](https://www.mkyong.com/maven/how-to-include-library-manully-into-maven-local-repository/)

```bash
mvn install:install-file -Dfile=lib/openchord_1.0.4.jar -DgroupId=de.uniba.wiai.lspi -DartifactId=openchord -Dversion=1.0.4 -Dpackaging=jar
```

To build the program issue the following maven command

```mvn clean compile assembly:single```

## Running the program

To run the master after building enter the following commands:

```bash
cd target
java -jar openchord-chat-client-1.0-SNAPSHOT-jar-with-dependencies.jar -master master localhost
```

To run a chat client after building enter the following commands:

```bash
cd target
java -jar openchord-chat-client-1.0-SNAPSHOT-jar-with-dependencies.jar -notmaster username localhost
```