FROM openjdk:8u181-jdk-stretch
WORKDIR /development
RUN apt-get update && apt-get install -y jq

RUN apt-get install -y \
        apt-transport-https \
        ca-certificates \
        curl \
        gnupg2 \
        software-properties-common

RUN curl -fsSL https://download.docker.com/linux/debian/gpg | apt-key add -

RUN add-apt-repository \
       "deb [arch=amd64] https://download.docker.com/linux/debian \
       $(lsb_release -cs) \
       stable"

RUN apt-get update

RUN apt-get install -y docker-ce-cli

RUN curl -L "https://github.com/docker/compose/releases/download/1.23.2/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose \
   && chmod +x /usr/local/bin/docker-compose

RUN mkdir /tmp-src

COPY build /tmp-src

RUN cd /tmp-src ; ./gradlew testClasses
RUN rm -fr /tmp-src

CMD echo Ready ; sleep 260000
