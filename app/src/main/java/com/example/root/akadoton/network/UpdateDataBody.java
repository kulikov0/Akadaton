package com.example.root.akadoton.network;

import com.google.gson.annotations.SerializedName;

public class UpdateDataBody {

    @SerializedName("light") String light;
    @SerializedName("blinds") String blinds;

    public UpdateDataBody(String light, String blinds) {
        this.light = light;
        this.blinds = blinds;
    }
}
