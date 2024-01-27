package com.example.testing;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.testing.adapter.PilihgejalaAdapter;
import com.example.testing.model.ModelPilihgejala;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class pilih_gejala extends AppCompatActivity {

    private ListView listView;
    private PilihgejalaAdapter konsultasiAdapter;
    private List<ModelPilihgejala> dataList;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilih_gejala);

        listView = findViewById(R.id.rvGejalaPenyakit);
        btnSubmit = findViewById(R.id.btnDiagnosa);

        // Inisialisasi data dari server atau sumber lainnya
        dataList = initData();

        // Inisialisasi adapter
        konsultasiAdapter = new PilihgejalaAdapter(this, R.layout.list_item_gejala, dataList);

        // Set adapter ke ListView
        listView.setAdapter(konsultasiAdapter);

        // Panggil updateDataToServer setelah membuat adapter
        konsultasiAdapter.updateDataToServer();
        fetchDataFromServer();

        // Menangani klik item ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Toggle status checkbox saat item ListView diklik
                ModelPilihgejala currentItem = dataList.get(position);
                currentItem.setSelected(!currentItem.isSelected());
                konsultasiAdapter.notifyDataSetChanged();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Mengumpulkan data yang dicentang dan mengirimnya ke server
                sendDataToServer();
//                Intent intent = new Intent(pilih_gejala.this, hasil.class);
//                startActivity(intent);
            }
        });
    }

    private List<ModelPilihgejala> initData() {
        // Implementasi inisialisasi data dari server
        // Contoh sederhana:
        List<ModelPilihgejala> data = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            ModelPilihgejala item = new ModelPilihgejala();
            item.setSelected(false);
            // Set atribut lainnya sesuai data dari server
            data.add(item);
        }
        return data;
    }

    private void fetchDataFromServer() {
        // Gantilah URL_SERVER_ANDA dengan URL server Anda
        String url = "https://sistempakarpkdrt.000webhostapp.com/random_data_cache.json";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Handle respon JSON dari server
                        parseJsonResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle kesalahan saat mengambil data dari server
                        Log.e("pilih_gejala", "Error: " + error.toString());
                    }
                }
        );

        // Tambahkan permintaan ke antrian
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    private void parseJsonResponse(JSONArray response) {
        try {
            dataList.clear(); // Hapus data sebelum menambahkan data baru dari server
            for (int i = 0; i < response.length(); i++) {
                JSONObject jsonObject = response.getJSONObject(i);
                ModelPilihgejala item = new ModelPilihgejala();

                // Ambil id_gejala dan nama_gejala dari JSON

                String kodegejala = jsonObject.getString("code");
                String idGejala = jsonObject.getString("id");
                String namaGejala = jsonObject.getString("name");

                // Set atribut di ModelPilihgejala
                item.setKodegejala(kodegejala);
                item.setIdGejala(idGejala);
                item.setNamaGejala(namaGejala);
                item.setSelected(false);

                // Tambahkan item ke dataList
                dataList.add(item);
            }

            // Refresh adapter setelah mendapatkan data dari server
            konsultasiAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    private void sendDataToServer() {
        String url = "https://sistempakarpkdrt.000webhostapp.com/itu.php"; // Ganti dengan URL yang benar

        // Membuat JSONObject untuk menyimpan data yang dicentang
        JSONObject jsonObject = new JSONObject();
        try {
            // Menambahkan data gejala ke dalam JSON
            JSONArray jsonArrayGejala = new JSONArray();
            for (ModelPilihgejala item : dataList) {
                if (item.isSelected()) {
                    JSONObject jsonGejala = new JSONObject();
                   // jsonGejala.put("id", item.getIdGejala());
                    jsonGejala.put("code", item.getKodegejala());
                  //  jsonGejala.put("name", item.getNamaGejala());

                    jsonArrayGejala.put(jsonGejala);
                }
            }
            jsonObject.put("code", jsonArrayGejala);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Buat permintaan POST menggunakan Volley
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle respon dari server jika berhasil
                        Toast.makeText(pilih_gejala.this, "Data berhasil dikirim", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(pilih_gejala.this, hasil.class);
                        startActivity(intent);

                        // Proses atau tampilkan hasil dari respons server
                        processServerResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle respon dari server jika terjadi kesalahan
                        Toast.makeText(pilih_gejala.this, "Terjadi kesalahan: " + error.toString(), Toast.LENGTH_SHORT).show();
                        if (error.networkResponse != null) {
                            int statusCode = error.networkResponse.statusCode;
                            Log.e("pilih_gejala", "Status Code: " + statusCode);
                        }
                    }
                }
        );

        // Tambahkan permintaan ke antrian
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    private void processServerResponse(JSONObject response) {
        // Tampilkan atau lakukan sesuatu dengan hasil dari server
        // Perhatikan bahwa ini hanya contoh, Anda mungkin perlu menyesuaikan sesuai dengan struktur respons server dan kebutuhan aplikasi Anda.
        try {
            // Mengekstrak data atau informasi yang diperlukan dari respons
            String hasilDiagnosa = response.getString("code");

            // Menampilkan hasil ke log
            Log.d("pilih_gejala", "Hasil Diagnosa: " + hasilDiagnosa);

            // Selanjutnya, Anda dapat melakukan tindakan tertentu berdasarkan hasilDiagnosa,
            // misalnya, menavigasi ke layar hasil atau menampilkan pesan kepada pengguna.
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
