package com.natael_raphael_guillaume.tourisme.vue.activity;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class FilterActivity extends AppCompatActivity {

    private RangeSlider budgetRange;
    private AutoCompleteTextView destinationEdit;
    private Spinner typeSpinner;
    private Button btnFilterConfirm;
    private Button btnFilterCancel;
    private CheckBox cbFilterDate;
    private CalendarView cvFilterDate;

    private String selectedDate;

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
        btnFilterConfirm = findViewById(R.id.btnFilterConfirm);
        btnFilterCancel = findViewById(R.id.btnFilterCancel);
        cbFilterDate = findViewById(R.id.cbFilterDate);
        cvFilterDate = findViewById(R.id.cvFilterDate);

        cvFilterDate.setVisibility(INVISIBLE);
        cvFilterDate.setEnabled(false);

        cbFilterDate.setOnClickListener(this::onDateCheckboxClicked);
        btnFilterCancel.setOnClickListener(this::onCancelClicked);
        btnFilterConfirm.setOnClickListener(this::onConfirmClicked);
        cvFilterDate.setOnDateChangeListener(this::onDateSelected);


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

    public void onDateCheckboxClicked(View view) {
        boolean enable = !cvFilterDate.isEnabled();
        cvFilterDate.setEnabled(enable);
        cvFilterDate.setVisibility(enable ? VISIBLE : INVISIBLE);
    }

    public void onCancelClicked(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    public void onConfirmClicked(View view) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        List<Float> values = budgetRange.getValues();

        String destination = destinationEdit.getText().toString().trim();
        String type = typeSpinner.getSelectedItemPosition() == 0
                ? "" :
                (String) typeSpinner.getSelectedItem();
        String date = cbFilterDate.isChecked() ?
                selectedDate : "";


        int[] budget = new int[]{
                (int) values.get(0).floatValue(),
                (int) values.get(1).floatValue()
        };

        Intent resultIntent = new Intent();
        resultIntent.putExtra("DESTINATION", destination);
        resultIntent.putExtra("TYPE", type);
        resultIntent.putExtra("DATE", date);
        resultIntent.putExtra("BUDGET", budget);

        setResult(RESULT_OK, resultIntent);
        finish();
    }

    public void onDateSelected(CalendarView view, int year, int month, int day) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day); // Month starts at 0 (January)
        selectedDate = sdf.format(calendar.getTime());
    }
}