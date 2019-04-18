package com.appzone.eyeres.activities_fragments.activity_home.fragments.fragment_cart;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.appzone.eyeres.R;
import com.appzone.eyeres.activities_fragments.activity_home.activity.HomeActivity;
import com.appzone.eyeres.models.CouponModel;
import com.appzone.eyeres.models.UserModel;
import com.appzone.eyeres.remote.Api;
import com.appzone.eyeres.share.Common;
import com.appzone.eyeres.singletone.UserSingleTone;

import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Client_Data extends Fragment {
    private static final String TAG = "COST";
    private EditText edt_name, edt_phone, edt_address, edt_note,edt_coupon_code;
    private CardView card_confirm, card_back;
    private ImageView arrow1, arrow2,image_cash,image_transfer,image_mada;
    private String current_language;
    private ImageView image_coupon_state;
    private ProgressBar progBar;
    private TextView tv_cash,tv_transfer,tv_mada,tv_active_coupon,tv_activated;
    private LinearLayout ll_cash,ll_transfer,ll_mada;
    private HomeActivity activity;
    private UserSingleTone userSingleTone;
    private UserModel userModel;
    private int payment_method =0;//default 0
    private String coupon_code="";
    private double coupon_value=0;
    private double total_after_discount = 0;
    private double total_before_discount = 0;
    private boolean inSideRiadCity = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_client_data, container, false);
        initView(view);
        return view;
    }

    public static Fragment_Client_Data newInstance(double total_cost) {

        Bundle bundle = new Bundle();
        bundle.putDouble(TAG,total_cost);
        Fragment_Client_Data fragment_client_data = new Fragment_Client_Data();
        fragment_client_data.setArguments(bundle);
        return fragment_client_data;
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


        edt_coupon_code = view.findViewById(R.id.edt_coupon_code);
        image_coupon_state = view.findViewById(R.id.image_coupon_state);
        progBar = view.findViewById(R.id.progBar);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        tv_active_coupon = view.findViewById(R.id.tv_active_coupon);
        tv_activated = view.findViewById(R.id.tv_activated);


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
               CreateAlertDialog();



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

        tv_active_coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String coupon_code = edt_coupon_code.getText().toString().trim();
                if (!TextUtils.isEmpty(coupon_code))
                {
                    CheckCouponActivation(coupon_code);

                }else
                    {
                        edt_coupon_code.setError(getString(R.string.field_req));

                    }
            }
        });

        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            total_before_discount = bundle.getDouble(TAG);

        }

    }

    private void updateCashSelect()
    {
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

    private void CheckCouponActivation(final String coupon_code) {
        progBar.setVisibility(View.VISIBLE);
        tv_activated.setVisibility(View.GONE);

        Api.getService()
                .getCouponValue(coupon_code)
                .enqueue(new Callback<CouponModel>() {
                    @Override
                    public void onResponse(Call<CouponModel> call, Response<CouponModel> response) {
                        progBar.setVisibility(View.GONE);
                        if (response.isSuccessful()&&response.body()!=null&&response.body().getData()!=null)
                        {

                            updateCouponData(response.body());


                        }else if (response.code() == 404)
                        {
                            tv_activated.setText(getString(R.string.inactive));
                            tv_activated.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<CouponModel> call, Throwable t) {
                        try {
                            progBar.setVisibility(View.GONE);
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }

    private void updateCouponData(CouponModel couponModel) {
        tv_activated.setText(getString(R.string.activated));
        tv_active_coupon.setEnabled(false);
        edt_coupon_code.setEnabled(false);
        image_coupon_state.setVisibility(View.VISIBLE);

        coupon_code = couponModel.getData().getCode();
        coupon_value = couponModel.getData().getValue();
        total_after_discount = total_before_discount-((total_before_discount*coupon_value)/100);


        tv_activated.setText(getString(R.string.activated)+"   ."+getString(R.string.have_discount)+coupon_value+" "+"%"+getString(R.string.discount)+". "+getString(R.string.total)+" : "+total_after_discount+" "+getString(R.string.rsa));
        tv_activated.setVisibility(View.VISIBLE);


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

            if (payment_method ==1)
            {
                if (inSideRiadCity)
                {
                    activity.UploadOrder(m_name, m_phone, m_address, m_note,payment_method,total_after_discount,coupon_code,coupon_value);

                }else
                    {
                        CreateAlertDialog();
                    }

            }else
                {
                    activity.UploadOrder(m_name, m_phone, m_address, m_note,payment_method,total_after_discount,coupon_code,coupon_value);

                }


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

    public void CreateAlertDialog()
    {
        final AlertDialog dialog = new AlertDialog.Builder(activity)
                .setCancelable(true)
                .create();

        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_cash_city,null);
        Button btn_continue = view.findViewById(R.id.btn_continue);
        Button btn_cancel = view.findViewById(R.id.btn_cancel);

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inSideRiadCity = true;
                updateCashSelect();
                dialog.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inSideRiadCity = false;
                dialog.dismiss();
            }
        });

        dialog.getWindow().getAttributes().windowAnimations=R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_window_bg);
        dialog.setView(view);
        dialog.show();
    }
}
