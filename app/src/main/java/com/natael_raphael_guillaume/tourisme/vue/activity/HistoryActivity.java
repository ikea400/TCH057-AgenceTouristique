package com.natael_raphael_guillaume.tourisme.vue.activity;

import static android.widget.CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER;

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

import com.natael_raphael_guillaume.tourisme.R;
import com.natael_raphael_guillaume.tourisme.modele.dao.HistoriqueDao;
import com.natael_raphael_guillaume.tourisme.sqlite.VoyageHistorique;
import com.natael_raphael_guillaume.tourisme.vue.adaptateurs.HistoriqueAdapteur;

public class HistoryActivity extends AppCompatActivity {

    private ListView lvHistorique;

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
                Cursor cursor = (Cursor)parent.getItemAtPosition(position);

                @IntRange(from = -1) int idIndex = cursor.getColumnIndex(VoyageHistorique.Colonnes.VOYAGE_ID);
                System.out.println(idIndex);

                if (idIndex >= 0) {
                    System.out.println(cursor.getString(idIndex));
                }
            });

            builder.setNegativeButton("Non", (dialog, which) -> {
                System.out.println("NON");
            });

            AlertDialog dialog = builder.create();
            dialog.show();

        });

        updateCursor();
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateCursor();
    }

    private void updateCursor() {
        try {
            //HistoriqueDao.addHistorique(this, "Québec, Québec", "2025-05-10", 250);

            Cursor cursor = HistoriqueDao.getHistoriqueCursor(this);

            HistoriqueAdapteur adapter = new HistoriqueAdapteur(this, cursor,FLAG_REGISTER_CONTENT_OBSERVER );

            lvHistorique.setAdapter(adapter);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}