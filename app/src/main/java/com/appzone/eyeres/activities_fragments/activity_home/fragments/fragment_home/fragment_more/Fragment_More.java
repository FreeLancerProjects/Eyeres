package com.appzone.eyeres.activities_fragments.activity_home.fragments.fragment_home.fragment_more;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import com.appzone.eyeres.R;
import com.appzone.eyeres.activities_fragments.activity_home.activity.HomeActivity;

import java.util.Locale;

import io.paperdb.Paper;

public class Fragment_More extends Fragment {

    private LinearLayout ll_terms,ll_profile,ll_language,ll_about,ll_question,ll_policy,ll_favorite;
    private ImageView arrow1,arrow2,arrow3,arrow4,arrow5,arrow6,arrow7,image_instagram,image_twitter,image_snapchat;
    private String current_language;
    private HomeActivity activity;
    private String [] language_array;

    public static Fragment_More newInstance()
    {
        return new Fragment_More();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        activity = (HomeActivity) getActivity();
        Paper.init(activity);
        current_language = Paper.book().read("lang", Locale.getDefault().getLanguage());

        arrow1 = view.findViewById(R.id.arrow1);
        arrow2 = view.findViewById(R.id.arrow2);
        arrow3 = view.findViewById(R.id.arrow3);
        arrow4 = view.findViewById(R.id.arrow4);
        arrow5 = view.findViewById(R.id.arrow5);
        arrow6 = view.findViewById(R.id.arrow6);
        arrow7 = view.findViewById(R.id.arrow7);

        language_array = new String[]{"English","العربية"};


        if (current_language.equals("ar"))
        {
            arrow1.setImageResource(R.drawable.black_left_arrow);
            arrow2.setImageResource(R.drawable.black_left_arrow);
            arrow3.setImageResource(R.drawable.black_left_arrow);

            arrow4.setImageResource(R.drawable.black_left_arrow);
            arrow5.setImageResource(R.drawable.black_left_arrow);
            arrow6.setImageResource(R.drawable.black_left_arrow);
            arrow7.setImageResource(R.drawable.black_left_arrow);


        }else
        {
            arrow1.setImageResource(R.drawable.black_right_arrow);
            arrow2.setImageResource(R.drawable.black_right_arrow);
            arrow3.setImageResource(R.drawable.black_right_arrow);

            arrow4.setImageResource(R.drawable.black_right_arrow);
            arrow5.setImageResource(R.drawable.black_right_arrow);
            arrow6.setImageResource(R.drawable.black_right_arrow);
            arrow7.setImageResource(R.drawable.black_right_arrow);

        }


        ll_terms = view.findViewById(R.id.ll_terms);
        ll_profile = view.findViewById(R.id.ll_profile);
        ll_language = view.findViewById(R.id.ll_language);
        ll_about = view.findViewById(R.id.ll_about);
        ll_question = view.findViewById(R.id.ll_question);
        ll_policy = view.findViewById(R.id.ll_policy);
        ll_favorite = view.findViewById(R.id.ll_favorite);

        image_twitter = view.findViewById(R.id.image_twitter);
        image_instagram = view.findViewById(R.id.image_instagram);
        image_snapchat = view.findViewById(R.id.image_snapchat);


        ll_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.NavigateToTermsCondition(1);
            }
        });

        ll_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.NavigateToTermsCondition(2);
            }
        });

        ll_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.NavigateToTermsCondition(3);
            }
        });

        ll_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.DisplayFragmentProfile();
            }
        });





        ll_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.DisplayFragmentQuestions();
            }
        });

        ll_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateLanguageDialog();
            }
        });
        ll_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activity.DisplayFragmentFavourite();

            }
        });

        image_instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createSocialIntent("https://www.instagram.com/iristoresa");
            }
        });

        image_twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createSocialIntent("https://twitter.com/iristore");

            }
        });

    }


    private void CreateLanguageDialog()
    {
        final AlertDialog dialog = new AlertDialog.Builder(activity)
                .setCancelable(true)
                .create();

        View view  = LayoutInflater.from(activity).inflate(R.layout.dialog_language,null);
        Button btn_select = view.findViewById(R.id.btn_select);
        Button btn_cancel = view.findViewById(R.id.btn_cancel);

        final NumberPicker numberPicker = view.findViewById(R.id.numberPicker);

        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(language_array.length-1);
        numberPicker.setDisplayedValues(language_array);
        numberPicker.setWrapSelectorWheel(false);
        if (current_language.equals("ar"))
        {
            numberPicker.setValue(1);

        }else
        {
            numberPicker.setValue(0);

        }
        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                int pos = numberPicker.getValue();
                if (pos == 0)
                {
                    activity.RefreshActivity("en");
                }else
                {
                    activity.RefreshActivity("ar");

                }

            }
        });




        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.setView(view);
        dialog.show();
    }

    private void createSocialIntent(String url)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(url));
        startActivity(intent);
    }
}
