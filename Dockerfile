FROM openjdk:17-oracle
EXPOSE 8081
ADD target/bookmygift.jar bookmygift.jar
ENTRYPOINT ["java","-jar","/bookmygift.jar"]