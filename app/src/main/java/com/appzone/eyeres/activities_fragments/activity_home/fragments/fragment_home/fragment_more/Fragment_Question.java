package com.appzone.eyeres.activities_fragments.activity_home.fragments.fragment_home.fragment_more;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.appzone.eyeres.R;
import com.appzone.eyeres.activities_fragments.activity_home.activity.HomeActivity;
import com.appzone.eyeres.adapters.QuestionAdapter;
import com.appzone.eyeres.models.OrderDataModel;
import com.appzone.eyeres.models.QuestionsDataModel;
import com.appzone.eyeres.remote.Api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Question extends Fragment{
    private RecyclerView recView;
    private RecyclerView.LayoutManager manager;
    private ProgressBar progBar;
    private LinearLayout ll_back;
    private ImageView image_back;
    private List<QuestionsDataModel.QuestionModel> questionModelList;
    private QuestionAdapter adapter;
    private boolean isLoading = false;
    private int current_page = 1;
    private HomeActivity activity;
    private String current_language;
    private TextView tv_no_q;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_question,container,false);
        initView(view);
        return view;
    }

    public static Fragment_Question newInstance()
    {
        return new Fragment_Question();
    }

    private void initView(View view)
    {
        questionModelList = new ArrayList<>();
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
        tv_no_q = view.findViewById(R.id.tv_no_q);


        progBar = view.findViewById(R.id.progBar);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getActivity(),R.color.colorPrimary), PorterDuff.Mode.SRC_IN);



        recView = view.findViewById(R.id.recView);
        manager = new LinearLayoutManager(getActivity());
        recView.setLayoutManager(manager);
        recView.setHasFixedSize(true);
        recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
        recView.setDrawingCacheEnabled(true);
        recView.setItemViewCacheSize(25);
        adapter = new QuestionAdapter(questionModelList,activity);
        recView.setAdapter(adapter);

        recView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy>0)
                {
                    int lastVisibleItemPos = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                    if (lastVisibleItemPos >=(recyclerView.getLayoutManager().getItemCount()-5)&& !isLoading){

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
        getQuestions();
    }

    public void getQuestions()
    {
        Api.getService()
                .getQuestions(1)
                .enqueue(new Callback<QuestionsDataModel>() {
                    @Override
                    public void onResponse(Call<QuestionsDataModel> call, Response<QuestionsDataModel> response) {

                        if (response.isSuccessful()&& response.body()!=null)
                        {
                            progBar.setVisibility(View.GONE);

                            questionModelList.clear();
                            questionModelList.addAll(response.body().getData());

                            if (questionModelList.size()>0)
                            {
                                tv_no_q.setVisibility(View.GONE);
                                adapter.notifyDataSetChanged();

                            }else
                                {
                                    tv_no_q.setVisibility(View.VISIBLE);

                                }

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
                    public void onFailure(Call<QuestionsDataModel> call, Throwable t) {
                        try {

                            progBar.setVisibility(View.GONE);
                            Toast.makeText(activity,getString(R.string.something), Toast.LENGTH_LONG).show();
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }

    private void loadMore(int page_index) {
        questionModelList.add(null);
        Api.getService()
                .getQuestions(page_index)
                .enqueue(new Callback<QuestionsDataModel>() {
                    @Override
                    public void onResponse(Call<QuestionsDataModel> call, Response<QuestionsDataModel> response) {


                        if (response.isSuccessful()&& response.body()!=null)
                        {
                            questionModelList.remove(questionModelList.size()-1);
                            if (response.body().getData().size()>0)
                            {
                                questionModelList.addAll(response.body().getData());
                                current_page = response.body().getMeta().getCurrent_page();

                            }


                            isLoading = false;
                            adapter.notifyDataSetChanged();


                        }else
                        {
                            Toast.makeText(activity,getString(R.string.failed), Toast.LENGTH_LONG).show();

                            try {
                                Log.e("error_code",response.code()+"_"+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<QuestionsDataModel> call, Throwable t) {
                        try {

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
