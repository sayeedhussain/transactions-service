# Digest for nonroot tag
FROM gcr.io/distroless/java21@sha256:0ba7333a0fb7cbcf90b18073132f4f445870608e8cdc179664f10ce4d00964c2
EXPOSE 8080

WORKDIR /app

COPY ./build/libs/*.jar test-pyramid.jar

ADD --chmod=444 https://dtdg.co/java-tracer-v1 dd-java-agent.jar

CMD ["test-pyramid.jar"]
