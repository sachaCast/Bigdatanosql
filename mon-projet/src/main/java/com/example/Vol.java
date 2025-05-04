package com.example;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public class Vol {
    private String id;      // Utilisé pour la compatibilité avec Cosmos DB comme identifiant
    private String _id;     // Nécessaire pour la clé de partition /_id
    private String numeroVolBase;
    private Instant dateDepart;
    private Instant dateArrivee;
    private String statut;
    private Compagnie compagnie;
    private Aeroport aeroportDepart;
    private Aeroport aeroportArrivee;
    private AvionSimple avion;
    private Equipage equipage;

    // Constructeur sans paramètres
    public Vol() {
        String generatedId = UUID.randomUUID().toString();
        this.setId(generatedId);  // Générer un ID unique si nécessaire
    }

    // Getters et setters
    public String getId() { return id; }
    public void setId(String id) {
        this.id = id;
        this._id = id;  // Synchroniser avec _id pour Cosmos DB
    }

    public String get_id() { return _id; }
    public void set_id(String _id) {
        this._id = _id;
        this.id = _id;  // Synchroniser avec id pour Cosmos DB
    }

    public String getNumeroVolBase() { return numeroVolBase; }
    public void setNumeroVolBase(String numeroVolBase) { this.numeroVolBase = numeroVolBase; }

    public Instant getDateDepart() { return dateDepart; }
    public void setDateDepart(Instant dateDepart) { this.dateDepart = dateDepart; }

    public Instant getDateArrivee() { return dateArrivee; }
    public void setDateArrivee(Instant dateArrivee) { this.dateArrivee = dateArrivee; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public Compagnie getCompagnie() { return compagnie; }
    public void setCompagnie(Compagnie compagnie) { this.compagnie = compagnie; }

    public Aeroport getAeroportDepart() { return aeroportDepart; }
    public void setAeroportDepart(Aeroport aeroportDepart) { this.aeroportDepart = aeroportDepart; }

    public Aeroport getAeroportArrivee() { return aeroportArrivee; }
    public void setAeroportArrivee(Aeroport aeroportArrivee) { this.aeroportArrivee = aeroportArrivee; }

    public AvionSimple getAvion() { return avion; }
    public void setAvion(AvionSimple avion) { this.avion = avion; }

    public Equipage getEquipage() { return equipage; }
    public void setEquipage(Equipage equipage) { this.equipage = equipage; }

    // Sous-classes
    public static class Compagnie {
        private String id;
        private String nom;

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getNom() { return nom; }
        public void setNom(String nom) { this.nom = nom; }
    }

    public static class Aeroport {
        private String id;
        private String ville;

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getVille() { return ville; }
        public void setVille(String ville) { this.ville = ville; }
    }

    public static class AvionSimple {
        private String id;
        private String modele;

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getModele() { return modele; }
        public void setModele(String modele) { this.modele = modele; }
    }

    public static class Equipage {
        private PiloteSimple commandant;
        private PiloteSimple opl;

        public PiloteSimple getCommandant() { return commandant; }
        public void setCommandant(PiloteSimple commandant) { this.commandant = commandant; }

        public PiloteSimple getOpl() { return opl; }
        public void setOpl(PiloteSimple opl) { this.opl = opl; }

        public static class PiloteSimple {
            private String id;
            private String nom;

            public String getId() { return id; }
            public void setId(String id) { this.id = id; }

            public String getNom() { return nom; }
            public void setNom(String nom) { this.nom = nom; }
        }
    }

    /**
     * Création d'un vol à partir d'une map, avec génération automatique d'ID si manquant,
     * et ajout du champ "type" et "_id" pour la compatibilité Cosmos DB.
     */
    public static Vol fromMap(Map<String, Object> map, String type) {
        Vol vol = new Vol();
        String id = (String) map.getOrDefault("id", UUID.randomUUID().toString());
        vol.setId(id);  // Cela synchronisera également _id

        vol.setNumeroVolBase((String) map.get("numeroVolBase"));
        vol.setDateDepart((Instant) map.get("dateDepart"));
        vol.setDateArrivee((Instant) map.get("dateArrivee"));
        vol.setStatut((String) map.get("statut"));

        // Assigner les autres objets imbriqués (companie, aéroports, etc.)
        Map<String, Object> compagnieMap = (Map<String, Object>) map.get("compagnie");
        if (compagnieMap != null) {
            Compagnie compagnie = new Compagnie();
            compagnie.setId((String) compagnieMap.get("id"));
            compagnie.setNom((String) compagnieMap.get("nom"));
            vol.setCompagnie(compagnie);
        }

        // Ajouter les autres objets imbriqués comme avion, aéroports et équipage
        // Assurez-vous de traiter chaque objet comme nécessaire en fonction de vos données.

        map.put("type", type);
        map.put("_id", id);  // Important pour la partition key

        return vol;
    }
}
