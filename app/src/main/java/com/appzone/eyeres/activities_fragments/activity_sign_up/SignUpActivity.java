package com.appzone.eyeres.activities_fragments.activity_sign_up;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.appzone.eyeres.R;
import com.appzone.eyeres.activities_fragments.activity_home.activity.HomeActivity;
import com.appzone.eyeres.activities_fragments.activity_sign_in.SignInActivity;
import com.appzone.eyeres.share.Common;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Locale;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class SignUpActivity extends AppCompatActivity {

    private EditText edt_name,edt_phone,edt_password,edt_country,edt_email;
    private ImageView image_personal,image_icon1,image_back_photo,image_arrow;
    private FrameLayout fl_sign_up;
    private LinearLayout ll_back;
    private final int IMG1=1;
    private Uri uri=null;
    private final String read_permission = Manifest.permission.READ_EXTERNAL_STORAGE;
    private View root;
    private Snackbar snackbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initView();
    }

    private void initView() {

        root = findViewById(R.id.root);
        ll_back  = findViewById(R.id.ll_back);
        edt_name = findViewById(R.id.edt_name);
        edt_phone =findViewById(R.id.edt_phone);
        edt_password =findViewById(R.id.edt_password);
        edt_email =findViewById(R.id.edt_email);
        edt_country =findViewById(R.id.edt_country);
        image_personal = findViewById(R.id.image_personal);
        image_icon1 = findViewById(R.id.image_icon1);
        image_back_photo = findViewById(R.id.image_back_photo);
        image_arrow = findViewById(R.id.image_arrow);
        fl_sign_up = findViewById(R.id.fl_sign_up);

        if (Locale.getDefault().getLanguage().equals("ar"))
        {
            image_arrow.setImageResource(R.drawable.green_right_arrow);
            image_back_photo.setImageResource(R.drawable.white_right_arrow);

        }else
        {
            image_arrow.setImageResource(R.drawable.green_left_arrow);
            image_back_photo.setImageResource(R.drawable.white_left_arrow);

        }
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigateToSignInActivity();
            }
        });

        image_personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Check_ReadPermission(IMG1);
            }
        });

        fl_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckDate();
            }
        });


    }

    private void CheckDate() {
        String m_name = edt_name.getText().toString().trim();
        String m_phone = edt_phone.getText().toString().trim();
        String m_country = edt_country.getText().toString().trim();
        String m_email = edt_email.getText().toString().trim();
        String m_password = edt_password.getText().toString().trim();


        if (!TextUtils.isEmpty(m_name)&&
                !TextUtils.isEmpty(m_phone)&&
                m_phone.length()==9&&
                !TextUtils.isEmpty(m_country)&&
                !TextUtils.isEmpty(m_email)&&
                Patterns.EMAIL_ADDRESS.matcher(m_email).matches()&&
                !TextUtils.isEmpty(m_password)&&
                uri!=null


                )
        {
            Common.CloseKeyBoard(this,edt_name);
            edt_name.setError(null);
            edt_phone.setError(null);
            edt_country.setError(null);
            edt_email.setError(null);
            edt_password.setError(null);

            Sign_Up(m_name,m_phone,m_country,m_email,m_password);
        }else
            {
                if (TextUtils.isEmpty(m_name))
                {
                    edt_name.setError(getString(R.string.field_req));
                }else
                {
                    edt_name.setError(null);

                }
                if (TextUtils.isEmpty(m_phone))
                {
                    edt_phone.setError(getString(R.string.field_req));
                }else if (m_phone.length()!=9)
                {
                    edt_phone.setError(getString(R.string.inv_phone));
                }
                else
                {
                    edt_phone.setError(null);

                }

                if (TextUtils.isEmpty(m_email))
                {
                    edt_email.setError(getString(R.string.field_req));
                }else if (!Patterns.EMAIL_ADDRESS.matcher(m_email).matches())
                {
                    edt_email.setError(getString(R.string.inv_email));
                }
                else
                {
                    edt_email.setError(null);

                }

                if (TextUtils.isEmpty(m_country))
                {
                    edt_country.setError(getString(R.string.field_req));
                }else
                {
                    edt_country.setError(null);

                }

                if (TextUtils.isEmpty(m_password))
                {
                    edt_password.setError(getString(R.string.field_req));
                }else
                {
                    edt_password.setError(null);

                }


                if (uri == null)
                {
                    Toast.makeText(this,getString(R.string.pers_photo_req), Toast.LENGTH_SHORT).show();
                }
            }
    }

    private void Sign_Up(String m_name, String m_phone, String m_country, String m_email, String m_password) {

        final ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.signingup));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        RequestBody name_part = Common.getRequestBodyText(m_name);
        RequestBody phone_part = Common.getRequestBodyText(m_phone);
        RequestBody email_part = Common.getRequestBodyText(m_email);
        RequestBody country_part = Common.getRequestBodyText(m_country);
        RequestBody password_part = Common.getRequestBodyText(m_password);

        try {
            MultipartBody.Part avatar_part = Common.getMultiPart(this,uri,"avatar");
           /* Api.getService()
                    .SignUp(name_part,phone_part,password_part,email_part,country_part,avatar_part)
                    .enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {


                            if (response.isSuccessful())
                            {
                                dialog.dismiss();
                                dismissSnackBar();

                                if (response.body()!=null && response.body().getUser()!=null)
                                {
                                    UserSingleTone userSingleTone = UserSingleTone.getInstance();
                                    Preferences preferences = Preferences.getInstance();
                                    UserModel userModel = response.body();
                                    userSingleTone.setUserModel(userModel);
                                    preferences.create_update_userData(SignUpActivity.this,userModel);

                                    NavigateToHomeActivity();



                                }else
                                {
                                    Common.CreateSignAlertDialog(SignUpActivity.this,getString(R.string.something));
                                }
                            }else
                            {

                                dismissSnackBar();
                                dialog.dismiss();

                                if (response.code()==422)
                                {
                                    Common.CreateSignAlertDialog(SignUpActivity.this,getString(R.string.phone_number_exists));

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
*/
        }catch (Exception e)
        {
            Toast.makeText(this, R.string.inc_img_path, Toast.LENGTH_SHORT).show();
        }



    }

    private void NavigateToSignInActivity()
    {
        Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
        startActivity(intent);
        finish();
    }
    private void NavigateToHomeActivity()
    {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
    private void Check_ReadPermission(int img_req)
    {
        if (ContextCompat.checkSelfPermission(this,read_permission)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{read_permission},img_req);
        }else
        {
            select_photo(img_req);
        }
    }
    private void select_photo(int img1) {
        Intent intent ;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        }else
        {
            intent = new Intent(Intent.ACTION_GET_CONTENT);

        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("image/*");
        startActivityForResult(intent,img1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG1 && resultCode == Activity.RESULT_OK && data!=null)
        {
            image_icon1.setVisibility(View.GONE);
            uri = data.getData();

            String path = Common.getImagePath(this,uri);
            if (path!=null)
            {
                Picasso.with(this).load(new File(path)).fit().into(image_personal);

            }else
                {
                    Picasso.with(this).load(uri).fit().into(image_personal);

                }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == IMG1)
        {
            if (grantResults.length>0)
            {
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    select_photo(IMG1);
                }else
                {
                    Toast.makeText(this,getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    public void CreateSnackBar(String msg)
    {
        snackbar = Common.CreateSnackBar(this,root,msg);
        snackbar.show();

    }

    public void dismissSnackBar()
    {
        if (snackbar!=null)
        {
            snackbar.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        NavigateToSignInActivity();
    }
}
