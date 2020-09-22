FROM ubuntu:latest

RUN apt-get update
RUN DEBIAN_FRONTEND="noninteractive" apt-get -y install tzdata
RUN apt-get install -q -y \
     openjdk-8-jre-headless \
     openjdk-8-jdk \
     git \
     maven \
     wget \
     iputils-ping \
     && apt-get clean

RUN mkdir /owasp
WORKDIR /owasp
RUN git clone https://github.com/fbondia/Benchmark
WORKDIR /owasp/benchmark

#RUN mvn clean package cargo:install
#
#RUN useradd -d /home/bench -m -s /bin/bash bench 
#RUN echo bench:bench | chpasswd
#
#RUN chown -R bench /owasp/
#ENV PATH /owasp/benchmark/:$PATH

RUN mvn clean package cargo:run \
-Dmaven.wagon.http.ssl.insecure=true \
-Dmaven.wagon.http.ssl.allowall=true \
-Dmaven.wagon.http.ssl.ignore.validity.dates=true \
-Pdeploywbugscout
