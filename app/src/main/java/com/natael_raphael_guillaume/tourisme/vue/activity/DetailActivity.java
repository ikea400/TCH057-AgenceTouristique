package com.natael_raphael_guillaume.tourisme.vue.activity;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.natael_raphael_guillaume.tourisme.R;
import com.natael_raphael_guillaume.tourisme.modele.dao.HistoriqueDao;
import com.natael_raphael_guillaume.tourisme.modele.entite.Voyage;
import com.natael_raphael_guillaume.tourisme.viewModele.DataViewModel;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DetailActivity extends AppCompatActivity {
    private TextView lblPrixDetailVoyage;
    private TextView lblDestinationDetailVoyage;
    private TextView lblDureeDetailVoyage;
    private TextView lblDescriptionDetailVoyage;
    private Spinner spDetailDates;
    private Button btnDetailReserver;
    private ImageView imageDetailVoyage;
    private EditText nbDePersonnesDetailVoyage;

    private Voyage voyage;
    private DataViewModel dataViewModel;
    private Voyage.Trip trip;
    private int nbPersonnes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        lblPrixDetailVoyage = findViewById(R.id.lblPrixDetailVoyage);
        lblDestinationDetailVoyage = findViewById(R.id.lblDestinationDetailVoyage);
        lblDureeDetailVoyage = findViewById(R.id.lblDureeDetailVoyage);
        lblDescriptionDetailVoyage = findViewById(R.id.lblDescriptionDetailVoyage);
        spDetailDates = findViewById(R.id.spDetailDates);
        btnDetailReserver = findViewById(R.id.btnDetailReserver);
        imageDetailVoyage = findViewById(R.id.ImageDetailVoyage);
        nbDePersonnesDetailVoyage = findViewById(R.id.nbDePersonnesDetailVoyage);

        Intent intent = getIntent();
        voyage = (Voyage) intent.getSerializableExtra("VOYAGE");
        if (voyage == null) {
            finish();
            return;
        }

        lblPrixDetailVoyage.setText(String.format(Locale.getDefault(), "%.2f$/Personne", voyage.getPrix()));
        lblDestinationDetailVoyage.setText(voyage.getDestination());
        lblDureeDetailVoyage.setText(String.format(Locale.getDefault(), "%d jours", voyage.getDuree_jours()));
        lblDescriptionDetailVoyage.setText(voyage.getDescription());
        btnDetailReserver.setOnClickListener(this::onReserverClicked);
        btnDetailReserver.setVisibility(INVISIBLE);

        nbDePersonnesDetailVoyage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString();
                System.out.println(text);
                nbPersonnes = text.isBlank() ? 0 : Integer.parseInt(text);

                Voyage.Trip trip = voyage.getTrips().get(spDetailDates.getSelectedItemPosition());

                btnDetailReserver.setVisibility(nbPersonnes <= trip.getNb_places_disponibles() ? VISIBLE : INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        getImageFromWeb(imageDetailVoyage);

        List<String> trips = voyage.getTrips().stream().map(DetailActivity::formatTrip).collect(Collectors.toList());

        ArrayAdapter<String> adapteur = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, trips);
        spDetailDates.setAdapter(adapteur);

        dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);

        // Observer les LiveData
        dataViewModel.getVoyages().observe(this, voyages -> {
            HistoriqueDao.addHistorique(this, voyage.getId(), voyage.getDestination(), trip.getDate(), voyage.getPrix() * nbPersonnes, nbPersonnes);
            finish();
        });

        dataViewModel.getErreur().observe(this, this::afficherMessage);
    }

    public static String formatTrip(Voyage.Trip trip) {
        if (trip.getNb_places_disponibles() > 0) {
            return String.format("%s %s places restantes", trip.getDate(), trip.getNb_places_disponibles());
        } else {
            return String.format("%s plein", trip.getDate());
        }
    }

    public void onReserverClicked(View view) {
        trip = voyage.getTrips().get(spDetailDates.getSelectedItemPosition());
        if (trip.getNb_places_disponibles() > 0) {
            dataViewModel.reserverVoyage(voyage.getId(), trip.getDate(), nbPersonnes);
        }
    }

    public void afficherMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        System.out.println(message);
    }

    private void getImageFromWeb(ImageView image) {
        OkHttpClient client = new OkHttpClient();
        new Thread(() -> {
            Request request = new Request.Builder().url(voyage.getImage_url()).build();
            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful() || response.body() == null) {
                    throw new IOException("Error with image thread " + response);
                }
                final Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
                // update UI on the main thread
                image.post(() -> {
                    if (bitmap != null) {
                        image.setImageBitmap(bitmap);
                    } else {
                        image.setImageResource(R.drawable.ic_launcher_foreground); // placeholder if image fails to load
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
                image.post(() -> image.setImageResource(R.drawable.ic_launcher_foreground));
            }
        }).start();
    }
}
