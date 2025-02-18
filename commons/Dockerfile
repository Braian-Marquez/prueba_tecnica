

FROM maven:3.8.3-openjdk-17-slim AS build

WORKDIR /app


COPY . .

RUN mvn clean package -DskipTests

FROM maven:3.8.3-openjdk-17-slim

WORKDIR /app

COPY --from=build /app/target/commons-1.0.0.jar ./commons-1.0.0.jar

RUN mvn install:install-file -Dfile=./commons-1.0.0.jar -DgroupId=com.tsg -DartifactId=commons -Dversion=1.0.0 -Dpackaging=jar