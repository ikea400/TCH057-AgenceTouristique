package com.natael_raphael_guillaume.tourisme.modele.dao;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.natael_raphael_guillaume.tourisme.modele.entite.Client;
import com.natael_raphael_guillaume.tourisme.modele.entite.Voyage;
import com.natael_raphael_guillaume.tourisme.viewModele.EcouteurDeDonnees;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpJsonService {
    private static final String URL_POINT_ENTREE = "http://10.0.2.2:3000";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static void getClients(String email, String mdp, EcouteurDeDonnees chargeurDeDonnees) {
        StringJoiner pathJoiner = new StringJoiner("&");

        if (email != null && !email.isBlank()) {
            pathJoiner.add("email=" + email);
        }
        if (mdp != null && !mdp.isBlank()) {
            pathJoiner.add("mdp=" + mdp);
        }

        String path = pathJoiner.length() > 0 ? "?" + pathJoiner : "";

        OkHttpClient okHttpClient = new OkHttpClient();

        Request request = new Request.Builder()
                .url(URL_POINT_ENTREE + "/clients/" + path)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                assert response.body() != null;
                final String jsonStr = response.body().string();
                //Traitement de la réponse ici
                if (!jsonStr.isEmpty()) {
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        List<Client> resultats = Arrays.asList(mapper.readValue(jsonStr, Client[].class));
                        chargeurDeDonnees.onDataLoaded(resultats);
                    } catch (JsonProcessingException e) {
                        chargeurDeDonnees.onError("Problème du JSON dans les clients reçus: " + jsonStr);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                chargeurDeDonnees.onError("Problème de communication avec le server");
                call.cancel();
            }
        });
    }

    public static void addClients(Client client, EcouteurDeDonnees chargeurDeDonnees) {
        OkHttpClient okHttpClient = new OkHttpClient();

        ObjectMapper mapper = new ObjectMapper();

        String jsonString;
        try {
            jsonString = mapper.writeValueAsString(client);
        } catch (JsonProcessingException e) {
            chargeurDeDonnees.onError("Probleme de sérialisation");
            return;
        }

        RequestBody body = RequestBody.create(jsonString, JSON);
        Request request = new Request.Builder()
                .url(URL_POINT_ENTREE + "/clients")
                .post(body)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                assert response.body() != null;
                final String jsonStr = response.body().string();
                //Traitement de la réponse ici
                if (!jsonStr.isEmpty()) {
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        Client resultat = mapper.readValue(jsonStr, Client.class);
                        chargeurDeDonnees.onDataLoaded(resultat);
                    } catch (JsonProcessingException e) {
                        chargeurDeDonnees.onError("Problème du JSON dans les clients reçus: " + jsonStr);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                chargeurDeDonnees.onError("Problème de communication avec le server");
                call.cancel();
            }
        });
    }

    public static void getVoyages(String destination, int[] budget, String type, String dateDepart, EcouteurDeDonnees chargeurDeDonnees) {
        StringJoiner pathJoiner = new StringJoiner("&");

        if (destination != null && !destination.isBlank()) {
            pathJoiner.add("destination=" + destination);
        }

        if (budget != null && budget.length == 2) {
            pathJoiner.add("prix_gte=" + budget[0]);
            pathJoiner.add("prix_lte=" + budget[1]);
        }

        if (type != null && !type.isBlank()) {
            pathJoiner.add("type_de_voyage=" + type);
        }

        String path = pathJoiner.length() > 0 ? "?" + pathJoiner : "";

        OkHttpClient okHttpClient = new OkHttpClient();

        Request request = new Request.Builder()
                .url(URL_POINT_ENTREE + "/voyages/" + path)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                assert response.body() != null;
                final String jsonStr = response.body().string();
                //Traitement de la réponse ici
                if (!jsonStr.isEmpty()) {
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        List<Voyage> resultats = Arrays.asList(mapper.readValue(jsonStr, Voyage[].class));
                        chargeurDeDonnees.onDataLoaded(resultats);
                    } catch (JsonProcessingException e) {
                        chargeurDeDonnees.onError("Problème du JSON dans les clients reçus: " + jsonStr);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                chargeurDeDonnees.onError("Problème de communication avec le server");
                call.cancel();
            }
        });
    }
}
