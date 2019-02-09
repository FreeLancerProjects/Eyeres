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

import com.appzone.eyeres.R;
import com.appzone.eyeres.activities_fragments.activity_home.activity.HomeActivity;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

public class Fragment_Store extends Fragment{
    private AHBottomNavigation ah_tab;
    private LinearLayout ll_slider_container,ll_add_recent,ll_most_seller;
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
        ah_tab = view.findViewById(R.id.ah_tab);
        ll_slider_container = view.findViewById(R.id.ll_slider_container);
        ll_add_recent = view.findViewById(R.id.ll_add_recent);
        ll_most_seller = view.findViewById(R.id.ll_most_seller);
        pager_slider = view.findViewById(R.id.pager_slider);
        tab_slider = view.findViewById(R.id.tab_slider);
        setUpTabUI();

        UpdateAHBottomNavigationPosition(0);
        activity.DisplayFragmentTransparent();

        ah_tab.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {

                UpdateAHBottomNavigationPosition(position);
                switch (position){
                    case 0:
                        activity.DisplayFragmentTransparent();
                        break;
                    case 1:
                        activity.DisplayFragmentColor();
                        break;
                    case 2:
                        activity.DisplayFragmentTools();
                        break;
                }


                return false;
            }
        });
    }
    private void setUpTabUI()
    {

        ah_tab.setInactiveColor(ContextCompat.getColor(getActivity(),R.color.white));
        ah_tab.setAccentColor(ContextCompat.getColor(getActivity(),R.color.white));
        ah_tab.setInactiveColor(ContextCompat.getColor(getActivity(),R.color.white));
        ah_tab.setForceTint(true);
        ah_tab.setColored(false);
        ah_tab.setTitleTextSizeInSp(15,13);
        ah_tab.setDefaultBackgroundColor(ContextCompat.getColor(getActivity(),R.color.transparent));
        ah_tab.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(getString(R.string.transparent), ContextCompat.getDrawable(getActivity(),R.drawable.trans_eye));
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(getString(R.string.color), ContextCompat.getDrawable(getActivity(),R.drawable.color_eye));
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(getString(R.string.tools), ContextCompat.getDrawable(getActivity(),R.drawable.tools_eye));
        ah_tab.addItem(item1);
        ah_tab.addItem(item2);
        ah_tab.addItem(item3);

    }

    public void UpdateAHBottomNavigationPosition(int pos)
    {
        ah_tab.setCurrentItem(pos,false);
    }
}
