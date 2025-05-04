package com.example;

import java.util.Map;
import java.util.UUID;

public class Equipage {
    private String id;     // Utilisé par Cosmos DB comme identifiant
    private String _id;    // Nécessaire pour la clé de partition /_id
    private Pilote commandant;
    private Pilote opl;

    // Constructeur sans paramètres
    public Equipage() {}

    // Getter et Setter pour `id`
    public String getId() { return id; }
    public void setId(String id) {
        this.id = id;
        this._id = id; // Synchroniser avec _id
    }

    // Getter et Setter pour `_id`
    public String get_id() { return _id; }
    public void set_id(String _id) {
        this._id = _id;
        this.id = _id; // Synchroniser avec id
    }

    // Getter et Setter pour `commandant`
    public Pilote getCommandant() { return commandant; }
    public void setCommandant(Pilote commandant) { this.commandant = commandant; }

    // Getter et Setter pour `opl`
    public Pilote getOpl() { return opl; }
    public void setOpl(Pilote opl) { this.opl = opl; }

    /**
     * Création d'un équipage à partir d'une map, avec génération automatique d'ID si manquant,
     * et ajout du champ "_id" pour la compatibilité Cosmos DB.
     */
    public static Equipage fromMap(Map<String, Object> map) {
        Equipage equipage = new Equipage();
        String id = (String) map.getOrDefault("id", UUID.randomUUID().toString());
        equipage.setId(id);  // Cela synchronisera également _id

        // Ajouter des champs à la map si nécessaire
        equipage.setCommandant((Pilote) map.get("commandant"));
        equipage.setOpl((Pilote) map.get("opl"));

        map.put("_id", id);  // Important pour la partition key

        return equipage;
    }
}
