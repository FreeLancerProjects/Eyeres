package com.appzone.eyeres.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appzone.eyeres.R;
import com.appzone.eyeres.activities_fragments.activity_home.fragments.fragment_home.fragment_orders.Fragment_Current_Orders;
import com.appzone.eyeres.activities_fragments.activity_home.fragments.fragment_home.fragment_orders.Fragment_New_Orders;
import com.appzone.eyeres.activities_fragments.activity_home.fragments.fragment_home.fragment_orders.Fragment_Previous_Orders;
import com.appzone.eyeres.models.OrderDataModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyHolder> {

    private List<OrderDataModel.OrderModel> orderModelList;
    private Context context;
    private Fragment fragment;
    private String current_language;

    public OrderAdapter(List<OrderDataModel.OrderModel> orderModelList, Context context, Fragment fragment) {
        this.orderModelList = orderModelList;
        this.context = context;
        this.fragment = fragment;
        Paper.init(context);
        current_language = Paper.book().read("lang", Locale.getDefault().getLanguage());

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_row, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {
        OrderDataModel.OrderModel orderModel = orderModelList.get(position);
        holder.BindData(orderModel);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderDataModel.OrderModel orderModel = orderModelList.get(holder.getAdapterPosition());

                if (fragment instanceof Fragment_New_Orders)
                {
                    Fragment_New_Orders fragment_new_orders = (Fragment_New_Orders) fragment;
                    fragment_new_orders.setItemData(orderModel);

                }else if (fragment instanceof Fragment_Current_Orders)
                {
                    Fragment_Current_Orders fragment_current_orders = (Fragment_Current_Orders) fragment;
                    fragment_current_orders.setItemData(orderModel);

                }else if (fragment instanceof Fragment_Previous_Orders)
                {

                    Fragment_Previous_Orders fragment_previous_orders = (Fragment_Previous_Orders) fragment;
                    fragment_previous_orders.setItemData(orderModel);

                }


            }
        });


    }

    @Override
    public int getItemCount() {
        return orderModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private ImageView image_state;
        private TextView tv_order_number, tv_order_total,tv_state, tv_date;

        public MyHolder(View itemView) {
            super(itemView);

            image_state = itemView.findViewById(R.id.image_state);
            tv_order_number = itemView.findViewById(R.id.tv_order_number);
            tv_order_total = itemView.findViewById(R.id.tv_order_total);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_state = itemView.findViewById(R.id.tv_state);

        }

        public void BindData(OrderDataModel.OrderModel orderModel) {


            if (fragment instanceof Fragment_New_Orders)
            {
                tv_state.setText(context.getString(R.string.not_accepted));
                image_state.setImageResource(R.drawable.clock3);

            }else if (fragment instanceof Fragment_Current_Orders)
            {
                tv_state.setText(context.getString(R.string.ongoing));
                image_state.setImageResource(R.drawable.clock2);

            }else if (fragment instanceof Fragment_Previous_Orders)
            {
                tv_state.setText(context.getString(R.string.completed));
                image_state.setImageResource(R.drawable.correct2);
            }


            tv_order_number.setText("#" + orderModel.getId());
            tv_order_total.setText(orderModel.getTotal() + " " + context.getString(R.string.rsa));
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE dd/MM/yyyy",new Locale(current_language));
            String date = dateFormat.format(new Date((orderModel.getUpdated_at()*1000)));
            tv_date.setText(date);

        }
    }
}
