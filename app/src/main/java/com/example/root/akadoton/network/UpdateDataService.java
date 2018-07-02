package com.example.root.akadoton.network;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UpdateDataService {

    @POST("/")
    Call<UpdateDataResponse> update(@Body UpdateDataBody body);

}
