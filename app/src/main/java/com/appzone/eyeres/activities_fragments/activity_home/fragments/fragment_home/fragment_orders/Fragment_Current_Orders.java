package com.appzone.eyeres.activities_fragments.activity_home.fragments.fragment_home.fragment_orders;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appzone.eyeres.R;
import com.appzone.eyeres.activities_fragments.activity_home.activity.HomeActivity;
import com.appzone.eyeres.models.UserModel;
import com.appzone.eyeres.singletone.UserSingleTone;

public class Fragment_Current_Orders extends Fragment{
    private RecyclerView recView;
    private RecyclerView.LayoutManager manager;
    private ProgressBar progBar,progBarLoadMore;
    private HomeActivity activity;
    private UserSingleTone userSingleTone;
    private UserModel userModel;
    private TextView tv_no_product;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_current_new_previous_order,container,false);
        initView(view);
        return view;
    }

    public static Fragment_Current_Orders newInstance()
    {
        return new Fragment_Current_Orders();
    }

    private void initView(View view)
    {
        userSingleTone = UserSingleTone.getInstance();
        userModel = userSingleTone.getUserModel();
        activity = (HomeActivity) getActivity();
        progBar = view.findViewById(R.id.progBar);
        progBarLoadMore = view.findViewById(R.id.progBarLoadMore);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getActivity(),R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        progBarLoadMore.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getActivity(),R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        tv_no_product = view.findViewById(R.id.tv_no_product);

        recView = view.findViewById(R.id.recView);
        manager = new LinearLayoutManager(getActivity());
        recView.setLayoutManager(manager);
        recView.setHasFixedSize(true);
        recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
        recView.setDrawingCacheEnabled(true);
        recView.setItemViewCacheSize(25);

        getOrders();
    }

    public void getOrders()
    {

    }

}
