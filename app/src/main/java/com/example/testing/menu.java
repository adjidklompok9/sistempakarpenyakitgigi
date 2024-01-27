package com.example.testing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }
    public void diagnosa(View view) {
        Intent intent = new Intent(menu.this, pilih_gejala.class);
        startActivity(intent);
    }
    public void daftar(View view) {
        Intent intent = new Intent(menu.this, daftar.class);
        startActivity(intent);
    }
    public void riwayat(View view) {
        Intent intent = new Intent(menu.this, riwayat.class);
        startActivity(intent);
    }
    public void tentang(View view) {
        Intent intent = new Intent(menu.this, tentang.class);
        startActivity(intent);
    }
}