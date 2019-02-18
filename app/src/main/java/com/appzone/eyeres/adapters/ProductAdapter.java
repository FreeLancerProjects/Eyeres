package com.appzone.eyeres.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appzone.eyeres.R;
import com.appzone.eyeres.activities_fragments.activity_home.fragments.fragment_home.fragment_store.Fragment_Color;
import com.appzone.eyeres.activities_fragments.activity_home.fragments.fragment_home.fragment_store.Fragment_Tools;
import com.appzone.eyeres.activities_fragments.activity_home.fragments.fragment_home.fragment_store.Fragment_Transparent;
import com.appzone.eyeres.models.ProductDataModel;
import com.appzone.eyeres.tags.Tags;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyHolder> {

    private List<ProductDataModel.ProductModel> productModelList;
    private Context context;
    private boolean isSignUp;
    private Fragment fragment;

    public ProductAdapter(List<ProductDataModel.ProductModel> productModelList, Context context, boolean isSignUp,Fragment fragment) {
        this.productModelList = productModelList;
        this.context = context;
        this.isSignUp = isSignUp;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.eye_row, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {
        ProductDataModel.ProductModel productModel = productModelList.get(position);
        holder.BindData(productModel);
        holder.image_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductDataModel.ProductModel productModel = productModelList.get(holder.getAdapterPosition());

                if (fragment instanceof Fragment_Transparent)
                {
                    Fragment_Transparent fragment_transparent = (Fragment_Transparent) fragment;
                    fragment_transparent.UpdateFavorite(productModel,holder.getAdapterPosition());
                }else if (fragment instanceof Fragment_Color)
                {
                    Fragment_Color fragment_color = (Fragment_Color) fragment;
                    fragment_color.UpdateFavorite(productModel,holder.getAdapterPosition());

                }else if (fragment instanceof Fragment_Tools)
                {
                    Fragment_Tools fragment_tools = (Fragment_Tools) fragment;
                    fragment_tools.UpdateFavorite(productModel,holder.getAdapterPosition());

                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductDataModel.ProductModel productModel = productModelList.get(holder.getAdapterPosition());

                if (fragment instanceof Fragment_Transparent)
                {
                    Fragment_Transparent fragment_transparent = (Fragment_Transparent) fragment;
                    fragment_transparent.setItemData(productModel);
                }else if (fragment instanceof Fragment_Color)
                {
                    Fragment_Color fragment_color = (Fragment_Color) fragment;
                    fragment_color.setItemData(productModel);

                }else if (fragment instanceof Fragment_Tools)
                {
                    Fragment_Tools fragment_tools = (Fragment_Tools) fragment;
                    fragment_tools.setItemData(productModel);

                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return productModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private TextView tv_name, tv_price;
        private ImageView image,image_fav;

        public MyHolder(View itemView) {
            super(itemView);

            tv_name = itemView.findViewById(R.id.tv_name);
            tv_price = itemView.findViewById(R.id.tv_price);
            image = itemView.findViewById(R.id.image);
            image_fav = itemView.findViewById(R.id.image_fav);

        }

        public void BindData(ProductDataModel.ProductModel productModel) {
            if (isSignUp) {
                image_fav.setVisibility(View.VISIBLE);

                if (productModel.getIs_favorite() == 0)
                {
                    image_fav.setImageResource(R.drawable.check_box_fav);

                }else
                    {
                        image_fav.setImageResource(R.drawable.fav_heart);


                    }

            } else {
                image_fav.setVisibility(View.INVISIBLE);

            }


            if (Locale.getDefault().getLanguage().equals("ar")) {
                tv_name.setText(productModel.getName_ar());
            } else {
                tv_name.setText(productModel.getName_en());

            }

            if (productModel.getFeatured() == 0)
            {
                tv_price.setText(productModel.getPrice()+" "+context.getString(R.string.rsa));
            }else
                {
                    tv_price.setText(productModel.getPrice_after_discount()+" "+context.getString(R.string.rsa));

                }

                if (productModel.getImages().size()>0)
                {
                    Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL+productModel.getImages().get(0))).fit().into(image);

                }

        }
    }
}
