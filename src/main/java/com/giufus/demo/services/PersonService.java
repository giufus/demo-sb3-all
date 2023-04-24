package com.giufus.demo.services;

import com.giufus.demo.entities.Person;
import com.giufus.demo.models.WebPerson;
import com.giufus.demo.repositories.PersonRepository;
import com.rabbitmq.stream.Producer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonService {

    private PersonRepository personRepository;

    private Producer personProducer;

    public PersonService(PersonRepository personRepository, @Qualifier("personProducer") Producer personProducer) {
        this.personRepository = personRepository;
        this.personProducer = personProducer;
    }

    public Collection<WebPerson> getPersons() {
        List<Person> all = personRepository.findAll();
        return all.stream()
                .map(PersonService::personMapper)
                .collect(Collectors.toSet());
    }

    public WebPerson insertPerson(String email, int age) {
        Person person = personRepository.save(new Person(email, age));
        return personMapper(person);
    }

    public static WebPerson personMapper(Person person) {
        return new WebPerson(person.getEmail().split("@")[0], person.getAge());
    }
}
