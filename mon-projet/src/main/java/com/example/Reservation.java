package com.example;

import java.time.Instant;
import java.util.UUID;

public class Reservation {
    private String _id; // Identifiant unique (clé primaire)
    private Instant dateReservation; // Date de la réservation
    private String classe; // Classe de la réservation (économique, affaires, etc.)
    private String siege; // Siege réservé
    private Passager passager; // Passager associé à la réservation
    private VolReference vol; // Vol associé à la réservation
    private String idCompagnieEmission; // Identifiant de la compagnie aérienne émettrice

    // Constructeur
    public Reservation() {
        this._id = UUID.randomUUID().toString(); // Génère un ID unique si nécessaire
    }

    // Getters et setters
    public String get_id() { return _id; }
    public void set_id(String _id) { this._id = _id; }

    public Instant getDateReservation() { return dateReservation; }
    public void setDateReservation(Instant dateReservation) { this.dateReservation = dateReservation; }

    public String getClasse() { return classe; }
    public void setClasse(String classe) { this.classe = classe; }

    public String getSiege() { return siege; }
    public void setSiege(String siege) { this.siege = siege; }

    public Passager getPassager() { return passager; }
    public void setPassager(Passager passager) { this.passager = passager; }

    public VolReference getVol() { return vol; }
    public void setVol(VolReference vol) { this.vol = vol; }

    public String getIdCompagnieEmission() { return idCompagnieEmission; }
    public void setIdCompagnieEmission(String idCompagnieEmission) { this.idCompagnieEmission = idCompagnieEmission; }

    // Sous-classe Passager (détails du passager)
    public static class Passager {
        private String _id; // Identifiant unique
        private String nom; // Nom du passager
        private String prenom; // Prénom du passager

        // Constructeur
        public Passager(String _id, String nom, String prenom) {
            this._id = _id;
            this.nom = nom;
            this.prenom = prenom;
        }

        // Getters et setters
        public String get_id() { return _id; }
        public void set_id(String _id) { this._id = _id; }

        public String getNom() { return nom; }
        public void setNom(String nom) { this.nom = nom; }

        public String getPrenom() { return prenom; }
        public void setPrenom(String prenom) { this.prenom = prenom; }
    }

    // Sous-classe VolReference (référence vers un vol)
    public static class VolReference {
        private String _id; // Identifiant unique du vol
        private String numeroVolBase; // Numéro du vol
        private Instant dateDepart; // Date de départ
        private String aeroportDepartId; // Aéroport de départ (ID)
        private String aeroportArriveeId; // Aéroport d'arrivée (ID)

        // Constructeur
        public VolReference(String _id, String numeroVolBase, Instant dateDepart, String aeroportDepartId, String aeroportArriveeId) {
            this._id = _id;
            this.numeroVolBase = numeroVolBase;
            this.dateDepart = dateDepart;
            this.aeroportDepartId = aeroportDepartId;
            this.aeroportArriveeId = aeroportArriveeId;
        }

        // Getters et setters
        public String get_id() { return _id; }
        public void set_id(String _id) { this._id = _id; }

        public String getNumeroVolBase() { return numeroVolBase; }
        public void setNumeroVolBase(String numeroVolBase) { this.numeroVolBase = numeroVolBase; }

        public Instant getDateDepart() { return dateDepart; }
        public void setDateDepart(Instant dateDepart) { this.dateDepart = dateDepart; }

        public String getAeroportDepartId() { return aeroportDepartId; }
        public void setAeroportDepartId(String aeroportDepartId) { this.aeroportDepartId = aeroportDepartId; }

        public String getAeroportArriveeId() { return aeroportArriveeId; }
        public void setAeroportArriveeId(String aeroportArriveeId) { this.aeroportArriveeId = aeroportArriveeId; }
    }
}
