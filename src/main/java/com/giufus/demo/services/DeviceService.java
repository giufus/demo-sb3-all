package com.giufus.demo.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.giufus.demo.Configurations;
import com.giufus.demo.models.DeviceMessage;
import com.rabbitmq.stream.*;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class DeviceService {

    private static final Logger log = LoggerFactory.getLogger(DeviceService.class);

    private Producer producer;
    private Environment environment;
    private ObjectMapper objectMapper;

    public DeviceService(@Qualifier("deviceProducer") Producer producer, Environment environment, ObjectMapper objectMapper) {
        this.producer = producer;
        this.environment = environment;
        this.objectMapper = objectMapper;
    }

    //@Async
    public void sendMessages(Collection<DeviceMessage> deviceMessage) throws JsonProcessingException, InterruptedException {

        long start = System.currentTimeMillis();

        int messageCount = deviceMessage.size();
        CountDownLatch confirmLatch = new CountDownLatch(messageCount);

        deviceMessage.stream()
                .forEach(dm -> {

                    final String deviceMessageAsJson;
                    try {
                        deviceMessageAsJson = objectMapper.writeValueAsString(dm);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }

                    Message message = producer.messageBuilder()
                            .properties()
                                .creationTime(System.currentTimeMillis())
                                .messageId(String.format("%s-%s", dm.deviceId(), UUID.randomUUID()))
                                .correlationId(String.format("%s-%s", dm.deviceId(), UUID.randomUUID()))
                            .messageBuilder()
                            .addData(deviceMessageAsJson.getBytes(StandardCharsets.UTF_8))
                            .build();

                    producer.send(message, confirmationStatus -> confirmLatch.countDown());

                });

        log.info("{} messages sent, waiting for confirmation...", messageCount);
        boolean done = confirmLatch.await(1, TimeUnit.MINUTES);

        log.info("Message confirmed? {} ({} ms)",
                done ? "yes" : "no",
                (System.currentTimeMillis() - start)
        );
    }

    public int consumeMessages(@NotNull String queue, Integer consumerTime) {

        String streamName = Configurations.appendDateString(queue);
        AtomicInteger count = new AtomicInteger(0);
        Consumer consumer = environment.consumerBuilder()
                // enable if you want offset tracking
                //.name("consumer-" + streamName)
                .stream(streamName)
                // not needed if you want offset tracking
                .offset(OffsetSpecification.first())
                .messageHandler((context, message) -> {
                    try {
                        log.info("Message consumed {} - offset is {}  - timestamp is {}",
                                    new String(message.getBodyAsBinary()), context.offset(), context.timestamp()
                        );
                        count.addAndGet(1);
                    } catch (Throwable ex) {
                        log.error("error reading msg {}", ex);
                    }
                })
                .build();

        try {
            Thread.currentThread().sleep(consumerTime);
            consumer.close();
        } catch (Exception e) {
            log.error("{}", e);
        }

        return count.get();

    }
}
