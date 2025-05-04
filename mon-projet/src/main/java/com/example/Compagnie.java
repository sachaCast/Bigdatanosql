package com.example;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Compagnie {
    @JsonProperty("id") // Remplacer "_id" par "id" pour suivre la convention Cosmos DB
    private String id;

    private String nom;
    private String alliance;

    // Getter et setter pour 'id'
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    // Getter et setter pour 'nom'
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    // Getter et setter pour 'alliance'
    public String getAlliance() { return alliance; }
    public void setAlliance(String alliance) { this.alliance = alliance; }
}
