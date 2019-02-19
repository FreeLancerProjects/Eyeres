package com.appzone.eyeres.activities_fragments.activity_home.fragments.fragment_details;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.appzone.eyeres.R;
import com.appzone.eyeres.activities_fragments.activity_home.activity.HomeActivity;
import com.appzone.eyeres.adapters.SliderAdapter;
import com.appzone.eyeres.models.PackageSizeModel;
import com.appzone.eyeres.models.ProductDataModel;
import com.appzone.eyeres.remote.Api;
import com.appzone.eyeres.share.Common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Details extends Fragment{
    private static String TAG="productModel";
    private LinearLayout ll_back,ll_slider_container,ll_eye_left_right,ll_1_eye,ll_add_cart;
    private ImageView arrow,image_2_left_increase,image_2_left_decrease,image_2_right_increase,image_2_right_decrease,image_increase_1_left_right,image_decrease_1_left_right;
    private ViewPager pager_slider;
    private TabLayout tab_slider;
    private TextView tv_name,tv_details,tv_counter_2_left,tv_counter_2_right,tv_counter_1_left_right;
    private Spinner spinner_package_size,spinner_2_left,spinner_2_right,spinner_1_left_right;
    private List<String> sizesList;
    private CheckBox checkbox;
    private int similar_eye = 1;
    private int counter_left_right = 1,counter_left=1,counter_right=1;
    private String degree_2_left="",degree_2_right="",degree_1_left_right ="";
    private SliderAdapter sliderAdapter;
    private ProductDataModel.ProductModel productModel;
    private String current_lang;
    private HomeActivity activity;
    private Timer timer;
    private TimerTask timerTask;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details,container,false);
        initView(view);
        return view;
    }

    public static Fragment_Details newInstance(ProductDataModel.ProductModel productModel)
    {
        Fragment_Details fragment_details = new Fragment_Details();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG,productModel);
        fragment_details.setArguments(bundle);
        return fragment_details;
    }
    private void initView(View view) {
        sizesList = new ArrayList<>();

        activity = (HomeActivity) getActivity();
        arrow = view.findViewById(R.id.arrow);
        current_lang = Locale.getDefault().getLanguage();
        if (current_lang.equals("ar"))
        {
            arrow.setImageResource(R.drawable.white_right_arrow);
        }else
            {
                arrow.setImageResource(R.drawable.white_left_arrow);

            }
        ll_back = view.findViewById(R.id.ll_back);
        ll_slider_container = view.findViewById(R.id.ll_slider_container);
        ll_eye_left_right = view.findViewById(R.id.ll_eye_left_right);
        ll_add_cart = view.findViewById(R.id.ll_add_cart);
        image_2_left_increase = view.findViewById(R.id.image_2_left_increase);
        image_2_left_decrease = view.findViewById(R.id.image_2_left_decrease);
        image_2_right_increase = view.findViewById(R.id.image_2_right_increase);
        image_2_right_decrease = view.findViewById(R.id.image_2_right_decrease);
        image_increase_1_left_right = view.findViewById(R.id.image_increase_1_left_right);
        image_decrease_1_left_right = view.findViewById(R.id.image_decrease_1_left_right);
        pager_slider = view.findViewById(R.id.pager_slider);
        tab_slider = view.findViewById(R.id.tab_slider);
        tv_name = view.findViewById(R.id.tv_name);
        tv_details = view.findViewById(R.id.tv_details);
        tv_counter_2_left = view.findViewById(R.id.tv_counter_2_left);
        tv_counter_2_right = view.findViewById(R.id.tv_counter_2_right);

        tv_counter_1_left_right = view.findViewById(R.id.tv_counter_1_left_right);
        spinner_package_size = view.findViewById(R.id.spinner_package_size);
        spinner_2_left = view.findViewById(R.id.spinner_2_left);
        spinner_2_right = view.findViewById(R.id.spinner_2_right);
        spinner_1_left_right = view.findViewById(R.id.spinner_1_left_right);
        ll_1_eye = view.findViewById(R.id.ll_1_eye);
        checkbox = view.findViewById(R.id.checkbox);
        tab_slider.setupWithViewPager(pager_slider);

        ///////////////////////spinners/////////////////////
        List<String> degreeList = getDegreeList();
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(activity,R.layout.spinner_row,degreeList);

        spinner_2_left.setAdapter(arrayAdapter);
        spinner_2_right.setAdapter(arrayAdapter);
        spinner_1_left_right.setAdapter(arrayAdapter);

        spinner_2_left.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position ==0)
                {
                    degree_2_left = "";
                }else
                {
                    degree_2_left = spinner_2_left.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_2_right.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position ==0)
                {
                    degree_2_right = "";
                }else
                {
                    degree_2_right = spinner_2_right.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_1_left_right.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position ==0)
                {
                    degree_1_left_right = "";
                }else
                {
                    degree_1_left_right = spinner_1_left_right.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ////////////////////////////////////////////////////

        checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkbox.isChecked())
                {
                    similar_eye = 2;
                    ll_1_eye.setVisibility(View.GONE);
                    ll_eye_left_right.setVisibility(View.VISIBLE);

                }else
                    {
                        similar_eye =1;
                        ll_eye_left_right.setVisibility(View.GONE);
                        ll_1_eye.setVisibility(View.VISIBLE);

                    }
            }
        });

        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.Back();
            }
        });

        image_2_left_increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increase_2_left_eye_counter();
            }
        });
        image_2_left_decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrease_2_left_eye_counter();
            }
        });

        image_2_right_increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increase_2_right_eye_counter();
            }
        });
        image_2_right_decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrease_2_right_eye_counter();
            }
        });


        image_increase_1_left_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increase_1_left_right_eye_counter();
            }
        });
        image_decrease_1_left_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrease_1_left_right_eye_counter();
            }
        });




        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            productModel = (ProductDataModel.ProductModel) bundle.getSerializable(TAG);
            UpdateUI(productModel);
        }

        getPackageSize();
    }



    private void UpdateUI(ProductDataModel.ProductModel productModel)
    {
        if (productModel.getImages().size()==0)
        {
            ll_slider_container.setVisibility(View.GONE);
        }else if (productModel.getImages().size()==1)
        {
            ll_slider_container.setVisibility(View.VISIBLE);
            sliderAdapter = new SliderAdapter(productModel.getImages(),activity);
            pager_slider.setAdapter(sliderAdapter);

        }else
            {
                ll_slider_container.setVisibility(View.VISIBLE);
                sliderAdapter = new SliderAdapter(productModel.getImages(),activity);
                pager_slider.setAdapter(sliderAdapter);
                timer = new Timer();
                timerTask = new MyTimerTask();
                timer.scheduleAtFixedRate(timerTask,6000,6000);

            }

            if (current_lang.equals("ar"))
            {
                tv_name.setText(productModel.getName_ar());
                tv_details.setText(productModel.getDescription_ar()+"\n"+productModel.getBrand().getName_ar());
            }else
                {
                    tv_name.setText(productModel.getName_en());
                    tv_details.setText(productModel.getDescription_en()+"\n"+productModel.getBrand().getName_en());

                }



    }
    private void getPackageSize()
    {

        final Dialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
        dialog.show();
        Api.getService()
                .getPackageSize()
                .enqueue(new Callback<PackageSizeModel>() {
                    @Override
                    public void onResponse(Call<PackageSizeModel> call, Response<PackageSizeModel> response) {
                        if (response.isSuccessful() && response.body()!=null)
                        {
                            dialog.dismiss();

                            UpdatePackageAdapter(response.body().getSizes());

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
                    public void onFailure(Call<PackageSizeModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }
    private void UpdatePackageAdapter(List<String> sizesList)
    {
        this.sizesList.clear();
        this.sizesList.add(getString(R.string.choose2));

        for (String size : sizesList)
        {
            String s = getString(R.string.package_of)+" "+size+" "+getString(R.string.lenses);
            this.sizesList.add(s);
        }


        spinner_package_size.setAdapter(new ArrayAdapter<>(activity,R.layout.spinner_row,sizesList));

    }
    private void increase_2_left_eye_counter()
    {
        counter_left +=1;
        tv_counter_2_left.setText(String.valueOf(counter_left));

    }
    private void decrease_2_left_eye_counter()
    {

        if (counter_left > 1)
        {
            counter_left -=1;
            tv_counter_2_left.setText(String.valueOf(counter_left));
        }
    }
    private void increase_2_right_eye_counter()
    {
        counter_right +=1;
        tv_counter_2_right.setText(String.valueOf(counter_right));
    }
    private void decrease_2_right_eye_counter()
    {
        if (counter_right > 1)
        {
            counter_right -=1;
            tv_counter_2_right.setText(String.valueOf(counter_right));
        }
    }
    private void decrease_1_left_right_eye_counter()
    {
        if (counter_left_right > 1)
        {
            counter_left_right -=1;
            tv_counter_1_left_right.setText(String.valueOf(counter_left_right));
        }
    }
    private void increase_1_left_right_eye_counter()
    {
        counter_left_right +=1;
        tv_counter_1_left_right.setText(String.valueOf(counter_left_right));
    }
    private List<String> getDegreeList()
    {
        List<String> degreeList = new ArrayList<>();
        degreeList.add(getString(R.string.choose2));
        for (double i = 6.00; i>= -6.00;i-=.25)
        {
            if (i>0.00)
            {
                degreeList.add("+"+i);
            }else if(i < 0.00)
            {
                degreeList.add(String.valueOf(i));

            }
        }


        for (double i = -6.50; i >= -12.00 ; i -= 0.50)
        {
            degreeList.add(String.valueOf(i));

        }

        return degreeList;
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
    public void onDestroyView()
    {
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
