FROM ubuntu:latest

MAINTAINER Xuri Li, xli892@wisc.edu

RUN mkdir -p /home/comment_filter

WORKDIR /home/comment_filter

RUN apt-get update && apt-get install -y \
    python3

RUN apt-get install -y python3-pip

COPY dependencies.txt ./

RUN pip3 install -r dependencies.txt

COPY . .

ENTRYPOINT ["python3", "comment_filtering_service.py"]
