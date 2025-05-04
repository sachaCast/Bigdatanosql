package com.example;

import com.azure.cosmos.*;
import com.azure.cosmos.models.*;
import com.azure.cosmos.util.CosmosPagedIterable;

import java.util.List;

public class EquipageDAO {
    private final CosmosContainer container;

    public EquipageDAO(CosmosContainer container) {
        this.container = container;
    }

    // Insérer un équipage
    public void insertEquipage(Equipage equipage) {
        if (findById(equipage.getId()) == null) {
            container.createItem(equipage, new PartitionKey(equipage.get_id()), new CosmosItemRequestOptions());
            System.out.println("Equipage inséré avec succès.");
        } else {
            System.out.println("L'équipage avec l'ID " + equipage.getId() + " existe déjà.");
        }
    }

    // Insérer plusieurs équipages
    public void insertEquipages(List<Equipage> equipages) {
        for (Equipage equipage : equipages) {
            container.createItem(equipage, new PartitionKey(equipage.get_id()), new CosmosItemRequestOptions());
        }
    }

    // Mettre à jour un équipage
    public void updateEquipage(String idEquipage, String field, Object newValue) {
        Equipage equipage = findById(idEquipage);
        if (equipage != null) {
            switch (field) {
                case "commandant":
                    equipage.setCommandant((Pilote) newValue);
                    break;
                case "opl":
                    equipage.setOpl((Pilote) newValue);
                    break;
                default:
                    System.out.println("Champ non pris en charge : " + field);
                    return;
            }

            container.upsertItem(equipage, new PartitionKey(equipage.get_id()), new CosmosItemRequestOptions());
            System.out.println("Equipage mis à jour avec succès.");
        } else {
            System.out.println("Equipage avec l'ID " + idEquipage + " non trouvé.");
        }
    }

    // Supprimer un équipage
    public void deleteEquipage(String idEquipage) {
        Equipage equipage = findById(idEquipage);
        if (equipage != null) {
            container.deleteItem(equipage.getId(), new PartitionKey(equipage.get_id()), new CosmosItemRequestOptions());
            System.out.println("Equipage supprimé avec succès.");
        } else {
            System.out.println("Equipage avec l'ID " + idEquipage + " non trouvé.");
        }
    }

    // Trouver un équipage par ID
    public Equipage findById(String id) {
        String query = "SELECT * FROM c WHERE c.id = @id";
        SqlParameter param = new SqlParameter("@id", id);
        SqlQuerySpec querySpec = new SqlQuerySpec(query, List.of(param));

        CosmosQueryRequestOptions options = new CosmosQueryRequestOptions();
        CosmosPagedIterable<Equipage> results = container.queryItems(querySpec, options, Equipage.class);

        for (Equipage equipage : results) {
            return equipage;
        }
        return null;
    }
}
