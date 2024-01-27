package com.example.testing.Database;

import com.example.testing.model.ModelPilihgejala;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;

public interface ApiService {
    @GET("process_data.php")
    Call<ModelPilihgejala> getHasilDiagnosa(@Body JsonObject request);
}
