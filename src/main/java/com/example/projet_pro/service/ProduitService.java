// ProduitService.java
package com.example.projet_pro.service;

import com.example.projet_pro.entity.ProduitCommande;
import com.example.projet_pro.repository.ProduitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value; // ✅ PAS lombok.Value
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;               // ✅ PAS io.jsonwebtoken.io.IOException
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProduitService {

    private final ProduitRepository produitRepository;

    @Value("${upload.dir:uploads}")
    private String uploadDir;

    public ProduitCommande saveProduitWithImage(
            String nom,
            String description,
            Double prix,
            Integer quantite,
            MultipartFile image
    ) throws IOException {

        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String filename = UUID.randomUUID() + "_" + image.getOriginalFilename();
        Path filePath = uploadPath.resolve(filename);
        Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        ProduitCommande produit = new ProduitCommande();
        produit.setNom(nom);
        produit.setDescription(description);
        produit.setPrix(prix);
        produit.setQuantite(quantite);
        produit.setImageUrl("/uploads/" + filename);

        return produitRepository.save(produit);
    }
    // ProduitService.java — ajouter cette méthode publique
    public String saveImage(MultipartFile image) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        String filename = UUID.randomUUID() + "_" + image.getOriginalFilename();
        Path filePath = uploadPath.resolve(filename);
        Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return filename;
    }
}