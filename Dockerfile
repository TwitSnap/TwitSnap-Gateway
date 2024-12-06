# Usar una imagen base de OpenJDK 21
FROM openjdk:21-jdk-slim AS build

# Establecer el directorio de trabajo
WORKDIR /app

# Copiar los archivos de Gradle, las envVars y el código fuente
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY newrelic .
#COPY .env .
COPY src src

# Dar permisos de ejecución al script gradlew
RUN chmod +x gradlew

# Construir la aplicación
RUN ./gradlew build -x test

# Crear una nueva imagen para el runtime
FROM openjdk:21-jdk-slim

# Establecer el directorio de trabajo
WORKDIR /app

# Copiar el jar generado desde la imagen de build, ademas del .env
COPY --from=build /app/build/libs/*.jar app.jar
#COPY --from=build /app/.env .env
COPY --from=build /app/newrelic /app/newrelic

# Comando para ejecutar la aplicación
ENTRYPOINT ["java","-javaagent:./newrelic/newrelic.jar" ,"-jar", "app.jar"]