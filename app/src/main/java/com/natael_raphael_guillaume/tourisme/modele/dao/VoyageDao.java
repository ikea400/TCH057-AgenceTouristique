package com.natael_raphael_guillaume.tourisme.modele.dao;

import com.natael_raphael_guillaume.tourisme.viewModele.EcouteurDeDonnees;

import org.json.JSONException;

import java.io.IOException;

public class VoyageDao {
    public static void getVoyages(String destination, int[] budget, String type, String dateDepart, EcouteurDeDonnees chargeurDeDonnees)
            throws JSONException, IOException {
        HttpJsonService.getVoyages(destination, budget, type, dateDepart, chargeurDeDonnees);
    }

}
