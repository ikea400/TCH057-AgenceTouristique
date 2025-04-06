package com.natael_raphael_guillaume.tourisme.vue.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.natael_raphael_guillaume.tourisme.R;
import com.natael_raphael_guillaume.tourisme.modele.dao.HistoriqueDao;
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
        try {

            //HistoriqueDao.addHistorique(this, "Québec, Québec", "2025-05-10", 250);

            Cursor cursor = HistoriqueDao.getHistoriqueCursor(this);

            HistoriqueAdapteur adapter = new HistoriqueAdapteur(this, cursor, HistoriqueAdapteur.NO_SELECTION);

            lvHistorique.setAdapter(adapter);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}