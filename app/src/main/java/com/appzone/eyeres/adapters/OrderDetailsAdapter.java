package com.appzone.eyeres.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appzone.eyeres.R;
import com.appzone.eyeres.models.OrderDataModel;
import com.appzone.eyeres.tags.Tags;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.MyHolder> {

    private List<OrderDataModel.ProductModel> productModelList;
    private Context context;

    public OrderDetailsAdapter(List<OrderDataModel.ProductModel> productModelList, Context context) {
        this.productModelList = productModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.details_product_row, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {
        OrderDataModel.ProductModel productModel = productModelList.get(position);
        holder.BindData(productModel);



    }

    @Override
    public int getItemCount() {
        return productModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView tv_cost,tv_name,tv_amount,tv_discount;

        public MyHolder(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            tv_cost = itemView.findViewById(R.id.tv_cost);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_amount = itemView.findViewById(R.id.tv_amount);
            tv_discount = itemView.findViewById(R.id.tv_discount);

        }

        public void BindData(OrderDataModel.ProductModel productModel) {

            if (Locale.getDefault().getLanguage().equals("ar"))
            {
                tv_name.setText(productModel.getName_ar());
                tv_discount.setBackgroundResource(R.drawable.offer_bg_left);
            }else
                {
                    tv_discount.setBackgroundResource(R.drawable.offer_bg_left);
                    tv_name.setText(productModel.getName_en());

                }

            tv_amount.setText(String.valueOf(productModel.getQuantity()));

            if (productModel.getFeatured()==0)
            {
                tv_discount.setVisibility(View.GONE);
                tv_cost.setText(productModel.getPrice()+" "+ context.getString(R.string.rsa));

            }else
                {
                    tv_discount.setText(productModel.getDiscount_percentage()+"%");
                    tv_discount.setVisibility(View.VISIBLE);
                    tv_cost.setText(productModel.getPrice_after_discount()+" "+ context.getString(R.string.rsa));


                }

                if (productModel.getImages().size()>0)
                {
                    Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL+productModel.getImages())).fit().into(image);

                }
        }
    }
}
