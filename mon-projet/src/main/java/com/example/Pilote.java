package com.example;

import java.util.Map;
import java.util.UUID;

public class Pilote {
    private String _id; // Utilisé pour la clé de partition
    private String nom;
    private String prenom;
    private String dateEmbauche;
    private String idCompagnie;

    // Constructeur sans paramètres
    public Pilote() {
        this._id = UUID.randomUUID().toString(); // Générer un ID unique si nécessaire
    }

    // Constructeur avec paramètres
    public Pilote(String nom, String prenom, String dateEmbauche, String idCompagnie) {
        this._id = UUID.randomUUID().toString(); // Générer un ID unique
        this.nom = nom;
        this.prenom = prenom;
        this.dateEmbauche = dateEmbauche;
        this.idCompagnie = idCompagnie;
    }

    // Getters et setters
    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getDateEmbauche() {
        return dateEmbauche;
    }

    public void setDateEmbauche(String dateEmbauche) {
        this.dateEmbauche = dateEmbauche;
    }

    public String getIdCompagnie() {
        return idCompagnie;
    }

    public void setIdCompagnie(String idCompagnie) {
        this.idCompagnie = idCompagnie;
    }

    /**
     * Convertir l'objet Pilote en une Map<String, Object> pour Cosmos DB
     * avec un champ type et _id nécessaire pour la partition key.
     */
    public static Pilote fromMap(Map<String, Object> map) {
        Pilote pilote = new Pilote();
        String id = (String) map.getOrDefault("id", UUID.randomUUID().toString());
        pilote.set_id(id);  // Synchroniser avec _id

        pilote.setNom((String) map.get("nom"));
        pilote.setPrenom((String) map.get("prenom"));
        pilote.setDateEmbauche((String) map.get("dateEmbauche"));
        pilote.setIdCompagnie((String) map.get("idCompagnie"));

        return pilote;
    }
}
