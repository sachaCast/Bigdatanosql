package com.example;

import com.azure.cosmos.*;
import com.azure.cosmos.models.*;
import com.azure.cosmos.util.CosmosPagedIterable;

import java.util.ArrayList;
import java.util.List;

public class AvionDAO {
    private final CosmosContainer container;

    public AvionDAO(CosmosClient client, String databaseName, String containerName) {
        this.container = client.getDatabase(databaseName).getContainer(containerName);
    }

    public void createAvion(Avion avion) {
        container.createItem(avion, avion.getPartitionKey(), new CosmosItemRequestOptions());
    }

    public Avion getAvionById(String id, String idCompagnie) {
        try {
            CosmosItemResponse<Avion> response = container.readItem(id, new PartitionKey(idCompagnie), Avion.class);
            return response.getItem();
        } catch (CosmosException e) {
            System.err.println("Erreur getAvionById : " + e.getMessage());
            return null;
        }
    }

    public List<Avion> getAllAvions() {
        String query = "SELECT * FROM c";
        CosmosPagedIterable<Avion> avions = container.queryItems(query, new CosmosQueryRequestOptions(), Avion.class);
        List<Avion> result = new ArrayList<>();
        avions.forEach(result::add);
        return result;
    }

    public void updateAvion(Avion avion) {
        container.upsertItem(avion, avion.getPartitionKey(), new CosmosItemRequestOptions());
    }

    public void deleteAvion(String id, String idCompagnie) {
        container.deleteItem(id, new PartitionKey(idCompagnie), new CosmosItemRequestOptions());
    }
}
