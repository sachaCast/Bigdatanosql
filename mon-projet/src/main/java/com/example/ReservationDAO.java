package com.example;

import com.azure.cosmos.*;
import com.azure.cosmos.models.*;
import com.azure.cosmos.util.CosmosPagedIterable;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {
    private final CosmosContainer container;

    public ReservationDAO(CosmosContainer container) {
        this.container = container;
    }

    public void createReservation(Reservation reservation) {
        CosmosItemResponse<Reservation> response = container.createItem(reservation);
        System.out.println("Réservation créée avec l'ID: " + reservation.get_id());
    }

    public Reservation getReservationById(String reservationId) {
        CosmosItemResponse<Reservation> response = container.readItem(reservationId, new PartitionKey(reservationId), Reservation.class);
        return response.getItem();
    }

    public void updateReservation(Reservation reservation) {
        CosmosItemResponse<Reservation> response = container.upsertItem(reservation);
        System.out.println("Réservation mise à jour avec l'ID: " + reservation.get_id());
    }

    public List<Reservation> getReservationsByPassagerId(String passagerId) {
        String query = "SELECT * FROM c WHERE c.passager._id = @passagerId";
        SqlQuerySpec sqlQuerySpec = new SqlQuerySpec(query, new SqlParameter("@passagerId", passagerId));

        CosmosQueryRequestOptions options = new CosmosQueryRequestOptions();
        CosmosPagedIterable<Reservation> results = container.queryItems(sqlQuerySpec, options, Reservation.class);
        List<Reservation> reservationList = new ArrayList<>();
        results.forEach(reservationList::add);
        return reservationList;
    }
}
