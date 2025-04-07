package com.natael_raphael_guillaume.tourisme.modele.entite;

import java.io.Serializable;
import java.util.List;

public class Voyage implements Serializable {
    public static class Trip implements Serializable {
        private String date;
        private String nb_places_disponibles;

        public Trip() {}

        public Trip(String date, String nbPlacesDisponibles) {
            this.date = date;
            this.nb_places_disponibles = nbPlacesDisponibles;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getNb_places_disponibles() {
            return nb_places_disponibles;
        }

        public void setNb_places_disponibles(String nb_places_disponibles) {
            this.nb_places_disponibles = nb_places_disponibles;
        }
    }

    List<Trip> trips;
    String id, nom_voyage, description,  destination, image_url, type_de_voyage, activites_incluses;
    double prix;
    int duree_jours;

    public Voyage() {}

    public List<Trip> getTrips() {
        return trips;
    }

    public void setTrips(List<Trip> trips) {
        this.trips = trips;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom_voyage() {
        return nom_voyage;
    }

    public void setNom_voyage(String nom_voyage) {
        this.nom_voyage = nom_voyage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getType_de_voyage() {
        return type_de_voyage;
    }

    public void setType_de_voyage(String type_de_voyage) {
        this.type_de_voyage = type_de_voyage;
    }

    public String getActivites_incluses() {
        return activites_incluses;
    }

    public void setActivites_incluses(String activites_incluses) {
        this.activites_incluses = activites_incluses;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public int getDuree_jours() {
        return duree_jours;
    }

    public void setDuree_jours(int duree_jours) {
        this.duree_jours = duree_jours;
    }
}
