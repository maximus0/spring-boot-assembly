FROM java:8
MAINTAINER chenweichao <chenweichao@news.com>

ADD target/xh-cms-pusher-0.1.jar app.jar
ADD defaultImg.jpg defaultImg.jpg
RUN bash -c 'touch /app.jar'

ENTRYPOINT ["java","-jar","/app.jar"]