FROM maven:3 as builder

ADD consumer/pom.xml pom.xml

RUN mvn dependency:go-offline

ADD consumer/src src
RUN mvn package

FROM java:8-alpine
COPY --from=builder target/kafka-consumer-jar-with-dependencies.jar kafka-consumer.jar
CMD ["java", "-jar", "kafka-consumer.jar"]