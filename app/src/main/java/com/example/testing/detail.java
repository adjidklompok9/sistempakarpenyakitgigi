package com.example.testing;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class detail extends AppCompatActivity {

    private ProgressDialog pDialog;
    private static final String url = "https://kelompok15nieboss.000webhostapp.com/spkulitwajah/get_penyakit.php";
    private TextView tv_nama_penyakit;
    private TextView tv_deskripsi;
    private TextView tv_solusi;
    private String id_penyakit;
    private TextView diagnosaulang;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setTitle("Detail Penyakit");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id_penyakit = extras.getString("id_penyakit");
        }

        tv_nama_penyakit = findViewById(R.id.detailnamapenyakit);
        tv_deskripsi = findViewById(R.id.deskripsipenya);
        tv_solusi = findViewById(R.id.solusidetai);


        getPenyakit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return false;
    }

    private void getPenyakit() {
        displayLoader();
        JSONObject request = new JSONObject();
        try {
            request.put("id_penyakit", id_penyakit);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.POST, url, request, response -> {
                    pDialog.dismiss();
                    try {
                        if (response.getInt("status") == 0) {
                            tv_nama_penyakit.setText(response.getString("nama_penyakit"));
                            tv_deskripsi.setText(response.getString("deskripsi"));
                            tv_solusi.setText(response.getString("solusi"));
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

    private void displayLoader() {
        pDialog = new ProgressDialog(detail.this);
        pDialog.setMessage("Sedang diproses...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }
    private void startDiagnosaCfActivity() {
        Intent intent = new Intent(detail.this, pilih_gejala.class);
        startActivity(intent);
        finish();  // Optional: Jika Anda ingin menutup activity saat pindah ke DiagnosaCfActivity
    }
}

