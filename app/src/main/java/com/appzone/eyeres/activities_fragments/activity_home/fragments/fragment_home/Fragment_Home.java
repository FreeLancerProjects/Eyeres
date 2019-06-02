package com.appzone.eyeres.activities_fragments.activity_home.fragments.fragment_home;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.appzone.eyeres.R;
import com.appzone.eyeres.activities_fragments.activity_home.activity.HomeActivity;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import java.util.Locale;

import io.paperdb.Paper;

public class Fragment_Home extends Fragment{

    private AHBottomNavigation ah_bottom;
    private HomeActivity activity;
    private ImageView image_search, image_back_photo;
    private TextView tv_cart_counter;
    private String current_language;
    private FrameLayout fl_cart_container;





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

        Paper.init(activity);
        current_language = Paper.book().read("lang", Locale.getDefault().getLanguage());

        image_back_photo = view.findViewById(R.id.image_back_photo);

        if (current_language.equals("ar")) {
            image_back_photo.setImageResource(R.drawable.white_right_arrow);

        } else {
            image_back_photo.setImageResource(R.drawable.white_left_arrow);



        }



        fl_cart_container = view.findViewById(R.id.fl_cart_container);

        image_search = view.findViewById(R.id.image_search);
        tv_cart_counter = view.findViewById(R.id.tv_cart_counter);
        ah_bottom = view.findViewById(R.id.ah_bottom);


        fl_cart_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               activity.DisplayFragmentCart();

            }
        });

        image_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               activity.DisplayFragmentSearch();
            }
        });

        setUpBottomTabUI();
        ah_bottom.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                //UpdateAHBottomNavigationPosition(position);
                switch (position)
                {
                    case 0:
                        activity.DisplayFragmentStore();
                        break;
                    case 1:
                        activity.DisplayFragmentOrders(0);
                        break;
                    case 2:
                        activity.DisplayFragmentOffers();
                        break;
                    case 3:
                        activity.DisplayFragmentSpecialOrder();
                        break;
                    case 4:
                        activity.DisplayFragmentMore();
                        break;

                }
                return false;
            }
        });

        image_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.DisplayFragmentSearch();
            }
        });
        UpdateAHBottomNavigationPosition(0);

    }


    public void updateCartCounter(int counter)
    {
        if (counter >0)
        {
            tv_cart_counter.setText(String.valueOf(counter));
            tv_cart_counter.setVisibility(View.VISIBLE);
        }else
            {
                tv_cart_counter.setVisibility(View.GONE);
            }
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
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(getString(R.string.sp_order), ContextCompat.getDrawable(getActivity(),R.drawable.ic_box),ContextCompat.getColor(getActivity(),R.color.colorPrimary));
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
