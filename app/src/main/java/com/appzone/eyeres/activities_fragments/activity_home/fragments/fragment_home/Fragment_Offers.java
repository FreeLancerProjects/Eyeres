package com.appzone.eyeres.activities_fragments.activity_home.fragments.fragment_home;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.appzone.eyeres.R;
import com.appzone.eyeres.activities_fragments.activity_home.activity.HomeActivity;
import com.appzone.eyeres.adapters.AdsSliderAdapter;
import com.appzone.eyeres.adapters.OffersAdapter;
import com.appzone.eyeres.models.AdsModel;
import com.appzone.eyeres.models.ProductDataModel;
import com.appzone.eyeres.remote.Api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Offers extends Fragment{
    private LinearLayout ll_slider_container;
    private ViewPager pager_slider;
    private TabLayout tab_slider;
    private AdsSliderAdapter adsSliderAdapter;
    private RecyclerView recView;
    private RecyclerView.LayoutManager manager;
    private List<ProductDataModel.ProductModel> productModelList;
    private OffersAdapter adapter;
    private ProgressBar progBar,progBarLoadMore;
    private HomeActivity activity;
    private TextView tv_no_product;
    private boolean isLoading = false;
    private int current_page = 1;
    private Timer timer;
    private TimerTask timerTask;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_offers,container,false);
        initView(view);
        return view;
    }

    public static Fragment_Offers newInstance()
    {
        return new Fragment_Offers();
    }

    private void initView(View view)
    {
        productModelList = new ArrayList<>();
        activity = (HomeActivity) getActivity();

        ll_slider_container = view.findViewById(R.id.ll_slider_container);
        pager_slider = view.findViewById(R.id.pager_slider);
        tab_slider = view.findViewById(R.id.tab_slider);
        tab_slider.setupWithViewPager(pager_slider);

        tv_no_product = view.findViewById(R.id.tv_no_product);
        progBarLoadMore = view.findViewById(R.id.progBarLoadMore);
        progBarLoadMore.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getActivity(),R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        progBar = view.findViewById(R.id.progBar);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getActivity(),R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        recView = view.findViewById(R.id.recView);
        manager = new LinearLayoutManager(getActivity());
        recView.setLayoutManager(manager);
        recView.setHasFixedSize(true);
        recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
        recView.setDrawingCacheEnabled(true);
        recView.setItemViewCacheSize(25);
        adapter = new OffersAdapter(productModelList,activity,this);
        recView.setAdapter(adapter);
        recView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy>0)
                {
                    int lastVisibleItemPos = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                    if (lastVisibleItemPos ==(recyclerView.getLayoutManager().getChildCount()-10)&& !isLoading){
                        isLoading = true;
                        int nextPageIndex = current_page+1;
                        loadMore(nextPageIndex);
                    }
                }
            }
        });
        getAds();
        getOffers();
    }
    private void getAds()
    {
        Api.getService()
                .getAds()
                .enqueue(new Callback<AdsModel>() {
                    @Override
                    public void onResponse(Call<AdsModel> call, Response<AdsModel> response) {

                        if (response.isSuccessful()&& response.body()!=null)
                        {

                           updateSliderUI(response.body().getData());

                        }else
                        {

                            try {
                                Log.e("error_code",response.code()+"_"+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<AdsModel> call, Throwable t) {
                        try {

                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });

    }
    private void updateSliderUI(List<AdsModel.Ads> data)
    {
        if (data.size()>0)
        {
            adsSliderAdapter = new AdsSliderAdapter(data,activity);
            pager_slider.setAdapter(adsSliderAdapter);
            if (data.size()>1)
            {
                timer = new Timer();
                timerTask = new MyTimerTask();
                timer.scheduleAtFixedRate(timerTask,6000,6000);

            }
            ll_slider_container.setVisibility(View.VISIBLE);

        }else
            {
                ll_slider_container.setVisibility(View.GONE);
            }
    }
    private void getOffers()
    {

        Api.getService()
                .getOffers(1)
                .enqueue(new Callback<ProductDataModel>() {
                    @Override
                    public void onResponse(Call<ProductDataModel> call, Response<ProductDataModel> response) {

                        if (response.isSuccessful()&& response.body()!=null)
                        {
                            progBar.setVisibility(View.GONE);
                            productModelList.clear();
                            productModelList.addAll(response.body().getData());
                            if (productModelList.size()>0)
                            {
                                tv_no_product.setVisibility(View.GONE);
                            }else
                            {
                                tv_no_product.setVisibility(View.VISIBLE);

                            }
                            adapter.notifyDataSetChanged();
                        }else
                        {
                            progBar.setVisibility(View.GONE);
                            Toast.makeText(activity,getString(R.string.failed), Toast.LENGTH_LONG).show();

                            try {
                                Log.e("error_code",response.code()+"_"+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ProductDataModel> call, Throwable t) {
                        try {

                            progBar.setVisibility(View.GONE);
                            Toast.makeText(activity,getString(R.string.something), Toast.LENGTH_LONG).show();
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });

    }
    private void loadMore(int page_index)
    {
        Api.getService()
                .getOffers(page_index)
                .enqueue(new Callback<ProductDataModel>() {
                    @Override
                    public void onResponse(Call<ProductDataModel> call, Response<ProductDataModel> response) {

                        if (response.isSuccessful()&& response.body()!=null)
                        {
                            progBarLoadMore.setVisibility(View.GONE);
                            productModelList.addAll(response.body().getData());
                            adapter.notifyDataSetChanged();
                            isLoading = false;
                            current_page = response.body().getMeta().getCurrent_page();

                        }else
                        {
                            progBarLoadMore.setVisibility(View.GONE);
                            Toast.makeText(activity,getString(R.string.failed), Toast.LENGTH_LONG).show();

                            try {
                                Log.e("error_code",response.code()+"_"+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ProductDataModel> call, Throwable t) {
                        try {

                            progBarLoadMore.setVisibility(View.GONE);
                            Toast.makeText(activity,getString(R.string.something), Toast.LENGTH_LONG).show();
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }

    public void setItemData(ProductDataModel.ProductModel productModel) {
     activity.DisplayFragmentDetails(productModel);
    }

    private class MyTimerTask extends TimerTask
    {
        @Override
        public void run() {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (pager_slider.getCurrentItem()<pager_slider.getAdapter().getCount()-1)
                    {
                        pager_slider.setCurrentItem(pager_slider.getCurrentItem()+1);
                    }else
                        {
                            pager_slider.setCurrentItem(0);
                        }
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        if (timer!=null)
        {
            timer.purge();
            timer.cancel();
        }
        if (timerTask!=null)
        {
            timerTask.cancel();
        }
        super.onDestroyView();
    }
}
