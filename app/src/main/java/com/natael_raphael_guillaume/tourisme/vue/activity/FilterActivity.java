package com.natael_raphael_guillaume.tourisme.vue.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.slider.RangeSlider;
import com.natael_raphael_guillaume.tourisme.R;
import com.natael_raphael_guillaume.tourisme.modele.entite.ETypeVoyage;

import java.util.ArrayList;

public class FilterActivity extends AppCompatActivity {

    private RangeSlider budgetRange;
    private AutoCompleteTextView destinationEdit;
    private Spinner typeSpinner;

    private static final String[] VOYAGES_TYPES = new String[]{
            ETypeVoyage.Aucun.toString(),
            ETypeVoyage.Aventure.toString(),
            ETypeVoyage.Nature.toString(),
            ETypeVoyage.Culturel.toString(),
            ETypeVoyage.BienEtre.toString()
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_filter);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        budgetRange = findViewById(R.id.budgetRange);
        destinationEdit = findViewById(R.id.destinationEdit);
        typeSpinner = findViewById(R.id.typeSpinner);

        Intent intent = getIntent();
        ArrayList<String> destinations = intent.getStringArrayListExtra("DESTINATIONS");

        if (destinations == null) {
            Toast.makeText(this, "destinations is null " + intent.getExtras(), Toast.LENGTH_SHORT).show();
        } else {
            ArrayAdapter<String> destinationsAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_dropdown_item_1line, destinations);
            destinationEdit.setAdapter(destinationsAdapter);
        }

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, VOYAGES_TYPES);
        typeSpinner.setAdapter(typeAdapter);
    }
}