package com.giufus.demo.services;

import com.giufus.demo.models.WebPerson;
import com.giufus.demo.services.containers.AbstractDatasourceContainer;
import com.giufus.demo.services.containers.AbstractMessageBrokerContainer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.TransactionSystemException;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Collection;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
@ActiveProfiles("it")
@DirtiesContext
class PersonServiceIT implements AbstractMessageBrokerContainer, AbstractDatasourceContainer {

    @Autowired
    PersonService personService;

    @BeforeAll
    static void setUp() {
        rabbitMqContainer.withReuse(true);
        postgresContainer.withReuse(true);
    }

    @AfterAll
    static void afterAll() {
    }


    @DynamicPropertySource
    public static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
        registry.add("spring.datasource.driver-class-name", postgresContainer::getDriverClassName);

        Integer mappedPort = rabbitMqContainer.getMappedPort(5552);
        registry.add("custom.rabbitmq.stream.port", () -> mappedPort);
    }

    @Test
    void testGetPersonsRetrieveCollectionOfWebUsers() {
        Collection<WebPerson> webPersons = personService.getPersons();
        assertInstanceOf(Set.class, webPersons);
        assertEquals(3, webPersons.size());
    }

    @Test
    void testInsertPersonFailValidationThrows() {
        String email = "foo";
        int age = 45;
        assertThrows(TransactionSystemException.class, () -> personService.insertPerson(email, age));
    }
}