package com.abhishek.paginationWithImageApi.service;

import android.content.Context;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {

    private static Retrofit newUserAPIService = null;
    private static OkHttpClient newUserHttpClient = null;

    // for users who are not signed in yet
    public static RestApiService getNewUserApiService(Context context) {
        if (newUserAPIService == null) {
            newUserAPIService = new Retrofit
                    .Builder()
                    .client(getNewUserHttpClient(context))
                    .baseUrl("https://api.flickr.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return newUserAPIService.create(RestApiService.class);
    }

    private static OkHttpClient getNewUserHttpClient(Context context) {
        if (newUserHttpClient == null) {
            newUserHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(1, TimeUnit.MINUTES)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(15, TimeUnit.SECONDS)
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request request = chain.request();
                            Response response = chain.proceed(request);
                            return response;
                        }
                    })
                    //here we adding Interceptor for full level logging
                    .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .build();
        }
        return newUserHttpClient;
    }


    public static Retrofit retrofit() {
        return newUserAPIService;
    }
}
