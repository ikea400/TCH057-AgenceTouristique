package com.natael_raphael_guillaume.tourisme.viewModele;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.natael_raphael_guillaume.tourisme.modele.Modele;
import com.natael_raphael_guillaume.tourisme.modele.ModeleManager;
import com.natael_raphael_guillaume.tourisme.modele.dao.ClientDao;
import com.natael_raphael_guillaume.tourisme.modele.entite.Client;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class ClientViewModel extends ViewModel {
    private final MutableLiveData<List<Client>> clientLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> erreurLiveData = new MutableLiveData<>();

    private final Modele modele;

    public ClientViewModel() {
        this.modele = ModeleManager.getModele();
    }

    public LiveData<List<Client>> getClients() {
        return clientLiveData;
    }

    public LiveData<String> getErreur() {
        return erreurLiveData;
    }

    public void trouverClient(String email, String mdp) {
        try {
            ClientDao.getClients(email, mdp, new EcouteurDeDonnees() {
                @Override
                public void onDataLoaded(Object data) {
                    List<Client> clients = (List<Client>) data;
                    modele.setClients(clients);
                    clientLiveData.postValue(clients);
                }

                @Override
                public void onError(String errorMessage) {
                    erreurLiveData.postValue(errorMessage);
                }
            });
        } catch (JSONException e) {
            erreurLiveData.postValue("Problème dans le JSON des comptes");
        } catch (IOException e) {
            erreurLiveData.postValue("Problème d'accès à l'API");
        }
    }
}
