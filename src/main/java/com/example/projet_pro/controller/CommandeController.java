package com.example.projet_pro.controller;

import com.example.projet_pro.entity.Commande;
import com.example.projet_pro.repository.CommandeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/commandes")
@RequiredArgsConstructor
public class CommandeController {

    private final CommandeRepository commandeRepository;

    // ← Créer une commande
    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'EXPERT', 'ADMIN')")
    public ResponseEntity<Commande> creerCommande(@RequestBody Commande commande) {
        commande.setStatut("EN_ATTENTE");
        return ResponseEntity.ok(commandeRepository.save(commande));
    }

    // ← Historique commandes utilisateur
    @GetMapping("/mes-commandes/{username}")
    @PreAuthorize("hasAnyRole('USER', 'EXPERT', 'ADMIN')")
    public ResponseEntity<List<Commande>> mesCommandes(@PathVariable String username) {
        return ResponseEntity.ok(commandeRepository.findByUsername(username));
    }

    // ← Toutes les commandes pour admin
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Commande>> toutesLesCommandes() {
        return ResponseEntity.ok(commandeRepository.findAll());
    }

    // ← Modifier le statut d'une commande
    @PutMapping("/{id}/statut")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> modifierStatut(
            @PathVariable String id,
            @RequestParam String statut
    ) {
        return commandeRepository.findById(id).map(commande -> {
            commande.setStatut(statut);
            return ResponseEntity.ok(commandeRepository.save(commande));
        }).orElse(ResponseEntity.notFound().build());
    }

    // ← Supprimer une commande
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> supprimer(@PathVariable String id) {
        commandeRepository.deleteById(id);
        return ResponseEntity.ok("Commande supprimée");
    }
}