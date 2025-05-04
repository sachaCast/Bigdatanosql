package com.example;

import com.azure.cosmos.*;
import com.azure.cosmos.models.*;
import com.azure.cosmos.util.CosmosPagedIterable;

import java.util.*;

public class AvionAggregationDAO {
    private final CosmosContainer container;

    // Constructeur qui prend directement le CosmosContainer
    public AvionAggregationDAO(CosmosContainer container) {
        this.container = container;
    }

    // Exemple 1 : Regrouper par modèle et compter le nombre d’avions
    public Map<String, Integer> countAvionsByModele() {
        String query = "SELECT c.modele, COUNT(1) AS nb FROM c WHERE c.type = 'avion' GROUP BY c.modele";
        CosmosPagedIterable<Map> resultSet = container.queryItems(query, new CosmosQueryRequestOptions(), Map.class);
        Map<String, Integer> result = new HashMap<>();
        for (Map row : resultSet) {
            String modele = (String) row.get("modele");
            Integer count = ((Number) row.get("nb")).intValue();
            result.put(modele, count);
        }
        return result;
    }

    // Exemple 2 : Moyenne de capacité par modèle
    public Map<String, Double> averageCapaciteByModele() {
        String query = "SELECT c.modele, AVG(c.capacite) AS moyenne FROM c WHERE c.type = 'avion' GROUP BY c.modele";
        CosmosPagedIterable<Map> resultSet = container.queryItems(query, new CosmosQueryRequestOptions(), Map.class);
        Map<String, Double> result = new HashMap<>();
        for (Map row : resultSet) {
            String modele = (String) row.get("modele");
            Double moyenne = ((Number) row.get("moyenne")).doubleValue();
            result.put(modele, moyenne);
        }
        return result;
    }

    // Exemple 3 : Trier les avions par capacité décroissante
    public List<Avion> getAvionsSortedByCapaciteDesc() {
        String query = "SELECT * FROM c WHERE c.type = 'avion' ORDER BY c.capacite DESC";
        CosmosPagedIterable<Avion> resultSet = container.queryItems(query, new CosmosQueryRequestOptions(), Avion.class);
        List<Avion> result = new ArrayList<>();
        resultSet.forEach(result::add);
        return result;
    }
}
