package com.appzone.eyeres.services;

import com.appzone.eyeres.models.UserModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Service {

    @FormUrlEncoded
    @POST("api/login")
    Call<UserModel> SignIn(@Field("phone") String phone,
                           @Field("password") String password
    );

    @Multipart
    @POST("api/sign-up")
    Call<UserModel> SignUp(@Part("name") RequestBody name,
                                    @Part("phone") RequestBody phone,
                                    @Part("password") RequestBody password,
                                    @Part("email") RequestBody email,
                                    @Part("country") RequestBody country,
                                    @Part MultipartBody.Part avatar

    );

}
