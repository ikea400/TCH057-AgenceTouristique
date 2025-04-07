package com.natael_raphael_guillaume.tourisme.modele.dao;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.natael_raphael_guillaume.tourisme.modele.entite.Client;
import com.natael_raphael_guillaume.tourisme.modele.entite.Voyage;
import com.natael_raphael_guillaume.tourisme.viewModele.EcouteurDeDonnees;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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

                        if (dateDepart != null && !dateDepart.isEmpty() && !resultats.isEmpty()) {
                            resultats = new ArrayList<>(resultats);
                            resultats.removeIf(voyage ->
                                    voyage.getTrips()
                                            .stream()
                                            .noneMatch(trip -> dateDepart.equals(trip.getDate()))
                            );
                        }

                        chargeurDeDonnees.onDataLoaded(resultats);
                    } catch (JsonProcessingException e) {
                        chargeurDeDonnees.onError("Problème du JSON dans les voyages reçus: " + e);
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

    public static void modifierVoyagePlaces(String id, String date, int placesDelta, EcouteurDeDonnees ecouteurDeDonnees) {
        getVoyagePlaces(id, date, new EcouteurDeDonnees() {
            @Override
            public void onDataLoaded(Object data) {
                List<Voyage.Trip> trips = (List<Voyage.Trip>) data;
                for (Voyage.Trip trip : trips) {
                    if (trip.getDate().equals(date)) {
                        trip.setNb_places_disponibles(trip.getNb_places_disponibles() + placesDelta);
                    }
                }

                setVoyagePlaces(id, date, trips, ecouteurDeDonnees);
            }

            @Override
            public void onError(String errorMessage) {
                ecouteurDeDonnees.onError(errorMessage);
            }
        });
    }

    private static void getVoyagePlaces(String id, String date, EcouteurDeDonnees ecouteurDeDonnees) {
        OkHttpClient okHttpClient = new OkHttpClient();

        Request request = new Request.Builder()
                .url(URL_POINT_ENTREE + "/voyages/" + id)
                .build();
        System.out.println(URL_POINT_ENTREE + "/voyages/" + id);
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                ecouteurDeDonnees.onError("Problème de communication avec le server");
                call.cancel();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                assert response.body() != null;
                final String jsonStr = response.body().string();
                //Traitement de la réponse ici
                if (!jsonStr.isEmpty()) {
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        List<Voyage.Trip> resultats = mapper.readValue(jsonStr, Voyage.class).getTrips();

                        ecouteurDeDonnees.onDataLoaded(resultats);
                    } catch (JsonProcessingException e) {
                        ecouteurDeDonnees.onError("Problème du JSON dans le voyage reçus du get: " + jsonStr);
                    }
                }
            }
        });
    }

    private static void setVoyagePlaces(String id, String date, List<Voyage.Trip> trips, EcouteurDeDonnees ecouteurDeDonnees) {
        OkHttpClient okHttpClient = new OkHttpClient();
        ObjectMapper mapper = new ObjectMapper();

        RequestBody body;
        try {
            JSONObject object = new JSONObject();
            // Serialize trips into a JSON array
            String tripsJson = mapper.writeValueAsString(trips);
            object.put("trips", new JSONArray(tripsJson));
            body = RequestBody.create(object.toString(), JSON);
        } catch (JsonProcessingException | JSONException e) {
            ecouteurDeDonnees.onError("Problème du JSON dans les trips reçus");
            return;
        }

        Request request = new Request.Builder()
                .url(URL_POINT_ENTREE + "/voyages/" + id)
                .patch(body)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                ecouteurDeDonnees.onError("Problème de communication avec le server");
                call.cancel();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                assert response.body() != null;

                final String jsonStr = response.body().string();
                try {
                    Voyage resultats = mapper.readValue(jsonStr, Voyage.class);
                    ecouteurDeDonnees.onDataLoaded(resultats);
                } catch (JsonProcessingException e) {
                    ecouteurDeDonnees.onError("Problème du JSON dans le voyage reçus: " + jsonStr);
                }
            }
        });
    }

    public static void reserverVoyage(String id, String date, EcouteurDeDonnees ecouteurDeDonnees) {
        OkHttpClient okHttpClient = new OkHttpClient();

        Request request = new Request.Builder()
                .url(URL_POINT_ENTREE + "/voyages/" + id)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                ecouteurDeDonnees.onError("Problème de communication avec le server");
                call.cancel();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                assert response.body() != null;
                final String jsonStr = response.body().string();
                //Traitement de la réponse ici
                if (!jsonStr.isEmpty()) {
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        List<Voyage.Trip> resultats = Arrays.asList(mapper.readValue(jsonStr, Voyage.Trip[].class));

                        for (Voyage.Trip trip : resultats) {
                            if (trip.getDate().equals(date)) {
                                trip.setNb_places_disponibles(trip.getNb_places_disponibles() - 1);
                            }
                        }

                        RequestBody body = RequestBody.create(mapper.writeValueAsString(resultats), JSON);
                        Request request2 = new Request.Builder()
                                .url(URL_POINT_ENTREE + "/voyages/" + id)
                                .post(body)
                                .build();

                        okHttpClient.newCall(request2).enqueue(new Callback() {
                            @Override
                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                ecouteurDeDonnees.onError("Problème de communication avec le server");
                                call.cancel();
                            }

                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                assert response.body() != null;
                                final String jsonStr2 = response.body().string();

                                Voyage resultats2 = mapper.readValue(jsonStr2, Voyage.class);
                                ecouteurDeDonnees.onDataLoaded(resultats2);
                            }
                        });

                    } catch (JsonProcessingException e) {
                        ecouteurDeDonnees.onError("Problème du JSON dans les trips reçus: " + jsonStr);
                    }
                }
            }
        });
    }
}
