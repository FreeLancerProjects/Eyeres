package com.appzone.eyeres.activities_fragments.activity_home.fragments.fragment_details;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appzone.eyeres.R;
import com.appzone.eyeres.models.UserModel;

public class Fragment_Details extends Fragment{
    private static String TAG="details";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details,container,false);
        initView(view);
        return view;
    }

    /// product details
    public static Fragment_Details newInstance(UserModel userModel)
    {
        Fragment_Details fragment_details = new Fragment_Details();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG,userModel);
        fragment_details.setArguments(bundle);
        return fragment_details;
    }
    private void initView(View view) {

        Bundle bundle = getArguments();
        if (bundle!=null)
        {

            UpdateUI();
        }
    }

    private void UpdateUI() {


    }
}
