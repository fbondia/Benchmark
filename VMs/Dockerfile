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
WORKDIR /owasp/Benchmark

# https://bugscout.iast/bugscout/maven/io/bugscout/iast/jvm/bugscout-iast-agent/1.0.1-SNAPSHOT/
# bugscout-iast-agent-1.0.1-SNAPSHOT.jar

RUN mvn clean package cargo:run \
-Dbugscout.agent.host=https://bugscout.iast\
-Dbugscout.agent.version=1.0.1-SNAPSHOT \
-Dmaven.wagon.http.ssl.insecure=true \
-Dmaven.wagon.http.ssl.allowall=true \
-Dmaven.wagon.http.ssl.ignore.validity.dates=true \
-Pdownloadwbugscout

RUN mvn clean package cargo:run -Pdeploywbugscout \
-Dbugscout.agent.location=/owasp/Benchmark/tools/Bugscout/bugscout-iast-agent.jar \
-Dbugscout.url=https://appliance/bugscout \
-Dbugscout.source=FIRST-TEST \
-Dbugscout.projectKey=first-test \
-Dbugscout.username=elastic \
-Dbugscout.password=changeme \
-Dbugscout.rules=


RUN useradd -d /home/bench -m -s /bin/bash bench 
RUN echo bench:bench | chpasswd
RUN chown -R bench /owasp/
ENV PATH /owasp/Benchmark/:$PATH

