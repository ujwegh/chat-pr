FROM openjdk:11.0.7-jdk
ADD target/chat-pr-0.0.1-SNAPSHOT.jar chat-pr.jar
ENTRYPOINT ["java", "-jar", "/chat-pr.jar"]