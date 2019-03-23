package com.appzone.eyeres.services;

import com.appzone.eyeres.models.AdsModel;
import com.appzone.eyeres.models.FavoriteIdModel;
import com.appzone.eyeres.models.OrderDataModel;
import com.appzone.eyeres.models.OrderToUploadModel;
import com.appzone.eyeres.models.PackageSizeModel;
import com.appzone.eyeres.models.ProductDataModel;
import com.appzone.eyeres.models.QuestionsDataModel;
import com.appzone.eyeres.models.Terms_ConditionModel;
import com.appzone.eyeres.models.UserModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
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
                                    @Part("email") RequestBody email

    );

    @GET("api/categories/{category_id}/products")
    Call<ProductDataModel> getProducts(@Path("category_id") int category_id,
                                       @Query("page") int page_index,
                                       @Query("type") int type,
                                       @Query("token") String token
                                       );


    @FormUrlEncoded
    @POST("/api/favorites")
    Call<FavoriteIdModel> makeFavorite(@Field("token") String token,
                                       @Field("product_id") int product_id
                                       );

    @FormUrlEncoded
    @POST("/api/favorites/{favorite_id}")
    Call<ResponseBody> deleteFavorite(@Path("favorite_id") int favorite_id,
                                      @Field("token") String token,
                                      @Field("_method") String delete
    );

    @GET("/api/favorites")
    Call<ProductDataModel> getFavorites(@Query("token") String user_token,@Query("page") int page);

    @GET("api/offers")
    Call<ProductDataModel> getOffers(@Query("page") int page_index);

    @GET("/api/get-terms-condition")
    Call<Terms_ConditionModel> getTerms(@Query("type") int type);

    @GET("/api/packages")
    Call<PackageSizeModel> getPackageSize();

    @FormUrlEncoded
    @POST("/api/logout")
    Call<ResponseBody> logout(@Field("token") String user_token);

    @Multipart
    @POST("/api/edit-profile")
    Call<UserModel> updateImage(@Part("token") RequestBody user_token,
                                @Part MultipartBody.Part avatar
                                );

    @FormUrlEncoded
    @POST("/api/edit-profile")
    Call<UserModel> updateName(@Field("token") String user_token,
                                @Field("name") String name
    );
    @FormUrlEncoded
    @POST("/api/edit-profile")
    Call<UserModel> updateEmail(@Field("token") String user_token,
                               @Field("email") String email
    );

    @GET("/api/search-products")
    Call<ProductDataModel> search(@Query("q") String query,
                                  @Query("page") int page_index);

    @GET("/api/ads")
    Call<AdsModel> getAds();

    @FormUrlEncoded
    @POST("/api/fire-base-token")
    Call<ResponseBody> updateToken(@Field("token") String user_token,
                                   @Field("fire_base_token") String fire_base_token);


    @GET("/api/order-status")
    Call<OrderDataModel> getMyOrders(@Query("type") String order_type,
                                     @Query("token") String user_token,
                                     @Query("page") int page_index
                                     );

    @GET("/api/q&a")
    Call<QuestionsDataModel> getQuestions(@Query("page") int page_index
    );

    @POST("/api/orders")
    Call<ResponseBody> sendOrder(@Body OrderToUploadModel orderToUploadModel);
}
