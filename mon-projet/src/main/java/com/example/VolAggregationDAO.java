package com.example;

import com.azure.cosmos.*;
import com.azure.cosmos.models.*;
import com.azure.cosmos.util.CosmosPagedIterable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VolAggregationDAO {
    private final CosmosContainer container;

    public VolAggregationDAO(CosmosContainer container) {
        this.container = container;
    }

    // 1. Nombre de vols par compagnie
    public List<Map<String, Object>> countVolsParCompagnie() {
        String query = "SELECT v.compagnie.id, " +
                "VALUE { 'compagnie': v.compagnie.id, 'total_vols': (SELECT VALUE COUNT(1) FROM v) } " +
                "FROM c JOIN v IN c.vols " +
                "WHERE c.type = 'flight' " +
                "GROUP BY v.compagnie.id";

        CosmosQueryRequestOptions options = new CosmosQueryRequestOptions();
        CosmosPagedIterable<Object> results = container.queryItems(query, options, Object.class);
        List<Map<String, Object>> resultList = new ArrayList<>();

        results.forEach(result -> {
            Map<String, Object> resultMap = (Map<String, Object>) result;
            resultList.add(resultMap);
        });

        return resultList;
    }

    // 2. Vols en retard par période
    public List<Map<String, Object>> volsEnRetardParPeriode(String dateDebut, String dateFin) {
        String query = "SELECT v.compagnie.id, COUNT(1) AS nombre_vols_retardes, AVG(v.duree_retard) AS retard_moyen " +
                "FROM c JOIN v IN c.vols " +
                "WHERE c.type = 'flight' AND v.statut = 'Delayed' " +
                "AND v.date_depart >= @dateDebut AND v.date_depart <= @dateFin " +
                "GROUP BY v.compagnie.id " +
                "ORDER BY retard_moyen DESC";

        SqlQuerySpec sqlQuerySpec = new SqlQuerySpec(
                query,
                new SqlParameter("@dateDebut", dateDebut),
                new SqlParameter("@dateFin", dateFin)
        );

        CosmosQueryRequestOptions options = new CosmosQueryRequestOptions();
        CosmosPagedIterable<Object> results = container.queryItems(sqlQuerySpec, options, Object.class);
        List<Map<String, Object>> resultList = new ArrayList<>();

        results.forEach(result -> {
            Map<String, Object> resultMap = (Map<String, Object>) result;
            resultList.add(resultMap);
        });

        return resultList;
    }

    // 3. Connexions entre aéroports (vols entre deux aéroports)
    public List<Map<String, Object>> connexionsEntreAeroports() {
        String query = "SELECT v.aeroport_depart.id, v.aeroport_arrivee.id, COUNT(1) AS nombre_vols " +
                "FROM c JOIN v IN c.vols " +
                "WHERE c.type = 'flight' " +
                "GROUP BY v.aeroport_depart.id, v.aeroport_arrivee.id " +
                "ORDER BY nombre_vols DESC";

        CosmosQueryRequestOptions options = new CosmosQueryRequestOptions();
        CosmosPagedIterable<Object> results = container.queryItems(query, options, Object.class);
        List<Map<String, Object>> resultList = new ArrayList<>();

        results.forEach(result -> {
            Map<String, Object> resultMap = (Map<String, Object>) result;
            resultList.add(resultMap);
        });

        return resultList;
    }

    // 4. Statistiques des retards par aéroport
    public List<Map<String, Object>> statsRetardsParAeroport() {
        String query = "SELECT v.aeroport_depart.id, COUNT(1) AS total_retards, AVG(v.duree_retard) AS retard_moyen " +
                "FROM c JOIN v IN c.vols " +
                "WHERE c.type = 'flight' AND v.statut = 'Delayed' " +
                "GROUP BY v.aeroport_depart.id " +
                "ORDER BY total_retards DESC";

        CosmosQueryRequestOptions options = new CosmosQueryRequestOptions();
        CosmosPagedIterable<Object> results = container.queryItems(query, options, Object.class);
        List<Map<String, Object>> resultList = new ArrayList<>();

        results.forEach(result -> {
            Map<String, Object> resultMap = (Map<String, Object>) result;
            resultList.add(resultMap);
        });

        return resultList;
    }

    // 5. Vols par type d'avion
    public List<Map<String, Object>> volsParTypeAvion() {
        String query = "SELECT v.avion.type, COUNT(1) AS nombre_vols " +
                "FROM c JOIN v IN c.vols " +
                "WHERE c.type = 'flight' " +
                "GROUP BY v.avion.type " +
                "ORDER BY nombre_vols DESC";

        CosmosQueryRequestOptions options = new CosmosQueryRequestOptions();
        CosmosPagedIterable<Object> results = container.queryItems(query, options, Object.class);
        List<Map<String, Object>> resultList = new ArrayList<>();

        results.forEach(result -> {
            Map<String, Object> resultMap = (Map<String, Object>) result;
            resultList.add(resultMap);
        });

        return resultList;
    }
}
