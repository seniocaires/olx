FROM maven:3.5 AS builder

WORKDIR /build

ADD . .

RUN mvn clean install

FROM openjdk:8-jre

ENV JAVA_HOME /usr/local/openjdk-8
ENV WEBSCRAPER_HOME=/usr/local/webscraper/bin
ENV PATH $PATH:$JAVA_HOME/bin
ENV PATH $PATH:$WEBSCRAPER_HOME

WORKDIR ${WEBSCRAPER_HOME}

COPY --from=builder /build/target/regiao-jar-with-dependencies.jar regiao.jar
COPY --from=builder /build/target/subregiao-jar-with-dependencies.jar subregiao.jar
COPY --from=builder /build/target/categoria-jar-with-dependencies.jar categoria.jar
COPY --from=builder /build/target/subcategoria-jar-with-dependencies.jar subcategoria.jar

RUN echo '#!/bin/sh' > regiao && \
    echo 'java -jar regiao.jar' >> regiao && \
    chmod +x regiao && \
    echo '#!/bin/sh' > subregiao && \
    echo 'java -jar subregiao.jar' >> subregiao && \
    chmod +x subregiao && \
    echo '#!/bin/sh' > categoria && \
    echo 'java -jar categoria.jar' >> categoria && \
    chmod +x categoria && \
    echo '#!/bin/sh' > subcategoria && \
    echo 'java -jar subcategoria.jar' >> subcategoria && \
    chmod +x subcategoria
