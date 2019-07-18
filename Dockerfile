FROM maven:3.6-jdk-8 AS builder

ARG http_proxy
ARG https_proxy
ARG no_proxy
ARG MAVEN_OPTS

ARG DIST_DIR
ENV DIST_DIR=${DIST_DIR}
ENV REPO_URL="https://github.com/agora-pnds/france-connect-stub"

WORKDIR /src

RUN git clone ${REPO_URL} \
    && cd $(basename ${REPO_URL}) \
    && mvn clean install

FROM adoptopenjdk/openjdk8:alpine-slim

ENV HTTP_PORT=1234
ENV STUBS_FOLDER=/opt/stubs

RUN mkdir -p /opt/stubs
COPY --from=builder /src/*/target/*.jar /opt

CMD ["sh", "-c", "java -jar /opt/france-connect-stub.jar -httpPort=${HTTP_PORT} -Ddir.stub=${STUBS_FOLDER}"]
