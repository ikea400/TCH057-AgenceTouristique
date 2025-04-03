package com.natael_raphael_guillaume.tourisme.modele.entite;

import java.util.List;

public class Voyage {
    public static class Trip {
        private String date;
        private String nbPlacesDisponibles;

        public Trip() {}

        public Trip(String date, String nbPlacesDisponibles) {
            this.date = date;
            this.nbPlacesDisponibles = nbPlacesDisponibles;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getNbPlacesDisponibles() {
            return nbPlacesDisponibles;
        }

        public void setNbPlacesDisponibles(String nbPlacesDisponibles) {
            this.nbPlacesDisponibles = nbPlacesDisponibles;
        }
    }

    List<Trip> trips;
    String id, nom_voyage, description,  destination, imageUrl, typeDeVoyage, activitesIncluses;
    double prix;
    int dureeJours;

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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTypeDeVoyage() {
        return typeDeVoyage;
    }

    public void setTypeDeVoyage(String typeDeVoyage) {
        this.typeDeVoyage = typeDeVoyage;
    }

    public String getActivitesIncluses() {
        return activitesIncluses;
    }

    public void setActivitesIncluses(String activitesIncluses) {
        this.activitesIncluses = activitesIncluses;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public int getDureeJours() {
        return dureeJours;
    }

    public void setDureeJours(int dureeJours) {
        this.dureeJours = dureeJours;
    }
}
