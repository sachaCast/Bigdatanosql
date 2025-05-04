package com.example;

import com.azure.cosmos.*;
import com.azure.cosmos.models.*;
import com.azure.cosmos.util.CosmosPagedIterable;

import java.util.List;

public class VolDAO {
    private final CosmosContainer container;

    public VolDAO(CosmosContainer container) {
        this.container = container;
    }

    // Insérer un vol
    public void insertVol(Vol vol) {
        if (findById(vol.get_id()) == null) {
            container.createItem(vol, new PartitionKey(vol.get_id()), new CosmosItemRequestOptions());
            System.out.println("Vol inséré avec succès.");
        } else {
            System.out.println("Le vol avec l'ID " + vol.get_id() + " existe déjà.");
        }
    }

    // Insérer plusieurs vols
    public void insertVols(List<Vol> vols) {
        for (Vol vol : vols) {
            container.createItem(vol, new PartitionKey(vol.get_id()), new CosmosItemRequestOptions());
        }
    }

    // Mettre à jour un vol
    public void updateVol(String idVol, String field, Object newValue) {
        Vol vol = findById(idVol);
        if (vol != null) {
            switch (field) {
                case "numeroVolBase":
                    vol.setNumeroVolBase((String) newValue);
                    break;
                case "statut":
                    vol.setStatut((String) newValue);
                    break;
                case "compagnie":
                    vol.setCompagnie((Vol.Compagnie) newValue);
                    break;
                case "aeroportDepart":
                    vol.setAeroportDepart((Vol.Aeroport) newValue);
                    break;
                case "aeroportArrivee":
                    vol.setAeroportArrivee((Vol.Aeroport) newValue);
                    break;
                case "avion":
                    vol.setAvion((Vol.AvionSimple) newValue);
                    break;
                case "equipage":
                    vol.setEquipage((Vol.Equipage) newValue);
                    break;
                default:
                    System.out.println("Champ non pris en charge : " + field);
                    return;
            }

            container.upsertItem(vol, new PartitionKey(vol.get_id()), new CosmosItemRequestOptions());
            System.out.println("Vol mis à jour avec succès.");
        } else {
            System.out.println("Vol avec l'ID " + idVol + " non trouvé.");
        }
    }

    // Supprimer un vol
    public void deleteVol(String idVol) {
        Vol vol = findById(idVol);
        if (vol != null) {
            container.deleteItem(vol.get_id(), new PartitionKey(vol.get_id()), new CosmosItemRequestOptions());
            System.out.println("Vol supprimé avec succès.");
        } else {
            System.out.println("Vol avec l'ID " + idVol + " non trouvé.");
        }
    }

    // Trouver un vol par ID
    public Vol findById(String id) {
        String query = "SELECT * FROM c WHERE c._id = @id";
        SqlParameter param = new SqlParameter("@id", id);
        SqlQuerySpec querySpec = new SqlQuerySpec(query, List.of(param));

        CosmosQueryRequestOptions options = new CosmosQueryRequestOptions();
        CosmosPagedIterable<Vol> results = container.queryItems(querySpec, options, Vol.class);

        for (Vol vol : results) {
            return vol;
        }
        return null;
    }
}
