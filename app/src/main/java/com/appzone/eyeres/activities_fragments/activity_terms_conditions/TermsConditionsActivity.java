package com.appzone.eyeres.activities_fragments.activity_terms_conditions;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appzone.eyeres.R;
import com.appzone.eyeres.models.Terms_ConditionModel;
import com.appzone.eyeres.remote.Api;

import java.io.IOException;
import java.util.Locale;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TermsConditionsActivity extends AppCompatActivity {
    private ImageView arrow;
    private LinearLayout ll_back;
    private SmoothProgressBar smoothprogressbar;
    private TextView tv_content;
    private String current_lang="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_conditions);
        initView();
    }

    private void initView() {
        current_lang = Locale.getDefault().getLanguage();
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

        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getTerms();
    }

    private void getTerms() {
        Api.getService()
                .getTerms()
                .enqueue(new Callback<Terms_ConditionModel>() {
                    @Override
                    public void onResponse(Call<Terms_ConditionModel> call, Response<Terms_ConditionModel> response) {

                        if (response.isSuccessful()&& response.body()!=null)
                        {
                            smoothprogressbar.setVisibility(View.GONE);
                            if (current_lang.equals("ar"))
                            {
                                tv_content.setText(response.body().getSite_terms_conditions().getAr());
                            }else
                                {
                                    tv_content.setText(response.body().getSite_terms_conditions().getEn());

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
}
