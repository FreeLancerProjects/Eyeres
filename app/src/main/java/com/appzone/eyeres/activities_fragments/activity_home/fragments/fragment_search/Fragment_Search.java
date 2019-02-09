package com.appzone.eyeres.activities_fragments.activity_home.fragments.fragment_search;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appzone.eyeres.R;
import com.appzone.eyeres.activities_fragments.activity_home.activity.HomeActivity;

public class Fragment_Search extends Fragment{
    private RecyclerView recView;
    private RecyclerView.LayoutManager manager;
    private HomeActivity activity;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search,container,false);
        initView(view);
        return view;
    }

    public static Fragment_Search newInstance()
    {
        return new Fragment_Search();
    }
    private void initView(View view) {
        activity = (HomeActivity) getActivity();
        recView = view.findViewById(R.id.recView);
        manager = new LinearLayoutManager(getActivity());
        recView.setLayoutManager(manager);
        recView.setHasFixedSize(true);
        recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
        recView.setDrawingCacheEnabled(true);
        recView.setItemViewCacheSize(25);
    }
}
