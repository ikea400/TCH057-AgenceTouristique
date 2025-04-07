package com.natael_raphael_guillaume.tourisme.vue.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.natael_raphael_guillaume.tourisme.R;
import com.natael_raphael_guillaume.tourisme.modele.entite.Voyage;

import java.util.List;
import java.util.stream.Collectors;

public class DetailActivity extends AppCompatActivity {
    private TextView lblPrixDetailVoyage;
    private TextView lblDestinationDetailVoyage;
    private TextView lblDureeDetailVoyage;
    private TextView lblDescriptionDetailVoyage;
    private Spinner spDetailDates;
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

        Intent intent = getIntent();
        Voyage voyage = (Voyage)intent.getSerializableExtra("VOYAGE");
        if (voyage == null) {
            finish();
            return;
        }

        lblPrixDetailVoyage.setText(String.format("%.1f$/Personne", voyage.getPrix()));
        lblDestinationDetailVoyage.setText(voyage.getDestination());
        lblDureeDetailVoyage.setText(voyage.getDuree_jours() + " jours");
        lblDescriptionDetailVoyage.setText(voyage.getDescription());

        List<String> trips = voyage.getTrips().stream()
                .map(trip ->
                        String.format("%s %s places restantes",
                                trip.getDate(),
                                trip.getNb_places_disponibles()))
                .collect(Collectors.toList());

        ArrayAdapter<String> adapteur = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, trips);
        spDetailDates.setAdapter(adapteur);
    }
}
