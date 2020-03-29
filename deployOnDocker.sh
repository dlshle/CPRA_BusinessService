#!/bin/sh

mvn clean package

sudo docker build -t cpra_web:1.0 .

sudo docker run -d -p 8080:8080 -t cpra_web:1.0
