package com.natael_raphael_guillaume.tourisme.modele.entite;

import androidx.annotation.NonNull;

public enum ETypeVoyage {

    Aucun("Aucun"),
    Culturel("Culturel"),
    Aventure("Aventure"),
    BienEtre("Bien-être"),
    Nature("Nature");

    ETypeVoyage(String value) {
        this.value = value;
    }

    @NonNull
    @Override
    public String toString() {
        return value;
    }

    private final String value;
}
