package com.appzone.eyeres.activities_fragments.activity_sign_in;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.appzone.eyeres.R;
import com.appzone.eyeres.activities_fragments.activity_sign_up.SignUpActivity;
import com.appzone.eyeres.preferences.Preferences;
import com.appzone.eyeres.share.Common;
import com.appzone.eyeres.singletone.UserSingleTone;

import java.util.Locale;

public class SignInActivity extends AppCompatActivity {
    private ImageView image_arrow;
    private LinearLayout ll_back;
    private EditText edt_phone,edt_password;
    private Button btn_sign_in,btn_new_account,btn_skip;
    private View root;
    private Snackbar snackbar;
    private UserSingleTone userSingleTone;
    private Preferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        initView();
    }

    private void initView() {


        image_arrow  = findViewById(R.id.image_arrow);

        if (Locale.getDefault().getLanguage().equals("ar"))
        {
            image_arrow.setImageResource(R.drawable.green_right_arrow);
        }else
            {
                image_arrow.setImageResource(R.drawable.green_left_arrow);

            }
        root = findViewById(R.id.root);
        ll_back  = findViewById(R.id.ll_back);
        edt_phone  = findViewById(R.id.edt_phone);
        edt_password  = findViewById(R.id.edt_password);
        btn_sign_in  = findViewById(R.id.btn_sign_in);
        btn_new_account  = findViewById(R.id.btn_new_account);
        btn_skip  = findViewById(R.id.btn_skip);


        btn_new_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigateToSignUpActivity();
            }
        });

        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckData();
            }
        });

        btn_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigateToHomeActivity();
            }
        });

        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    private void NavigateToHomeActivity() {

    }

    private void NavigateToSignUpActivity() {

        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
        finish();
    }

    private void CheckData() {

        String mPhone = edt_phone.getText().toString().trim();
        String mPassword = edt_password.getText().toString().trim();

        if (!TextUtils.isEmpty(mPhone) &&
                mPhone.length()==9 &&
                TextUtils.isEmpty(mPassword))
        {
            Common.CloseKeyBoard(this,edt_phone);
            edt_phone.setError(null);
            edt_password.setError(null);
            Sign_in(mPhone,mPassword);

        }else
            {
                if (TextUtils.isEmpty(mPhone)) {
                    edt_phone.setError(getString(R.string.field_req));
                }else if (mPhone.length() !=9)
                {
                    edt_phone.setError(getString(R.string.inv_phone));

                }

                if (TextUtils.isEmpty(mPassword))
                {
                    edt_password.setError(getString(R.string.field_req));
                }
            }
    }

    private void Sign_in(String mPhone, String mPassword) {
        final ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.signning_in));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        /*Api.getService()
                .SignIn(mPhone,mPassword)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        if (response.isSuccessful())
                        {
                            dialog.dismiss();
                            DismissSnackBar();
                            if (response.body()!=null&&response.body().getUser()!=null)
                            {
                                userSingleTone = UserSingleTone.getInstance();
                                preferences = Preferences.getInstance();
                                UserModel userModel = response.body();
                                userSingleTone.setUserModel(userModel);
                                preferences.create_update_userData(SignInActivity.this,userModel);
                                NavigateToHomeActivity();



                            }
                        }else
                        {
                            dialog.dismiss();
                            if (response.code()==404)
                            {
                                Common.CreateSignAlertDialog(SignInActivity.this,getString(R.string.inc_phonr_password));
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
                });*/
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

}
