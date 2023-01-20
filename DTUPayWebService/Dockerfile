# @Author Jákup Viljam Dam - s185095
FROM adoptopenjdk:11-jre-hotspot
WORKDIR /usr/src
COPY target/quarkus-app /usr/src/quarkus-app
CMD java -Xmx64m -jar quarkus-app/quarkus-run.jar