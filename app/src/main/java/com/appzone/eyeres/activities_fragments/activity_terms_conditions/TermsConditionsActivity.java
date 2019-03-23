package com.appzone.eyeres.activities_fragments.activity_terms_conditions;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appzone.eyeres.R;
import com.appzone.eyeres.local_manager.LocalManager;
import com.appzone.eyeres.models.Terms_ConditionModel;
import com.appzone.eyeres.remote.Api;

import java.io.IOException;
import java.util.Locale;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TermsConditionsActivity extends AppCompatActivity {
    private ImageView arrow;
    private LinearLayout ll_back;
    private SmoothProgressBar smoothprogressbar;
    private TextView tv_content,tv_title;
    private String current_lang="";

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalManager.updateResources(newBase,LocalManager.getLanguage(newBase)));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_conditions);
        initView();
        getDataFromIntent();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();

        if (intent!=null)
        {
            int type = intent.getIntExtra("type",0);

            updateUI(type);
        }
    }



    private void initView() {

        Paper.init(this);

        current_lang = Paper.book().read("lang",Locale.getDefault().getLanguage());
        arrow = findViewById(R.id.arrow);
        if (current_lang.equals("ar"))
        {
            arrow.setImageResource(R.drawable.white_right_arrow);
        }else
            {
                arrow.setImageResource(R.drawable.white_left_arrow);

            }
        ll_back = findViewById(R.id.ll_back);
        smoothprogressbar = findViewById(R.id.smoothprogressbar);
        tv_content = findViewById(R.id.tv_content);
        tv_title = findViewById(R.id.tv_title);

        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                if (current_lang.equals("ar"))
                {
                    overridePendingTransition(R.anim.from_left,R.anim.to_right);

                }else
                {
                    overridePendingTransition(R.anim.from_right,R.anim.to_left);

                }
            }
        });


    }

    private void updateUI(int type) {

        if (type == 1)
        {
            tv_title.setText(getString(R.string.terms_and_conditions));
            getTerms(1);
        }else if (type == 2)
            {
                tv_title.setText(getString(R.string.about_tour));
                getTerms(2);

            }

        else if (type == 3)
        {
            tv_title.setText(R.string.delivery_policy);
            getTerms(3);


        }

    }




    private void getTerms(int type) {
        Api.getService()
                .getTerms(type)
                .enqueue(new Callback<Terms_ConditionModel>() {
                    @Override
                    public void onResponse(Call<Terms_ConditionModel> call, Response<Terms_ConditionModel> response) {

                        if (response.isSuccessful()&& response.body()!=null)
                        {
                            smoothprogressbar.setVisibility(View.GONE);
                            if (current_lang.equals("ar"))
                            {
                                if (response.body().getSite_terms_conditions()!=null&&response.body().getSite_terms_conditions().getAr()!=null)
                                {
                                    tv_content.setText(response.body().getSite_terms_conditions().getAr());

                                }
                            }else
                                {
                                    if (response.body().getSite_terms_conditions()!=null&&response.body().getSite_terms_conditions().getEn()!=null)
                                    {
                                        tv_content.setText(response.body().getSite_terms_conditions().getEn());

                                    }

                                }
                        }else
                            {
                                smoothprogressbar.setVisibility(View.GONE);
                                Toast.makeText(TermsConditionsActivity.this,getString(R.string.failed), Toast.LENGTH_LONG).show();

                                try {
                                    Log.e("error_code",response.code()+"_"+response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                    }

                    @Override
                    public void onFailure(Call<Terms_ConditionModel> call, Throwable t) {

                        try {
                            smoothprogressbar.setVisibility(View.GONE);
                            Toast.makeText(TermsConditionsActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("Error",t.getMessage());
                        }catch (Exception e)
                        {

                        }
                    }
                });

    }

    private void getAboutUs() {

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (current_lang.equals("ar"))
        {
            overridePendingTransition(R.anim.from_left,R.anim.to_right);

        }else
        {
            overridePendingTransition(R.anim.from_right,R.anim.to_left);

        }
    }
}
