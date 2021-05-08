#!/usr/bin/env bash

# deleting old

docker container stop travel-with-me-server
docker container rm travel-with-me-server

docker image rm travel-with-me-jdbc

docker container stop travel-with-me-mysql
docker container rm travel-with-me-mysql

docker network rm travel-with-me

# creating

docker network create travel-with-me

docker container run --name travel-with-me-mysql \
                     --network travel-with-me \
                     -e MYSQL_ROOT_PASSWORD=forever18 \
                     -e MYSQL_DATABASE=travel_with_me \
                     -d mysql

sleep 1m

docker image build -t travel-with-me-jdbc .

sleep 1m

docker container run --network travel-with-me \
                     --name travel-with-me-server \
                     -p 9090:9090 \
                     -d travel-with-me-jdbc
