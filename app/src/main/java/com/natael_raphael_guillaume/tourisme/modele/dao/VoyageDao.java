package com.natael_raphael_guillaume.tourisme.modele.dao;

import com.natael_raphael_guillaume.tourisme.viewModele.EcouteurDeDonnees;

import org.json.JSONException;

import java.io.IOException;

public class VoyageDao {
    public static void getVoyages(String destination, int[] budget, String type, String dateDepart, EcouteurDeDonnees chargeurDeDonnees)
            throws JSONException, IOException {
        HttpJsonService.getVoyages(destination, budget, type, dateDepart, chargeurDeDonnees);
    }

    public static void reserverVoyages(String id, String dateDepart, int nbPersonnes, EcouteurDeDonnees ecouteurDeDonnees)
            throws JSONException, IOException{
        HttpJsonService.modifierVoyagePlaces(id, dateDepart, -nbPersonnes, ecouteurDeDonnees);
    }

    public static void annulerVoyages(String id, String dateDepart, int nbPersonnes, EcouteurDeDonnees ecouteurDeDonnees)
            throws JSONException, IOException{
        HttpJsonService.modifierVoyagePlaces(id, dateDepart, nbPersonnes, ecouteurDeDonnees);
    }


}
