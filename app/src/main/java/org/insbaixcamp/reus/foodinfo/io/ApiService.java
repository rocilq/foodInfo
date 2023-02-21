package org.insbaixcamp.reus.foodinfo.io;


import org.insbaixcamp.reus.foodinfo.MainActivity;
import org.insbaixcamp.reus.foodinfo.io.response.ProductResponse;
import org.insbaixcamp.reus.foodinfo.model.Code;
import org.insbaixcamp.reus.foodinfo.model.Product;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("product/{code}")
    Call<Code> getCode(@Path("code") String code);

    // Hay que poner rutas para llamar a la api, por ejemplo, para coger los alergenos o el nombre para probar
    // Ya está el modelo de product creado, hazlo parecido y el path(code) tiene que ser igual para que coja el codigo
    // ves probando

}
