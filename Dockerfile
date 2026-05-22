FROM openjdk:27-ea-oraclelinux10

WORKDIR /expense-tracker-app

COPY target/expense-tracker.jar expense-tracker.jar   

ENTRYPOINT ["java", "-jar", "expense-tracker.jar"] 