package com.appzone.eyeres.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appzone.eyeres.R;
import com.appzone.eyeres.activities_fragments.activity_home.fragments.fragment_home.fragment_store.Fragment_Color;
import com.appzone.eyeres.models.BrandsDataModel;
import com.appzone.eyeres.tags.Tags;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class BrandsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final int ITEM_DATA = 1;
    private final int ITEM_LOAD = 2;
    private List<BrandsDataModel.BrandModel> brandModelList;
    private Context context;
    private Fragment_Color fragment;
    private String current_language;

    public BrandsAdapter(List<BrandsDataModel.BrandModel> brandModelList, Context context, Fragment_Color fragment) {
        this.brandModelList = brandModelList;
        this.context = context;
        this.fragment = fragment;
        Paper.init(context);
        current_language = Paper.book().read("lang", Locale.getDefault().getLanguage());

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==ITEM_DATA)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.brand_row, parent, false);
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
            BrandsDataModel.BrandModel brandModel = brandModelList.get(position);
            myHolder.BindData(brandModel);

            myHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BrandsDataModel.BrandModel brandModel = brandModelList.get(holder.getAdapterPosition());
                    fragment.setBrandDataItem(brandModel);

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
        return brandModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private TextView tv_name;
        private ImageView image;

        public MyHolder(View itemView) {
            super(itemView);

            tv_name = itemView.findViewById(R.id.tv_name);
            image = itemView.findViewById(R.id.image);

        }


        public void BindData(BrandsDataModel.BrandModel brandModel) {

            if (current_language.equals("ar")) {
                tv_name.setText(brandModel.getName_ar());
            } else {
                tv_name.setText(brandModel.getName_en());

            }

            new MyAsyncTask().execute(brandModel.getImage());



        }

        public class MyAsyncTask extends AsyncTask<String,Void,Bitmap> {
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
        BrandsDataModel.BrandModel brandModel = brandModelList.get(position);

        if (brandModel==null)
        {
            return ITEM_LOAD;

        }else
            {
                return ITEM_DATA;


            }
    }
}
