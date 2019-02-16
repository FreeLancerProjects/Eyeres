package com.appzone.eyeres.activities_fragments.activity_terms_conditions;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appzone.eyeres.R;

import java.util.Locale;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

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


    }
}
