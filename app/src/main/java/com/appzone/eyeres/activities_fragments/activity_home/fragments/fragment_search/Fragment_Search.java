package com.appzone.eyeres.activities_fragments.activity_home.fragments.fragment_search;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.appzone.eyeres.R;
import com.appzone.eyeres.activities_fragments.activity_home.activity.HomeActivity;
import com.appzone.eyeres.adapters.AutocompleteSearchAdapter;
import com.appzone.eyeres.adapters.SearchAdapter;
import com.appzone.eyeres.models.ProductDataModel;
import com.appzone.eyeres.preferences.Preferences;
import com.appzone.eyeres.remote.Api;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Search extends Fragment{
    private ImageView arrow;
    private LinearLayout ll_back;
    private Button btn_search;
    private EditText edt_search;
    private ExpandableLayout expand_layout;
    private AutocompleteSearchAdapter searchAdapter;
    private List<String> queriesList;
    private ProgressBar progBar,progBarLoadMore;
    private TextView tv_no_search;
    private RecyclerView recView,recViewSearch;
    private RecyclerView.LayoutManager manager;
    private SearchAdapter adapter;
    private List<ProductDataModel.ProductModel> productModelList;
    private HomeActivity activity;
    private String current_language;
    private Preferences preferences;
    private int current_page = 1;
    private boolean isLoading = false;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search,container,false);
        initView(view);
        return view;
    }

    public static Fragment_Search newInstance()
    {
        return new Fragment_Search();
    }
    private void initView(View view)
    {
        queriesList = new ArrayList<>();
        productModelList = new ArrayList<>();
        preferences = Preferences.getInstance();
        current_language = Locale.getDefault().getLanguage();
        arrow = view.findViewById(R.id.arrow);
        if (current_language.equals("ar"))
        {
            arrow.setImageResource(R.drawable.white_right_arrow);
        }else
            {
                arrow.setImageResource(R.drawable.white_left_arrow);

            }

        activity = (HomeActivity) getActivity();
        ll_back = view.findViewById(R.id.ll_back);
        btn_search = view.findViewById(R.id.btn_search);
        edt_search = view.findViewById(R.id.edt_search);
        expand_layout = view.findViewById(R.id.expand_layout);
        tv_no_search = view.findViewById(R.id.tv_no_search);

        progBar = view.findViewById(R.id.progBar);
        progBarLoadMore = view.findViewById(R.id.progBarLoadMore);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getActivity(),R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        progBarLoadMore.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getActivity(),R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        manager = new LinearLayoutManager(getActivity());
        ///////////////////////////////////////////////
        recViewSearch = view.findViewById(R.id.recViewSearch);
        recViewSearch.setLayoutManager(manager);
        searchAdapter = new AutocompleteSearchAdapter(activity,queriesList,this);
        recViewSearch.setAdapter(searchAdapter);
        ////////////////////////////////////////////////
        recView = view.findViewById(R.id.recView);
        recView.setLayoutManager(manager);
        recView.setHasFixedSize(true);
        recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
        recView.setDrawingCacheEnabled(true);
        recView.setItemViewCacheSize(25);

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
                        String query = edt_search.getText().toString().trim();
                        loadMore(query,nextPageIndex);
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

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (edt_search.getText().toString().trim().length()>0)
                {
                    btn_search.setVisibility(View.VISIBLE);
                    if (preferences.getAllSearchQueries(activity).size()>0)
                    {
                        queriesList.addAll(preferences.getAllSearchQueries(activity));
                        searchAdapter.notifyDataSetChanged();
                        expand_layout.expand(true);
                    }
                }else
                    {
                        btn_search.setVisibility(View.GONE);
                        productModelList.clear();
                        adapter.notifyDataSetChanged();
                        tv_no_search.setVisibility(View.VISIBLE);

                    }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expand_layout.isExpanded())
                {
                    expand_layout.collapse(true);

                }
                tv_no_search.setVisibility(View.GONE);
                String query = edt_search.getText().toString().trim();
                Search(query);
            }
        });
    }

    public void setItemDataForSearch(String query)
    {
        edt_search.setText(query);
        expand_layout.collapse(true);
        btn_search.setVisibility(View.VISIBLE);
        tv_no_search.setVisibility(View.GONE);
        Search(query);
    }

    private void Search(String query)
    {
        progBar.setVisibility(View.VISIBLE);
        Api.getService()
                .search(query,1)
                .enqueue(new Callback<ProductDataModel>() {
                    @Override
                    public void onResponse(Call<ProductDataModel> call, Response<ProductDataModel> response) {
                        if (response.isSuccessful() && response.body()!=null)
                        {
                            progBar.setVisibility(View.GONE);
                            productModelList.clear();
                            productModelList.addAll(response.body().getData());
                            if (productModelList.size()>0)
                            {
                                tv_no_search.setVisibility(View.GONE);
                            }else
                                {
                                    tv_no_search.setVisibility(View.VISIBLE);

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
                            Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }
    private void loadMore(String query,int page_index)
    {
        progBarLoadMore.setVisibility(View.VISIBLE);
        Api.getService()
                .search(query,page_index)
                .enqueue(new Callback<ProductDataModel>() {
                    @Override
                    public void onResponse(Call<ProductDataModel> call, Response<ProductDataModel> response) {
                        if (response.isSuccessful() && response.body()!=null)
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
                            Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }
    public void setItemData(ProductDataModel.ProductModel productModel)
    {
        String query = edt_search.getText().toString().trim();
        if (!TextUtils.isEmpty(query))
        {
            preferences.SaveQuery(activity,query);

        }
        activity.DisplayFragmentDetails(productModel);
    }
}
