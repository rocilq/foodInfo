package org.insbaixcamp.reus.foodinfo.io;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiAdapter extends Activity {
    private static ApiService API_SERVICE;




    public static ApiService getApiService(String data) {

        /**
         * Localhost IP for AVD emulators: 10.0.2.2
         *
         */

         String BASE_URL = "https://world.openfoodfacts.org/api/v0/product/" + data + "/" ;

        // Creamos un interceptor y le indicamos el log level a usar


        final HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Asociamos el interceptor a las peticiones
        final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        if (API_SERVICE == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build()) // <-- set log level
                    .build();

            API_SERVICE = retrofit.create(ApiService.class);
        }

        return API_SERVICE;
    }

}
