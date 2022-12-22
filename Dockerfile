FROM openjdk:17-oracle
EXPOSE 8080
ADD target/bookmyticket.jar bookmyticket.jar
ENTRYPOINT ["java","-jar","/bookmyticket.jar"]