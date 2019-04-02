package com.appzone.eyeres.activities_fragments.activity_home.fragments.fragment_home;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appzone.eyeres.R;
import com.appzone.eyeres.activities_fragments.activity_home.activity.HomeActivity;
import com.appzone.eyeres.adapters.ProductAdapter;
import com.appzone.eyeres.adapters.TrendAdapter;
import com.appzone.eyeres.models.BrandsDataModel;
import com.appzone.eyeres.models.ProductDataModel;
import com.appzone.eyeres.models.UserModel;
import com.appzone.eyeres.remote.Api;
import com.appzone.eyeres.singletone.UserSingleTone;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Brand_Details extends Fragment {
    private static final String TAG = "DATA";
    private HomeActivity activity;
    private TextView tv_title,tv_no_trend;
    private LinearLayout ll_back;
    private ImageView image_back;
    private List<BrandsDataModel.Trends> trendsList;
    private TrendAdapter trendAdapter;
    private List<ProductDataModel.ProductModel> productModelList;
    private ProductAdapter productAdapter;
    private RecyclerView recView,recViewTrend;
    private RecyclerView.LayoutManager manager,managerTrend;
    private ProgressBar progBar;
    private UserSingleTone userSingleTone;
    private UserModel userModel;
    private BrandsDataModel.BrandModel brandModel;
    private String current_language;
    private boolean isLoading = false;
    private int current_page = 1;
    private int trend_id;


    public static Fragment_Brand_Details newInstance(BrandsDataModel.BrandModel brandModel)
    {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG, brandModel);

        Fragment_Brand_Details fragment_brand_details = new Fragment_Brand_Details();
        fragment_brand_details.setArguments(bundle);
        return fragment_brand_details;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_brand_details,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        activity = (HomeActivity) getActivity();
        Paper.init(activity);
        current_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        userSingleTone = UserSingleTone.getInstance();
        userModel = userSingleTone.getUserModel();
        trendsList = new ArrayList<>();
        productModelList = new ArrayList<>();

        image_back = view.findViewById(R.id.image_back);

        if (current_language.equals("ar"))
        {
            image_back.setImageResource(R.drawable.white_right_arrow);
        }else
        {
            image_back.setImageResource(R.drawable.white_left_arrow);

        }

        tv_no_trend = view.findViewById(R.id.tv_no_trend);

        tv_title = view.findViewById(R.id.tv_title);
        ll_back = view.findViewById(R.id.ll_back);


        progBar = view.findViewById(R.id.progBar);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        recViewTrend = view.findViewById(R.id.recViewTrend);
        managerTrend = new LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false);
        recViewTrend.setLayoutManager(managerTrend);
        trendAdapter = new TrendAdapter(trendsList,activity,this);
        recViewTrend.setAdapter(trendAdapter);

        recView = view.findViewById(R.id.recView);
        manager = new GridLayoutManager(activity,2);
        recView.setLayoutManager(manager);
        recView.setHasFixedSize(true);
        recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
        recView.setDrawingCacheEnabled(true);
        recView.setItemViewCacheSize(25);
        recView.setNestedScrollingEnabled(true);
        if (userModel!=null)
        {
            productAdapter = new ProductAdapter(productModelList,activity,true,this);

        }else
            {
                productAdapter = new ProductAdapter(productModelList,activity,true,this);

            }

            recView.setAdapter(productAdapter);

        Bundle bundle = getArguments();

        if (bundle!=null)
        {
            brandModel = (BrandsDataModel.BrandModel) bundle.getSerializable(TAG);
            UpdateUI(brandModel);
        }

        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.Back();
            }
        });

        recView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastVisibleItemPos = ((GridLayoutManager)recView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int totalItems = recView.getLayoutManager().getItemCount();


                if (dy>0)
                {
                    if (lastVisibleItemPos >= (totalItems-4)&& !isLoading){
                        productModelList.add(null);
                        productAdapter.notifyItemInserted(productModelList.size()-1);
                        isLoading = true;
                        int nextPageIndex = current_page+1;
                        LoadMore(nextPageIndex);
                    }
                }

            }
        });

    }

    private void UpdateUI(BrandsDataModel.BrandModel brandModel) {

        if (current_language.equals("ar"))
        {
            tv_title.setText(brandModel.getName_ar());
        }else
            {
                tv_title.setText(brandModel.getName_en());

            }

        this.trendsList.addAll(brandModel.getTrends());
        trendAdapter.notifyDataSetChanged();

        if (brandModel.getTrends().size() >0)
        {
            trend_id = brandModel.getTrends().get(0).getId();
            tv_no_trend.setVisibility(View.GONE);
            getProducts();
        }else
            {
                progBar.setVisibility(View.GONE);
                tv_no_trend.setVisibility(View.VISIBLE);

            }


    }

    public void getProducts()
    {

        productModelList.clear();
        productAdapter.notifyDataSetChanged();
        progBar.setVisibility(View.VISIBLE);

        Api.getService()
                    .getProductOfTrends(trend_id, 1 )
                    .enqueue(new Callback<ProductDataModel>() {
                        @Override
                        public void onResponse(Call<ProductDataModel> call, Response<ProductDataModel> response) {
                            if (response.isSuccessful()) {
                                progBar.setVisibility(View.GONE);
                                if (response.body() != null) {


                                    Log.e("size",response.body().getData().size()+"_");


                                    productModelList.addAll(response.body().getData());
                                    if (productModelList.size() > 0) {
                                        productAdapter.notifyDataSetChanged();
                                        tv_no_trend.setVisibility(View.GONE);
                                    } else {
                                        tv_no_trend.setVisibility(View.VISIBLE);

                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ProductDataModel> call, Throwable t) {
                            try {
                                progBar.setVisibility(View.GONE);
                                Log.e("Error", t.getMessage());

                            } catch (Exception e) {

                            }
                        }
                    });


    }
    private void LoadMore(int page_index)
    {
        Api.getService()
                .getProductOfTrends(trend_id, page_index)
                .enqueue(new Callback<ProductDataModel>() {
                    @Override
                    public void onResponse(Call<ProductDataModel> call, Response<ProductDataModel> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {

                                productModelList.remove(productModelList.size()-1);
                                productAdapter.notifyItemChanged(productModelList.size()-1);

                                if (response.body().getData().size()>0)
                                {
                                    current_page = response.body().getMeta().getCurrent_page();
                                    productModelList.addAll(response.body().getData());

                                }
                                isLoading = false;
                                productAdapter.notifyDataSetChanged();

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ProductDataModel> call, Throwable t) {
                        try {
                            Log.e("Error", t.getMessage());

                        } catch (Exception e) {

                        }
                    }
                });
    }

    public void setItemData(ProductDataModel.ProductModel productModel) {
        activity.DisplayFragmentLensesDetails(productModel);
    }

    public void setItemTrendData(BrandsDataModel.Trends trends) {
        this.trend_id = trends.getId();
        getProducts();
    }
}
