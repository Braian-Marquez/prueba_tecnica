
FROM braianm95/tsg-commons:1.0.0 AS build

WORKDIR /app

COPY . .


RUN mvn clean package -DskipTests

FROM openjdk:17-oracle

WORKDIR /app

COPY --from=build /app/target/posts-1.0.0.jar ./posts-1.0.0.jar


EXPOSE 8082


ENV JAVA_OPTS="-Djava.security.egd=file:/dev/./urandom -Djava.awt.headless=true"
    
CMD ["java","-jar","posts-1.0.0.jar"]
