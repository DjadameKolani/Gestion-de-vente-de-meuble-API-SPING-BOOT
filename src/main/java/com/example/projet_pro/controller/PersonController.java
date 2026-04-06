package com.example.projet_pro.controller;

import com.example.projet_pro.entity.Person;
import com.example.projet_pro.repository.PersonRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/persons")
public class PersonController {

    // ← corrigé : PersonRepository (pas PersonController)
    private final PersonRepository personRepository;

    public PersonController(PersonRepository personRepository) {
        this.personRepository = personRepository; // ← corrigé
    }

    // GET tous les persons
    @GetMapping
    public ResponseEntity<List<Person>> getAllPersons() {
        List<Person> persons = personRepository.findAll();
        return new ResponseEntity<>(persons, HttpStatus.OK);
    }

    // POST créer une personne
    @PostMapping
    public ResponseEntity<Person> createPerson(@RequestBody Person person) { // ← @RequestBody (pas @PathVariable)
        Person personCreated = personRepository.save(person);
        return new ResponseEntity<>(personCreated, HttpStatus.CREATED);
    }

    // GET par id
    @GetMapping("/{id}")
    public ResponseEntity<Person> getPersonById(@PathVariable String id) { // ← String (pas Long) pour MongoDB
        Optional<Person> person = personRepository.findById(id); // ← corrigé personRepository
        return person.map(value -> new ResponseEntity<>(value, HttpStatus.OK)) // ← person.map (pas Person.map)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // PUT modifier une personne
    @PutMapping("/{id}")
    public ResponseEntity<Person> updatePerson(@PathVariable String id,
                                               @RequestBody Person person) {
        return personRepository.findById(id)
                .map(existing -> {
                    person.setId(id);
                    return new ResponseEntity<>(personRepository.save(person), HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // DELETE supprimer une personne
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePerson(@PathVariable String id) {
        return personRepository.findById(id)
                .map(existing -> {
                    personRepository.deleteById(id);
                    return new ResponseEntity<>("Person supprimé", HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>("Person non trouvé", HttpStatus.NOT_FOUND));
    }
}