package com.natael_raphael_guillaume.tourisme.modele;

import com.natael_raphael_guillaume.tourisme.modele.entite.Client;
import com.natael_raphael_guillaume.tourisme.modele.entite.Voyage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Modele {
    private List<Client> clients;
    private List<Voyage> voyages;

    public List<Client> getClients() {
        if (clients == null) {
            return Collections.emptyList();
        }
        return clients;
    }
    public void setClients(List<Client> clients) {
        this.clients = clients;
    }

    public void addClient(Client client) {
        if (clients == null) {
            clients = new ArrayList<>();
        }
        clients.add(client);
    }

    public List<Voyage> getVoyages() {
        if (voyages == null) {
            return Collections.emptyList();
        }
        return voyages;
    }

    public void setVoyages(List<Voyage> voyages) {
        this.voyages = voyages;
    }
}
