package com.example;

import com.azure.cosmos.models.PartitionKey;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Avion {

    @JsonProperty("id") // Obligatoire : Cosmos DB attend une propriété "id"
    private String id;

    private String modele;
    private int capacite;
    private String idCompagnie; // peut être la clé de partition

    // Constructeur par défaut requis pour la désérialisation
    public Avion() {}

    // Getters et setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModele() {
        return modele;
    }

    public void setModele(String modele) {
        this.modele = modele;
    }

    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    public String getIdCompagnie() {
        return idCompagnie;
    }

    public void setIdCompagnie(String idCompagnie) {
        this.idCompagnie = idCompagnie;
    }

    // Méthode utilitaire si tu veux la clé de partition rapidement
    public PartitionKey getPartitionKey() {
        return new PartitionKey(this.idCompagnie);
    }
}
