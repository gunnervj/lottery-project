#! /bin/bash

: "${DOCKER_PASSWORD:?Need to set DOCKER_PASSWORD}"
: "${DOCKER_USERNAME:?Need to set DOCKER_USERNAME}"
: "${VERSION:?Need to set VERSION}"

echo "$DOCKER_PASSWORD" | docker login --username="$DOCKER_USERNAME" --password-stdin
docker build  ./ticket-service/ -t "$DOCKER_USERNAME"/ticket-service:latest
docker build  ./printer-service/ -t "$DOCKER_USERNAME"/printer-service:latest
docker build  ./lottery-service/ -t "$DOCKER_USERNAME"/lottery-service:latest
docker tag  "$DOCKER_USERNAME"/ticket-service:latest "$DOCKER_USERNAME"/ticket-service:"$VERSION"
docker tag  "$DOCKER_USERNAME"/printer-service:latest "$DOCKER_USERNAME"/printer-service:"$VERSION"
docker tag  "$DOCKER_USERNAME"/ticket-service:latest "$DOCKER_USERNAME"/lottery-service:"$VERSION"
docker push "$DOCKER_USERNAME"/ticket-service:"$VERSION"
docker push "$DOCKER_USERNAME"/printer-service:"$VERSION"
docker push "$DOCKER_USERNAME"/lottery-service:"$VERSION"
docker push "$DOCKER_USERNAME"/ticket-service:latest
docker push "$DOCKER_USERNAME"/printer-service:latest
docker push "$DOCKER_USERNAME"/lottery-service:latest