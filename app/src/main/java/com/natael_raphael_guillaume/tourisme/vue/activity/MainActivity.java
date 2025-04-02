package com.natael_raphael_guillaume.tourisme.vue.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.natael_raphael_guillaume.tourisme.R;
import com.natael_raphael_guillaume.tourisme.viewModele.ClientViewModel;

public class MainActivity extends AppCompatActivity {

    private Button btnLinkRegister;
    private Button btnLogin;
    private EditText textLoginEmail;
    private EditText textLoginPassword;

    private ClientViewModel clientViewModel;

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

        // Initialisation du ViewModel
        clientViewModel = new ViewModelProvider(this).get(ClientViewModel.class);

        // Observer les LiveData
        clientViewModel.getClients().observe(this, clients -> {

            if (clients.size() != 1) {
                afficherMessage("Identifiants invalide");
            }
            else {
                Intent intent = new Intent(this, AccueilActivity.class);

                startActivity(intent);
            }

        });

        clientViewModel.getErreur().observe(this, this::afficherMessage);

        btnLogin.setOnClickListener(v -> {
            String email = textLoginEmail.getText().toString().trim();
            String mdp = textLoginPassword.getText().toString().trim();
            if (email.isBlank() || mdp.isBlank()) {
                afficherMessage("Identifiants invalide");
                return;
            }

            int r = checkSelfPermission("android.permission.INTERNET");
            if (r == PackageManager.PERMISSION_GRANTED) {
                clientViewModel.trouverClient(email, mdp);
            } else {
                afficherMessage("Accès Internet non permis !");
            }
        });

        btnLinkRegister.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterActivity.class);

            startActivity(intent);
        });
    }

    public void afficherMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}