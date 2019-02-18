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
import com.appzone.eyeres.activities_fragments.activity_home.fragments.fragment_home.Fragment_Favourite;
import com.appzone.eyeres.models.ProductDataModel;
import com.appzone.eyeres.tags.Tags;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.MyHolder> {

    private List<ProductDataModel.ProductModel> productModelList;
    private Context context;
    private Fragment_Favourite fragment;

    public FavoritesAdapter(List<ProductDataModel.ProductModel> productModelList, Context context, Fragment_Favourite fragment) {
        this.productModelList = productModelList;
        this.context = context;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.favourite_row, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {
        ProductDataModel.ProductModel productModel = productModelList.get(position);
        holder.BindData(productModel);

        holder.img_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductDataModel.ProductModel productModel = productModelList.get(holder.getAdapterPosition());
                fragment.deleteFavorite(productModel,holder.getAdapterPosition());
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductDataModel.ProductModel productModel = productModelList.get(holder.getAdapterPosition());

                fragment.setItemData(productModel);

            }
        });



    }

    @Override
    public int getItemCount() {
        return productModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private TextView tv_name, tv_price;
        private RoundedImageView image;
        private ImageView img_fav;

        public MyHolder(View itemView) {
            super(itemView);

            tv_name = itemView.findViewById(R.id.tv_name);
            tv_price = itemView.findViewById(R.id.tv_price);
            image = itemView.findViewById(R.id.image);
            img_fav = itemView.findViewById(R.id.img);

        }

        public void BindData(ProductDataModel.ProductModel productModel) {

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
