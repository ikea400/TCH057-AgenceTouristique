package com.natael_raphael_guillaume.tourisme.viewModele;

public interface EcouteurDeDonnees {
    void onDataLoaded(Object data);
    void onError(String errorMessage);
}