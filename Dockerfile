FROM ghcr.io/graalvm/graalvm-ce:java11-21.0.0.2 AS builder
RUN gu install native-image

COPY . /home/app/hello-world-micronaut
WORKDIR /home/app/hello-world-micronaut

RUN native-image --no-server -cp target/hello-world-*.jar

FROM frolvlad/alpine-glibc:alpine-3.12
RUN apk update && apk add libstdc++

COPY --from=builder /home/app/hello-world-micronaut/hello-world-micronaut /app/hello-world-micronaut


EXPOSE 5000
ENTRYPOINT ["/app/hello-world-micronaut"]
