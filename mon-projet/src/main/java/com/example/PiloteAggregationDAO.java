package com.example;

import com.azure.cosmos.*;
import com.azure.cosmos.models.*;
import com.azure.cosmos.util.CosmosPagedIterable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PiloteAggregationDAO {
    private final CosmosContainer container;

    public PiloteAggregationDAO(CosmosContainer container) {
        this.container = container;
    }

    // 1. Nombre de pilotes par compagnie
    public List<Map<String, Object>> countPilotesParCompagnie() {
        String query = "SELECT c.idCompagnie, " +
                "VALUE { 'idCompagnie': c.idCompagnie, 'total_pilotes': (SELECT VALUE COUNT(1) FROM p WHERE p.idCompagnie = c.idCompagnie) } " +
                "FROM c WHERE c.type = 'pilote'";

        // Exécuter la requête SQL et récupérer les résultats
        CosmosQueryRequestOptions options = new CosmosQueryRequestOptions();
        CosmosPagedIterable<Object> results = container.queryItems(query, options, Object.class);
        List<Map<String, Object>> resultList = new ArrayList<>();

        // Convertir les résultats en une liste de Map<String, Object>
        results.forEach(result -> {
            Map<String, Object> resultMap = (Map<String, Object>) result;
            resultList.add(resultMap);
        });

        return resultList;
    }

    // 2. Pilotes les plus expérimentés (par exemple, ceux ayant plus de 5 ans d'expérience)
    public List<Map<String, Object>> pilotesExperiences(String dateDebut) {
        String query = "SELECT c.nom, c.prenom, DATEDIFF(CURRENT_DATE, c.dateEmbauche) AS experience " +
                "FROM c WHERE c.dateEmbauche <= @dateDebut " +
                "ORDER BY experience DESC";

        // Paramètres de la requête SQL
        SqlQuerySpec sqlQuerySpec = new SqlQuerySpec(
                query,
                new SqlParameter("@dateDebut", dateDebut)
        );

        // Exécuter la requête SQL et récupérer les résultats
        CosmosQueryRequestOptions options = new CosmosQueryRequestOptions();
        CosmosPagedIterable<Object> results = container.queryItems(sqlQuerySpec, options, Object.class);
        List<Map<String, Object>> resultList = new ArrayList<>();

        // Convertir les résultats en une liste de Map<String, Object>
        results.forEach(result -> {
            Map<String, Object> resultMap = (Map<String, Object>) result;
            resultList.add(resultMap);
        });

        return resultList;
    }

    // 3. Pilotes ayant un nom spécifique (exemple de recherche filtrée)
    public List<Map<String, Object>> pilotesParNom(String nom) {
        String query = "SELECT c.nom, c.prenom, c.idCompagnie " +
                "FROM c WHERE c.nom = @nom";

        // Paramètres de la requête SQL
        SqlQuerySpec sqlQuerySpec = new SqlQuerySpec(
                query,
                new SqlParameter("@nom", nom)
        );

        // Exécuter la requête SQL et récupérer les résultats
        CosmosQueryRequestOptions options = new CosmosQueryRequestOptions();
        CosmosPagedIterable<Object> results = container.queryItems(sqlQuerySpec, options, Object.class);
        List<Map<String, Object>> resultList = new ArrayList<>();

        // Convertir les résultats en une liste de Map<String, Object>
        results.forEach(result -> {
            Map<String, Object> resultMap = (Map<String, Object>) result;
            resultList.add(resultMap);
        });

        return resultList;
    }
}
