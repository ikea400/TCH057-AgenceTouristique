package com.natael_raphael_guillaume.tourisme.vue.activity;

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
import com.natael_raphael_guillaume.tourisme.modele.entite.Client;
import com.natael_raphael_guillaume.tourisme.viewModele.DataViewModel;

public class RegisterActivity extends AppCompatActivity {

    private EditText editRegisterNom;
    private EditText editRegisterPrenom;
    private EditText editRegisterEmail;
    private EditText editRegisterAge;
    private EditText editRegisterTelephone;
    private EditText editRegisterAddresse;
    private EditText editRegisterPassword;
    private TextView registerError;
    private Button btnDoRegister;
    private Button btnGoLogin;

    private DataViewModel clientViewModel;

    private Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Liaison avec l'interface
        editRegisterNom = findViewById(R.id.editRegisterNom);
        editRegisterPrenom = findViewById(R.id.editRegisterPrenom);
        editRegisterEmail = findViewById(R.id.editRegisterEmail);
        editRegisterAge = findViewById(R.id.editRegisterAge);
        editRegisterTelephone = findViewById(R.id.editRegisterTelephone);
        editRegisterAddresse = findViewById(R.id.editRegisterAddresse);
        editRegisterPassword = findViewById(R.id.editRegisterPassword);
        registerError = findViewById(R.id.registerError);
        btnDoRegister = findViewById(R.id.btnDoRegister);
        btnGoLogin = findViewById(R.id.btnGoLogin);

        // Initialisation du ViewModel
        clientViewModel = new ViewModelProvider(this).get(DataViewModel.class);

        // Observer les LiveData
        clientViewModel.getClients().observe(this, clients -> {
            if (!clients.isEmpty()) {
                if (client == null) {
                    finish();
                } else {
                    afficherMessage("Courriel est déjà utilisé");
                    client = null;
                }
                return;
            }

            if (client != null) {
                clientViewModel.ajouterClient(client);
                client = null;
            }
        });

        clientViewModel.getErreur().observe(this, this::afficherMessage);

        // Clic sur le bouton d'enregistrement
        btnDoRegister.setOnClickListener(v -> {
            client = null;

            String nom = editRegisterNom.getText().toString().trim();
            String prenom = editRegisterPrenom.getText().toString().trim();
            String email = editRegisterEmail.getText().toString().trim();
            String ageStr = editRegisterAge.getText().toString().trim();
            String telephone = editRegisterTelephone.getText().toString().trim();
            String addresse = editRegisterAddresse.getText().toString().trim();
            String password = editRegisterPassword.getText().toString().trim();
            if (nom.isBlank() ||
                    prenom.isBlank() ||
                    email.isBlank() ||
                    ageStr.isBlank() ||
                    telephone.isBlank() ||
                    addresse.isBlank() ||
                    password.isBlank()
            ) {
                afficherMessage("Tous les champts sont requis");
                return;
            }

            int age;
            try {
                age = Integer.parseInt(ageStr);
            } catch (NumberFormatException ignored) {
                afficherMessage("Age doit etre un nombre");
                return;
            }

            int r = checkSelfPermission("android.permission.INTERNET");
            if (r == PackageManager.PERMISSION_GRANTED) {
                client = new Client(nom, prenom, email, telephone, password, addresse, age, null);
                clientViewModel.trouverClient(email, null);
            } else {
                afficherMessage("Accès Internet non permis !");
            }
        });

        // Clic sur le bouton de connection
        btnGoLogin.setOnClickListener(v -> {
            finish();
        });
    }

    public void afficherMessage(String message) {
        //Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        registerError.setText(message);
    }
}