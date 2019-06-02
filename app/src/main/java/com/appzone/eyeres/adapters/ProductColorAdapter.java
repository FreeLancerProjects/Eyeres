package com.appzone.eyeres.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appzone.eyeres.R;
import com.appzone.eyeres.activities_fragments.activity_home.fragments.fragment_home.fragment_store.Fragment_Color;
import com.appzone.eyeres.activities_fragments.activity_home.fragments.fragment_home.fragment_store.Fragment_Tools;
import com.appzone.eyeres.activities_fragments.activity_home.fragments.fragment_home.fragment_store.Fragment_Transparent;
import com.appzone.eyeres.models.ProductDataModel;
import com.appzone.eyeres.tags.Tags;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class ProductColorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final int ITEM_DATA = 1;
    private final int ITEM_LOAD = 2;
    private List<ProductDataModel.ProductModel> productModelList;
    private Context context;
    private boolean isSignUp;
    private Fragment fragment;
    private String current_language;

    public ProductColorAdapter(List<ProductDataModel.ProductModel> productModelList, Context context, boolean isSignUp, Fragment fragment) {
        this.productModelList = productModelList;
        this.context = context;
        this.isSignUp = isSignUp;
        this.fragment = fragment;
        Paper.init(context);
        current_language = Paper.book().read("lang", Locale.getDefault().getLanguage());

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==ITEM_DATA)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.eye_color_row, parent, false);
            return new MyHolder(view);
        }else
            {
                View view = LayoutInflater.from(context).inflate(R.layout.progress_load_more, parent, false);
                return new LoadMoreHolder(view);
            }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyHolder)
        {
            MyHolder myHolder = (MyHolder) holder;
            ProductDataModel.ProductModel productModel = productModelList.get(position);
            myHolder.BindData(productModel);
            myHolder.image_fav.setOnClickListener(new View.OnClickListener() {
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
            myHolder.itemView.setOnClickListener(new View.OnClickListener() {
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
        }else
            {
                LoadMoreHolder loadMoreHolder = (LoadMoreHolder) holder;
                loadMoreHolder.progressBar.setIndeterminate(true);
            }

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


            if (current_language.equals("ar")) {
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
                    new MyAsyncTask().execute(productModel.getImages().get(0));
                }

        }

        public class MyAsyncTask extends AsyncTask<String,Void,Bitmap>
        {
            @Override
            protected Bitmap doInBackground(String... strings) {
                Bitmap bitmap = null;
                try {
                    bitmap = Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL+strings[0])).priority(Picasso.Priority.HIGH).transform(new Transformation() {
                        @Override
                        public Bitmap transform(Bitmap source) {
                            int size = Math.min(source.getWidth(), source.getHeight());
                            int x = (source.getWidth() - size) / 2;
                            int y = (source.getHeight() - size) / 2;
                            Bitmap result = Bitmap.createBitmap(source, x, y, size, size);
                            if (result != source) {
                                source.recycle();
                            }
                            return result;

                        }

                        @Override
                        public String key() {
                            return "square()";
                        }
                    }).get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                image.setImageBitmap(bitmap);
            }
        }
    }

    public class LoadMoreHolder extends RecyclerView.ViewHolder {
        private ProgressBar progressBar;
        public LoadMoreHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progBar);
            progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        }
    }

    @Override
    public int getItemViewType(int position) {
        ProductDataModel.ProductModel productModel = productModelList.get(position);


        if (productModel==null)
        {
            return ITEM_LOAD;

        }else
            {
                return ITEM_DATA;


            }
    }
}
