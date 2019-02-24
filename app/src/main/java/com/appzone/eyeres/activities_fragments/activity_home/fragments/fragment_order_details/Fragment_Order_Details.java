package com.appzone.eyeres.activities_fragments.activity_home.fragments.fragment_order_details;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appzone.eyeres.R;
import com.appzone.eyeres.activities_fragments.activity_home.activity.HomeActivity;
import com.appzone.eyeres.adapters.OrderDetailsAdapter;
import com.appzone.eyeres.models.OrderDataModel;
import com.appzone.eyeres.tags.Tags;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Fragment_Order_Details extends Fragment {
    private static final String TAG = "ORDER_DATA";
    private OrderDataModel.OrderModel orderModel;
    private ImageView image_back;
    private LinearLayout ll_back;
    private TextView tv_not_approved,tv_order_cost;
    private String current_language;
    private Button btn_cancel;
    private AppBarLayout app_bar;
    private RecyclerView recView;
    private RecyclerView.LayoutManager manager;
    private OrderDetailsAdapter adapter;
    private List<OrderDataModel.ProductModel> productModelList;
    private HomeActivity activity;

    ///////////////////////////////////
    private ImageView image1,image2,image3;
    private TextView tv1,tv2,tv3,tv_order_id;
    private View view1,view2;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_details,container,false);
        initView(view);
        return  view;
    }

    public static Fragment_Order_Details newInstance(OrderDataModel.OrderModel orderModel)
    {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG,orderModel);
        Fragment_Order_Details fragment_order_details = new Fragment_Order_Details();
        fragment_order_details.setArguments(bundle);
        return fragment_order_details;
    }

    private void initView(View view) {

        activity = (HomeActivity) getActivity();
        productModelList = new ArrayList<>();
        current_language  = Locale.getDefault().getLanguage();

        image_back = view.findViewById(R.id.image_back);
        if (current_language.equals("ar"))
        {
            image_back.setImageResource(R.drawable.white_right_arrow);
        }else
        {
            image_back.setImageResource(R.drawable.white_left_arrow);

        }
        ll_back = view.findViewById(R.id.ll_back);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.Back();
            }
        });
        app_bar = view.findViewById(R.id.app_bar);
        app_bar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int totalRang = appBarLayout.getTotalScrollRange();

                if ((totalRang+verticalOffset)<=30)
                {
                   tv_not_approved.setVisibility(View.GONE);
                }else
                {
                    tv_not_approved.setVisibility(View.VISIBLE);

                }
            }
        });


        tv_order_cost = view.findViewById(R.id.tv_order_cost);
        tv_not_approved = view.findViewById(R.id.tv_not_approved);
        btn_cancel = view.findViewById(R.id.btn_cancel);

        ///////////////////////////////////////////////////////
        image1 = view.findViewById(R.id.image1);
        image2 = view.findViewById(R.id.image2);
        image3 = view.findViewById(R.id.image3);

        tv1 = view.findViewById(R.id.tv1);
        tv2 = view.findViewById(R.id.tv2);
        tv3 = view.findViewById(R.id.tv3);

        tv_order_id = view.findViewById(R.id.tv_order_id);
        view1 = view.findViewById(R.id.view1);
        view2 = view.findViewById(R.id.view2);

        recView = view.findViewById(R.id.recView);
        manager = new LinearLayoutManager(getActivity());
        recView.setLayoutManager(manager);
        recView.setHasFixedSize(true);
        recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
        recView.setDrawingCacheEnabled(true);
        recView.setItemViewCacheSize(25);
        recView.setNestedScrollingEnabled(false);
        adapter = new OrderDetailsAdapter(productModelList,activity);
        recView.setAdapter(adapter);
        ///////////////////////////////////////////////////////

        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            orderModel = (OrderDataModel.OrderModel) bundle.getSerializable(TAG);
            UpdateUI(orderModel);
        }

    }

    private void UpdateUI(OrderDataModel.OrderModel orderModel) {
        if (orderModel!=null)
        {

            updateStepView(orderModel.getStatus());
            tv_order_id.setText(getString(R.string.order_number)+" #"+orderModel.getId());
            tv_order_cost.setText(getString(R.string.order_cost)+" "+getString(R.string.rsa));
            productModelList.clear();
            productModelList.addAll(orderModel.getItemsList());
            adapter.notifyDataSetChanged();
        }

    }

    public void updateStepView(int completePosition)
    {
        switch (completePosition)
        {
            case 0:
                ClearStepUI();

                break;
            case Tags.accepted_order:
                image1.setBackgroundResource(R.drawable.step_green_circle);
                image1.setImageResource(R.drawable.step_green_true);
                view1.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.green_color));
                tv1.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorPrimary));

                image2.setBackgroundResource(R.drawable.step_green_circle);
                image2.setImageResource(R.drawable.step_green_list);
                view2.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.green_color));
                tv2.setTextColor(ContextCompat.getColor(getActivity(),R.color.colorPrimary));
                btn_cancel.setVisibility(View.GONE);
                tv_not_approved.setText(getString(R.string.order_ongoing));

                break;




        }
    }
    private void ClearStepUI()
    {
        tv_not_approved.setText(getString(R.string.not_approved));
        image1.setBackgroundResource(R.drawable.gray_circle);
        image1.setImageResource(R.drawable.step_gray_true);
        view1.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.gray3));
        tv1.setTextColor(ContextCompat.getColor(getActivity(),R.color.gray3));

        image2.setBackgroundResource(R.drawable.gray_circle);
        image2.setImageResource(R.drawable.step_gray_list);
        view2.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.gray3));
        tv2.setTextColor(ContextCompat.getColor(getActivity(),R.color.gray3));

        image3.setBackgroundResource(R.drawable.gray_circle);
        image3.setImageResource(R.drawable.step_gray_heart);
        tv3.setTextColor(ContextCompat.getColor(getActivity(),R.color.gray3));


    }
}
