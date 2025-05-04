package com.example;


import com.azure.cosmos.*;
import com.azure.cosmos.models.*;
import com.azure.cosmos.util.CosmosPagedIterable;

import java.util.*;

public class CompagnieAggregationDAO {
    private final CosmosContainer container;

    // Constructeur
    public CompagnieAggregationDAO(CosmosClient client, String databaseName, String containerName) {
        this.container = client.getDatabase(databaseName).getContainer(containerName);
    }

    // Exemple 1 : Regrouper par alliance et compter le nombre de compagnies
    public Map<String, Integer> countCompagniesByAlliance() {
        String query = "SELECT c.alliance, COUNT(1) AS nb FROM c GROUP BY c.alliance";
        CosmosPagedIterable<Map> resultSet = container.queryItems(query, new CosmosQueryRequestOptions(), Map.class);

        Map<String, Integer> result = new HashMap<>();
        for (Map row : resultSet) {
            String alliance = (String) row.get("alliance");
            Integer count = ((Number) row.get("nb")).intValue();
            result.put(alliance, count);
        }
        return result;
    }

    // Exemple 2 : Moyenne de la taille des compagnies par alliance
    public Map<String, Double> averageTailleByAlliance() {
        String query = "SELECT c.alliance, AVG(LEN(c.nom)) AS moyenne_taille FROM c GROUP BY c.alliance";
        CosmosPagedIterable<Map> resultSet = container.queryItems(query, new CosmosQueryRequestOptions(), Map.class);

        Map<String, Double> result = new HashMap<>();
        for (Map row : resultSet) {
            String alliance = (String) row.get("alliance");
            Double moyenne = ((Number) row.get("moyenne_taille")).doubleValue();
            result.put(alliance, moyenne);
        }
        return result;
    }

    // Exemple 3 : Lister les compagnies triées par nom
    public List<Compagnie> getCompagniesSortedByNom() {
        String query = "SELECT * FROM c ORDER BY c.nom";
        CosmosPagedIterable<Compagnie> resultSet = container.queryItems(query, new CosmosQueryRequestOptions(), Compagnie.class);

        List<Compagnie> result = new ArrayList<>();
        resultSet.forEach(result::add);
        return result;
    }

}
