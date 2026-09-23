FROM eclipse-temurin:21-jdk
RUN mkdir -p /opt/demo-academico
ENV TZ="America/Bogota"
ENV WORKSPACE /opt/demo-academico
WORKDIR ${WORKSPACE}
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-Xmx1024m","-jar","app.jar"]