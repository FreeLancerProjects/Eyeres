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
import com.appzone.eyeres.activities_fragments.activity_home.fragments.fragment_cart.Fragment_Cart;
import com.appzone.eyeres.models.ItemCartModel;
import com.appzone.eyeres.tags.Tags;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyHolder> {

    private List<ItemCartModel> itemCartModelList;
    private Context context;
    private Fragment_Cart fragment;
    public CartAdapter(List<ItemCartModel> itemCartModelList, Context context,Fragment_Cart fragment) {
        this.itemCartModelList = itemCartModelList;
        this.context = context;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_row, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {
        ItemCartModel itemCartModel= itemCartModelList.get(position);
        holder.BindData(itemCartModel);
        holder.image_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.Delete(holder.getAdapterPosition());
            }
        });

        holder.image_increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemCartModel itemCartModel= itemCartModelList.get(holder.getAdapterPosition());
                int counter = Integer.parseInt(holder.tv_amount.getText().toString().trim())+1;
                holder.tv_amount.setText(String.valueOf(counter));
                itemCartModel.setQuantity(counter);
                double item_total_cost = counter*itemCartModel.getProduct_cost();
                itemCartModel.setTotal(item_total_cost);

                fragment.UpdateList_Increase_Decrease(holder.getAdapterPosition(),itemCartModel);
            }
        });

        holder.image_decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Integer.parseInt(holder.tv_amount.getText().toString().trim())>1)
                {
                    int counter = Integer.parseInt(holder.tv_amount.getText().toString().trim())-1;

                    ItemCartModel itemCartModel= itemCartModelList.get(holder.getAdapterPosition());
                    holder.tv_amount.setText(String.valueOf(counter));
                    itemCartModel.setQuantity(counter);
                    double item_total_cost = counter*itemCartModel.getProduct_cost();
                    itemCartModel.setTotal(item_total_cost);

                    fragment.UpdateList_Increase_Decrease(holder.getAdapterPosition(),itemCartModel);

                }


                }
        });


    }

    @Override
    public int getItemCount() {
        return itemCartModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private ImageView image,image_delete,image_increase,image_decrease;
        private TextView tv_cost,tv_name,tv_amount;

        public MyHolder(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            image_delete = itemView.findViewById(R.id.image_delete);
            image_increase = itemView.findViewById(R.id.image_increase);
            image_decrease = itemView.findViewById(R.id.image_decrease);

            tv_cost = itemView.findViewById(R.id.tv_cost);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_amount = itemView.findViewById(R.id.tv_amount);

        }

        public void BindData(ItemCartModel itemCartModel) {

            if (Locale.getDefault().getLanguage().equals("ar"))
            {
                tv_name.setText(itemCartModel.getProduct_name_ar());
            }else
                {
                    tv_name.setText(itemCartModel.getProduct_name_en());

                }

            tv_amount.setText(String.valueOf(itemCartModel.getQuantity()));

            tv_cost.setText(itemCartModel.getProduct_cost()+" "+ context.getString(R.string.rsa));

            Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL+itemCartModel.getProduct_image())).fit().into(image);

        }
    }
}
