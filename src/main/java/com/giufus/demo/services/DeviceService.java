package com.giufus.demo.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.giufus.demo.models.DeviceMessage;
import com.rabbitmq.stream.Message;
import com.rabbitmq.stream.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@Service
public class DeviceService {

    private static final Logger log = LoggerFactory.getLogger(DeviceService.class);

    private Producer producer;

    private ObjectMapper objectMapper;

    public DeviceService(@Qualifier("deviceProducer") Producer producer, ObjectMapper objectMapper) {
        this.producer = producer;
        this.objectMapper = objectMapper;
    }

    //@Async
    public void sendMessages(Collection<DeviceMessage> deviceMessage) throws JsonProcessingException, InterruptedException {

        long start = System.currentTimeMillis();

        int messageCount = deviceMessage.size();
        CountDownLatch confirmLatch = new CountDownLatch(messageCount);

        deviceMessage.stream()
                .forEach(dm -> {
                    String messageId = String.format("%s-%s", dm.deviceId(), UUID.randomUUID());
                    final String deviceMessageAsJson;
                    try {
                        deviceMessageAsJson = objectMapper.writeValueAsString(dm);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }

                    Message message = producer.messageBuilder()
                            .properties()
                            .creationTime(System.currentTimeMillis())
                            .messageId(messageId)
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
}
