package com.appzone.eyeres.activities_fragments.activity_home.fragments.fragment_home.fragment_store;

import android.app.ProgressDialog;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.appzone.eyeres.R;
import com.appzone.eyeres.activities_fragments.activity_home.activity.HomeActivity;
import com.appzone.eyeres.adapters.ProductColorAdapter;
import com.appzone.eyeres.models.FavoriteIdModel;
import com.appzone.eyeres.models.ProductDataModel;
import com.appzone.eyeres.models.UserModel;
import com.appzone.eyeres.remote.Api;
import com.appzone.eyeres.share.Common;
import com.appzone.eyeres.singletone.UserSingleTone;
import com.appzone.eyeres.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Color extends Fragment{
    private RecyclerView recView,recViewBrands;
    private RecyclerView.LayoutManager manager,managerBrand;
    private ProgressBar progBar,progBarBrands;
    private HomeActivity activity;
    private int orderBy = Tags.type_most_sell;
    private int current_page = 1;
    private List<ProductDataModel.ProductModel> productModelList,first20MostProductList;
    private ProductColorAdapter adapter;
    private UserSingleTone userSingleTone;
    private UserModel userModel;
    private boolean isLoading = false;
    private String user_token = "";
    private TextView tv_no_product,tv_no_brand;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_color,container,false);
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
        first20MostProductList = new ArrayList<>();
        tv_no_product = view.findViewById(R.id.tv_no_product);

        progBar = view.findViewById(R.id.progBar);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        progBarBrands = view.findViewById(R.id.progBarBrands);
        progBarBrands.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        recViewBrands = view.findViewById(R.id.recViewBrands);
        tv_no_brand = view.findViewById(R.id.tv_no_brand);

        recView = view.findViewById(R.id.recView);
        manager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        recView.setLayoutManager(manager);
        recView.setHasFixedSize(true);
        recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
        recView.setDrawingCacheEnabled(true);
        recView.setItemViewCacheSize(25);
        recView.setNestedScrollingEnabled(true);

        managerBrand = new LinearLayoutManager(activity);
        recViewBrands.setLayoutManager(managerBrand);
        recViewBrands.setHasFixedSize(true);
        recViewBrands.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
        recViewBrands.setDrawingCacheEnabled(true);
        recViewBrands.setItemViewCacheSize(25);
        recViewBrands.setNestedScrollingEnabled(true);

        if (userModel == null) {

            adapter = new ProductColorAdapter(productModelList, activity, false,this);
        } else {
            user_token = userModel.getToken();
            adapter = new ProductColorAdapter(productModelList, activity, true,this);

        }
        recView.setAdapter(adapter);


        recView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastVisibleItemPos = ((LinearLayoutManager)recView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int totalItems = recView.getLayoutManager().getItemCount();
                Log.e("lastVisibleItemPos",lastVisibleItemPos+"_");
                Log.e("totalItems",totalItems+"_");

                if (lastVisibleItemPos >= (totalItems-4)&& !isLoading){
                    Log.e("load","load");
                    productModelList.add(null);
                    adapter.notifyItemInserted(productModelList.size()-1);
                    isLoading = true;
                    int nextPageIndex = current_page+1;
                    LoadMore(nextPageIndex,orderBy);
                }

            }
        });
        getProducts();
    }

    public void getProducts()
    {
        if (productModelList.size()==0)
        {
            Api.getService()
                    .getProducts(2, 1, orderBy,user_token)
                    .enqueue(new Callback<ProductDataModel>() {
                        @Override
                        public void onResponse(Call<ProductDataModel> call, Response<ProductDataModel> response) {
                            if (response.isSuccessful()) {
                                progBar.setVisibility(View.GONE);
                                if (response.body() != null) {

                                    Log.e("size3",response.body().getData().size()+"_");

                                    productModelList.clear();

                                    if (current_page == 1)
                                    {

                                            first20MostProductList.clear();


                                            first20MostProductList.addAll(response.body().getData());

                                    }


                                    productModelList.addAll(response.body().getData());
                                    if (productModelList.size() > 0) {
                                        adapter.notifyDataSetChanged();
                                        tv_no_product.setVisibility(View.GONE);

                                    } else {
                                        tv_no_product.setVisibility(View.VISIBLE);


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

    }
    private void LoadMore(int page_index, int orderBy)
    {
        Api.getService()
                .getProducts(2, page_index, orderBy,user_token)
                .enqueue(new Callback<ProductDataModel>() {
                    @Override
                    public void onResponse(Call<ProductDataModel> call, Response<ProductDataModel> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {

                                productModelList.remove(productModelList.size()-1);
                                adapter.notifyItemChanged(productModelList.size()-1);

                                if (response.body().getData().size()>0)
                                {
                                    current_page = response.body().getMeta().getCurrent_page();
                                    productModelList.addAll(response.body().getData());

                                }
                                isLoading = false;
                                adapter.notifyDataSetChanged();

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

    public void Refresh()
    {
        Api.getService()
                .getProducts(2, 1, orderBy,user_token)
                .enqueue(new Callback<ProductDataModel>() {
                    @Override
                    public void onResponse(Call<ProductDataModel> call, Response<ProductDataModel> response) {
                        if (response.isSuccessful()) {
                            progBar.setVisibility(View.GONE);
                            if (response.body() != null) {

                                Log.e("size3",response.body().getData().size()+"_");

                                productModelList.clear();

                                if (current_page == 1)
                                {

                                    first20MostProductList.clear();


                                    first20MostProductList.addAll(response.body().getData());

                                }


                                productModelList.addAll(response.body().getData());
                                if (productModelList.size() > 0) {
                                    adapter.notifyDataSetChanged();
                                    tv_no_product.setVisibility(View.GONE);

                                } else {
                                    tv_no_product.setVisibility(View.VISIBLE);


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
    public void UpdateFavorite(final ProductDataModel.ProductModel productModel, final int pos)
    {
        if (productModel.getIs_favorite() == 1)
        {
            deleteFavorite(productModel,pos);
        }else
        {
            makeFavorite(pos,productModel);
        }
    }
    private void makeFavorite(final int pos, final ProductDataModel.ProductModel productModel)
    {
        final ProgressDialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
        dialog.show();
        Api.getService()
                .makeFavorite(user_token,productModel.getId())
                .enqueue(new Callback<FavoriteIdModel>() {
                    @Override
                    public void onResponse(Call<FavoriteIdModel> call, Response<FavoriteIdModel> response) {
                        if (response.isSuccessful() && response.body()!=null)
                        {
                            dialog.dismiss();
                            productModel.setIs_favorite(1);
                            productModel.setFavorite_id(response.body().getFavorite_id());
                            productModelList.set(pos, productModel);
                            adapter.notifyItemChanged(pos, productModel);
                            activity.RefreshFragmentFavorite();
                        }else
                        {
                            dialog.dismiss();
                            Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                            try {
                                dialog.dismiss();
                                Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                Log.e("Error_code",response.code()+"_"+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<FavoriteIdModel> call, Throwable t) {

                        try {
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });

    }
    private void deleteFavorite(final ProductDataModel.ProductModel productModel , final int pos)
    {
        final ProgressDialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
        dialog.show();
        Api.getService()
                .deleteFavorite(productModel.getFavorite_id(),user_token,"delete")
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful() && response.body()!=null)
                        {
                            dialog.dismiss();
                            productModel.setIs_favorite(0);
                            productModelList.set(pos, productModel);
                            adapter.notifyItemChanged(pos, productModel);
                            activity.RefreshFragmentFavorite();
                        }else
                        {
                            dialog.dismiss();
                            Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                            try {
                                Log.e("Error_code",response.code()+"_"+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                        try {
                            dialog.dismiss();
                            Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }

    public void setItemData(ProductDataModel.ProductModel productModel) {
        activity.DisplayFragmentLensesDetails(productModel);
    }
}
