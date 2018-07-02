package com.example.root.akadoton.network;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class UpdateDataResponse {

    @SerializedName("light_out") private final String lightsOut;
    @SerializedName("light_in") private final String lightsIn;
    @SerializedName("windows") private final String windows;
    @SerializedName("windows_ang") private final String windowsAng;
    public UpdateDataResponse(@Nullable String lightsOut,
                              @Nullable String lightsIn,
                              @Nullable String windows,
                              @Nullable String windowsAng) {
        this.lightsOut = lightsOut;
        this.lightsIn = lightsIn;
        this.windows = windows;
        this.windowsAng = windowsAng;
    }

    @NonNull public String getLightsOut() {return lightsOut;}
    @NonNull public String getLightsIn() {return lightsIn;}
    @NonNull public String getWindows() {return windows;}
    @NonNull public String getWindowsAng() {return windowsAng;}

}
