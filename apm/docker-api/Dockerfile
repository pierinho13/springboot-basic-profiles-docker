FROM eclipse-temurin:17-jre

WORKDIR /app

COPY apm-examples-api/target/apm-example-0.0.1.jar app.jar

ADD https://repo1.maven.org/maven2/co/elastic/apm/elastic-apm-agent/1.50.0/elastic-apm-agent-1.50.0.jar \
    /opt/elastic/elastic-apm-agent.jar

ENV JAVA_TOOL_OPTIONS="-javaagent:/opt/elastic/elastic-apm-agent.jar"

ENTRYPOINT ["java", "-jar", "app.jar"]
