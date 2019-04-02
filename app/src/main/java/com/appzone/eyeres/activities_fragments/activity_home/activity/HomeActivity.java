package com.appzone.eyeres.activities_fragments.activity_home.activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appzone.eyeres.R;
import com.appzone.eyeres.activities_fragments.activity_home.fragments.fragment_cart.Fragment_Cart;
import com.appzone.eyeres.activities_fragments.activity_home.fragments.fragment_cart.Fragment_Client_Data;
import com.appzone.eyeres.activities_fragments.activity_home.fragments.fragment_details.Fragment_Accessories_Details;
import com.appzone.eyeres.activities_fragments.activity_home.fragments.fragment_details.Fragment_Lenses_Details;
import com.appzone.eyeres.activities_fragments.activity_home.fragments.fragment_home.Fragment_Brand_Details;
import com.appzone.eyeres.activities_fragments.activity_home.fragments.fragment_home.fragment_more.Fragment_Favourite;
import com.appzone.eyeres.activities_fragments.activity_home.fragments.fragment_home.Fragment_Home;
import com.appzone.eyeres.activities_fragments.activity_home.fragments.fragment_home.Fragment_Offers;
import com.appzone.eyeres.activities_fragments.activity_home.fragments.fragment_home.Fragment_Profile;
import com.appzone.eyeres.activities_fragments.activity_home.fragments.fragment_home.fragment_more.Fragment_More;
import com.appzone.eyeres.activities_fragments.activity_home.fragments.fragment_home.fragment_more.Fragment_Question;
import com.appzone.eyeres.activities_fragments.activity_home.fragments.fragment_home.fragment_orders.Fragment_Orders;
import com.appzone.eyeres.activities_fragments.activity_home.fragments.fragment_home.fragment_store.Fragment_Color;
import com.appzone.eyeres.activities_fragments.activity_home.fragments.fragment_home.fragment_store.Fragment_Special_Lenses;
import com.appzone.eyeres.activities_fragments.activity_home.fragments.fragment_home.fragment_store.Fragment_Store;
import com.appzone.eyeres.activities_fragments.activity_home.fragments.fragment_home.fragment_store.Fragment_Tools;
import com.appzone.eyeres.activities_fragments.activity_home.fragments.fragment_home.fragment_store.Fragment_Transparent;
import com.appzone.eyeres.activities_fragments.activity_home.fragments.fragment_order_details.Fragment_Order_Details;
import com.appzone.eyeres.activities_fragments.activity_home.fragments.fragment_search.Fragment_Search;
import com.appzone.eyeres.activities_fragments.activity_sign_in.activity.SignInActivity;
import com.appzone.eyeres.activities_fragments.activity_terms_conditions.TermsConditionsActivity;
import com.appzone.eyeres.local_manager.LocalManager;
import com.appzone.eyeres.models.BrandsDataModel;
import com.appzone.eyeres.models.ItemCartModel;
import com.appzone.eyeres.models.OrderDataModel;
import com.appzone.eyeres.models.OrderStatusModel;
import com.appzone.eyeres.models.OrderToUploadModel;
import com.appzone.eyeres.models.ProductDataModel;
import com.appzone.eyeres.models.UserModel;
import com.appzone.eyeres.preferences.Preferences;
import com.appzone.eyeres.remote.Api;
import com.appzone.eyeres.share.Common;
import com.appzone.eyeres.singletone.OrderCartSingleTone;
import com.appzone.eyeres.singletone.UserSingleTone;
import com.appzone.eyeres.tags.Tags;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    //////////////////////////////////////////
    private Fragment_Home fragment_home;
    private Fragment_Lenses_Details fragment_Lenses_details;
    private Fragment_Accessories_Details fragment_accessories_details;
    private Fragment_Search fragment_search;
    private Fragment_Cart fragment_cart;
    private Fragment_Client_Data fragment_client_data;
    private Fragment_Brand_Details fragment_brand_details;
    /////////////////////////////////////////
    private Fragment_Store fragment_store;
    private Fragment_Transparent fragment_transparent;
    private Fragment_Color fragment_color;
    private Fragment_Tools fragment_tools;
    ////////////////////////////////////////////
    private Fragment_Orders fragment_orders;
    private Fragment_Offers fragment_offers;
    private Fragment_Favourite fragment_favourite;
    private Fragment_Profile fragment_profile;
    private Fragment_Order_Details fragment_order_details;
    private Fragment_More fragment_more;
    private Fragment_Question fragment_question;
    private Fragment_Special_Lenses fragment_special_lenses;

    //////////////////////////////////////////
    private Preferences preferences;
    private UserSingleTone userSingleTone;
    private UserModel userModel;
    private OrderToUploadModel orderToUploadModel;
    private OrderCartSingleTone orderCartSingleTone;
    private String current_language;
    private int fragment_count = 0;
    //////////////////////////////////////////////


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalManager.updateResources(newBase, LocalManager.getLanguage(newBase)));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        fragmentManager = getSupportFragmentManager();

        initView();
        DisplayFragmentHome();
        getDataFromIntent();

    }


    private void initView() {

        Paper.init(this);
        current_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        orderCartSingleTone = OrderCartSingleTone.newInstance();
        preferences = Preferences.getInstance();
        String session = preferences.getSession(this);
        userSingleTone = UserSingleTone.getInstance();
        if (session.equals(Tags.session_login)) {
            userSingleTone.setUserModel(preferences.getUserData(this));
        }
        userModel = userSingleTone.getUserModel();


        if (userModel != null) {
            if (!EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().register(this);
            }

            updateUserToken(userModel.getToken());
        }

        if (preferences.getCartList(this).size() > 0) {
            orderCartSingleTone.setItemCartModelList(preferences.getCartList(this));
            UpdateCartCounter(orderCartSingleTone.getItemsCount());
        }

    }

    private void getDataFromIntent() {

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("status")) {
                final int status = intent.getIntExtra("status", 0);

                if (status == Tags.accepted_order) {


                    new Handler()
                            .postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    DisplayFragmentOrders(status);

                                }
                            }, 1);
                } else if (status == Tags.refused_order) {

                    new Handler()
                            .postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    DisplayFragmentOrders(status);

                                }
                            }, 1);
                } else if (status == Tags.finished_order) {

                    new Handler()
                            .postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    DisplayFragmentOrders(status);

                                }
                            }, 1);
                }
            }

            if (intent.hasExtra("signup")) {
                new Handler()
                        .postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                CreateWelcomeNotification();

                            }
                        }, 2000);
            }

        }

    }

    public void updateUserData(UserModel userModel) {
        this.userModel = userModel;
    }


    public void UpdateCartCounter(final int counter) {
        new Handler()
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (fragment_home != null && fragment_home.isAdded()) {
                            fragment_home.updateCartCounter(counter);
                        }
                    }
                }, 1);
    }

    private void CreateWelcomeNotification() {
        String sound_path = "android.resource://" + getPackageName() + "/" + R.raw.not;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String CHANNEL_ID = "my_channel_01";
            CharSequence CHANNEL_NAME = "channel_name";
            int IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE);
            channel.setShowBadge(true);
            channel.setSound(Uri.parse(sound_path), new AudioAttributes.Builder()
                    .setLegacyStreamType(AudioManager.STREAM_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)
                    .build()
            );

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setChannelId(CHANNEL_ID);
            builder.setSound(Uri.parse(sound_path));
            builder.setContentTitle(getString(R.string.welcome));
            builder.setContentText(getString(R.string.welcome_thank));
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setLargeIcon(bitmap);


            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (manager != null) {
                manager.createNotificationChannel(channel);
                manager.notify(1, builder.build());

            }


        } else {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setSound(Uri.parse(sound_path));
            builder.setContentTitle(getString(R.string.welcome));
            builder.setContentText(getString(R.string.welcome_thank));
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setLargeIcon(bitmap);


            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (manager != null) {
                manager.notify(1, builder.build());

            }
        }
    }

    /////////////////////////////////////////////////////
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ListenToNotification(OrderStatusModel orderStatusModel) {
        final int status = orderStatusModel.getStatus();
        RefreshFragmentOrders(status);
        new Handler()
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if (fragment_order_details != null && fragment_order_details.isAdded()) {
                            fragment_order_details.updateStepView(status);
                        }
                    }
                }, 1);
    }

    public void RefreshFragmentOrders(final int status) {
        new Handler()
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if (fragment_orders != null && fragment_orders.isAdded()) {
                            fragment_orders.RefreshFragments();
                            if (status == Tags.accepted_order) {
                                fragment_orders.NavigateToFragmentCurrent();

                            } else if (status == Tags.finished_order) {
                                fragment_orders.NavigateToFragmentPrevious();

                            } else if (status == Tags.refused_order) {
                                fragment_orders.NavigateToFragmentNew();

                            } else if (status == Tags.new_order) {
                                fragment_orders.NavigateToFragmentNew();

                            }
                        }
                    }
                }, 1);
    }
    /////////////////////////////////////////////////////


    private void updateUserToken(final String user_token) {
        FirebaseInstanceId.getInstance()
                .getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (task.isSuccessful()) {
                            String token = task.getResult().getToken();
                            Api.getService()
                                    .updateToken(user_token, token)
                                    .enqueue(new Callback<ResponseBody>() {
                                        @Override
                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                            if (response.isSuccessful()) {
                                                Log.e("sucess", "token_updated");
                                            } else {
                                                try {
                                                    Log.e("error_code", response.code() + "_" + response.errorBody().string());
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                                            try {
                                                Log.e("error", t.getMessage());
                                            } catch (Exception e) {
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    public void DisplayFragmentHome() {


        if (fragment_home == null) {
            fragment_home = Fragment_Home.newInstance();
        }

        if (!fragment_home.isAdded()) {

            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_home, "fragment_home").addToBackStack("fragment_home").commit();
            new Handler()
                    .postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            DisplayFragmentStore();

                        }
                    }, 1);

        } else {

            fragmentManager.beginTransaction().show(fragment_home).commit();


        }

    }

    public void DisplayFragmentLensesDetails(ProductDataModel.ProductModel productModel) {
        fragment_count++;
        //HideFragmentHome();
        fragment_Lenses_details = Fragment_Lenses_Details.newInstance(productModel);

        if (!fragment_Lenses_details.isAdded()) {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_Lenses_details, "fragment_Lenses_details").addToBackStack("fragment_Lenses_details").commit();
        }
    }

    public void DisplayFragmentAccessoriesDetails(ProductDataModel.ProductModel productModel) {
        fragment_count++;

        //HideFragmentHome();
        fragment_accessories_details = Fragment_Accessories_Details.newInstance(productModel);

        if (!fragment_accessories_details.isAdded()) {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_accessories_details, "fragment_accessories_details").addToBackStack("fragment_accessories_details").commit();
        }
    }


    /////////////////////////////////////////////////////////////////

    public void DisplayFragmentTransparent() {
        if (fragment_color != null && fragment_color.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_color).commit();
        }
        if (fragment_tools != null && fragment_tools.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_tools).commit();
        }

        if (fragment_transparent == null) {
            fragment_transparent = Fragment_Transparent.newInstance();
        }

        if (!fragment_transparent.isAdded()) {
            fragmentManager.beginTransaction().add(R.id.store_fragment_container, fragment_transparent, "fragment_transparent").addToBackStack("fragment_transparent").commit();

        } else {
            fragmentManager.beginTransaction().show(fragment_transparent).commit();
            new Handler()
                    .postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            fragment_transparent.getProducts();
                        }
                    }, 1);
        }


    }

    public void DisplayFragmentColor() {

        if (fragment_transparent != null && fragment_transparent.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_transparent).commit();
        }
        if (fragment_tools != null && fragment_tools.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_tools).commit();
        }

        if (fragment_color == null) {
            fragment_color = Fragment_Color.newInstance();
        }

        if (!fragment_color.isAdded()) {
            fragmentManager.beginTransaction().add(R.id.store_fragment_container, fragment_color, "fragment_color").addToBackStack("fragment_color").commit();
        } else {
            fragmentManager.beginTransaction().show(fragment_color).commit();
            new Handler()
                    .postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            fragment_color.getProducts();
                            fragment_store.setSelectedDefault();
                        }
                    }, 1);

        }

    }

    public void DisplayFragmentTools() {


        if (fragment_transparent != null && fragment_transparent.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_transparent).commit();
        }
        if (fragment_color != null && fragment_color.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_color).commit();
        }

        if (fragment_tools == null) {
            fragment_tools = Fragment_Tools.newInstance();
        }

        if (!fragment_tools.isAdded()) {
            fragmentManager.beginTransaction().add(R.id.store_fragment_container, fragment_tools, "fragment_tools").addToBackStack("fragment_tools").commit();
        } else {
            fragmentManager.beginTransaction().show(fragment_tools).commit();
            new Handler()
                    .postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            fragment_color.getProducts();
                        }
                    }, 1);
        }

    }

    //////////////////////////////////////////////////////////////////
    public void DisplayFragmentSearch() {
        fragment_count++;

        //HideFragmentHome();

        fragment_search = Fragment_Search.newInstance();

        if (!fragment_search.isAdded()) {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_search, "fragment_search").addToBackStack("fragment_search").commit();
        }

    }

    public void DisplayFragmentCart() {

        fragment_count ++;
        //HideFragmentHome();
        fragment_cart = Fragment_Cart.newInstance();

        if (!fragment_cart.isAdded()) {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_cart, "fragment_cart").addToBackStack("fragment_cart").commit();
        } else {
            fragmentManager.beginTransaction().show(fragment_cart).commit();
        }

    }

    public void DisplayFragmentClientData(double total_cost) {

        if (userModel == null) {
            Common.CreateUserNotSignInAlertDialog(this, orderCartSingleTone.getItemCartModelList());
        } else {
            fragment_count++;

            fragment_client_data = Fragment_Client_Data.newInstance(total_cost);

            if (!fragment_client_data.isAdded()) {
                fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_client_data, "fragment_client_data").addToBackStack("fragment_client_data").commit();
            } else {
                fragmentManager.beginTransaction().show(fragment_client_data).commit();
            }
        }


    }

    public void DisplayFragmentOrderDetails(OrderDataModel.OrderModel orderModel) {

        fragment_count++;

        // HideFragmentHome();
        fragment_order_details = Fragment_Order_Details.newInstance(orderModel);

        if (!fragment_order_details.isAdded()) {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_order_details, "fragment_order_details").addToBackStack("fragment_order_details").commit();
        } else {
            fragmentManager.beginTransaction().show(fragment_order_details).commit();
        }

    }

    public void DisplayFragmentBrandDetails(BrandsDataModel.BrandModel brandModel) {
        fragment_count++;

       // HideFragmentHome();
        fragment_brand_details = Fragment_Brand_Details.newInstance(brandModel);

        if (!fragment_brand_details.isAdded()) {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_brand_details, "fragment_brand_details").addToBackStack("fragment_brand_details").commit();
        } else {
            fragmentManager.beginTransaction().show(fragment_brand_details).commit();
        }

    }

    //////////////////////////////////////////////////////////////////
    public void DisplayFragmentStore() {
        if (fragment_orders != null && fragment_orders.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_orders).commit();
        }
        if (fragment_offers != null && fragment_offers.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_offers).commit();
        }
        if (fragment_special_lenses != null && fragment_special_lenses.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_special_lenses).commit();
        }
        if (fragment_more != null && fragment_more.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_more).commit();
        }

        if (fragment_store == null) {

            fragment_store = Fragment_Store.newInstance();
        }

        if (!fragment_store.isAdded()) {

            fragmentManager.beginTransaction().add(R.id.home_fragment_container, fragment_store, "fragment_store").addToBackStack("fragment_store").commit();
            new Handler()
                    .postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            DisplayFragmentColor();

                        }
                    }, 1);

        } else {
            fragmentManager.beginTransaction().show(fragment_store).commit();


        }
        UpdateBottomNavigationPosition(0);


    }

    public void DisplayFragmentOrders(final int status) {
        if (userModel == null) {
            Common.CreateUserNotSignInAlertDialog(this, orderCartSingleTone.getItemCartModelList());
        } else {

            if (fragment_store != null && fragment_store.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_store).commit();
            }
            if (fragment_offers != null && fragment_offers.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_offers).commit();
            }
            if (fragment_special_lenses != null && fragment_special_lenses.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_special_lenses).commit();
            }
            if (fragment_more != null && fragment_more.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_more).commit();
            }

            if (fragment_orders == null) {
                fragment_orders = Fragment_Orders.newInstance();
            }

            if (fragment_orders.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_orders).commit();
            } else {
                fragmentManager.beginTransaction().add(R.id.home_fragment_container, fragment_orders, "fragment_orders").addToBackStack("fragment_orders").commit();

            }
            new Handler()
                    .postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (status == Tags.new_order) {
                                fragment_orders.NavigateToFragmentNew();
                            } else if (status == Tags.accepted_order) {
                                fragment_orders.NavigateToFragmentCurrent();
                            } else if (status == Tags.refused_order) {
                                fragment_orders.NavigateToFragmentNew();

                            } else if (status == Tags.finished_order) {
                                fragment_orders.NavigateToFragmentPrevious();
                            }
                        }
                    }, 1);
            UpdateBottomNavigationPosition(1);
        }


    }

    public void DisplayFragmentOffers() {


        if (fragment_store != null && fragment_store.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_store).commit();
        }
        if (fragment_orders != null && fragment_orders.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_orders).commit();
        }
        if (fragment_special_lenses != null && fragment_special_lenses.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_special_lenses).commit();
        }
        if (fragment_more != null && fragment_more.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_more).commit();
        }

        if (fragment_offers == null) {
            fragment_offers = Fragment_Offers.newInstance();
        }

        if (fragment_offers.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_offers).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.home_fragment_container, fragment_offers, "fragment_offers").addToBackStack("fragment_offers").commit();

        }
        UpdateBottomNavigationPosition(2);


    }

    public void DisplayFragmentFavourite() {
        if (userModel == null) {
            Common.CreateUserNotSignInAlertDialog(this, orderCartSingleTone.getItemCartModelList());

        } else {

            fragment_count++;

            //HideFragmentHome();
            if (fragment_favourite == null) {
                fragment_favourite = Fragment_Favourite.newInstance();
            }

            if (fragment_favourite.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_favourite).commit();
            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_favourite, "fragment_favourite").addToBackStack("fragment_favourite").commit();

            }

        }


    }

    public void DisplayFragmentSpecialOrder() {
        if (fragment_store != null && fragment_store.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_store).commit();
        }
        if (fragment_orders != null && fragment_orders.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_orders).commit();
        }
        if (fragment_offers != null && fragment_offers.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_offers).commit();
        }
        if (fragment_more != null && fragment_more.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_more).commit();
        }

        if (fragment_special_lenses == null) {
            fragment_special_lenses = Fragment_Special_Lenses.newInstance();
        }

        if (fragment_special_lenses.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_special_lenses).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.home_fragment_container, fragment_special_lenses, "fragment_special_lenses").addToBackStack("fragment_special_lenses").commit();

        }
        UpdateBottomNavigationPosition(3);


    }

    public void DisplayFragmentMore() {
        if (fragment_store != null && fragment_store.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_store).commit();
        }
        if (fragment_orders != null && fragment_orders.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_orders).commit();
        }
        if (fragment_offers != null && fragment_offers.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_offers).commit();
        }
        if (fragment_favourite != null && fragment_favourite.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_favourite).commit();
        }

        if (fragment_more == null) {
            fragment_more = Fragment_More.newInstance();
        }

        if (fragment_more.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_more).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.home_fragment_container, fragment_more, "fragment_more").addToBackStack("fragment_more").commit();

        }

        UpdateBottomNavigationPosition(4);

    }

    public void DisplayFragmentQuestions() {

        fragment_count++;

        //HideFragmentHome();
        fragment_question = Fragment_Question.newInstance();

        if (!fragment_question.isAdded()) {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_question, "fragment_question").addToBackStack("fragment_question").commit();
        }
    }

    public void DisplayFragmentProfile() {
        if (userModel == null) {
            Common.CreateUserNotSignInAlertDialog(this, orderCartSingleTone.getItemCartModelList());

        } else {
            fragment_count++;

            //HideFragmentHome();
            if (fragment_profile == null) {
                fragment_profile = Fragment_Profile.newInstance();
            }

            if (fragment_profile.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_profile).commit();
            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_profile, "fragment_profile").addToBackStack("fragment_profile").commit();

            }

        }


    }

    private void UpdateBottomNavigationPosition(int pos) {
        if (fragment_home != null && fragment_home.isAdded()) {
            fragment_home.UpdateAHBottomNavigationPosition(pos);
        }
    }

    public void NavigateToSignInActivity() {

        if (orderCartSingleTone.getItemsCount() > 0) {
            CreateCartAlertDialog();
        } else {

            if (userModel != null) {
                finish();
            } else {
                Intent intent = new Intent(this, SignInActivity.class);
                startActivity(intent);
                finish();
            }


        }

    }

    public void RefreshFragmentFavorite() {
        new Handler()
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (fragment_favourite != null && fragment_favourite.isAdded()) {
                            fragment_favourite.getFavorite();
                        }
                    }
                }, 1);
    }

    public void RefreshFragmentStore() {
        new Handler()
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (fragment_transparent != null && fragment_transparent.isAdded()) {
                            fragment_transparent.Refresh();
                        }

                        if (fragment_color != null && fragment_color.isAdded()) {
                            fragment_color.Refresh();
                        }
                        if (fragment_tools != null && fragment_tools.isAdded()) {
                            fragment_tools.Refresh();
                        }
                    }
                }, 1);
    }

    public void HideFragmentHome() {
        if (fragment_home != null && fragment_home.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_home).commit();
        }
    }

    //////////////////////////////////////////////
    public void SaveItemsData(List<ItemCartModel> itemCartModelList, double total_product_cost) {
        if (orderToUploadModel == null) {
            orderToUploadModel = new OrderToUploadModel();
        }

        orderToUploadModel.setItemsList(itemCartModelList);
        orderToUploadModel.setTotal(total_product_cost);


    }

    public void UploadOrder(String name, String phone, String address, String note, int payment_method, double total_after_discount,String coupon_code,double coupon_value) {


        if (orderToUploadModel != null) {
            orderToUploadModel.setToken(userModel.getToken());
            orderToUploadModel.setName(name);
            orderToUploadModel.setEmail(userModel.getEmail());
            orderToUploadModel.setPhone(phone);
            orderToUploadModel.setAddress(address);
            orderToUploadModel.setNotes(note);
            orderToUploadModel.setPayment_method(payment_method);
            orderToUploadModel.setTotal_after_discount(total_after_discount);
            orderToUploadModel.setCoupon_code(coupon_code);
            orderToUploadModel.setCoupon_value(coupon_value);

            final ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
            dialog.show();
            Api.getService()
                    .sendOrder(orderToUploadModel)
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                fragment_count--;
                                HomeActivity.super.onBackPressed();
                                new Handler()
                                        .postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (fragment_cart != null && fragment_cart.isAdded()) {
                                                    clearOrderData();
                                                    fragment_cart.clearCart();
                                                }

                                                if (fragment_orders != null && fragment_orders.isAdded()) {
                                                    RefreshFragmentOrders(Tags.new_order);
                                                }

                                                dialog.dismiss();
                                                Toast.makeText(HomeActivity.this, getString(R.string.succ), Toast.LENGTH_SHORT).show();
                                            }
                                        }, 1);
                            } else {
                                dialog.dismiss();
                                Toast.makeText(HomeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                try {
                                    Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            try {
                                dialog.dismiss();
                                Toast.makeText(HomeActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                Log.e("Error", t.getMessage());
                            } catch (Exception e) {
                            }
                        }
                    });


        }


    }

    public void NavigateToTermsCondition(int type) {
        Intent intent = new Intent(this, TermsConditionsActivity.class);
        intent.putExtra("type", type);
        startActivity(intent);
        if (current_language.equals("ar")) {
            overridePendingTransition(R.anim.from_right, R.anim.to_left);
        } else {
            overridePendingTransition(R.anim.from_left, R.anim.to_right);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragmentList = fragmentManager.getFragments();
        for (Fragment fragment : fragmentList) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<Fragment> fragmentList = fragmentManager.getFragments();
        for (Fragment fragment : fragmentList) {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void Logout() {
        final ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.logging_out));
        dialog.show();
        Api.getService()
                .logout(userModel.getToken())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            new Handler()
                                    .postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                            if (manager != null) {
                                                manager.cancelAll();
                                            }
                                        }
                                    }, 1);
                            userModel = null;
                            userSingleTone.clear(HomeActivity.this);
                            dialog.dismiss();
                            NavigateToSignInActivity();

                        } else {
                            dialog.dismiss();

                            Toast.makeText(HomeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                            try {
                                Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            Toast.makeText(HomeActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("Error", t.getMessage());
                        } catch (Exception e) {
                        }
                    }
                });
    }

    public void CreateCartAlertDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setCancelable(true)
                .create();

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_remove_cart_item, null);
        FrameLayout fl_delete = view.findViewById(R.id.fl_delete);
        FrameLayout fl_cancel = view.findViewById(R.id.fl_cancel);

        TextView tv_not_dialog = view.findViewById(R.id.tv_not_dialog);
        TextView tv_content = view.findViewById(R.id.tv_content);
        tv_not_dialog.setText(String.valueOf(orderCartSingleTone.getItemsCount()));
        if (orderCartSingleTone.getItemsCount() > 1) {
            tv_content.setText(getString(R.string.the_cart_contains) + " " + orderCartSingleTone.getItemsCount() + " " + getString(R.string.items) + " " + getString(R.string.delete_items));

        } else if (orderCartSingleTone.getItemsCount() == 1) {
            tv_content.setText(getString(R.string.the_cart_contains) + " " + orderCartSingleTone.getItemsCount() + " " + getString(R.string.item) + " " + getString(R.string.delete_item));

        }

        fl_delete.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                        clearOrderData();
                    }
                });
        fl_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_window_bg);
        dialog.setView(view);
        dialog.show();
    }

    private void clearOrderData() {
        preferences.clear_cart(this);
        orderCartSingleTone.clear();
        orderToUploadModel = null;
        UpdateCartCounter(0);
    }


    public void Back() {
        if (fragment_count>0)
        {
            Log.e("fragment_count",fragment_count+"_");
            super.onBackPressed();
            fragment_count = fragment_count-1;
        }else if (fragment_count == 0) {
            if (fragment_home!=null&&!fragment_home.isVisible())
            {
                DisplayFragmentHome();

            }
            if (fragment_store != null && !fragment_store.isVisible()) {

                DisplayFragmentStore();
            } else {
                if (fragment_color != null && !fragment_color.isVisible()) {

                    DisplayFragmentColor();
                } else {

                    NavigateToSignInActivity();

                }
            }
        }
       /*
        }

       *//* if (fragment_brand_details != null && fragment_brand_details.isAdded()) {

        } else if (fragment_accessories_details != null && fragment_accessories_details.isAdded()) {
            super.onBackPressed();

        } else if (fragment_Lenses_details != null && fragment_Lenses_details.isAdded()) {
            super.onBackPressed();
        } else if (fragment_question != null && fragment_question.isAdded()) {
            super.onBackPressed();

        } else if (fragment_favourite != null && fragment_favourite.isAdded()) {
            super.onBackPressed();

        } else if (fragment_profile != null && fragment_profile.isAdded()) {
            super.onBackPressed();
        } else if (fragment_client_data != null && fragment_client_data.isAdded()) {
            super.onBackPressed();

        } else if (fragment_order_details != null && fragment_order_details.isAdded()) {
            super.onBackPressed();

        } else if (fragment_search != null && fragment_search.isAdded()) {
            super.onBackPressed();

        } else if (fragment_cart != null && fragment_cart.isAdded()) {
            super.onBackPressed();
        } else {
            if (fragment_home != null && fragment_home.isVisible()) {

                if (fragment_store != null && !fragment_store.isVisible()) {

                    DisplayFragmentStore();
                } else {
                    if (fragment_color != null && !fragment_color.isVisible()) {

                        DisplayFragmentColor();
                    } else {

                        NavigateToSignInActivity();

                    }
                }
        }



            *//**//*if (fragment_store != null && !fragment_store.isVisible()) {

                DisplayFragmentStore();
            } else {
                Log.e("1", "1");
                if (fragment_color != null && !fragment_color.isVisible()) {
                    Log.e("2", "2");

                    DisplayFragmentColor();
                } else {
                    Log.e("3", "3");

                    NavigateToSignInActivity();

                }
            }*//**//*


        }*//*
         */

    }


    @Override
    public void onBackPressed() {
        Back();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }


    public void RefreshActivity(String lang) {
        Paper.book().write("lang", lang);
        LocalManager.setNewLocale(this, lang);
        new Handler()
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                }, 1050);

    }
}
