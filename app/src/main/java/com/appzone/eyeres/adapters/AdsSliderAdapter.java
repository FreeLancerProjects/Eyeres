package com.appzone.eyeres.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.appzone.eyeres.R;
import com.appzone.eyeres.models.AdsModel;
import com.appzone.eyeres.tags.Tags;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.IOException;
import java.util.List;

public class AdsSliderAdapter extends PagerAdapter {

    private List<AdsModel.Ads> adsList;
    private Context context;
    private ImageView image;
    public AdsSliderAdapter(List<AdsModel.Ads> adsList, Context context) {
        this.adsList = adsList;
        this.context = context;
    }


    @Override
    public int getCount() {
        return adsList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.slider_row,container,false);
        image = view.findViewById(R.id.image);
        Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL+adsList.get(position).getImage())).fit().into(image);
        Log.e("image"+position,Tags.IMAGE_URL+adsList.get(position).getImage());

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
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
