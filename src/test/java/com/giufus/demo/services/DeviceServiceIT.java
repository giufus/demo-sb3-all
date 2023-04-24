package com.giufus.demo.services;

import com.giufus.demo.models.DeviceMessage;
import com.giufus.demo.services.containers.AbstractMessageBrokerContainer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Arrays;
import java.util.Collection;

@Testcontainers
@SpringBootTest
@ActiveProfiles("it")
@DirtiesContext
class DeviceServiceIT implements AbstractMessageBrokerContainer {

    @Autowired
    private DeviceService deviceService;


    @BeforeAll
    static void setUp() {
        rabbitMqContainer.withReuse(true);
    }

    @AfterAll
    static void afterAll() {
    }

    @DynamicPropertySource
    public static void overrideProperties(DynamicPropertyRegistry registry) {
        Integer mappedPort = rabbitMqContainer.getMappedPort(5552);
        registry.add("custom.rabbitmq.stream.port", () -> mappedPort);

        // disable db init, h2, not needed in this test
        registry.add("spring.sql.init.mode", () -> "never");
    }

    @Test
    void testSendMessagesDoesNotThrow() {

        Collection<DeviceMessage> deviceMessages = Arrays.asList(
                new DeviceMessage("sampleDevice1", 1.11D, 2.22D, 3.33D)
        );

        Assertions.assertDoesNotThrow(() -> deviceService.sendMessages(deviceMessages));
    }


}
