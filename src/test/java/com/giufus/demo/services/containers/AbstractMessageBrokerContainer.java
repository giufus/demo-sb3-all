package com.giufus.demo.services.containers;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;

import java.time.Duration;

public interface AbstractMessageBrokerContainer {

    Integer[] RABBITMQ_EXPOSED_PORTS = new Integer[] {5552, 5672, 15672};

    @Container
    GenericContainer rabbitMqContainer = new GenericContainer(
            new ImageFromDockerfile()
                    .withDockerfileFromBuilder(builder ->
                            builder
                                    .from("rabbitmq:3.12.0-beta.6-management")
                                    .env("RABBITMQ_SERVER_ADDITIONAL_ERL_ARGS", "-rabbitmq_stream advertised_host localhost")
                                    //.run("echo stream.listeners.tcp.1 = 5552 >> /etc/rabbitmq/conf.d/10-defaults.conf")
                                    .run("rabbitmq-plugins enable rabbitmq_stream")
                                    .run("rabbitmq-plugins enable rabbitmq_stream_management")
                                    .build()))
            .withExposedPorts(RABBITMQ_EXPOSED_PORTS)
            .withStartupTimeout(Duration.ofMinutes(3));
}
