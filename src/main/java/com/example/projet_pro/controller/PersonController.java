package com.example.projet_pro.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/persons")
public class PersonController {
    final PersonController personController;

    public PersonController (PersonRepository personRepository) {
        this.personController = personRepository;

    }
    @PostMapping
    public ResponseEntity<Person> getPersonnById(@PathVariable Person person ){
        Person personCreated = personRepository.save(person);
        return new ResponseEntity<>(personCreated, HttpStatus.CREATED);

    }
    @GetMapping("{id}")
    public ResponseEntity<Peron> getPersonById(@PathVariable Long id){
        Optional<Person> person = personRespository.findById(id);
        return Person.map(value -> new ResponseEntity<>(value,HttpStatus.OK)).orElseGet(()->new ResponseEntity<>(HttpStatus.NOT_FOUND));


    }
}
