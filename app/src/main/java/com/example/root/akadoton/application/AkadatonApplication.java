package com.example.root.akadoton.application;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.root.akadoton.network.HttpLoggingInterceptor;
import com.example.root.akadoton.network.UpdateDataService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AkadatonApplication extends Application {

    @Nullable private volatile UpdateDataService updateDataService = null;


    @NonNull
    private final Object retrofitLock = new Object();

    @NonNull private final Object updateDataServiceLock = new Object();

    @Nullable
    private volatile Retrofit retrofit = null;

    @NonNull private Retrofit provideRetrofit() {
        Retrofit localInstance = retrofit;
        if (localInstance == null) {
            synchronized (retrofitLock) {
                localInstance = retrofit;
                if (localInstance == null) {
                    localInstance = retrofit = buildRetrofit();
                }
            }
        }

        assert (localInstance != null);

        return localInstance;
    }

    @NonNull private Retrofit buildRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("http://10.30.92.76")
                .client(buildClient())
                .validateEagerly(true)
                .addConverterFactory(GsonConverterFactory.create(provideGson()))
                .build();
    }

    @NonNull private OkHttpClient buildClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .cache(new Cache(getDir("okhttp-cache", Context.MODE_PRIVATE), Long.MAX_VALUE))
                .connectionPool(new ConnectionPool(1, 1, TimeUnit.SECONDS))
                .build();
    }


    @NonNull private Gson provideGson() {
        GsonBuilder builder = new GsonBuilder();

        return builder.create();
    }

    @NonNull public UpdateDataService provideUpdateDataService() {
        UpdateDataService localInstance = updateDataService;
        if (localInstance == null) {
            synchronized (updateDataServiceLock) {
                localInstance = updateDataService;
                if (localInstance == null) {
                    localInstance = updateDataService = provideRetrofit().create(UpdateDataService.class);
                }

            }
        }

        assert (localInstance != null);
        return localInstance;
    }
}
