package com.natael_raphael_guillaume.tourisme.vue.activity;

import static android.widget.CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.IntRange;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.natael_raphael_guillaume.tourisme.R;
import com.natael_raphael_guillaume.tourisme.modele.dao.HistoriqueDao;
import com.natael_raphael_guillaume.tourisme.sqlite.VoyageHistorique;
import com.natael_raphael_guillaume.tourisme.viewModele.DataViewModel;
import com.natael_raphael_guillaume.tourisme.vue.adaptateurs.HistoriqueAdapteur;

public class HistoryActivity extends AppCompatActivity {

    private ListView lvHistorique;
    private DataViewModel dataViewModel;

    private String historiqueId;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_history);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        lvHistorique = findViewById(R.id.lvHistorique);

        lvHistorique.setOnItemClickListener((parent, view, position, id) -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Annuler la réservation");
            builder.setMessage("Êtes-vous sûr de vouloir annuler votre réservation ? Cette action est irréversible.");

            builder.setPositiveButton("Oui", (dialog, which) -> {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);

                @IntRange(from = -1) int idIndex = cursor.getColumnIndex(VoyageHistorique.Colonnes.ID);
                System.out.println(idIndex);

                @IntRange(from = -1) int voyageIdIndex = cursor.getColumnIndex(VoyageHistorique.Colonnes.VOYAGE_ID);
                System.out.println(voyageIdIndex);

                @IntRange(from = -1) int dateIndex = cursor.getColumnIndex(VoyageHistorique.Colonnes.DATE);
                System.out.println(dateIndex);


                @IntRange(from = -1) int statutIndex = cursor.getColumnIndex(VoyageHistorique.Colonnes.STATUT);
                System.out.println(statutIndex);

                if (statutIndex >= 0 && idIndex >= 0 && voyageIdIndex >= 0 && dateIndex >= 0) {
                    @SuppressLint("Range") String statut = cursor.getString(statutIndex);
                    if (!statut.equalsIgnoreCase(HistoriqueDao.CONFIRMEE)) return;

                    historiqueId = cursor.getString(idIndex);

                    System.out.println(cursor.getString(dateIndex));
                    dataViewModel.annulerVoyage(cursor.getString(voyageIdIndex), cursor.getString(dateIndex));
                }
            });

            builder.setNegativeButton("Non", (dialog, which) -> {
                System.out.println("NON");
            });

            AlertDialog dialog = builder.create();
            dialog.show();

        });

        updateCursor();

        dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);

        // Observer les LiveData
        dataViewModel.getVoyages().observe(this, voyages -> {
            System.out.println(voyages);
            HistoriqueDao.cancelHistorique(this, historiqueId);
            updateCursor();
        });

        dataViewModel.getErreur().observe(this, this::afficherMessage);
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateCursor();
    }

    private void updateCursor() {
        try {
            Cursor cursor = HistoriqueDao.getHistoriqueCursor(this);

            HistoriqueAdapteur adapter = new HistoriqueAdapteur(this, cursor, FLAG_REGISTER_CONTENT_OBSERVER);

            lvHistorique.setAdapter(adapter);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void afficherMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        System.out.println(message);
    }
}