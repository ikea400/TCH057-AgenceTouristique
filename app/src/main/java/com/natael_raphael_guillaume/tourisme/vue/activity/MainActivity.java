package com.natael_raphael_guillaume.tourisme.vue.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.natael_raphael_guillaume.tourisme.R;
import com.natael_raphael_guillaume.tourisme.viewModele.DataViewModel;

public class MainActivity extends AppCompatActivity {

    private Button btnLinkRegister;
    private Button btnLogin;
    private EditText textLoginEmail;
    private EditText textLoginPassword;
    private TextView loginError;

    private DataViewModel dataViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Liaison avec l'interface
        btnLinkRegister = findViewById(R.id.btnLinkRegister);
        btnLogin = findViewById(R.id.btnLogin);
        textLoginEmail = findViewById(R.id.textLoginEmail);
        textLoginPassword = findViewById(R.id.textLoginPassword);
        loginError = findViewById(R.id.loginError);

        loginError.setText("");

        // Initialisation du ViewModel
        dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);

        // Observer les LiveData
        dataViewModel.getClients().observe(this, clients -> {

            if (clients.size() != 1) {
                afficherMessage("Identifiants invalide");
            }
            else {
                Intent intent = new Intent(this, AccueilActivity.class);

                startActivity(intent);
            }

        });

        dataViewModel.getErreur().observe(this, this::afficherMessage);

        // Click sur le bouton de connection
        btnLogin.setOnClickListener(v -> {
            String email = textLoginEmail.getText().toString().trim();
            String mdp = textLoginPassword.getText().toString().trim();
            if (email.isBlank() || mdp.isBlank()) {
                afficherMessage("Identifiants invalide");
                return;
            }

            int r = checkSelfPermission("android.permission.INTERNET");
            if (r == PackageManager.PERMISSION_GRANTED) {
                dataViewModel.trouverClient(email, mdp);
            } else {
                afficherMessage("Accès Internet non permis !");
            }
        });

        // Click sur le bouton d'enregistrement
        btnLinkRegister.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterActivity.class);

            startActivity(intent);
        });



        /// BYPASSE LOGIN




        Intent intent = new Intent(this, AccueilActivity.class);

        //startActivity(intent);
    }

    public void afficherMessage(String message) {
        //Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        loginError.setText(message);
    }
}