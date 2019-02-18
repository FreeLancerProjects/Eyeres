package com.appzone.eyeres.activities_fragments.activity_home.fragments.fragment_home.fragment_store;

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
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.appzone.eyeres.R;
import com.appzone.eyeres.activities_fragments.activity_home.activity.HomeActivity;
import com.appzone.eyeres.adapters.ProductAdapter;
import com.appzone.eyeres.models.ProductDataModel;
import com.appzone.eyeres.models.UserModel;
import com.appzone.eyeres.remote.Api;
import com.appzone.eyeres.singletone.UserSingleTone;
import com.appzone.eyeres.tags.Tags;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Color extends Fragment{
    private RecyclerView recView;
    private RecyclerView.LayoutManager manager;
    private ProgressBar progBar, progBarLoadMore;
    private LinearLayout ll_add_recent, ll_most_seller, ll_orderBy;
    private HomeActivity activity;
    private int orderBy = Tags.type_add_recent;
    private int current_page = 1;
    private List<ProductDataModel.ProductModel> productModelList,first20RecentProductList,first20MostProductList;
    private ProductAdapter adapter;
    private UserSingleTone userSingleTone;
    private UserModel userModel;
    private boolean isLoading = false;
    private String user_token = "";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_transparent_color_tools,container,false);
        initView(view);
        return view;
    }

    public static Fragment_Color newInstance()
    {
        return new Fragment_Color();
    }
    private void initView(View view) {
        userSingleTone = UserSingleTone.getInstance();
        userModel = userSingleTone.getUserModel();
        activity = (HomeActivity) getActivity();
        productModelList = new ArrayList<>();
        first20RecentProductList = new ArrayList<>();
        first20MostProductList = new ArrayList<>();

        progBar = view.findViewById(R.id.progBar);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        progBarLoadMore = view.findViewById(R.id.progBarLoadMore);
        progBarLoadMore.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        ll_add_recent = view.findViewById(R.id.ll_add_recent);
        ll_most_seller = view.findViewById(R.id.ll_most_seller);
        ll_orderBy = view.findViewById(R.id.ll_orderBy);

        recView = view.findViewById(R.id.recView);
        manager = new GridLayoutManager(getActivity(), 2);
        recView.setLayoutManager(manager);
        recView.setHasFixedSize(true);
        recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
        recView.setDrawingCacheEnabled(true);
        recView.setItemViewCacheSize(25);

        if (userModel == null) {
            adapter = new ProductAdapter(productModelList, activity, false,this);
        } else {

            adapter = new ProductAdapter(productModelList, activity, true,this);

        }
        recView.setAdapter(adapter);

        ll_add_recent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current_page =1;
                isLoading = false;
                orderBy = Tags.type_add_recent;
                ll_add_recent.setBackgroundResource(R.drawable.ll_add_recent_bg);
                ll_most_seller.setBackgroundResource(R.drawable.ll_add_most_bg);

                if (first20RecentProductList.size()>0)
                {
                    productModelList.clear();
                    productModelList.addAll(first20RecentProductList);
                    adapter.notifyDataSetChanged();
                }

            }
        });

        ll_most_seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current_page = 1;
                isLoading = false;
                orderBy = Tags.type_most_sell;
                ll_add_recent.setBackgroundResource(R.drawable.ll_add_most_bg);
                ll_most_seller.setBackgroundResource(R.drawable.ll_add_recent_bg);

                if (first20MostProductList.size()>0)
                {
                    productModelList.clear();
                    productModelList.addAll(first20RecentProductList);
                    adapter.notifyDataSetChanged();
                }else
                {
                    getProducts(current_page,Tags.type_most_sell);
                }


            }
        });

        recView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy>0)
                {
                    int lastVisibleItemPos = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                    if (lastVisibleItemPos >=(recyclerView.getLayoutManager().getChildCount()-10)&& !isLoading){
                        isLoading = true;
                        int nextPageIndex = current_page+1;
                        LoadMore(nextPageIndex,orderBy);
                    }
                }
            }
        });
        getProducts(current_page, orderBy);
    }

    private void getProducts(int page_index, final int orderBy)
    {
        Api.getService()
                .getProducts(1, page_index, orderBy,user_token)
                .enqueue(new Callback<ProductDataModel>() {
                    @Override
                    public void onResponse(Call<ProductDataModel> call, Response<ProductDataModel> response) {
                        if (response.isSuccessful()) {
                            progBar.setVisibility(View.GONE);
                            if (response.body() != null) {

                                productModelList.clear();
                                first20RecentProductList.clear();
                                first20MostProductList.clear();

                                if (orderBy == Tags.type_add_recent)
                                {
                                    first20RecentProductList.addAll(response.body().getData());
                                }else
                                {
                                    first20MostProductList.addAll(response.body().getData());

                                }

                                productModelList.addAll(response.body().getData());
                                if (productModelList.size() > 0) {
                                    ll_orderBy.setVisibility(View.VISIBLE);
                                    adapter.notifyDataSetChanged();
                                } else {
                                    ll_orderBy.setVisibility(View.GONE);

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
    private void LoadMore(int page_index, int orderBy)
    {
        Api.getService()
                .getProducts(1, page_index, orderBy,user_token)
                .enqueue(new Callback<ProductDataModel>() {
                    @Override
                    public void onResponse(Call<ProductDataModel> call, Response<ProductDataModel> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {

                                progBarLoadMore.setVisibility(View.GONE);
                                isLoading = false;
                                current_page = response.body().getMeta().getCurrent_page();
                                productModelList.addAll(response.body().getData());
                                adapter.notifyDataSetChanged();

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ProductDataModel> call, Throwable t) {
                        try {
                            progBarLoadMore.setVisibility(View.GONE);
                            Log.e("Error", t.getMessage());

                        } catch (Exception e) {

                        }
                    }
                });
    }


    public void UpdateFavorite(boolean isFavorite, int pos) {

        ProductDataModel.ProductModel productModel = productModelList.get(pos);
        if (isFavorite)
        {
            productModel.setIs_favorite(1);
        }else
        {
            productModel.setIs_favorite(0);

        }

        productModelList.set(pos,productModel);
        adapter.notifyItemChanged(pos,productModel);


    }

    public void setItemData(ProductDataModel.ProductModel productModel) {
    }
}
