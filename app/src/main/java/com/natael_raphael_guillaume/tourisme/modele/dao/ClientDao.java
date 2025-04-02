package com.natael_raphael_guillaume.tourisme.modele.dao;

import com.natael_raphael_guillaume.tourisme.modele.entite.Client;
import com.natael_raphael_guillaume.tourisme.viewModele.EcouteurDeDonnees;

import org.json.JSONException;

import java.io.IOException;

public class ClientDao {
    public static void getClients(String email, String mdp,
                                  EcouteurDeDonnees ecouteurDeDonnees)
            throws JSONException, IOException {
        HttpJsonService.getClients(email, mdp, ecouteurDeDonnees);
    }

    public static void addClient(Client client, EcouteurDeDonnees ecouteurDeDonnees)
            throws JSONException, IOException{
        HttpJsonService.addClients(client, ecouteurDeDonnees);
    }
}
