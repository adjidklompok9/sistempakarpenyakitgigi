package com.example.testing;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

public class splash extends AppCompatActivity {

    private ProgressBar simpleProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // inisialisasi progress bar
        simpleProgressBar = findViewById(R.id.progressbar);

        // tampilkan progress bar setelah 5 detik
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // tampilkan progress bar
                simpleProgressBar.setVisibility(View.VISIBLE);

                // setelah 5 detik, buat Intent untuk pindah ke halaman login
                Intent intent = new Intent(splash.this, menu.class);
                startActivity(intent);

                // tutup aktivitas saat ini (splash activity)
                finish();
            }
        }, 3000);
    }
}
