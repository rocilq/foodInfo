package org.insbaixcamp.reus.foodinfo.io;

import org.insbaixcamp.reus.foodinfo.io.response.ProductResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    @GET("product")
    Call<ProductResponse> getDiseases();

//    @FormUrlEncoded
//    @POST("upload/photo")
//    Call<SimpleResponse> postPhoto(
//            @Field("image") String base64,
//            @Field("extension") String extension,
//            @Field("user_id") String user_id
//    );
//
//    @GET("login")
//    Call<LoginResponse> getLogin(
//            @Query("username") String username,
//            @Query("password") String password
//    );
//
//    @FormUrlEncoded
//    @POST("product")
//    Call<SimpleResponse> postNewProduct(
//            @Field("code") String code,
//            @Field("name") String name,
//            @Field("description") String description
//    );
}
