package com.appzone.eyeres.activities_fragments.activity_home.fragments.fragment_home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appzone.eyeres.R;

public class Fragment_More extends Fragment{

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more,container,false);
        initView(view);
        return view;
    }

    public static Fragment_More newInstance()
    {
        return new Fragment_More();
    }

    private void initView(View view) {

    }
}
