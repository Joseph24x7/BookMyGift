FROM openjdk:17-oracle
EXPOSE 8081
ADD target/bookmyticket.jar bookmyticket.jar
ENTRYPOINT ["java","-jar","/bookmyticket.jar"]