package com.example;

import com.azure.cosmos.*;
import com.azure.cosmos.models.*;
import com.azure.cosmos.util.CosmosPagedIterable;

import java.util.List;

public class AeroportDAO {
    private final CosmosContainer container;

    public AeroportDAO(CosmosContainer container) {
        this.container = container;
    }

    // Insérer un aéroport
    public void insertAeroport(Aeroport aeroport) {
        if (findById(aeroport.getId()) == null) {
            container.createItem(aeroport, new PartitionKey(aeroport.get_id()), new CosmosItemRequestOptions());
            System.out.println("Aéroport inséré avec succès.");
        } else {
            System.out.println("L'aéroport avec l'ID " + aeroport.getId() + " existe déjà.");
        }
    }

    // Insérer plusieurs aéroports
    public void insertAeroports(List<Aeroport> aeroports) {
        for (Aeroport aeroport : aeroports) {
            container.createItem(aeroport, new PartitionKey(aeroport.get_id()), new CosmosItemRequestOptions());
        }
    }

    // Mettre à jour un aéroport
    public void updateAeroport(String idAeroport, String field, Object newValue) {
        Aeroport aeroport = findById(idAeroport);
        if (aeroport != null) {
            switch (field) {
                case "nom":
                    aeroport.setNom((String) newValue);
                    break;
                case "ville":
                    aeroport.setVille((String) newValue);
                    break;
                case "pays":
                    aeroport.setPays((String) newValue);
                    break;
                default:
                    System.out.println("Champ non pris en charge : " + field);
                    return;
            }

            container.upsertItem(aeroport, new PartitionKey(aeroport.get_id()), new CosmosItemRequestOptions());
            System.out.println("Aéroport mis à jour avec succès.");
        } else {
            System.out.println("Aéroport avec l'ID " + idAeroport + " non trouvé.");
        }
    }

    // Supprimer un aéroport
    public void deleteAeroport(String idAeroport) {
        Aeroport aeroport = findById(idAeroport);
        if (aeroport != null) {
            container.deleteItem(aeroport.getId(), new PartitionKey(aeroport.get_id()), new CosmosItemRequestOptions());
            System.out.println("Aéroport supprimé avec succès.");
        } else {
            System.out.println("Aéroport avec l'ID " + idAeroport + " non trouvé.");
        }
    }

    // Trouver un aéroport par ID
    public Aeroport findById(String id) {
        String query = "SELECT * FROM c WHERE c.id = @id";
        SqlParameter param = new SqlParameter("@id", id);
        SqlQuerySpec querySpec = new SqlQuerySpec(query, List.of(param));

        CosmosQueryRequestOptions options = new CosmosQueryRequestOptions();
        CosmosPagedIterable<Aeroport> results = container.queryItems(querySpec, options, Aeroport.class);

        for (Aeroport aeroport : results) {
            return aeroport;
        }
        return null;
    }
}
