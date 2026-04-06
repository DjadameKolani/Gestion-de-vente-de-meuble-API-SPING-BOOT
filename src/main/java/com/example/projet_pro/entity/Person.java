package com.example.projet_pro.entity;



import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "persons")
@Data
public class Person {

    @Id
    private String id;

    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String adresse;
}