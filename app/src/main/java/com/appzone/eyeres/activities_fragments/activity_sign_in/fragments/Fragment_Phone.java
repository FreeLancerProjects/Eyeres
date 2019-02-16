package com.appzone.eyeres.activities_fragments.activity_sign_in.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appzone.eyeres.R;
import com.appzone.eyeres.activities_fragments.activity_sign_in.activity.SignInActivity;
import com.appzone.eyeres.share.Common;
import com.mukesh.countrypicker.Country;
import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.listeners.OnCountryPickerListener;

public class Fragment_Phone extends Fragment implements OnCountryPickerListener{
    private LinearLayout ll_country;
    private TextView tv_country,tv_code,tv_note;
    private EditText edt_phone;
    private FloatingActionButton fab;
    private SignInActivity activity;
    private CountryPicker picker;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phone,container,false);
        initView(view);
        return view;
    }

    public static Fragment_Phone newInstance(){
        return new Fragment_Phone();
    }
    private void initView(View view) {

        activity = (SignInActivity) getActivity();
        ll_country = view.findViewById(R.id.ll_country);
        tv_country = view.findViewById(R.id.tv_country);
        tv_note = view.findViewById(R.id.tv_note);
        tv_code = view.findViewById(R.id.tv_code);
        edt_phone = view.findViewById(R.id.edt_phone);
        fab = view.findViewById(R.id.fab);
        ll_country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picker.showDialog(activity);
            }
        });

        tv_note.setText(getString(R.string.never_share_phone_number)+"\n"+getString(R.string.your_privacy_guaranteed));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckData();
            }
        });
        CreateCountryDialog();

    }

    private void CheckData() {
        String phone = edt_phone.getText().toString().trim();

        if (!TextUtils.isEmpty(phone)&& phone.length()>=6&& phone.length()<13)
        {
            edt_phone.setError(null);
            Common.CloseKeyBoard(activity,edt_phone);
            activity.DisplayFragmentCompleteProfile(phone);
        }else
            {
                if (TextUtils.isEmpty(phone))
                {
                    edt_phone.setError(getString(R.string.field_req));
                }else if (phone.length()<6 || phone.length()>=13)
                {
                    edt_phone.setError(getString(R.string.inv_phone));
                }
            }
    }

    private void CreateCountryDialog() {
        CountryPicker.Builder builder = new CountryPicker.Builder()
                .canSearch(true)
                .with(activity)
                .listener(this);
        picker = builder.build();
        updateUi(picker.getCountryFromSIM());
    }


    @Override
    public void onSelectCountry(Country country) {
       updateUi(country);
    }

    private void updateUi(Country country) {
        tv_country.setText(country.getName());
        tv_code.setText(country.getDialCode());
    }
}
