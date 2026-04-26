package com.example.projet_pro.controller;

import com.example.projet_pro.entity.ProduitCommande;
import com.example.projet_pro.repository.ProduitRepository;
import com.example.projet_pro.service.ProduitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/produits")
@RequiredArgsConstructor
public class ProduitController {

    private final ProduitRepository produitRepository;
    private final ProduitService produitService;

    @GetMapping
    public ResponseEntity<List<ProduitCommande>> getAll() {
        return ResponseEntity.ok(produitRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        return produitRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> create(
            @RequestPart("nom")         String nom,
            @RequestPart("description") String description,
            @RequestPart("prix")        String prix,
            @RequestPart("quantite")    String quantite,
            @RequestPart("image")       MultipartFile image
    ) {
        if (nom == null || nom.isBlank())
            return ResponseEntity.badRequest().body("Le nom est obligatoire");
        if (image == null || image.isEmpty())
            return ResponseEntity.badRequest().body("L'image est obligatoire");

        try {
            ProduitCommande saved = produitService.saveProduitWithImage(
                    nom, description,
                    Double.parseDouble(prix),
                    Integer.parseInt(quantite),
                    image
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la sauvegarde de l'image");
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> update(
            @PathVariable String id,
            @RequestPart("nom")         String nom,
            @RequestPart("description") String description,
            @RequestPart("prix")        String prix,
            @RequestPart("quantite")    String quantite,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        if (!produitRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Produit introuvable : " + id);

        try {
            ProduitCommande existing = produitRepository.findById(id).orElseThrow();
            existing.setNom(nom);
            existing.setDescription(description);
            existing.setPrix(Double.parseDouble(prix));
            existing.setQuantite(Integer.parseInt(quantite));

            if (image != null && !image.isEmpty()) {
                String filename = produitService.saveImage(image);
                existing.setImageUrl("/uploads/" + filename);
            }

            return ResponseEntity.ok(produitRepository.save(existing));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la sauvegarde de l'image");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable String id) {
        if (!produitRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Produit introuvable : " + id);
        produitRepository.deleteById(id);
        return ResponseEntity.ok("Produit supprimé avec succès");
    }
}