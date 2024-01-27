package com.example.testing;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class hasil extends AppCompatActivity {

    private VideoView videoView;
    private TextView tvDentitas;
    private TextView tvNamaPenyakit,sol,det;
    private Button kon,ulang;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hasil);

     //   videoView = findViewById(R.id.videoView);
        tvDentitas = findViewById(R.id.persentasi);
        tvNamaPenyakit = findViewById(R.id.penya);
        sol = findViewById(R.id.solusipenyakitgigi);
        det = findViewById(R.id.detailpenyakitgigi);
        kon=findViewById(R.id.buttonkons);
        ulang=findViewById(R.id.buttondiag);
        // Set the video path
//        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.your_video; // Change 'your_video' to your video file in the 'res/raw' directory
//        Uri uri = Uri.parse(videoPath);
//        videoView.setVideoURI(uri);
//
//        // Start playing the video
//        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                mp.setLooping(true);
//                videoView.start();
//            }
//        });

        // Fetch data from PHP script
        fetchDataFromPhp();

        kon.setOnClickListener(v -> {
            openTelegram();
        });
        ulang.setOnClickListener(v -> {
            Intent intent = new Intent(hasil.this, pilih_gejala.class);
            startActivity(intent);
            finish();
        });
    }

    private void fetchDataFromPhp() {
        // Replace 'your_php_script_url' with the actual URL of your PHP script
        String phpScriptUrl = "https://sistempakarpkdrt.000webhostapp.com/process_data.php";

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, phpScriptUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            double dentitas = jsonObject.getDouble("dentitas");
                            String namaPenyakit = jsonObject.getString("nama_penyakit");
                            String solusi = jsonObject.getString("solusi");
                            String detail = jsonObject.getString("detail");
                            // Update TextViews
                            tvDentitas.setText(""+dentitas+"%");
                            tvNamaPenyakit.setText(namaPenyakit);
                            sol.setText(solusi);
                            det.setText(detail);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        queue.add(stringRequest);
    }
    private void openTelegram() {
        // Tentukan URI Telegram
        String telegramUri = "https://t.me/gigikita_bot"; // Ganti dengan username Telegram yang diinginkan

        // Jika perangkat seluler, buka aplikasi Telegram
        if (isMobileDevice()) {
            openTelegramApp(telegramUri);
        } else {
            // Jika perangkat bukan seluler (mungkin laptop), buka web Telegram
            openTelegramWeb(telegramUri);
        }
    }

    private void openTelegramApp(String uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(uri));
        startActivity(intent);
    }

    private void openTelegramWeb(String uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(uri));
        startActivity(intent);
    }

    private boolean isMobileDevice() {
        // Logika deteksi perangkat seluler di sini
        // Misalnya, kita dapat memeriksa ukuran layar atau fitur khusus perangkat seluler
        // Ini hanyalah contoh sederhana, dan Anda mungkin perlu menyesuaikannya sesuai kebutuhan
        return getResources().getConfiguration().smallestScreenWidthDp < 600;
    }
}
