package com.appzone.eyeres.activities_fragments.activity_home.fragments.fragment_home.fragment_more;

import android.app.ProgressDialog;
import android.graphics.PorterDuff;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.appzone.eyeres.R;
import com.appzone.eyeres.activities_fragments.activity_home.activity.HomeActivity;
import com.appzone.eyeres.adapters.FavoritesAdapter;
import com.appzone.eyeres.models.ProductDataModel;
import com.appzone.eyeres.models.UserModel;
import com.appzone.eyeres.remote.Api;
import com.appzone.eyeres.share.Common;
import com.appzone.eyeres.singletone.UserSingleTone;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Favourite extends Fragment{
    private RecyclerView recView;
    private RecyclerView.LayoutManager manager;
    private ProgressBar progBar,progBarLoadMore;
    private List<ProductDataModel.ProductModel> productList;
    private FavoritesAdapter adapter;
    private boolean isLoading = false;
    private int current_page = 1;
    private HomeActivity activity;
    private UserSingleTone userSingleTone;
    private UserModel userModel;
    private TextView tv_no_product;
    private LinearLayout ll_back;
    private ImageView image_back;
    private String current_language;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_favourite,container,false);
        initView(view);
        return view;
    }

    public static Fragment_Favourite newInstance()
    {
        return new Fragment_Favourite();
    }

    private void initView(View view)
    {
        productList = new ArrayList<>();
        userSingleTone = UserSingleTone.getInstance();
        userModel = userSingleTone.getUserModel();
        activity = (HomeActivity) getActivity();
        Paper.init(activity);
        current_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        image_back = view.findViewById(R.id.image_back);

        if (current_language.equals("ar"))
        {
            image_back.setImageResource(R.drawable.white_right_arrow);

        }else
        {
            image_back.setImageResource(R.drawable.white_left_arrow);

        }



        ll_back = view.findViewById(R.id.ll_back);

        progBar = view.findViewById(R.id.progBar);
        progBarLoadMore = view.findViewById(R.id.progBarLoadMore);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getActivity(),R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        progBarLoadMore.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getActivity(),R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        tv_no_product = view.findViewById(R.id.tv_no_product);

        recView = view.findViewById(R.id.recView);
        manager = new LinearLayoutManager(getActivity());
        recView.setLayoutManager(manager);
        recView.setHasFixedSize(true);
        recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
        recView.setDrawingCacheEnabled(true);
        recView.setItemViewCacheSize(25);
        adapter = new FavoritesAdapter(productList,activity,this);
        recView.setAdapter(adapter);





        recView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy>0)
                {
                    progBarLoadMore.setVisibility(View.VISIBLE);

                    int lastVisibleItemPos = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                    if (lastVisibleItemPos >= (recyclerView.getLayoutManager().getItemCount()-19)&& !isLoading){
                        isLoading = true;
                        int nextPageIndex = current_page+1;
                        loadMore(nextPageIndex);
                    }
                }
            }
        });

        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.Back();
            }
        });

        getFavorite();
    }

    public void getFavorite() {

        Api.getService()
                .getFavorites(userModel.getToken(),1)
                .enqueue(new Callback<ProductDataModel>() {
                    @Override
                    public void onResponse(Call<ProductDataModel> call, Response<ProductDataModel> response) {

                        if (response.isSuccessful()&& response.body()!=null)
                        {
                            progBar.setVisibility(View.GONE);

                            productList.clear();
                            productList.addAll(response.body().getData());
                            if (productList.size()>0)
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

    private void loadMore(int page_index) {
        Api.getService()
                .getFavorites(userModel.getToken(),page_index)
                .enqueue(new Callback<ProductDataModel>() {
                    @Override
                    public void onResponse(Call<ProductDataModel> call, Response<ProductDataModel> response) {

                        if (response.isSuccessful()&& response.body()!=null)
                        {
                            progBarLoadMore.setVisibility(View.GONE);
                            productList.addAll(response.body().getData());
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

    public void deleteFavorite(ProductDataModel.ProductModel productModel, final int pos)
    {
        final ProgressDialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
        dialog.show();
        Api.getService()
                .deleteFavorite(productModel.getFavorite_id(),userModel.getToken(),"delete")
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful() && response.body()!=null)
                        {

                            productList.remove(pos);
                            adapter.notifyItemRemoved(pos);
                            activity.RefreshFragmentStore();
                            dialog.dismiss();
                            if (productList.size() ==0)
                            {
                                tv_no_product.setVisibility(View.VISIBLE);
                            }

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
