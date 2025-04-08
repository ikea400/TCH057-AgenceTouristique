package com.natael_raphael_guillaume.tourisme.viewModele;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.natael_raphael_guillaume.tourisme.modele.Modele;
import com.natael_raphael_guillaume.tourisme.modele.ModeleManager;
import com.natael_raphael_guillaume.tourisme.modele.dao.ClientDao;
import com.natael_raphael_guillaume.tourisme.modele.dao.VoyageDao;
import com.natael_raphael_guillaume.tourisme.modele.entite.Client;
import com.natael_raphael_guillaume.tourisme.modele.entite.Voyage;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class DataViewModel extends ViewModel {
    private final MutableLiveData<List<Client>> clientLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Voyage>> voyageLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> erreurLiveData = new MutableLiveData<>();

    private final Modele modele;

    public DataViewModel() {
        this.modele = ModeleManager.getModele();
    }

    public LiveData<List<Client>> getClients() {
        return clientLiveData;
    }

    public LiveData<List<Voyage>> getVoyages() {
        return voyageLiveData;
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
            erreurLiveData.postValue("Problème dans le JSON des clients");
        } catch (IOException e) {
            erreurLiveData.postValue("Problème d'accès à l'API");
        }
    }

    public void ajouterClient(Client client) {
        try {
            ClientDao.addClient(client, new EcouteurDeDonnees() {
                @Override
                public void onDataLoaded(Object data) {
                    Client client = (Client) data;

                    modele.addClient(client);
                    clientLiveData.postValue(modele.getClients());
                }

                @Override
                public void onError(String errorMessage) {
                    erreurLiveData.postValue(errorMessage);
                }
            });
        } catch (JSONException e) {
            erreurLiveData.postValue("Problème dans le JSON des clients");
        } catch (IOException e) {
            erreurLiveData.postValue("Problème d'accès à l'API");
        }
    }

    public void trouverVoyages(String destination, int[] budget, String type, String dateDepart) {
        try {
            VoyageDao.getVoyages(destination, budget, type, dateDepart, new EcouteurDeDonnees() {
                @Override
                public void onDataLoaded(Object data) {
                    List<Voyage> voyages = (List<Voyage>) data;
                    modele.setVoyages(voyages);
                    voyageLiveData.postValue(voyages);
                }

                @Override
                public void onError(String errorMessage) {
                    erreurLiveData.postValue(errorMessage);
                }
            });
        } catch (JSONException e) {
            erreurLiveData.postValue("Problème dans le JSON des clients");
        } catch (IOException e) {
            erreurLiveData.postValue("Problème d'accès à l'API");
        }
    }

    public void reserverVoyage(String id, String dateDepart, int nbPersonnes) {
        try {
            VoyageDao.reserverVoyages(id, dateDepart, nbPersonnes, new EcouteurDeDonnees() {
                @Override
                public void onDataLoaded(Object data) {
                    Voyage voyage = (Voyage) data;

                    List<Voyage> voyages = modele.getVoyages();

                    for (int i = 0; i < voyages.size(); i++) {
                        if (voyages.get(i).getId().equals(voyage.getId())) {
                            voyages.set(i, voyage);
                        }
                    }

                    modele.setVoyages(voyages);
                    voyageLiveData.postValue(voyages);
                }

                @Override
                public void onError(String errorMessage) {
                    erreurLiveData.postValue(errorMessage);
                }
            });
        } catch (JSONException e) {
            erreurLiveData.postValue("Problème dans le JSON des clients");
        } catch (IOException e) {
            erreurLiveData.postValue("Problème d'accès à l'API");
        }
    }

    public void annulerVoyage(String id, String dateDepart, int nbPersonnes) {
        try {
            VoyageDao.annulerVoyages(id, dateDepart, nbPersonnes, new EcouteurDeDonnees() {
                @Override
                public void onDataLoaded(Object data) {
                    Voyage voyage = (Voyage) data;

                    List<Voyage> voyages = modele.getVoyages();

                    for (int i = 0; i < voyages.size(); i++) {
                        if (voyages.get(i).getId().equals(voyage.getId())) {
                            voyages.set(i, voyage);
                        }
                    }

                    modele.setVoyages(voyages);
                    voyageLiveData.postValue(voyages);
                }

                @Override
                public void onError(String errorMessage) {
                    erreurLiveData.postValue(errorMessage);
                }
            });
        } catch (JSONException e) {
            erreurLiveData.postValue("Problème dans le JSON des clients");
        } catch (IOException e) {
            erreurLiveData.postValue("Problème d'accès à l'API");
        }
    }
}
