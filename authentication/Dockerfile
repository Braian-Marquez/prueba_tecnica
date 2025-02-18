
FROM braianm95/tsg-commons:1.0.0 AS build

WORKDIR /app

COPY . .


RUN mvn clean package -DskipTests

FROM openjdk:17-oracle

WORKDIR /app

COPY --from=build /app/target/authentication-1.0.0.jar ./authentication-1.0.0.jar


EXPOSE 8081

# Define las variables de entorno necesarias
ENV JAVA_OPTS="-Djava.security.egd=file:/dev/./urandom -Djava.awt.headless=true"
    
CMD ["java","-jar","authentication-1.0.0.jar"]
