package com.example;

import com.azure.cosmos.*;
import com.azure.cosmos.models.*;
import java.util.*;

public class CompagnieDAO {
    private final CosmosContainer container;

    // Constructeur modifié pour accepter directement un CosmosContainer
    public CompagnieDAO(CosmosContainer container) {
        this.container = container;
    }

    // Insertion d'une nouvelle compagnie
    public void insertCompagnie(Compagnie compagnie) {
        CosmosItemResponse<Compagnie> response = container.createItem(compagnie);
        System.out.println("Compagnie insérée avec succès : " + response.getItem().getId());
    }

    // Suppression d'une compagnie par son ID et PartitionKey
    public void deleteCompagnie(String id, String partitionKeyValue) {
        Compagnie compagnie = getCompagnieById(id, partitionKeyValue); // Chercher la compagnie
        if (compagnie != null) {
            container.deleteItem(compagnie.getId(), new PartitionKey(partitionKeyValue), new CosmosItemRequestOptions());
            System.out.println("Compagnie supprimée avec succès : " + id);
        } else {
            System.out.println("Compagnie avec l'ID " + id + " non trouvée.");
        }
    }

    // Récupération d'une compagnie par son ID
    public Compagnie getCompagnieById(String id, String partitionKeyValue) {
        try {
            CosmosItemResponse<Compagnie> response = container.readItem(id, new PartitionKey(partitionKeyValue), Compagnie.class);
            return response.getItem();
        } catch (CosmosException e) {
            System.out.println("Erreur lors de la récupération de la compagnie : " + e.getMessage());
            return null;
        }
    }
}