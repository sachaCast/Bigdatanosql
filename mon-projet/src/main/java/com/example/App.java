package com.example;

import com.azure.cosmos.*;
import com.azure.cosmos.models.*;
import com.azure.cosmos.util.CosmosPagedIterable;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class App {
    /* DE COMMENTER CETTE ZONE DU CODE
    private static final String ACCOUNT_ENDPOINT = "https://sacha.documents.azure.com:443/";
    private static final String ACCOUNT_KEY = "GNwnoLYpRkc8QmKeMjFmHKrMfXtZd7oxpppvljjuLrdkkqzTNcZWkIVSDz1rpZJyXBkyZavOLKNIACDbrnQSAw==";
    private static final String DATABASE_NAME = "compagnieDB";
    private static final String CONTAINER_NAME = "compagnies";*/

    public static void main(String[] args) {
        CosmosClient client = new CosmosClientBuilder()
                .endpoint(ACCOUNT_ENDPOINT)
                .key(ACCOUNT_KEY)
                .consistencyLevel(ConsistencyLevel.EVENTUAL)
                .buildClient();

        // Créer ou obtenir la base de données
        CosmosDatabaseResponse response = client.createDatabaseIfNotExists(DATABASE_NAME);
        CosmosDatabase database = client.getDatabase(response.getProperties().getId());

        // Créer ou obtenir le conteneur
        CosmosContainerResponse containerResponse = database.createContainerIfNotExists(
                new CosmosContainerProperties(CONTAINER_NAME, "/_id"),
                ThroughputProperties.createManualThroughput(400)
        );

        CosmosContainer container = client.getDatabase(DATABASE_NAME).getContainer(CONTAINER_NAME);

        System.out.println("Base de données et conteneur prêts.");

        // Vérifie s’il y a des données déjà présentes
        CosmosPagedIterable<Object> results = container.queryItems("SELECT * FROM c", new CosmosQueryRequestOptions(), Object.class);
        boolean isContainerEmpty = !results.iterator().hasNext();

        if (isContainerEmpty) {
            System.out.println("Le conteneur est vide. Insertion des données...");

            // Insère les 20 premiers documents de chaque fichier JSON avec leur type
            insererDonnees(container, "src/main/resources/aircrafts.json", "aircraft");
            insererDonnees(container, "src/main/resources/airlines.json", "airline");
            insererDonnees(container, "src/main/resources/airports.json", "airport");
            insererDonnees(container, "src/main/resources/flights.json", "flight");
            insererDonnees(container, "src/main/resources/reservations.json", "reservation");
            insererDonnees(container, "src/main/resources/passengers.json", "passenger");
            insererDonnees(container, "src/main/resources/pilots.json", "pilot");

            System.out.println("Les données ont été insérées dans le conteneur.");
        } else {
            System.out.println("Le conteneur contient déjà des données.");
        }

        // Test resuates
        if (!isContainerEmpty) {
            System.out.println("Début du test des requêtes...");

            // Test de l'insertion d'un aéroport
            AeroportDAO aeroportDAO = new AeroportDAO(container);
            Aeroport aeroport = new Aeroport();
            aeroport.setId("1000000");  // Utilisation de 'id' au lieu de '_id'
            aeroport.setNom("Aeroport de Paris");
            aeroport.setVille("Paris");
            aeroport.setPays("France");
            aeroportDAO.insertAeroport(aeroport);

            // Suppression de l'aéroport via AeroportDAO
            aeroportDAO.deleteAeroport("1000000");


        }


        client.close();
    }

    public static void insererDonnees(CosmosContainer container, String cheminFichierJson, String type) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<Map<String, Object>> documents = mapper.readValue(
                new File(cheminFichierJson),
                new TypeReference<List<Map<String, Object>>>() {}
            );

            // Sélectionner uniquement les 20 premiers documents
            List<Map<String, Object>> first20Documents = documents.subList(0, Math.min(20, documents.size()));

            // Insérer les 20 premiers documents
            for (Map<String, Object> doc : first20Documents) {
                // Si '_id' existe, le renommer en 'id'
                if (doc.containsKey("_id")) {
                    Object idValue = doc.remove("_id");
                    doc.put("id", idValue);
                } else {
                    // Si '_id' n'existe pas, créer un ID unique pour chaque document
                    doc.put("id", java.util.UUID.randomUUID().toString());
                }

                // Ajouter un champ 'type' pour spécifier le type de document
                doc.put("type", type);

                // Insérer l'élément dans le conteneur Cosmos DB
                container.createItem(doc);
            }

            System.out.println("20 premiers documents de type " + type + " insérés depuis : " + cheminFichierJson);
        } catch (IOException e) {
            System.err.println("Erreur lecture fichier : " + cheminFichierJson);
            e.printStackTrace();
        } catch (CosmosException e) {
            System.err.println("Erreur Cosmos DB lors de l’insertion.");
            e.printStackTrace();
        }
    }
}
