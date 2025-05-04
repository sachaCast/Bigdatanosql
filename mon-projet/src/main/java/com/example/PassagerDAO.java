package com.example;

import com.azure.cosmos.*;
import com.azure.cosmos.models.*;
import com.azure.cosmos.util.CosmosPagedIterable;
import java.util.List;

public class PassagerDAO {
    private final CosmosContainer container;

    public PassagerDAO(CosmosContainer container) {
        this.container = container;
    }

    // Insérer un passager
    public void insertPassager(Passager passager) {
        if (findById(passager.getId()) == null) {
            container.createItem(passager, new PartitionKey(passager.get_id()), new CosmosItemRequestOptions());
            System.out.println("Passager inséré avec succès.");
        } else {
            System.out.println("Le passager avec l'ID " + passager.getId() + " existe déjà.");
        }
    }

    // Insérer plusieurs passagers
    public void insertPassagers(List<Passager> passagers) {
        for (Passager passager : passagers) {
            container.createItem(passager, new PartitionKey(passager.get_id()), new CosmosItemRequestOptions());
        }
    }

    // Mettre à jour un passager
    public void updatePassager(String idPassager, String field, Object newValue) {
        Passager passager = findById(idPassager);
        if (passager != null) {
            switch (field) {
                case "nom":
                    passager.setNom((String) newValue);
                    break;
                case "prenom":
                    passager.setPrenom((String) newValue);
                    break;
                case "dateNaissance":
                    passager.setDateNaissance((String) newValue);
                    break;
                case "email":
                    passager.setEmail((String) newValue);
                    break;
                case "telephone":
                    passager.setTelephone((String) newValue);
                    break;
                default:
                    System.out.println("Champ non pris en charge : " + field);
                    return;
            }

            container.upsertItem(passager, new PartitionKey(passager.get_id()), new CosmosItemRequestOptions());
            System.out.println("Passager mis à jour avec succès.");
        } else {
            System.out.println("Passager avec l'ID " + idPassager + " non trouvé.");
        }
    }

    // Supprimer un passager
    public void deletePassager(String idPassager) {
        Passager passager = findById(idPassager);
        if (passager != null) {
            container.deleteItem(passager.getId(), new PartitionKey(passager.get_id()), new CosmosItemRequestOptions());
            System.out.println("Passager supprimé avec succès.");
        } else {
            System.out.println("Passager avec l'ID " + idPassager + " non trouvé.");
        }
    }

    // Trouver un passager par ID
    public Passager findById(String id) {
        String query = "SELECT * FROM c WHERE c.id = @id";
        SqlParameter param = new SqlParameter("@id", id);
        SqlQuerySpec querySpec = new SqlQuerySpec(query, List.of(param));

        CosmosQueryRequestOptions options = new CosmosQueryRequestOptions();
        CosmosPagedIterable<Passager> results = container.queryItems(querySpec, options, Passager.class);

        for (Passager passager : results) {
            return passager;
        }
        return null;
    }
}
