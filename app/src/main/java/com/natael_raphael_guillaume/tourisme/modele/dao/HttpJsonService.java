package com.natael_raphael_guillaume.tourisme.modele.dao;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.natael_raphael_guillaume.tourisme.modele.entite.Client;
import com.natael_raphael_guillaume.tourisme.viewModele.EcouteurDeDonnees;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpJsonService {
    private static final String URL_POINT_ENTREE = "http://10.0.2.2:3000";

    public static void getClients(String email, String mdp, EcouteurDeDonnees chargeurDeDonnees) {
        //?email=francois.dubois@example.com&mdp=Dubois!456
        String path = "";
        if (email != null && !email.isBlank()) {
            path += "email=" + email;
        }
        if (mdp != null && !mdp.isBlank()) {
            if (!path.isEmpty()) {
                path += "&";
            }
            path += "mdp=" + mdp;
        }

        if (!path.isEmpty()) {
            path = "?" + path;
        }

        OkHttpClient okHttpClient = new OkHttpClient();

        Request request = new Request.Builder()
                .url(URL_POINT_ENTREE + "/clients/"+path)
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
                chargeurDeDonnees.onError("Problème");
                call.cancel();
            }
        });
    }
}
