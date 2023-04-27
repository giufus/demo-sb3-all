package com.giufus.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.stream.Address;
import com.rabbitmq.stream.Environment;
import com.rabbitmq.stream.Producer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.datetime.DateFormatter;

import java.time.Duration;
import java.util.Date;
import java.util.Locale;

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

    private static final DateFormatter dateFormatter = new DateFormatter("yyyyMMdd");

    @Bean
    Environment getRabbitEnvironment() {

        Address entryPoint = new Address(rabbitStreamHost, rabbitStreamPort);

        Environment environment = Environment.builder()
                .addressResolver(address -> entryPoint)
                .uri(String.format("rabbitmq-stream://%s:%d", rabbitStreamHost, rabbitStreamPort))
                .rpcTimeout(Duration.ofMinutes(1))
                .build();

        environment.streamCreator()
                .stream(appendDateString(personQueue))
                .maxAge(Duration.ofHours(8))
                .create();

        environment.streamCreator()
                .stream(appendDateString(deviceQueue))
                .maxAge(Duration.ofHours(8))
                .create();

        return environment;
    }

    @Bean("personProducer")
    Producer getPersonProducer(Environment environment) {

        return environment.producerBuilder()
                // enable if you need deduplication, pass publishingId when building message
                //.name("personProducer")
                .stream(appendDateString(personQueue)).build();
    }

    @Bean("deviceProducer")
    Producer getDeviceProducer(Environment environment) {

        return environment.producerBuilder()
                // enable if you need deduplication, pass publishingId when building message
                //.name("deviceProducer")
                .stream(appendDateString(deviceQueue)).build();
    }

    @Bean
    ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }

    public static String appendDateString(String prefix) {
        return String.format("%s-%s", prefix, dateFormatter.print(new Date(), Locale.ENGLISH));
    }
}
