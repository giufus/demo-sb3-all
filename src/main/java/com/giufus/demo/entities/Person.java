package com.giufus.demo.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

@Entity
public class Person {

    public Person() {
    }

    public Person(String email, int age) {
        this.email = email;
        this.age = age;
    }

    @Id
    @Email
    private String email;

    @NotNull
    private int age;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}