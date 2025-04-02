<<<<<<<< HEAD:app/src/main/java/com/natael_raphael_guillaume/tourisme/activites/Accueil.java
package com.natael_raphael_guillaume.tourisme.activites;
========
package com.natael_raphael_guillaume.tourisme;
>>>>>>>> origin/master:app/src/main/java/com/natael_raphael_guillaume/tourisme/MainActivity.java

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

<<<<<<<< HEAD:app/src/main/java/com/natael_raphael_guillaume/tourisme/activites/Accueil.java
import com.natael_raphael_guillaume.tourisme.R;


public class Accueil extends AppCompatActivity {
========
public class MainActivity extends AppCompatActivity {
>>>>>>>> origin/master:app/src/main/java/com/natael_raphael_guillaume/tourisme/MainActivity.java

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
    }
}