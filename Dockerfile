FROM openjdk:latest
COPY target/Purchasing-0.0.0-A03.jar /usr/src/
WORKDIR /usr/src/
CMD ["java", "-jar", "Purchasing-0.0.0-A03.jar"]
