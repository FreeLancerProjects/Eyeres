package com.appzone.eyeres.activities_fragments.activity_home.fragments.fragment_home;

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
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

public class Fragment_Home extends Fragment{

    private AHBottomNavigation ah_tab,ah_bottom;
    private LinearLayout ll_slider_container,ll_add_recent,ll_most_seller;
    private ViewPager pager_slider;
    private TabLayout tab_slider;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {

        ah_tab = view.findViewById(R.id.ah_tab);
        ah_bottom = view.findViewById(R.id.ah_bottom);

        ll_slider_container = view.findViewById(R.id.ll_slider_container);
        ll_add_recent = view.findViewById(R.id.ll_add_recent);
        ll_most_seller = view.findViewById(R.id.ll_most_seller);
        pager_slider = view.findViewById(R.id.pager_slider);
        tab_slider = view.findViewById(R.id.tab_slider);
        setUpTabUI();
        setUpBottomTabUI();

    }



    private void setUpTabUI() {

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
    private void setUpBottomTabUI() {

        ah_bottom.setInactiveColor(ContextCompat.getColor(getActivity(),R.color.colorPrimary));
        ah_bottom.setInactiveColor(ContextCompat.getColor(getActivity(),R.color.colorPrimary));
        ah_bottom.setAccentColor(ContextCompat.getColor(getActivity(),R.color.white));
        ah_bottom.setTitleTextSizeInSp(15,13);
        ah_bottom.setDefaultBackgroundColor(ContextCompat.getColor(getActivity(),R.color.white));
        ah_bottom.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        ah_bottom.setForceTint(true);
        ah_bottom.setColored(false);

        AHBottomNavigationItem item1 = new AHBottomNavigationItem(getString(R.string.store), ContextCompat.getDrawable(getActivity(),R.drawable.bag),ContextCompat.getColor(getActivity(),R.color.colorPrimary));
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(getString(R.string.orders), ContextCompat.getDrawable(getActivity(),R.drawable.nav_cart),ContextCompat.getColor(getActivity(),R.color.colorPrimary));
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(getString(R.string.offers), ContextCompat.getDrawable(getActivity(),R.drawable.nav_offer),ContextCompat.getColor(getActivity(),R.color.colorPrimary));
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(getString(R.string.favourite), ContextCompat.getDrawable(getActivity(),R.drawable.star),ContextCompat.getColor(getActivity(),R.color.colorPrimary));
        AHBottomNavigationItem item5 = new AHBottomNavigationItem(getString(R.string.more), ContextCompat.getDrawable(getActivity(),R.drawable.more),ContextCompat.getColor(getActivity(),R.color.colorPrimary));

        ah_bottom.addItem(item1);
        ah_bottom.addItem(item2);
        ah_bottom.addItem(item3);
        ah_bottom.addItem(item4);
        ah_bottom.addItem(item5);

    }
}
