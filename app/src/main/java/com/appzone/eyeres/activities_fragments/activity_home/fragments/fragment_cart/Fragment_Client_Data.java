package com.appzone.eyeres.activities_fragments.activity_home.fragments.fragment_cart;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appzone.eyeres.R;
import com.appzone.eyeres.activities_fragments.activity_home.activity.HomeActivity;
import com.appzone.eyeres.models.UserModel;
import com.appzone.eyeres.share.Common;
import com.appzone.eyeres.singletone.UserSingleTone;

import java.util.Locale;

import io.paperdb.Paper;

public class Fragment_Client_Data extends Fragment {
    private EditText edt_name, edt_phone, edt_address, edt_note;
    private CardView card_confirm, card_back;
    private ImageView arrow1, arrow2,image_cash,image_transfer,image_mada;
    private String current_language;
    private TextView tv_cash,tv_transfer,tv_mada;
    private LinearLayout ll_cash,ll_transfer,ll_mada;
    private HomeActivity activity;
    private UserSingleTone userSingleTone;
    private UserModel userModel;
    private int payment_method =0;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_client_data, container, false);
        initView(view);
        return view;
    }

    public static Fragment_Client_Data newInstance() {
        return new Fragment_Client_Data();
    }

    private void initView(View view) {
        activity = (HomeActivity) getActivity();
        Paper.init(activity);

        current_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        userSingleTone = UserSingleTone.getInstance();
        userModel = userSingleTone.getUserModel();
        arrow1 = view.findViewById(R.id.arrow1);
        arrow2 = view.findViewById(R.id.arrow2);
        if (current_language.equals("ar")) {
            arrow1.setImageResource(R.drawable.white_right_arrow);
            arrow2.setImageResource(R.drawable.white_left_arrow);

        } else {
            arrow1.setImageResource(R.drawable.white_left_arrow);
            arrow2.setImageResource(R.drawable.white_right_arrow);

        }
        image_cash = view.findViewById(R.id.image_cash);
        image_transfer = view.findViewById(R.id.image_transfer);
        image_mada = view.findViewById(R.id.image_mada);
        tv_cash = view.findViewById(R.id.tv_cash);
        tv_transfer = view.findViewById(R.id.tv_transfer);
        tv_mada = view.findViewById(R.id.tv_mada);
        ll_cash = view.findViewById(R.id.ll_cash);
        ll_transfer = view.findViewById(R.id.ll_transfer);
        ll_mada = view.findViewById(R.id.ll_mada);


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

        ll_cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payment_method = 1;

                ll_cash.setBackgroundResource(R.drawable.payment_checked);
                image_cash.setColorFilter(ContextCompat.getColor(activity,R.color.white), PorterDuff.Mode.SRC_IN);
                tv_cash.setTextColor(ContextCompat.getColor(activity,R.color.white));

                ll_transfer.setBackgroundResource(R.drawable.payment_unchecked);
                image_transfer.setColorFilter(ContextCompat.getColor(activity,R.color.gray), PorterDuff.Mode.SRC_IN);
                tv_transfer.setTextColor(ContextCompat.getColor(activity,R.color.gray));

                ll_mada.setBackgroundResource(R.drawable.payment_unchecked);
                image_mada.setImageResource(R.drawable.mada_gay);
                tv_mada.setTextColor(ContextCompat.getColor(activity,R.color.gray));



            }
        });

        ll_transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payment_method = 2;

                ll_transfer.setBackgroundResource(R.drawable.payment_checked);
                image_transfer.setColorFilter(ContextCompat.getColor(activity,R.color.white), PorterDuff.Mode.SRC_IN);
                tv_transfer.setTextColor(ContextCompat.getColor(activity,R.color.white));

                ll_cash.setBackgroundResource(R.drawable.payment_unchecked);
                image_cash.setColorFilter(ContextCompat.getColor(activity,R.color.gray), PorterDuff.Mode.SRC_IN);
                tv_cash.setTextColor(ContextCompat.getColor(activity,R.color.gray));

                ll_mada.setBackgroundResource(R.drawable.payment_unchecked);
                image_mada.setImageResource(R.drawable.mada_gay);
                tv_mada.setTextColor(ContextCompat.getColor(activity,R.color.gray));


            }
        });

        ll_mada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payment_method = 3;

                ll_mada.setBackgroundResource(R.drawable.payment_checked);
                image_mada.setImageResource(R.drawable.mada_white);
                tv_mada.setTextColor(ContextCompat.getColor(activity,R.color.white));

                ll_transfer.setBackgroundResource(R.drawable.payment_unchecked);
                image_transfer.setColorFilter(ContextCompat.getColor(activity,R.color.gray), PorterDuff.Mode.SRC_IN);
                tv_transfer.setTextColor(ContextCompat.getColor(activity,R.color.gray));

                ll_cash.setBackgroundResource(R.drawable.payment_unchecked);
                image_cash.setColorFilter(ContextCompat.getColor(activity,R.color.gray), PorterDuff.Mode.SRC_IN);
                tv_cash.setTextColor(ContextCompat.getColor(activity,R.color.gray));


            }
        });

    }


    private void updateUi() {

        if (userModel != null) {
            edt_name.setText(userModel.getName());
            edt_phone.setText(userModel.getPhone());

        }


    }

    private void CheckData() {

        String m_name = edt_name.getText().toString().trim();
        String m_phone = edt_phone.getText().toString().trim();
        String m_address = edt_address.getText().toString().trim();
        String m_note = edt_note.getText().toString().trim();

        if (!TextUtils.isEmpty(m_name) &&
                !TextUtils.isEmpty(m_phone) &&
                !TextUtils.isEmpty(m_address)&&
                payment_method!=0
        ) {
            edt_name.setError(null);
            edt_phone.setError(null);
            edt_address.setError(null);
            edt_note.setError(null);
            Common.CloseKeyBoard(activity, edt_name);
            activity.UploadOrder(m_name, m_phone, m_address, m_note,payment_method);

        } else {
            if (TextUtils.isEmpty(m_name)) {
                edt_name.setError(getString(R.string.field_req));

            } else {
                edt_name.setError(null);

            }
            if (TextUtils.isEmpty(m_phone)) {
                edt_phone.setError(getString(R.string.field_req));

            } else {
                edt_phone.setError(null);

            }

            if (TextUtils.isEmpty(m_address)) {
                edt_address.setError(getString(R.string.field_req));

            } else {
                edt_address.setError(null);

            }

            if (payment_method == 0)
            {
                Toast.makeText(activity, R.string.sel_pay_method, Toast.LENGTH_LONG).show();
            }

        }

    }
}
