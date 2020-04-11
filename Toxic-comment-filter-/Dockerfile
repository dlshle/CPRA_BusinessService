FROM ubuntu:latest

MAINTAINER Xuri Li, xli892@wisc.edu

RUN apt-get update && apt-get install -y \
    python-pip

COPY dependencies.txt ./

RUN pip install -r dependencies.txt

COPY . .

ENTRYPOINT ["python3", "comment_filtering_service.py"]
