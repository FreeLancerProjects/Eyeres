package com.appzone.eyeres.activities_fragments.activity_home.fragments.fragment_home.fragment_store;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appzone.eyeres.R;
import com.appzone.eyeres.activities_fragments.activity_home.activity.HomeActivity;
import com.appzone.eyeres.adapters.AdsSliderAdapter;
import com.appzone.eyeres.models.AdsModel;
import com.appzone.eyeres.remote.Api;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Store extends Fragment {
    private LinearLayout ll_transparent, ll_color, ll_accessories;
    private TextView tv_transparent, tv_colored, tv_accessories;
    private ImageView image_transparent, image_colored, image_accessories;

    private HomeActivity activity;

    private ViewPager pager_slider;
    private TabLayout tab_slider;
    private AdsSliderAdapter adsSliderAdapter;
    private FrameLayout fl_slider;
    private Timer timer;
    private TimerTask timerTask;
    private ProgressBar progressBarSlider;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store, container, false);
        initView(view);
        return view;
    }


    public static Fragment_Store newInstance() {
        return new Fragment_Store();
    }

    private void initView(View view) {


        activity = (HomeActivity) getActivity();


        ll_transparent = view.findViewById(R.id.ll_transparent);
        ll_color = view.findViewById(R.id.ll_color);
        ll_accessories = view.findViewById(R.id.ll_accessories);

        image_accessories = view.findViewById(R.id.image_accessories);
        image_colored = view.findViewById(R.id.image_colored);
        image_transparent = view.findViewById(R.id.image_transparent);


        tv_transparent = view.findViewById(R.id.tv_transparent);
        tv_colored = view.findViewById(R.id.tv_colored);
        tv_accessories = view.findViewById(R.id.tv_accessories);


        pager_slider = view.findViewById(R.id.pager_slider);
        tab_slider = view.findViewById(R.id.tab_slider);
        tab_slider.setupWithViewPager(pager_slider);
        fl_slider = view.findViewById(R.id.fl_slider);
        progressBarSlider = view.findViewById(R.id.progressBarSlider);
        progressBarSlider.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);


        setSelectedDefault();

        ll_transparent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                image_transparent.setPadding(0,0,0,0);
                image_accessories.setPadding(15,15,15,15);
                image_colored.setPadding(15,15,15,15);

                activity.DisplayFragmentTransparent();
                ll_transparent.setBackgroundResource(R.color.white);
                tv_transparent.setTextColor(ContextCompat.getColor(activity, R.color.colorPrimary));

                ll_color.setBackgroundResource(R.drawable.unselected_tools);
                tv_colored.setTextColor(ContextCompat.getColor(activity, R.color.white));

                ll_accessories.setBackgroundResource(R.drawable.unselected);
                tv_accessories.setTextColor(ContextCompat.getColor(activity, R.color.white));
            }
        });

        ll_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                setSelectedDefault();


            }
        });
        ll_accessories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                image_transparent.setPadding(15,15,15,15);
                image_accessories.setPadding(0,0,0,0);
                image_colored.setPadding(15,15,15,15);


                activity.DisplayFragmentTools();

                ll_transparent.setBackgroundResource(R.drawable.unselected);
                tv_transparent.setTextColor(ContextCompat.getColor(activity, R.color.white));

                ll_color.setBackgroundResource(R.drawable.unselected_trans);
                tv_colored.setTextColor(ContextCompat.getColor(activity, R.color.white));

                ll_accessories.setBackgroundResource(R.color.white);
                tv_accessories.setTextColor(ContextCompat.getColor(activity, R.color.colorPrimary));


            }
        });

        getAds();

    }

    public void setSelectedDefault() {


        activity.DisplayFragmentColor();

        image_transparent.setPadding(15,15,15,15);
        image_accessories.setPadding(15,15,15,15);
        image_colored.setPadding(0,0,0,0);


        ll_transparent.setBackgroundResource(R.drawable.unselected_trans);
        tv_transparent.setTextColor(ContextCompat.getColor(activity, R.color.white));

        ll_color.setBackgroundResource(R.color.white);
        tv_colored.setTextColor(ContextCompat.getColor(activity, R.color.colorPrimary));

        ll_accessories.setBackgroundResource(R.drawable.unselected_tools);
        tv_accessories.setTextColor(ContextCompat.getColor(activity, R.color.white));


    }


    private void getAds() {
        Api.getService()
                .getAds()
                .enqueue(new Callback<AdsModel>() {
                    @Override
                    public void onResponse(Call<AdsModel> call, Response<AdsModel> response) {

                        if (response.isSuccessful() && response.body() != null) {
                            progressBarSlider.setVisibility(View.GONE);

                            updateSliderUI(response.body().getData());

                        } else {
                            progressBarSlider.setVisibility(View.GONE);
                            fl_slider.setVisibility(View.GONE);

                            try {
                                Log.e("error_code", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<AdsModel> call, Throwable t) {
                        try {
                            progressBarSlider.setVisibility(View.GONE);
                            fl_slider.setVisibility(View.GONE);

                            Log.e("Error", t.getMessage());
                        } catch (Exception e) {
                        }
                    }
                });

    }

    private void updateSliderUI(List<AdsModel.Ads> data) {

        if (data.size() > 0) {
            fl_slider.setVisibility(View.VISIBLE);
            adsSliderAdapter = new AdsSliderAdapter(data, activity);
            pager_slider.setAdapter(adsSliderAdapter);
            if (data.size() > 1) {
                for (int i = 0; i < tab_slider.getTabCount() - 1; i++) {
                    View view = ((ViewGroup) tab_slider.getChildAt(0)).getChildAt(i);

                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                    params.setMargins(4, 0, 4, 0);
                    tab_slider.requestLayout();
                }
                timer = new Timer();
                timerTask = new MyTimerTask();
                timer.scheduleAtFixedRate(timerTask, 6000, 6000);


            }

        } else {
            fl_slider.setVisibility(View.GONE);
        }
    }


    private class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (pager_slider.getCurrentItem() < pager_slider.getAdapter().getCount() - 1) {
                        pager_slider.setCurrentItem(pager_slider.getCurrentItem() + 1);
                    } else {
                        pager_slider.setCurrentItem(0);
                    }
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        if (timer != null) {
            timer.purge();
            timer.cancel();
        }
        if (timerTask != null) {
            timerTask.cancel();
        }
        super.onDestroyView();


    }

}
