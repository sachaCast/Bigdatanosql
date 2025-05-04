package com.example;

import java.util.Map;
import java.util.UUID;

public class Passager {
    private String id;        // Utilisé par Cosmos DB comme identifiant
    private String _id;       // Nécessaire pour la clé de partition /_id
    private String nom;
    private String prenom;
    private String dateNaissance;
    private String email;
    private String telephone;

    // Constructeur sans paramètres
    public Passager() {
        this.id = UUID.randomUUID().toString(); // Générer un ID unique si nécessaire
        this._id = id; // Synchronisation de l'ID avec _id
    }

    // Getters et setters
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

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(String dateNaissance) { this.dateNaissance = dateNaissance; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    /**
     * Création d'un passager à partir d'une map, avec génération automatique d'ID si manquant,
     * et ajout du champ "_id" pour la compatibilité Cosmos DB.
     */
    public static Passager fromMap(Map<String, Object> map) {
        Passager passager = new Passager();
        String id = (String) map.getOrDefault("id", UUID.randomUUID().toString());
        passager.setId(id);  // Cela synchronisera également _id
        passager.setNom((String) map.get("nom"));
        passager.setPrenom((String) map.get("prenom"));
        passager.setDateNaissance((String) map.get("dateNaissance"));
        passager.setEmail((String) map.get("email"));
        passager.setTelephone((String) map.get("telephone"));

        map.put("_id", id);  // Important pour la partition key

        return passager;
    }
}
