package com.example.testing;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class login extends AppCompatActivity {

Button buttonlog,buttondaf;


    private ProgressDialog pDialog;
    public static final String login_url = "https://sistempakarparu1.000webhostapp.com/login.php";
    private EditText et_usernamee;
    private EditText et_password;
    private String username;
    private String password;
    private SessionHandler session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");

        session = new SessionHandler(getApplicationContext());

        et_usernamee = findViewById(R.id.et_usernamee);
        et_password = findViewById(R.id.et_password);

        Button login = findViewById(R.id.buttonlog);
        Button daftar = findViewById(R.id.buttondaf);

        login.setOnClickListener(v -> {
            username = et_usernamee.getText().toString().toLowerCase().trim();
            password = et_password.getText().toString().trim();
            if (validateInputs()) {
                login();
            }
        });

        daftar.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(), register.class);
            startActivity(i);
            finish();
        });
    }

    private boolean validateInputs() {
        if (username.equals("")) {
            et_usernamee.setError("Username tidak boleh kosong");
            et_usernamee.requestFocus();
            return false;
        }
        if (password.equals("")) {
            et_password.setError("Password tidak boleh kosong");
            et_password.requestFocus();
            return false;
        }
        return true;
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(login.this);
        pDialog.setMessage("Sedang diproses...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void login() {
        displayLoader();
        JSONObject request = new JSONObject();
        try {
            request.put("username", username);
            request.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, login_url, request, response -> {
                    pDialog.dismiss();
                    try {
                        if (response.getInt("status") == 0) {
                            session.loginUser(response.getString("id_pengguna"),
                                    response.getString("nama_lengkap"));

                               openTelegram(response.getString("level"));



                        } else {
                            Toast.makeText(getApplicationContext(),
                                    response.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    pDialog.dismiss();
                    Toast.makeText(getApplicationContext(),
                            error.getMessage(), Toast.LENGTH_SHORT).show();
                });

        MySingleton.getInstance(this).addToRequestQueue(jsArrayRequest);
    }

    private void openTelegram(String userLevel) {
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