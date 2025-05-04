package com.example;

import java.util.Map;
import java.util.UUID;

public class Aeroport {
    private String id;     // Utilisé par Cosmos DB comme identifiant
    private String _id;    // Nécessaire pour la clé de partition /_id
    private String nom;
    private String ville;
    private String pays;

    // Constructeur sans paramètres
    public Aeroport() {}

    public String getId() { return id; }

    public void setId(String id) {
        this.id = id;
        this._id = id; // Synchroniser avec _id
    }

    public String get_id() { return _id; }

    public void set_id(String _id) {
        this._id = _id;
        this.id = _id; // Synchroniser avec id
    }

    public String getNom() { return nom; }

    public void setNom(String nom) { this.nom = nom; }

    public String getVille() { return ville; }

    public void setVille(String ville) { this.ville = ville; }

    public String getPays() { return pays; }

    public void setPays(String pays) { this.pays = pays; }

    /**
     * Création d'un aéroport à partir d'une map, avec génération automatique d'ID si manquant,
     * et ajout du champ "type" et "_id" pour la compatibilité Cosmos DB.
     */
    public static Aeroport fromMap(Map<String, Object> map, String type) {
        Aeroport aeroport = new Aeroport();
        String id = (String) map.getOrDefault("id", UUID.randomUUID().toString());
        aeroport.setId(id);  // Cela synchronisera également _id
        aeroport.setNom((String) map.get("nom"));
        aeroport.setVille((String) map.get("ville"));
        aeroport.setPays((String) map.get("pays"));

        map.put("type", type);
        map.put("_id", id);  // Important pour la partition key

        return aeroport;
    }
}
