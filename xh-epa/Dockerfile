FROM java:8
MAINTAINER chenweichao <chenweichao@news.com>

ADD target/xh-epa-0.1.jar app.jar
RUN bash -c 'touch /app.jar'

ENTRYPOINT ["java","-jar","/app.jar"]