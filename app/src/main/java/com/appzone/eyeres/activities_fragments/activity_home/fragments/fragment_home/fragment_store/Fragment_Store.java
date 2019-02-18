package com.appzone.eyeres.activities_fragments.activity_home.fragments.fragment_home.fragment_store;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appzone.eyeres.R;
import com.appzone.eyeres.activities_fragments.activity_home.activity.HomeActivity;

public class Fragment_Store extends Fragment{
    private LinearLayout ll_slider_container,ll_transparent,ll_color,ll_accessories;
    private TextView tv_transparent,tv_colored,tv_accessories;
    private ViewPager pager_slider;
    private TabLayout tab_slider;
    private HomeActivity activity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store,container,false);
        initView(view);
        return view;
    }

    public static Fragment_Store newInstance()
    {
        return new Fragment_Store();
    }
    private void initView(View view) {

        activity = (HomeActivity) getActivity();
        ll_slider_container = view.findViewById(R.id.ll_slider_container);

        pager_slider = view.findViewById(R.id.pager_slider);
        tab_slider = view.findViewById(R.id.tab_slider);

        ll_transparent = view.findViewById(R.id.ll_transparent);
        ll_color = view.findViewById(R.id.ll_color);
        ll_accessories = view.findViewById(R.id.ll_accessories);

        tv_transparent = view.findViewById(R.id.tv_transparent);
        tv_colored = view.findViewById(R.id.tv_colored);
        tv_accessories = view.findViewById(R.id.tv_accessories);



        ll_transparent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activity.DisplayFragmentTransparent();
                UpdateUITextColor();
            }
        });

        ll_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activity.DisplayFragmentColor();
                tv_transparent.setTextColor(ContextCompat.getColor(activity,R.color.gray3));
                tv_colored.setTextColor(ContextCompat.getColor(activity,R.color.white));
                tv_accessories.setTextColor(ContextCompat.getColor(activity,R.color.gray3));

            }
        });
        ll_accessories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activity.DisplayFragmentTools();
                tv_transparent.setTextColor(ContextCompat.getColor(activity,R.color.gray3));
                tv_colored.setTextColor(ContextCompat.getColor(activity,R.color.gray3));
                tv_accessories.setTextColor(ContextCompat.getColor(activity,R.color.white));

            }
        });

        activity.DisplayFragmentTransparent();


    }

    public void UpdateUITextColor()
    {
        tv_transparent.setTextColor(ContextCompat.getColor(activity,R.color.white));
        tv_colored.setTextColor(ContextCompat.getColor(activity,R.color.gray3));
        tv_accessories.setTextColor(ContextCompat.getColor(activity,R.color.gray3));

    }


}
