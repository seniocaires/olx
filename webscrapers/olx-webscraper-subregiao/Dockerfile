FROM maven:3.5 AS builder

WORKDIR /build

ADD . .

RUN mvn clean install

FROM openjdk:8

ENV JAVA_HOME /usr/local/openjdk-8
ENV PATH $PATH:$JAVA_HOME/bin

WORKDIR /app

COPY --from=builder /build/target/olx-webscraper-subregiao-jar-with-dependencies.jar run.jar

ENTRYPOINT ["java", "-jar", "run.jar"]
