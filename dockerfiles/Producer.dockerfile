FROM maven:3 as builder

ADD producer/pom.xml pom.xml

RUN mvn dependency:go-offline

ADD producer/src src
RUN mvn package

FROM java:8-alpine
COPY --from=builder target/kafka-producer-jar-with-dependencies.jar kafka-producer.jar
CMD ["java", "-jar", "kafka-producer.jar"]