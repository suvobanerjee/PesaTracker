package com.excogit.suvo.pesatracker.rest;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by suvo on 3/17/2017.
 */

public class ApiClient {

    public static final String BASE_URL = "http://apps.travelnlettravel.com";
    private static Retrofit retrofit;

    public static Retrofit getClient()
    {
        if(retrofit == null)
        {


            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(getRequestHeader())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    private static OkHttpClient getRequestHeader()
    {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1,TimeUnit.MINUTES)
                .build();

        return okHttpClient;
    }

}
