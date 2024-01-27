package com.example.testing.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.testing.R;
import com.example.testing.model.ModelPilihgejala;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class PilihgejalaAdapter extends ArrayAdapter<ModelPilihgejala> {

    private List<ModelPilihgejala> dataList;

    public PilihgejalaAdapter(Context context, int resource, List<ModelPilihgejala> dataList) {
        super(context, resource, dataList);
        this.dataList = dataList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_gejala, parent, false);
        }

        // Mendapatkan referensi ke item di posisi tertentu
        final ModelPilihgejala currentItem = dataList.get(position);

        // Inisialisasi elemen UI dalam item
        final TextView namaGejalaTextView = convertView.findViewById(R.id.namaGejalaTextView);

        final CheckBox checkBoxItem = convertView.findViewById(R.id.listgejala);
        // Set nilai checkbox sesuai data dari server
        namaGejalaTextView.setText(currentItem.getNamaGejala());

        checkBoxItem.setChecked(currentItem.isSelected());

        // Menangani perubahan status checkbox
        checkBoxItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentItem.setSelected(checkBoxItem.isChecked());
            }
        });

        return convertView;
    }

    public void updateDataToServer() {
        String url = "https://sistempakarpkdrt.000webhostapp.com/random_data_cache.json";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Handle respon dari server jika berhasil
                        // Misalnya, tampilkan pesan atau lakukan sesuatu setelah data berhasil terkirim

                        try {
                            // Bersihkan dataList
                            dataList.clear();

                            // Tambahkan data baru dari respons JSON
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);

                                // Gantilah ini sesuai dengan struktur data dari server Anda
                                String namaGejala = jsonObject.getString("name");
                                String kode = jsonObject.getString("code");
                                String idgejala = jsonObject.getString("id");

                                // Perhatikan bahwa di server tidak ada kunci "selected"
                                // Jadi, kita tidak bisa langsung mendapatkan nilai isSelected dari server

                                ModelPilihgejala modelPilihgejala = new ModelPilihgejala();
                                modelPilihgejala.setKodegejala(kode);
                                modelPilihgejala.setNamaGejala(namaGejala);
                                modelPilihgejala.setIdGejala(idgejala);

                                modelPilihgejala.setSelected(false); // Tidak ada info isSelected dari server

                                dataList.add(modelPilihgejala);
                            }

                            // Memberi tahu adapter bahwa data telah berubah
                            notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle respon dari server jika terjadi kesalahan
                        // Misalnya, tampilkan pesan kesalahan
                    }
                }
        );

        // Tambahkan permintaan ke antrian
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonArrayRequest);
    }
}
