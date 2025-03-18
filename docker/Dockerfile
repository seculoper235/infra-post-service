FROM amazoncorretto:17.0.13

WORKDIR /app

COPY build/libs ./

ENV DB_URL "192.168.0.14"
ENV DB_USER "postteller"
ENV DB_PASS "post123!"

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "infra-post-service-0.0.1-SNAPSHOT.jar"]
