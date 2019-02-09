package com.appzone.eyeres.activities_fragments.activity_home.fragments.fragment_home.fragment_store;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.appzone.eyeres.R;
import com.appzone.eyeres.activities_fragments.activity_home.activity.HomeActivity;

public class Fragment_Tools extends Fragment{

    private RecyclerView recView;
    private RecyclerView.LayoutManager manager;
    private ProgressBar progBar;
    private HomeActivity activity;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_transparent_color_tools,container,false);
        initView(view);
        return view;
    }

    public static Fragment_Tools newInstance()
    {
        return new Fragment_Tools();
    }
    private void initView(View view) {
        activity = (HomeActivity) getActivity();
        progBar = view.findViewById(R.id.progBar);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getActivity(),R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        recView = view.findViewById(R.id.recView);
        manager = new GridLayoutManager(getActivity(),2);
        recView.setLayoutManager(manager);
        recView.setHasFixedSize(true);
        recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
        recView.setDrawingCacheEnabled(true);
        recView.setItemViewCacheSize(25);
    }
}
