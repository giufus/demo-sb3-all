package com.giufus.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.stream.Address;
import com.rabbitmq.stream.Environment;
import com.rabbitmq.stream.Producer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class Configurations {

    @Value("${custom.rabbitmq.stream.host}")
    private String rabbitStreamHost;

    @Value("${custom.rabbitmq.stream.port}")
    private Integer rabbitStreamPort;

    @Value("${custom.rabbitmq.queue.stream.person}")
    private String personQueue;

    @Value("${custom.rabbitmq.queue.stream.device}")
    private String deviceQueue;



    /*
    @Bean(name = "echoWebClient")
    WebClient produceEchoWebClient(WebClient webClient) {
        return WebClient.builder()
                .baseUrl(echoServerUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
    */

    @Bean
    Environment getRabbitEnvironment() {

        Address entryPoint = new Address(rabbitStreamHost, rabbitStreamPort);

        Environment environment = Environment.builder()
                .addressResolver(address -> entryPoint)
                .uri(String.format("rabbitmq-stream://%s:%d", rabbitStreamHost, rabbitStreamPort))
                .rpcTimeout(Duration.ofMinutes(1))
                .build();


        environment.streamCreator()
                .stream(personQueue).create();

        environment.streamCreator()
                .stream(deviceQueue).create();

        return environment;
    }

    @Bean("personProducer")
    Producer getPersonProducer(Environment environment) {
        return environment.producerBuilder()
                .stream(personQueue).build();
    }

    @Bean("deviceProducer")
    Producer getDeviceProducer(Environment environment) {
        return environment.producerBuilder()
                .stream(deviceQueue).build();
    }

    @Bean
    ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }

}
