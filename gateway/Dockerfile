# Usa una imagen base oficial de OpenJDK
FROM openjdk:17-oracle

# Define el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el archivo JAR del servicio al contenedor
COPY target/gateway-1.0.0.jar app.jar

# Expone el puerto en el que el servicio escucha (por defecto, 8080 para Spring Boot)
EXPOSE 8080
ENV CONSUL_HOST=consul
ENV CONSUL_PORT=8500
# Define el comando para ejecutar la aplicación
CMD ["java", "-jar", "app.jar"]
