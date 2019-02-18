package com.appzone.eyeres.services;

import com.appzone.eyeres.models.ProductDataModel;
import com.appzone.eyeres.models.UserModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Service {

    @FormUrlEncoded
    @POST("/api/login")
    Call<UserModel> SignIn(@Field("phone") String phone
    );

    @Multipart
    @POST("api/sign-up")
    Call<UserModel> SignUp(@Part("name") RequestBody name,
                                    @Part("phone") RequestBody phone,
                                    @Part("email") RequestBody email,
                                    @Part MultipartBody.Part avatar

    );

    @GET("api/categories/{category_id}/products")
    Call<ProductDataModel> getProducts(@Path("category_id") int category_id,
                                       @Query("page") int page_index,
                                       @Query("type") int type,
                                       @Query("token") String token
                                       );



}
