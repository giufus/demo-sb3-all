package com.giufus.demo.services;

import com.giufus.demo.entities.Person;
import com.giufus.demo.models.WebPerson;
import com.giufus.demo.repositories.PersonRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @Mock
    PersonRepository personRepository;

    @InjectMocks
    PersonService personService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testGetPersonsRetrieveCollectionOfWebUsers() {

        List<Person> persons = Arrays.asList(
                new Person("foo@bar.com", 45),
                new Person("boo@bar.com", 46),
                new Person("baz@bar.com", 47)
        );

        Mockito.when(personRepository.findAll()).thenReturn(persons);
        Collection<WebPerson> webPersons = personService.getPersons();

        verify(personRepository, atMostOnce()).findAll();
        assertInstanceOf(Set.class, webPersons);
        assertEquals(3, persons.size());


    }
}