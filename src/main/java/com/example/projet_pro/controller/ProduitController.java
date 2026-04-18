package com.example.projet_pro.controller;

import com.example.projet_pro.entity.Produit;
import com.example.projet_pro.repository.ProduitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produits")
@RequiredArgsConstructor
public class ProduitController {

    private final ProduitRepository produitRepository;

    // ← Accessible à tous les rôles connectés
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'EXPERT', 'ADMIN')")
    public ResponseEntity<List<Produit>> getAll() {
        return ResponseEntity.ok(produitRepository.findAll());
    }

    // ← Réservé à EXPERT et ADMIN
    @PostMapping
    @PreAuthorize("hasAnyRole('EXPERT', 'ADMIN')")
    public ResponseEntity<?> create(@RequestBody Produit produit) {

        // Validation nom
        if (produit.getNom() == null || produit.getNom().isEmpty()) {
            return ResponseEntity.badRequest().body("Le nom du produit est obligatoire");
        }

        // Validation prix
       if (produit.getPrix() == null || produit.getPrix() < 0) {
            return ResponseEntity.badRequest().body("Le prix doit être positif");
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(produitRepository.save(produit));
    }

    // ← Réservé à ADMIN uniquement
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> delete(@PathVariable String id) {

        // Vérifier que le produit existe avant de supprimer
        if (!produitRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Produit introuvable avec l'id : " + id);
        }

        produitRepository.deleteById(id);
        return ResponseEntity.ok("Produit supprimé avec succès");
    }

    // ← Réservé à EXPERT et ADMIN
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('EXPERT', 'ADMIN')")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody Produit produit) {

        if (!produitRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Produit introuvable avec l'id : " + id);
        }

        produit.setId(id); // ← forcer l'id pour ne pas en créer un nouveau
        return ResponseEntity.ok(produitRepository.save(produit));
    }
}