package com.natael_raphael_guillaume.tourisme.modele;

import com.natael_raphael_guillaume.tourisme.modele.entite.Client;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Modele {
    private List<Client> clients;

    public List<Client> getClients() {
        if (clients == null) {
            return Collections.emptyList();
        }
        return clients;
    }
    public void setClients(List<Client> livres) {
        this.clients = livres;
    }
}
