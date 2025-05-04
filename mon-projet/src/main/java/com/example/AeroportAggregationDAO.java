package com.example;

import com.azure.cosmos.*;
import com.azure.cosmos.models.*;
import com.azure.cosmos.util.CosmosPagedIterable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AeroportAggregationDAO {
    private final CosmosContainer container;

    public AeroportAggregationDAO(CosmosContainer container) {
        this.container = container;
    }

    // 1. Nombre de vols par aéroport
    public List<Map<String, Object>> countVolsParAeroport() {
        String query = "SELECT c.nom, " +
                "VALUE { 'nom': c.nom, 'total_vols': (SELECT VALUE COUNT(1) FROM v WHERE v.aeroport_depart.id = c.id OR v.aeroport_arrivee.id = c.id) } " +
                "FROM c WHERE c.type = 'airport'";

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

    // 2. Aéroports les plus fréquentés par période
    public List<Map<String, Object>> aeroportsFrequentes(String dateDebut, String dateFin) {
        String query = "SELECT c.nom, COUNT(1) AS nombre_vols " +
                "FROM c JOIN v IN c.vols " +
                "WHERE c.type = 'airport' AND v.date_depart >= @dateDebut AND v.date_depart <= @dateFin " +
                "GROUP BY c.nom " +
                "ORDER BY nombre_vols DESC";

        // Paramètres de la requête SQL
        SqlQuerySpec sqlQuerySpec = new SqlQuerySpec(
                query,
                new SqlParameter("@dateDebut", dateDebut),
                new SqlParameter("@dateFin", dateFin)
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

    // 3. Connexions entre aéroports
    public List<Map<String, Object>> connexionsAeroports() {
        String query = "SELECT c.nom, v.aeroport_arrivee.id, COUNT(1) AS nombre_vols " +
                "FROM c JOIN v IN c.vols " +
                "WHERE c.type = 'airport' " +
                "GROUP BY c.nom, v.aeroport_arrivee.id " +
                "ORDER BY nombre_vols DESC";

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

    // 4. Statistiques des retards par aéroport
    public List<Map<String, Object>> statsRetardsParAeroport() {
        String query = "SELECT c.nom, COUNT(1) AS total_retards, AVG(v.duree_retard) AS retard_moyen " +
                "FROM c JOIN v IN c.vols " +
                "WHERE c.type = 'airport' AND v.statut = 'Delayed' " +
                "GROUP BY c.nom " +
                "ORDER BY total_retards DESC";

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

    // 5. Aéroports par compagnie
    public List<Map<String, Object>> aeroportsParCompagnie() {
        String query = "SELECT c.nom, v.compagnie.id, COUNT(1) AS nombre_vols " +
                "FROM c JOIN v IN c.vols " +
                "WHERE c.type = 'airport' " +
                "GROUP BY c.nom, v.compagnie.id " +
                "ORDER BY nombre_vols DESC";

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
}
