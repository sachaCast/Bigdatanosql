package com.example;

import com.azure.cosmos.*;
import com.azure.cosmos.models.*;
import com.azure.cosmos.util.CosmosPagedIterable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReservationAggregationDAO {
    private final CosmosContainer container;

    public ReservationAggregationDAO(CosmosContainer container) {
        this.container = container;
    }

    // 1. Nombre de réservations par passager
    public List<Map<String, Object>> countReservationsByPassager() {
        String query = "SELECT c.passager._id, COUNT(1) AS total_reservations " +
                       "FROM c " +
                       "GROUP BY c.passager._id";

        CosmosQueryRequestOptions options = new CosmosQueryRequestOptions();
        CosmosPagedIterable<Object> results = container.queryItems(query, options, Object.class);
        List<Map<String, Object>> resultList = new ArrayList<>();

        results.forEach(result -> {
            Map<String, Object> resultMap = (Map<String, Object>) result;
            resultList.add(resultMap);
        });

        return resultList;
    }

    // 2. Réservations par vol
    public List<Map<String, Object>> reservationsByVol() {
        String query = "SELECT c.vol._id, COUNT(1) AS total_reservations " +
                       "FROM c " +
                       "GROUP BY c.vol._id";

        CosmosQueryRequestOptions options = new CosmosQueryRequestOptions();
        CosmosPagedIterable<Object> results = container.queryItems(query, options, Object.class);
        List<Map<String, Object>> resultList = new ArrayList<>();

        results.forEach(result -> {
            Map<String, Object> resultMap = (Map<String, Object>) result;
            resultList.add(resultMap);
        });

        return resultList;
    }

    // 3. Réservations par compagnie émettrice
    public List<Map<String, Object>> reservationsByCompagnieEmission() {
        String query = "SELECT c.idCompagnieEmission, COUNT(1) AS total_reservations " +
                       "FROM c " +
                       "GROUP BY c.idCompagnieEmission";

        CosmosQueryRequestOptions options = new CosmosQueryRequestOptions();
        CosmosPagedIterable<Object> results = container.queryItems(query, options, Object.class);
        List<Map<String, Object>> resultList = new ArrayList<>();

        results.forEach(result -> {
            Map<String, Object> resultMap = (Map<String, Object>) result;
            resultList.add(resultMap);
        });

        return resultList;
    }

    // 4. Réservations par classe
    public List<Map<String, Object>> reservationsByClasse() {
        String query = "SELECT c.classe, COUNT(1) AS total_reservations " +
                       "FROM c " +
                       "GROUP BY c.classe";

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
