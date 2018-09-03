FROM ubuntu:18.04 as downloader
RUN  apt-get update \
    && apt-get install -y wget

RUN wget http://ftp.unicamp.br/pub/apache/zookeeper/current/zookeeper-3.4.12.tar.gz

RUN tar -xvzf zookeeper-3.4.12.tar.gz
RUN rm -rf zookeeper-3.4.12/src zookeeper-3.4.12/docs 
RUN cp zookeeper-3.4.12/conf/zoo_sample.cfg zookeeper-3.4.12/conf/zoo.cfg


FROM java:8
COPY --from=downloader zookeeper-3.4.12 zookeeper
CMD ["./zookeeper/bin/zkServer.sh", "start-foreground"]