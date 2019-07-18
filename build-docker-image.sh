#!/usr/bin/env bash

set -x

WORKING_DIR=$(dirname "$0")

docker build \
    --build-arg http_proxy="${http_proxy}" \
    --build-arg https_proxy="${https_proxy}" \
    --build-arg no_proxy="${no_proxy}" \
    --build-arg MAVEN_OPTS="${MAVEN_OPTS}" \
    -t france-connect-stub \
    "${WORKING_DIR}"
