package com.example;

import com.azure.cosmos.*;
import com.azure.cosmos.models.*;
import com.azure.cosmos.util.CosmosPagedIterable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EquipageAggregationDAO {
    private final CosmosContainer container;

    public EquipageAggregationDAO(CosmosContainer container) {
        this.container = container;
    }

    // 1. Nombre d'équipages par pilote
    public List<Map<String, Object>> countEquipagesParPilote() {
        String query = "SELECT p.nom, " +
                "VALUE { 'nom': p.nom, 'total_equipages': (SELECT VALUE COUNT(1) FROM e WHERE e.commandant.id = p.id OR e.opl.id = p.id) } " +
                "FROM p WHERE p.type = 'pilote'";

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

    // 2. Equipages les plus fréquents par période
    public List<Map<String, Object>> equipagesFrequent(String dateDebut, String dateFin) {
        String query = "SELECT e.commandant.nom, COUNT(1) AS nombre_equipages " +
                "FROM e JOIN v IN e.vols " +
                "WHERE e.type = 'equipage' AND v.date_depart >= @dateDebut AND v.date_depart <= @dateFin " +
                "GROUP BY e.commandant.nom " +
                "ORDER BY nombre_equipages DESC";

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

    // 3. Connexions entre équipages
    public List<Map<String, Object>> connexionsEquipages() {
        String query = "SELECT e.commandant.nom, v.vol_arrivee.id, COUNT(1) AS nombre_vols " +
                "FROM e JOIN v IN e.vols " +
                "WHERE e.type = 'equipage' " +
                "GROUP BY e.commandant.nom, v.vol_arrivee.id " +
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

    // 4. Statistiques des retards par équipage
    public List<Map<String, Object>> statsRetardsParEquipage() {
        String query = "SELECT e.commandant.nom, COUNT(1) AS total_retards, AVG(v.duree_retard) AS retard_moyen " +
                "FROM e JOIN v IN e.vols " +
                "WHERE e.type = 'equipage' AND v.statut = 'Delayed' " +
                "GROUP BY e.commandant.nom " +
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

    // 5. Equipages par vol
    public List<Map<String, Object>> equipagesParVol() {
        String query = "SELECT e.commandant.nom, v.vol.id, COUNT(1) AS nombre_vols " +
                "FROM e JOIN v IN e.vols " +
                "WHERE e.type = 'equipage' " +
                "GROUP BY e.commandant.nom, v.vol.id " +
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
