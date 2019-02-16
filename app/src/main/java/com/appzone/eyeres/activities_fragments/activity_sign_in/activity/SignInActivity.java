package com.appzone.eyeres.activities_fragments.activity_sign_in.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.appzone.eyeres.R;
import com.appzone.eyeres.activities_fragments.activity_home.activity.HomeActivity;
import com.appzone.eyeres.activities_fragments.activity_sign_in.fragments.Fragment_Chooser;
import com.appzone.eyeres.activities_fragments.activity_sign_in.fragments.Fragment_Complete_Profile;
import com.appzone.eyeres.activities_fragments.activity_sign_in.fragments.Fragment_Phone;
import com.appzone.eyeres.activities_fragments.activity_terms_conditions.TermsConditionsActivity;
import com.appzone.eyeres.share.Common;

import java.util.List;

public class SignInActivity extends AppCompatActivity {

    private View root;
    private Snackbar snackbar;
    private FragmentManager fragmentManager;
    private Fragment_Chooser fragment_chooser;
    private Fragment_Phone fragment_phone;
    private Fragment_Complete_Profile fragment_complete_profile;
    public String phone="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        initView();
    }

    private void initView() {

        fragmentManager = getSupportFragmentManager();
        root = findViewById(R.id.root);
        DisplayFragmentChooser();



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
    public void NavigateToHomeActivity(boolean isSkip)
    {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        if (!isSkip)
        {
            finish();

        }

    }

    public void NavigateToTermsActivity() {
        Intent intent = new Intent(this, TermsConditionsActivity.class);
        startActivity(intent);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragmentList = fragmentManager.getFragments();
        for (Fragment fragment : fragmentList)
        {
            fragment.onActivityResult(requestCode, resultCode, data);
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
