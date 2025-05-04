package com.example;

import com.azure.cosmos.*;
import com.azure.cosmos.models.*;
import com.azure.cosmos.util.CosmosPagedIterable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PassagerAggregationDAO {
    private final CosmosContainer container;

    public PassagerAggregationDAO(CosmosContainer container) {
        this.container = container;
    }

    // 1. Nombre de passagers par aéroport (en fonction des vols)
    public List<Map<String, Object>> countPassagersParAeroport() {
        String query = "SELECT c.nom, " +
                "VALUE { 'nom': c.nom, 'total_passagers': (SELECT VALUE COUNT(1) FROM p WHERE p.aeroport.id = c.id) } " +
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

    // 2. Passagers les plus fréquents sur une période donnée
    public List<Map<String, Object>> passagersFrequents(String dateDebut, String dateFin) {
        String query = "SELECT p.nom, p.prenom, COUNT(1) AS nombre_vols " +
                "FROM c JOIN v IN c.vols JOIN p IN v.passagers " +
                "WHERE p.dateNaissance >= @dateDebut AND p.dateNaissance <= @dateFin " +
                "GROUP BY p.nom, p.prenom " +
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

    // 3. Connexions passagers-aéroports
    public List<Map<String, Object>> connexionsPassagersAeroports() {
        String query = "SELECT p.nom, p.prenom, v.aeroport_arrivee.id, COUNT(1) AS nombre_vols " +
                "FROM c JOIN v IN c.vols JOIN p IN v.passagers " +
                "WHERE p._id IS NOT NULL " +
                "GROUP BY p.nom, p.prenom, v.aeroport_arrivee.id " +
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

    // 4. Statistiques des passagers par vol
    public List<Map<String, Object>> statsPassagersParVol() {
        String query = "SELECT v.id, COUNT(p._id) AS total_passagers, AVG(v.duree_retard) AS retard_moyen " +
                "FROM c JOIN v IN c.vols JOIN p IN v.passagers " +
                "WHERE p._id IS NOT NULL " +
                "GROUP BY v.id " +
                "ORDER BY total_passagers DESC";

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

    // 5. Passagers par compagnie
    public List<Map<String, Object>> passagersParCompagnie() {
        String query = "SELECT v.compagnie.id, COUNT(p._id) AS nombre_passagers " +
                "FROM c JOIN v IN c.vols JOIN p IN v.passagers " +
                "WHERE p._id IS NOT NULL " +
                "GROUP BY v.compagnie.id " +
                "ORDER BY nombre_passagers DESC";

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
