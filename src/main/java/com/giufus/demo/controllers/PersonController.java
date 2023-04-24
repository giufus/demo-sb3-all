package com.giufus.demo.controllers;

import com.giufus.demo.models.WebPerson;
import com.giufus.demo.services.PersonService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("person")
public class PersonController {

    private PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    public Collection<WebPerson> getPersons() {
        return personService.getPersons();
    }

}
