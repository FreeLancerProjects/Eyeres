package com.appzone.eyeres.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appzone.eyeres.R;
import com.appzone.eyeres.activities_fragments.activity_home.fragments.fragment_home.Fragment_Brand_Details;
import com.appzone.eyeres.models.BrandsDataModel;
import com.appzone.eyeres.tags.Tags;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class TrendAdapter extends RecyclerView.Adapter<TrendAdapter.MyHolder> {

    private List<BrandsDataModel.Trends> trendsModelList;
    private Context context;
    private Fragment_Brand_Details fragment;
    private String current_language;
    private SparseBooleanArray sparseBooleanArray;

    public TrendAdapter(List<BrandsDataModel.Trends> trendsModelList, Context context, Fragment_Brand_Details fragment) {
        this.trendsModelList = trendsModelList;
        this.context = context;
        this.fragment = fragment;
        sparseBooleanArray = new SparseBooleanArray();
        sparseBooleanArray.put(0,true);
        Paper.init(context);
        current_language = Paper.book().read("lang", Locale.getDefault().getLanguage());


    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.trend_row, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {
        BrandsDataModel.Trends trends= trendsModelList.get(position);
        holder.BindData(trends);

        if (sparseBooleanArray.get(position))
        {
            holder.tv_name.setBackgroundResource(R.drawable.selected_trend);
            holder.tv_name.setTextColor(ContextCompat.getColor(context,R.color.white));
        }else
            {
                holder.tv_name.setBackgroundResource(R.color.transparent);
                holder.tv_name.setTextColor(ContextCompat.getColor(context,R.color.black));

            }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BrandsDataModel.Trends trends= trendsModelList.get(holder.getAdapterPosition());
                sparseBooleanArray.clear();
                sparseBooleanArray.put(holder.getAdapterPosition(),true);
                fragment.setItemTrendData(trends);
                notifyDataSetChanged();

            }
        });




    }

    @Override
    public int getItemCount() {
        return trendsModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView tv_name;

        public MyHolder(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            tv_name = itemView.findViewById(R.id.tv_name);

        }

        public void BindData(BrandsDataModel.Trends trends) {


            if (current_language.equals("ar"))
            {
                tv_name.setText(trends.getName_ar());
            }else
                {
                    tv_name.setText(trends.getName_en());

                }


                new MyAsyncTask().execute(trends.getImage());
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
}
