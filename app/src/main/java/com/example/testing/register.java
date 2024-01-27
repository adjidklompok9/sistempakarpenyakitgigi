package com.example.testing;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class register extends AppCompatActivity {

    private ProgressDialog pDialog;
    public static final String url = "https://sistempakarparu1.000webhostapp.com/daftar.php";
    private EditText et_nama_lengkap;
    private EditText et_username;
    private EditText et_password;
    private EditText et_telepon;
    private EditText et_alamat;
    private String nama_lengkap;
    private String username;
    private String password;
    private String telepon;
    private String alamat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("Daftar User");
        et_nama_lengkap = findViewById(R.id.Namalengkap);
        et_username = findViewById(R.id.usernamee);
        et_password = findViewById(R.id.passwordd);
        et_telepon = findViewById(R.id.nohp);
        et_alamat = findViewById(R.id.alamatt);

        Button daftar = findViewById(R.id.butdaftar);

        daftar.setOnClickListener(v -> {
            nama_lengkap = et_nama_lengkap.getText().toString().trim();
            username = et_username.getText().toString().toLowerCase().trim();
            password = et_password.getText().toString().trim();
            telepon = et_telepon.getText().toString().trim();
            alamat = et_alamat.getText().toString().trim();
            if (validateInputs()) {
                daftar();
            }
        });

    }

    private boolean validateInputs() {
        if (nama_lengkap.equals("")) {
            et_nama_lengkap.setError("Nama Lengkap tidak boleh kosong");
            et_nama_lengkap.requestFocus();
            return false;
        }
        if (username.equals("")) {
            et_username.setError("Username tidak boleh kosong");
            et_username.requestFocus();
            return false;
        }
        if (password.equals("")) {
            et_password.setError("Password tidak boleh kosong");
            et_password.requestFocus();
            return false;
        }
        if (telepon.equals("")) {
            et_telepon.setError("Telepon tidak boleh kosong");
            et_telepon.requestFocus();
            return false;
        }
        if (alamat.equals("")) {
            et_alamat.setError("Alamat tidak boleh kosong");
            et_alamat.requestFocus();
            return false;
        }
        return true;
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(register.this);
        pDialog.setMessage("Sedang diproses...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void daftar() {
        displayLoader();
        JSONObject request = new JSONObject();
        try {
            request.put("nama_lengkap", nama_lengkap);
            request.put("username", username);
            request.put("password", password);
            request.put("telepon", telepon);
            request.put("alamat", alamat);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, request, response -> {
                    pDialog.dismiss();
                    try {
                        if (response.getInt("status") == 0) {
                            Toast.makeText(getApplicationContext(),
                                    response.getString("message"), Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(), login.class);
                            startActivity(i);
                            finish();
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
                            "Terjadi kesalahan, harap coba lagi", Toast.LENGTH_SHORT).show();
                });

        MySingleton.getInstance(this).addToRequestQueue(jsObjectRequest);
    }
}
