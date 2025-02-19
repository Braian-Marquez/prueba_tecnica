# 📌 API REST con Microservicios, Spring Boot y JWT

Esta API permite gestionar usuarios y posts mediante una arquitectura de **microservicios**, con autenticación basada en **JWT** y control de acceso. Se utiliza **Consul** para el registro de servicios y **Spring Cloud Gateway** como puerta de enlace de la API.  

## 🚀 Tecnologías Utilizadas

- **Java 17**
- **Spring Boot 3.1.2**
- **Spring Security + JWT** (Autenticación)
- **Spring Cloud Gateway** (Gestión de tráfico y seguridad)
- **Spring Cloud Consul** (Registro y descubrimiento de servicios)
- **JPA + Hibernate** (Persistencia)
- **PostgreSQL** (Base de datos)
- **Lombok** (Para reducir código boilerplate)
- **Docker & Portainer** (Gestión de contenedores)
- **AWS EC2** (Infraestructura en la nube)
- **Nginx** (Proxy inverso)
- **Postman** (Pruebas y documentación)

---

## 📂 Arquitectura del Proyecto

Este sistema sigue un enfoque basado en **microservicios**, donde cada módulo cumple una función específica.

```plaintext
├── tsg-gateway (Spring Cloud Gateway)
├── tsg-auth (Gestión de autenticación y JWT)
├── tsg-posts (Gestión de posts)
├── Consul (Registro de servicios y configuración dinámica)
└── Nginx (Proxy inverso)
```
## 🛠️ Configuración de la Base de Datos

El sistema usa PostgreSQL como base de datos. Se recomienda configurar las credenciales en un archivo .env o gestionarlas de manera segura en AWS.

# Configuración de la base de datos (en application.properties o .env)

```properties
# Configuración de la base de datos (en application.properties o .env)
spring.datasource.url=jdbc:postgresql://<IP_PRIVADA>:5432/tsg
spring.datasource.username=postgres
spring.datasource.password=<TU_PASSWORD>
```

## 🔄 Pasos para Iniciar el Proyecto

### 1️⃣ Clonar el repositorio

```bash
git clone <URL_DEL_REPO>
cd <NOMBRE_DEL_PROYECTO>
```

## 1️⃣ Levantar Consul
Consul se ejecuta en el puerto 8500, pero NO debe ser expuesto al público.
Se debe crear una red en Docker llamada app-network
```yaml
services:
  consul:
    image: hashicorp/consul:latest
    container_name: consul
    command: agent -server -bootstrap-expect=1 -client=0.0.0.0 -ui
    ports:
      - "8500:8500"
      - "8600:8600/udp"
    environment:
      - CONSUL_BIND_INTERFACE=eth0
    volumes:
      - consul-data:/consul/data
    networks:
      - app-network

volumes:
  consul-data:
    driver: local

networks:
  app-network:
    external: true
```
## 2️⃣ Agregar la Configuración en Consul
Para que los servicios tsg-auth y tsg-posts obtengan su configuración desde Consul, debes crear las claves en el KV Store.

## 📌 Registrar Configuración en Consul
Ejecuta los siguientes comandos para registrar la configuración en Consul KV:

```yaml
consul kv put config/tsg-auth/data '
spring:
  application:
    name: tsg-auth
  datasource:
    url: jdbc:postgresql://52.207.27.199:5432/tsg
    username: postgres
    password: Pa55w0rd
    driver-class-name: org.postgresql.Driver
  jpa:
    database-platform: org.hibernate.dialect.PostgreSQLDialect
    hibernate:
      ddl-auto: update
auth:
  security:
    SECRET_KEY: super-secret-key
'
```

```yaml
consul kv put config/tsg-posts/data '
spring:
  application:
    name: tsg-posts
  datasource:
    url: jdbc:postgresql://52.207.27.199:5432/tsg
    username: postgres
    password: Pa55w0rd
    driver-class-name: org.postgresql.Driver
  jpa:
    database-platform: org.hibernate.dialect.PostgreSQLDialect
    hibernate:
      ddl-auto: update
'
```


## 2️⃣ Levantar los Microservicios (Estos pasos no son necesarios para correr 
el pryecto mas que nada es a nivel informativo de como esta funcionando actualmente la arquitectura)

```yaml
services:

  tsg-auth:
    image: braianm95/tsg-auth
    container_name: tsg-auth
    environment:
      - CONSUL_HOST=consul
      - CONSUL_PORT=8500
    depends_on:
      - tsg-gateway
    networks:
      - app-network

  tsg-posts:
    image: braianm95/tsg-posts
    container_name: tsg-posts
    environment:
      - CONSUL_HOST=consul
      - CONSUL_PORT=8500
    depends_on:
      - tsg-gateway
    networks:
      - app-network
      
  tsg-gateway:
    image: braianm95/tsg-gateway
    container_name: tsg-gateway
    environment:
      - CONSUL_HOST=consul
      - CONSUL_PORT=8500
    networks:
      - app-network

networks:
  app-network:
    external: true
```

## Pruebas Unitarias con Mockito

He implementado pruebas unitarias en los proyectos `tsg-auth` y `tsg-posts` utilizando [Mockito](https://site.mockito.org/), un framework de simulación para pruebas en Java. Mockito nos permite crear objetos simulados (mocks) y definir su comportamiento, facilitando la verificación de la lógica de negocio de nuestras aplicaciones de manera aislada.

### Configuración de Dependencias

Para incorporar Mockito en nuestros proyectos, añadimos la siguiente dependencia al archivo `pom.xml`:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>




