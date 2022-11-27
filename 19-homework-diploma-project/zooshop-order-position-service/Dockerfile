FROM adoptopenjdk/openjdk11:jdk-11.0.11_9-alpine as builder

WORKDIR /opt/app

COPY . .

RUN ./mvnw clean verify

FROM adoptopenjdk/openjdk11:jre-11.0.11_9-alpine

ENV PORT 8080
ENV ENVIRONMENT_NAME=""

WORKDIR /opt/app

COPY --from=builder /opt/app/target/*.jar ./

EXPOSE $PORT

CMD ["sh", "-c", "find -type f -name '*.jar' | env spring_profiles_active=$ENVIRONMENT_NAME xargs java -jar -Dserver.port=${PORT}"]
