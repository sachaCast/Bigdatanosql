package com.example;

import com.azure.cosmos.*;
import com.azure.cosmos.models.*;
import com.azure.cosmos.util.CosmosPagedIterable;

import java.util.List;

public class PiloteDAO {
    private final CosmosContainer container;

    public PiloteDAO(CosmosContainer container) {
        this.container = container;
    }

    // Insérer un pilote
    public void insertPilote(Pilote pilote) {
        if (findById(pilote.get_id()) == null) {
            container.createItem(pilote, new PartitionKey(pilote.get_id()), new CosmosItemRequestOptions());
            System.out.println("Pilote inséré avec succès.");
        } else {
            System.out.println("Le pilote avec l'ID " + pilote.get_id() + " existe déjà.");
        }
    }

    // Insérer plusieurs pilotes
    public void insertPilotes(List<Pilote> pilotes) {
        for (Pilote pilote : pilotes) {
            container.createItem(pilote, new PartitionKey(pilote.get_id()), new CosmosItemRequestOptions());
        }
    }

    // Mettre à jour un pilote
    public void updatePilote(String idPilote, String field, Object newValue) {
        Pilote pilote = findById(idPilote);
        if (pilote != null) {
            switch (field) {
                case "nom":
                    pilote.setNom((String) newValue);
                    break;
                case "prenom":
                    pilote.setPrenom((String) newValue);
                    break;
                case "dateEmbauche":
                    pilote.setDateEmbauche((String) newValue);
                    break;
                case "idCompagnie":
                    pilote.setIdCompagnie((String) newValue);
                    break;
                default:
                    System.out.println("Champ non pris en charge : " + field);
                    return;
            }

            container.upsertItem(pilote, new PartitionKey(pilote.get_id()), new CosmosItemRequestOptions());
            System.out.println("Pilote mis à jour avec succès.");
        } else {
            System.out.println("Pilote avec l'ID " + idPilote + " non trouvé.");
        }
    }

    // Supprimer un pilote
    public void deletePilote(String idPilote) {
        Pilote pilote = findById(idPilote);
        if (pilote != null) {
            container.deleteItem(pilote.get_id(), new PartitionKey(pilote.get_id()), new CosmosItemRequestOptions());
            System.out.println("Pilote supprimé avec succès.");
        } else {
            System.out.println("Pilote avec l'ID " + idPilote + " non trouvé.");
        }
    }

    // Trouver un pilote par ID
    public Pilote findById(String id) {
        String query = "SELECT * FROM c WHERE c._id = @id";
        SqlParameter param = new SqlParameter("@id", id);
        SqlQuerySpec querySpec = new SqlQuerySpec(query, List.of(param));

        CosmosQueryRequestOptions options = new CosmosQueryRequestOptions();
        CosmosPagedIterable<Pilote> results = container.queryItems(querySpec, options, Pilote.class);

        for (Pilote pilote : results) {
            return pilote;
        }
        return null;
    }
}
