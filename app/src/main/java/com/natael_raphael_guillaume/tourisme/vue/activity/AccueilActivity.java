package com.natael_raphael_guillaume.tourisme.vue.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.natael_raphael_guillaume.tourisme.R;
import com.natael_raphael_guillaume.tourisme.vue.adaptateurs.VoyageAdapter;
import com.natael_raphael_guillaume.tourisme.modele.entite.Voyage;
import com.natael_raphael_guillaume.tourisme.viewModele.DataViewModel;

import java.util.ArrayList;
import java.util.List;

public class AccueilActivity extends AppCompatActivity {
    private Button btnFiltres;
    private Button btnHistorique;
    private DataViewModel dataViewModel;

    private ArrayList<String> destinations;
    private ActivityResultLauncher<Intent> launcher;
    private ListView lvVoyages;

    private String destination;
    private String type;
    private String dateDepart;
    private int[] budget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_accueil);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), this::onFilterResult);

        btnFiltres = findViewById(R.id.btnFiltres);
        btnHistorique = findViewById(R.id.btnHistorique);
        lvVoyages = findViewById(R.id.listeViewVoyages);

        lvVoyages.setOnItemClickListener((parent, view, position, id) -> {
           Voyage voyage = (Voyage)parent.getItemAtPosition(position);
            System.out.println(voyage);
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra("VOYAGE", voyage);
            startActivity(intent);
        });

        // Initialisation du ViewModel
        dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);

        // Observer les LiveData
        dataViewModel.getVoyages().observe(this, voyages -> {
            updateDestinations(voyages);

            afficherMessage(Integer.toString(voyages.size()));
        });

        dataViewModel.getErreur().observe(this, this::afficherMessage);

        dataViewModel.trouverVoyages(destination, budget, type, dateDepart);

        btnFiltres.setOnClickListener(this::onFiltreClicked);
        btnHistorique.setOnClickListener(this::onHistoryClicked);

        dataViewModel.getVoyages().observe(this, voyages -> {
            VoyageAdapter adapter = new VoyageAdapter(this, R.layout.liste_view_voyages, voyages);
            lvVoyages.setAdapter(adapter);
        });
    }

    public void afficherMessage(String message) {
        //Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void onFiltreClicked(View view) {
        if (destinations == null) return;
        Intent intent = new Intent(this, FilterActivity.class);
        intent.putStringArrayListExtra("DESTINATIONS", destinations);

        launcher.launch(intent);
    }

    public void onHistoryClicked(View view) {
        Intent intent = new Intent(this, HistoryActivity.class);

        startActivity(intent);
    }

    public void onFilterResult(ActivityResult result) {
        Intent resultIntent;
        if (result.getResultCode() != RESULT_OK ||
                (resultIntent = result.getData()) == null) return;

        destination = resultIntent.getStringExtra("DESTINATION");
        type = resultIntent.getStringExtra("TYPE");
        dateDepart = resultIntent.getStringExtra("DATE");
        budget = resultIntent.getIntArrayExtra("BUDGET");

        dataViewModel.trouverVoyages(destination, budget, type, dateDepart);
    }

    public void updateDestinations(List<Voyage> voyages) {
        if (destinations == null) destinations = new ArrayList<>();
        for (Voyage voyage : voyages) {
            if (!destinations.contains(voyage.getDestination())) {
                destinations.add(voyage.getDestination());
            }
        }
    }
}