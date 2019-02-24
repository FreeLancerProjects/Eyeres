package com.appzone.eyeres.activities_fragments.activity_home.fragments.fragment_cart;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.appzone.eyeres.R;
import com.appzone.eyeres.activities_fragments.activity_home.activity.HomeActivity;
import com.appzone.eyeres.models.UserModel;
import com.appzone.eyeres.share.Common;
import com.appzone.eyeres.singletone.UserSingleTone;

import java.util.Locale;

public class Fragment_Client_Data extends Fragment {
    private EditText edt_name,edt_phone,edt_address,edt_note;
    private CardView card_confirm,card_back;
    private ImageView arrow1,arrow2;
    private String current_language;
    private HomeActivity activity;
    private UserSingleTone userSingleTone;
    private UserModel userModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_client_data,container,false);
        initView(view);
        return view;
    }

    public static Fragment_Client_Data newInstance()
    {
        return new Fragment_Client_Data();
    }
    private void initView(View view) {
        userSingleTone = UserSingleTone.getInstance();
        userModel = userSingleTone.getUserModel();
        activity = (HomeActivity) getActivity();
        arrow1 = view.findViewById(R.id.arrow1);
        arrow2 = view.findViewById(R.id.arrow2);
        current_language  = Locale.getDefault().getLanguage();

        if (current_language.equals("ar"))
        {
            arrow1.setBackgroundResource(R.drawable.white_right_arrow);
            arrow2.setBackgroundResource(R.drawable.white_left_arrow);

        }else
        {
            arrow1.setBackgroundResource(R.drawable.white_left_arrow);
            arrow2.setBackgroundResource(R.drawable.white_right_arrow);

        }
        edt_name = view.findViewById(R.id.edt_name);
        edt_phone = view.findViewById(R.id.edt_phone);
        edt_address = view.findViewById(R.id.edt_address);
        edt_note = view.findViewById(R.id.edt_note);
        card_confirm = view.findViewById(R.id.card_confirm);
        card_back = view.findViewById(R.id.card_back);

        card_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.Back();
            }
        });

        updateUi();
        card_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckData();
            }
        });

    }



    private void updateUi()
    {

        if (userModel!=null)
        {
            edt_name.setText(userModel.getName());
            edt_phone.setText(userModel.getPhone());

        }



    }
    private void CheckData()
    {

        String m_name = edt_name.getText().toString().trim();
        String m_phone = edt_phone.getText().toString().trim();
        String m_address = edt_address.getText().toString().trim();
        String m_note = edt_note.getText().toString().trim();

        if (!TextUtils.isEmpty(m_name)&&
                !TextUtils.isEmpty(m_phone)&&
                !TextUtils.isEmpty(m_address)
                )
        {
            edt_name.setError(null);
            edt_phone.setError(null);
            edt_address.setError(null);
            edt_note.setError(null);
            Common.CloseKeyBoard(activity,edt_name);
            activity.UploadOrder(m_name,m_phone,m_address,m_note);

        }else
            {
                if (TextUtils.isEmpty(m_name))
                {
                    edt_name.setError(getString(R.string.field_req));

                }else
                    {
                        edt_name.setError(null);

                    }
                if (TextUtils.isEmpty(m_phone))
                {
                    edt_phone.setError(getString(R.string.field_req));

                }else
                {
                    edt_phone.setError(null);

                }

                if (TextUtils.isEmpty(m_address))
                {
                    edt_address.setError(getString(R.string.field_req));

                }else
                {
                    edt_address.setError(null);

                }

            }

    }
}
