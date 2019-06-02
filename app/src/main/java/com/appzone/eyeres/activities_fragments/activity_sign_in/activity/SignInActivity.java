package com.appzone.eyeres.activities_fragments.activity_sign_in.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.appzone.eyeres.R;
import com.appzone.eyeres.activities_fragments.activity_home.activity.HomeActivity;
import com.appzone.eyeres.activities_fragments.activity_sign_in.fragments.Fragment_Chooser;
import com.appzone.eyeres.activities_fragments.activity_sign_in.fragments.Fragment_Complete_Profile;
import com.appzone.eyeres.activities_fragments.activity_sign_in.fragments.Fragment_Language;
import com.appzone.eyeres.activities_fragments.activity_sign_in.fragments.Fragment_Phone;
import com.appzone.eyeres.activities_fragments.activity_terms_conditions.TermsConditionsActivity;
import com.appzone.eyeres.local_manager.LocalManager;
import com.appzone.eyeres.models.UserModel;
import com.appzone.eyeres.preferences.Preferences;
import com.appzone.eyeres.remote.Api;
import com.appzone.eyeres.share.Common;
import com.appzone.eyeres.singletone.UserSingleTone;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {

    private View root;
    private Snackbar snackbar;
    private FragmentManager fragmentManager;
    private Fragment_Chooser fragment_chooser;
    private Fragment_Phone fragment_phone;
    private Fragment_Complete_Profile fragment_complete_profile;
    private Fragment_Language fragment_language;
    public String phone="";
    private Preferences preferences;
    private String current_language;
    private int ACCEPT_RULE_REQ = 1;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalManager.updateResources(newBase,LocalManager.getLanguage(newBase)));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        initView();
        if (savedInstanceState==null)
        {
            int state = preferences.getFragmentState(this);
            if (state == 1)
            {
                DisplayFragmentChooser();

            }else
                {
                    DisplayFragmentLanguage();

                }

        }
    }

    private void initView() {

        Paper.init(this);
        current_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        preferences = Preferences.getInstance();
        fragmentManager = getSupportFragmentManager();
        root = findViewById(R.id.root);




    }

    public void DisplayFragmentLanguage()
    {
        preferences.saveLoginFragmentState(this,0);

        if (fragment_language == null)
        {
            fragment_language = Fragment_Language.newInstance();
        }
        fragmentManager.beginTransaction().add(R.id.fragment_sign_container,fragment_language,"fragment_language").addToBackStack("fragment_language").commit();
    }
    public void DisplayFragmentChooser()
    {
        if (fragment_chooser==null)
        {
            fragment_chooser = Fragment_Chooser.newInstance();
        }

        if (fragment_chooser.isAdded())
        {
            fragmentManager.beginTransaction().show(fragment_chooser).commit();
        }else
            {
                fragmentManager.beginTransaction().add(R.id.fragment_sign_container,fragment_chooser,"fragment_chooser").addToBackStack("fragment_chooser").commit();
            }

    }

    public void DisplayFragmentPhone()
    {
        if (fragment_phone==null)
        {
            fragment_phone = Fragment_Phone.newInstance();
        }

        if (fragment_phone.isAdded())
        {
            fragmentManager.beginTransaction().show(fragment_phone).commit();
        }else
        {
            fragmentManager.beginTransaction().add(R.id.fragment_sign_container,fragment_phone,"fragment_phone").addToBackStack("fragment_phone").commit();
        }

    }

    public void DisplayFragmentCompleteProfile(String phone)
    {
        this.phone = phone;

        if (fragment_complete_profile==null)
        {
            fragment_complete_profile = Fragment_Complete_Profile.newInstance();
        }

        if (fragment_complete_profile.isAdded())
        {
            fragmentManager.beginTransaction().show(fragment_complete_profile).commit();
        }else
        {
            fragmentManager.beginTransaction().add(R.id.fragment_sign_container,fragment_complete_profile,"fragment_complete_profile").addToBackStack("fragment_complete_profile").commit();
        }

    }
    public void NavigateToHomeActivity(boolean isSignUp)
    {
        Intent intent = new Intent(this, HomeActivity.class);
        if (isSignUp)
        {
            intent.putExtra("signup",1);
        }
        startActivity(intent);
        finish();
        if (current_language.equals("ar"))
        {
            overridePendingTransition(R.anim.from_right,R.anim.to_left);


        }else
        {
            overridePendingTransition(R.anim.from_left,R.anim.to_right);


        }


    }

    public void NavigateToTermsActivity()
    {
        Intent intent = new Intent(this, TermsConditionsActivity.class);
        intent.putExtra("type",1);
        startActivity(intent);
        if (current_language.equals("ar"))
        {
            overridePendingTransition(R.anim.from_right,R.anim.to_left);


        }else
        {
            overridePendingTransition(R.anim.from_left,R.anim.to_right);


        }
    }

    public void signIn(final String phone)
    {
        final ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.wait));
        dialog.show();
        Api.getService()
                .SignIn(phone)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        if (response.isSuccessful())
                        {
                            DismissSnackBar();
                            dialog.dismiss();

                            if (response.body()!=null)
                            {
                                UserSingleTone userSingleTone = UserSingleTone.getInstance();
                                Preferences preferences = Preferences.getInstance();

                                UserModel userModel = response.body();

                                userSingleTone.setUserModel(userModel);
                                preferences.create_update_userData(SignInActivity.this,userModel);
                                NavigateToHomeActivity(false);
                            }



                        }else
                            {
                                dialog.dismiss();

                                if (response.code() == 402)
                                {
                                    DisplayFragmentCompleteProfile(phone);
                                }else
                                    {
                                        CreateSnackBar(getString(R.string.failed));

                                    }
                                try {
                                    Log.e("error_code",response.code()+"_"+response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            CreateSnackBar(getString(R.string.something));
                            Log.e("Error",t.getMessage());

                        }catch (Exception e){}
                    }
                });
    }
    public void signUp(String name, String email)
    {

        final ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.wait));
        dialog.show();

        RequestBody name_part = Common.getRequestBodyText(name);
        RequestBody phone_part = Common.getRequestBodyText(phone);
        RequestBody email_part = Common.getRequestBodyText(email);

        Api.getService()
                .SignUp(name_part,phone_part,email_part)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {


                        if (response.isSuccessful())
                        {
                            dialog.dismiss();
                            DismissSnackBar();

                            if (response.body()!=null)
                            {
                                UserSingleTone userSingleTone = UserSingleTone.getInstance();
                                Preferences preferences = Preferences.getInstance();
                                UserModel userModel = response.body();
                                userSingleTone.setUserModel(userModel);
                                preferences.create_update_userData(SignInActivity.this,userModel);

                                NavigateToHomeActivity(true);



                            }else
                            {
                                Common.CreateSignAlertDialog(SignInActivity.this,getString(R.string.something));
                            }
                        }else
                        {

                            dialog.dismiss();

                            if (response.code()== 422)
                            {
                                Common.CreateSignAlertDialog(SignInActivity.this,getString(R.string.phone_number_exists));

                            }else
                            {
                                CreateSnackBar(getString(R.string.failed));
                            }

                            try {
                                Log.e("error_code",response.code()+"_"+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            CreateSnackBar(getString(R.string.something));
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }
    private void CreateSnackBar(String msg)
    {
        snackbar = Common.CreateSnackBar(this,root,msg);
        snackbar.show();

    }
    private void DismissSnackBar()
    {
        if (snackbar!=null)
        {
            snackbar.dismiss();
        }
    }

    public void RefreshActivity(String selected_language)
    {
        Paper.book().write("lang",selected_language);
        LocalManager.setNewLocale(this,selected_language);
        preferences.saveLoginFragmentState(this,1);
        Intent intent = getIntent();
        finish();
        if (selected_language.equals("ar"))
        {
            overridePendingTransition(R.anim.from_left,R.anim.to_right);


        }else
        {
            overridePendingTransition(R.anim.from_right,R.anim.to_left);

        }
        startActivity(intent);
        if (selected_language.equals("ar"))
        {
            overridePendingTransition(R.anim.from_right,R.anim.to_left);



        }else
        {
            overridePendingTransition(R.anim.from_left,R.anim.to_right);

        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragmentList = fragmentManager.getFragments();
        for (Fragment fragment : fragmentList)
        {
            fragment.onActivityResult(requestCode, resultCode, data);
        }

        if (requestCode == ACCEPT_RULE_REQ && resultCode == RESULT_OK && data!=null)
        {
            if (fragment_complete_profile!=null&&fragment_complete_profile.isVisible())
            {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fragment_complete_profile.acceptRule();
                    }
                },1);
            }
        }



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<Fragment> fragmentList = fragmentManager.getFragments();
        for (Fragment fragment : fragmentList)
        {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    public void Back() {

        String name = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount()-1).getName();
        if (name.equals("fragment_chooser"))
        {
            finish();
        }else
            {
                super.onBackPressed();
            }
    }

    @Override
    public void onBackPressed() {
        Back();
    }



}
