package com.appzone.eyeres.activities_fragments.activity_home.fragments.fragment_home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appzone.eyeres.R;
import com.appzone.eyeres.activities_fragments.activity_home.activity.HomeActivity;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

public class Fragment_Home extends Fragment{

    private AHBottomNavigation ah_bottom;
    private HomeActivity activity;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        initView(view);
        return view;
    }


    public static Fragment_Home newInstance()
    {
        return new Fragment_Home();
    }

    private void initView(View view)
    {
        activity = (HomeActivity) getActivity();
        ah_bottom = view.findViewById(R.id.ah_bottom);
        setUpBottomTabUI();
        ah_bottom.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                UpdateAHBottomNavigationPosition(position);
                switch (position)
                {
                    case 0:
                        activity.DisplayFragmentStore();
                        break;
                    case 1:
                        activity.DisplayFragmentOrders();
                        break;
                    case 2:
                        activity.DisplayFragmentOffers();
                        break;
                    case 3:
                        activity.DisplayFragmentFavourite();
                        break;
                    case 4:
                        activity.DisplayFragmentMore();
                        break;

                }
                return false;
            }
        });

        activity.DisplayFragmentStore();
        UpdateAHBottomNavigationPosition(0);

    }

    private void setUpBottomTabUI()
    {

        ah_bottom.setInactiveColor(ContextCompat.getColor(getActivity(),R.color.gray));
        ah_bottom.setAccentColor(ContextCompat.getColor(getActivity(),R.color.colorPrimary));
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

    public void UpdateAHBottomNavigationPosition(int pos)
    {
        ah_bottom.setCurrentItem(pos,false);
    }
}
