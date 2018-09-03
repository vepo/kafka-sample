FROM ubuntu:18.04 as downloader
RUN  apt-get update \
    && apt-get install -y wget

RUN wget http://ftp.unicamp.br/pub/apache/kafka/2.0.0/kafka_2.11-2.0.0.tgz

RUN tar -xvzf kafka_2.11-2.0.0.tgz
RUN rm -rf kafka_2.11-2.0.0/site-docs kafka_2.11-2.0.0/bin/windows
ADD ./dockerfiles/kafka/start-kafka.sh kafka_2.11-2.0.0/start-kafka.sh
RUN chmod a+x kafka_2.11-2.0.0/start-kafka.sh

FROM java:8
COPY --from=downloader kafka_2.11-2.0.0 kafka
CMD ["./kafka/start-kafka.sh"]