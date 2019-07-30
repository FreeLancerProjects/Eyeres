package com.appzone.eyeres.activities_fragments.activity_home.fragments.fragment_home.fragment_store;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.appzone.eyeres.R;
import com.appzone.eyeres.activities_fragments.activity_home.activity.HomeActivity;
import com.appzone.eyeres.models.Special_Lenses_Model;
import com.appzone.eyeres.remote.Api;

import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Special_Lenses extends Fragment {
    private ProgressBar progBar;
    private Button btn_whatsApp;
    private TextView tv_content;
    private HomeActivity activity;
    private String current_language;
    private String phone="",lang="";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_special_lenses,container,false);
        initView(view);
        return view;
    }

    public static Fragment_Special_Lenses newInstance()
    {
        return  new Fragment_Special_Lenses();
    }

    private void initView(View view) {

        activity = (HomeActivity) getActivity();
        Paper.init(activity);
        current_language = Paper.book().read("lang", Locale.getDefault().getLanguage());

        progBar = view.findViewById(R.id.progBar);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        btn_whatsApp = view.findViewById(R.id.btn_whatsApp);
        tv_content = view.findViewById(R.id.tv_content);

        btn_whatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(phone))
                {
                     if (phone.startsWith("966"))
                    {
                        phone = "+"+phone;

                    }else if (phone.startsWith("00966"))
                    {
                        phone = phone.replaceFirst("00","+");

                    }

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("whatsapp://send?phone=+9660530512812"));
                    startActivity(intent);
                }else
                    {
                        Toast.makeText(activity, R.string.cannot_make_conv, Toast.LENGTH_LONG).show();
                    }

            }
        });

        getSpecialLenses();
    }

    private void getSpecialLenses() {

        if (current_language.equals("ar"))
        {
            lang="ar";
        }else
        {
            lang="en";
        }


        Api.getService()
                .getSpecialLenses(lang)
                .enqueue(new Callback<Special_Lenses_Model>() {
                    @Override
                    public void onResponse(Call<Special_Lenses_Model> call, Response<Special_Lenses_Model> response) {
                        progBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body()!=null&&response.body().getSpecial_lenses()!=null)
                        {

                            updateUI(response.body());

                        }else
                            {
                                Toast.makeText(activity, lang, Toast.LENGTH_SHORT).show();
                                Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                    }

                    @Override
                    public void onFailure(Call<Special_Lenses_Model> call, Throwable t) {
                        try {
                            Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();

                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }

    private void updateUI(Special_Lenses_Model special_lenses_model) {

        btn_whatsApp.setVisibility(View.VISIBLE);
        phone = special_lenses_model.getSpecial_lenses().getPhone();

       /* if (current_language.equals("ar")&&lang=="ar")
        {*/
            tv_content.setText(special_lenses_model.getSpecial_lenses().getDescription().getAr());
       /* }else
            {
                tv_content.setText(special_lenses_model.getSpecial_lenses().getDescription().getAr());

            }*/
    }
}
