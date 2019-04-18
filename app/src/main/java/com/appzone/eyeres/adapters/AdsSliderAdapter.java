package com.appzone.eyeres.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.appzone.eyeres.R;
import com.appzone.eyeres.models.AdsModel;
import com.appzone.eyeres.tags.Tags;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdsSliderAdapter extends PagerAdapter {

    private List<AdsModel.Ads> adsList;
    private Context context;

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
        ImageView image = view.findViewById(R.id.image);
        Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL+adsList.get(position).getImage())).into(image);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
