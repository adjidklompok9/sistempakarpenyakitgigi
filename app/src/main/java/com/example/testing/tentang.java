package com.example.testing;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class tentang extends AppCompatActivity {
    Button pakar,kreator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tentang);
        pakar=findViewById(R.id.pak);
        kreator=findViewById(R.id.kre);

        pakar.setOnClickListener(view -> {
            Intent intent = new Intent(tentang.this, pakar.class);
            startActivity(intent);

        });
        kreator.setOnClickListener(view -> {
            Intent intent = new Intent(tentang.this, Kreator.class);
            startActivity(intent);

        });
    }
}