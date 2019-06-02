package com.appzone.eyeres.activities_fragments.activity_home.fragments.fragment_home.fragment_orders;

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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.appzone.eyeres.R;
import com.appzone.eyeres.activities_fragments.activity_home.activity.HomeActivity;
import com.appzone.eyeres.adapters.OrderAdapter;
import com.appzone.eyeres.models.OrderDataModel;
import com.appzone.eyeres.models.UserModel;
import com.appzone.eyeres.remote.Api;
import com.appzone.eyeres.singletone.UserSingleTone;
import com.appzone.eyeres.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_New_Orders extends Fragment{
    private RecyclerView recView;
    private RecyclerView.LayoutManager manager;
    private ProgressBar progBar,progBarLoadMore;
    private List<OrderDataModel.OrderModel> orderModelList;
    private OrderAdapter adapter;
    private boolean isLoading = false;
    private int current_page = 1;
    private HomeActivity activity;
    private UserSingleTone userSingleTone;
    private UserModel userModel;
    private TextView tv_no_order;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_current_new_previous_order,container,false);
        initView(view);
        return view;
    }

    public static Fragment_New_Orders newInstance()
    {
        return new Fragment_New_Orders();
    }

    private void initView(View view)
    {
        orderModelList = new ArrayList<>();
        userSingleTone = UserSingleTone.getInstance();
        userModel = userSingleTone.getUserModel();
        activity = (HomeActivity) getActivity();
        progBar = view.findViewById(R.id.progBar);
        progBarLoadMore = view.findViewById(R.id.progBarLoadMore);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getActivity(),R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        progBarLoadMore.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getActivity(),R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        tv_no_order = view.findViewById(R.id.tv_no_order);

        recView = view.findViewById(R.id.recView);
        manager = new LinearLayoutManager(getActivity());
        recView.setLayoutManager(manager);
        recView.setHasFixedSize(true);
        recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
        recView.setDrawingCacheEnabled(true);
        recView.setItemViewCacheSize(25);
        adapter = new OrderAdapter(orderModelList,activity,this);
        recView.setAdapter(adapter);

        recView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy>0)
                {
                    int lastVisibleItemPos = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                    if (lastVisibleItemPos >=(recyclerView.getLayoutManager().getItemCount()-19)&& !isLoading){
                        progBarLoadMore.setVisibility(View.VISIBLE);

                        isLoading = true;
                        int nextPageIndex = current_page+1;
                        loadMore(nextPageIndex);
                    }
                }
            }
        });
        getOrders();
    }

    public void getOrders()
    {
        Api.getService()
                .getMyOrders(Tags.NEW_ORDER,userModel.getToken(),1)
                .enqueue(new Callback<OrderDataModel>() {
                    @Override
                    public void onResponse(Call<OrderDataModel> call, Response<OrderDataModel> response) {

                        if (response.isSuccessful()&& response.body()!=null)
                        {
                            progBar.setVisibility(View.GONE);

                            orderModelList.clear();
                            orderModelList.addAll(response.body().getData());
                            if (orderModelList.size()>0)
                            {
                                tv_no_order.setVisibility(View.GONE);
                            }else
                            {
                                tv_no_order.setVisibility(View.VISIBLE);

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
                    public void onFailure(Call<OrderDataModel> call, Throwable t) {
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
                .getMyOrders(Tags.NEW_ORDER,userModel.getToken(),page_index)
                .enqueue(new Callback<OrderDataModel>() {
                    @Override
                    public void onResponse(Call<OrderDataModel> call, Response<OrderDataModel> response) {

                        if (response.isSuccessful()&& response.body()!=null)
                        {
                            progBarLoadMore.setVisibility(View.GONE);
                            orderModelList.addAll(response.body().getData());
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
                    public void onFailure(Call<OrderDataModel> call, Throwable t) {
                        try {

                            progBarLoadMore.setVisibility(View.GONE);
                            Toast.makeText(activity,getString(R.string.something), Toast.LENGTH_LONG).show();
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }


    public void setItemData(OrderDataModel.OrderModel orderModel) {
        activity.DisplayFragmentOrderDetails(orderModel);

    }
}
