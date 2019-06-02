package com.appzone.eyeres.activities_fragments.activity_home.fragments.fragment_search;

import android.graphics.PorterDuff;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.appzone.eyeres.share.Common;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Search extends Fragment{
    private ExpandableLayout expand_layout;
    private AutocompleteSearchAdapter searchAdapter;
    private List<String> queriesList;
    private ProgressBar progBar,progBarLoadMore;
    public TextView tv_no_search;
    private RecyclerView recView,recViewSearch;
    private RecyclerView.LayoutManager manager,manager2;
    private SearchAdapter adapter;
    private List<ProductDataModel.ProductModel> productModelList;
    private HomeActivity activity;
    private String current_language;
    private Preferences preferences;
    private int current_page = 1;
    private boolean isLoading = false;
    private String query="";
    private ImageView arrow;
    private LinearLayout ll_search,ll_search_data_container;
    private EditText edt_search;
    private Button btn_search;
    private Animation animation,animation2;


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
        activity = (HomeActivity) getActivity();
        Paper.init(activity);
        current_language = Paper.book().read("lang", Locale.getDefault().getLanguage());

        queriesList = new ArrayList<>();
        productModelList = new ArrayList<>();
        preferences = Preferences.getInstance();
        expand_layout = view.findViewById(R.id.expand_layout);
        tv_no_search = view.findViewById(R.id.tv_no_search);

        progBar = view.findViewById(R.id.progBar);
        progBarLoadMore = view.findViewById(R.id.progBarLoadMore);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getActivity(),R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        progBarLoadMore.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getActivity(),R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        manager = new LinearLayoutManager(getActivity());
        manager2 = new LinearLayoutManager(getActivity());
        ///////////////////////////////////////////////
        arrow = view.findViewById(R.id.arrow);

        if (current_language.equals("ar")) {
            arrow.setImageResource(R.drawable.search_right_arrow);

            animation = AnimationUtils.loadAnimation(activity, R.anim.search_enter_from_left);

        } else {
            arrow.setImageResource(R.drawable.search_left_arrow);

            animation = AnimationUtils.loadAnimation(activity, R.anim.search_enter_from_right);


        }
        animation2 = AnimationUtils.loadAnimation(activity, R.anim.fragment_fade_in);

        ll_search_data_container = view.findViewById(R.id.ll_search_data_container);
        ll_search = view.findViewById(R.id.ll_search);
        edt_search = view.findViewById(R.id.edt_search);
        btn_search = view.findViewById(R.id.btn_search);
        ll_search.setVisibility(View.VISIBLE);
        ll_search.clearAnimation();
        ll_search.startAnimation(animation);
        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = edt_search.getText().toString().trim();
                if (TextUtils.isEmpty(query)) {
                    btn_search.setVisibility(View.GONE);
                    updateSearch_state2();
                } else {
                    btn_search.setVisibility(View.VISIBLE);
                    updateSearch_state1();

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String query = edt_search.getText().toString().trim();
                Common.CloseKeyBoard(activity, edt_search);
                Search(query);
            }

        });
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.Back();
            }
        });
        ///////////////////////////////////////////////
        recViewSearch = view.findViewById(R.id.recViewSearch);
        recViewSearch.setLayoutManager(manager2);
        searchAdapter = new AutocompleteSearchAdapter(activity,queriesList,this);
        recViewSearch.setAdapter(searchAdapter);
        ////////////////////////////////////////////////
        recView = view.findViewById(R.id.recView);
        recView.setLayoutManager(manager);
        recView.setHasFixedSize(true);
        recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
        recView.setDrawingCacheEnabled(true);
        recView.setItemViewCacheSize(25);
        adapter = new SearchAdapter(productModelList,activity,this);
        recView.setAdapter(adapter);
        recView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy>0)
                {
                    int lastVisibleItemPos = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                    if (lastVisibleItemPos ==(recyclerView.getLayoutManager().getItemCount()-10)&& !isLoading){
                        isLoading = true;
                        int nextPageIndex = current_page+1;
                        loadMore(query,nextPageIndex);
                    }
                }
            }
        });

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //activity.HideFragmentHome();
                ll_search_data_container.setVisibility(View.VISIBLE);
                ll_search_data_container.clearAnimation();
                ll_search_data_container.startAnimation(animation2);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }

    public void updateSearch_state1()
    {
        if (preferences.getAllSearchQueries(activity).size()>0)
        {
            if (!expand_layout.isExpanded())
            {
                queriesList.clear();
                queriesList.addAll(preferences.getAllSearchQueries(activity));
                searchAdapter.notifyDataSetChanged();
                expand_layout.setVisibility(View.VISIBLE);
                expand_layout.expand(true);
            }

        }
    }
    public void updateSearch_state2()
    {
        productModelList.clear();
        adapter.notifyDataSetChanged();
        tv_no_search.setVisibility(View.VISIBLE);
        expand_layout.collapse(true);

    }

    public void setItemDataForSearch(String query)
    {
        edt_search.setText(query);
        btn_search.setVisibility(View.VISIBLE);
        Search(query);

    }
    public void Search(String query)
    {
        Common.CloseKeyBoard(activity,edt_search);
        expand_layout.setVisibility(View.GONE);
        tv_no_search.setVisibility(View.GONE);

        this.query = query;
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
        if (!TextUtils.isEmpty(query))
        {
            preferences.SaveQuery(activity,query);

        }
        activity.DisplayFragmentLensesDetails(productModel);
    }
}
