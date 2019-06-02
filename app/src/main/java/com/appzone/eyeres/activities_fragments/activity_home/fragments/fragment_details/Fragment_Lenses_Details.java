package com.appzone.eyeres.activities_fragments.activity_home.fragments.fragment_details;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.appzone.eyeres.R;
import com.appzone.eyeres.activities_fragments.activity_home.activity.HomeActivity;
import com.appzone.eyeres.adapters.SliderAdapter;
import com.appzone.eyeres.models.ItemCartModel;
import com.appzone.eyeres.models.ProductDataModel;
import com.appzone.eyeres.share.Common;
import com.appzone.eyeres.singletone.OrderCartSingleTone;
import com.appzone.eyeres.tags.Tags;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import io.paperdb.Paper;

public class Fragment_Lenses_Details extends Fragment {
    private static String TAG = "productModel";
    private LinearLayout ll_back, ll_eye_left_right, ll_1_eye, ll_add_cart, ll_degree_2, ll_deviation_2, ll_axis_2, ll_counter_2, ll_degree_1, ll_deviation_1, ll_axis_1, ll_counter_1;
    private ImageView arrow, image_2_left_increase, image_2_left_decrease, image_2_right_increase, image_2_right_decrease, image_increase_1_left_right, image_decrease_1_left_right;
    private ViewPager pager_slider;
    private TabLayout tab_slider;
    private TextView tv_name, tv_details, tv_counter_2_left, tv_counter_2_right, tv_counter_1_left_right;
    private Spinner spinner_2_left, spinner_2_right, spinner_1_left_right, spinner_deviation_1_left_right, spinner_axis_1_left_right, spinner_deviation_2_left, spinner_deviation_2_right, spinner_axis_2_left, spinner_axis_2_right;
    private CheckBox checkbox;
    private int similar_eye = 1;
    private int counter_left_right = 1, counter_left = 1, counter_right = 1;
    private String degree_2_left = "", degree_2_right = "", degree_1_left_right = "", deviation_degree_1_left_right = "", deviation_degree_left = "", deviation_degree_right = "", axis_1_left_right = "", axis_left = "", axis_right = "";
    private SliderAdapter sliderAdapter;
    private ProductDataModel.ProductModel productModel;
    private String current_language;
    private HomeActivity activity;
    private Timer timer;
    private TimerTask timerTask;
    private LinearLayout ll_container;
    private OrderCartSingleTone orderCartSingleTone;
    private List<String> deviationDegreeList, axisList, degreeList;
    private boolean hasDegree = false, hasAxis = false, hasDeviation = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lenses_details, container, false);
        initView(view);
        return view;
    }

    public static Fragment_Lenses_Details newInstance(ProductDataModel.ProductModel productModel) {
        Fragment_Lenses_Details fragment_Lenses_details = new Fragment_Lenses_Details();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG, productModel);
        fragment_Lenses_details.setArguments(bundle);
        return fragment_Lenses_details;
    }

    private void initView(View view) {

        deviationDegreeList = new ArrayList<>();
        axisList = new ArrayList<>();
        degreeList = new ArrayList<>();


        orderCartSingleTone = OrderCartSingleTone.newInstance();
        activity = (HomeActivity) getActivity();
        Paper.init(activity);
        current_language = Paper.book().read("lang", Locale.getDefault().getLanguage());

        arrow = view.findViewById(R.id.arrow);
        if (current_language.equals("ar")) {
            arrow.setImageResource(R.drawable.white_right_arrow);
        } else {
            arrow.setImageResource(R.drawable.white_left_arrow);

        }

        ll_back = view.findViewById(R.id.ll_back);


        ll_container = view.findViewById(R.id.ll_container);
        ll_1_eye = view.findViewById(R.id.ll_1_eye);
        ll_eye_left_right = view.findViewById(R.id.ll_eye_left_right);
        checkbox = view.findViewById(R.id.checkbox);


        ll_degree_1 = view.findViewById(R.id.ll_degree_1);
        ll_deviation_1 = view.findViewById(R.id.ll_deviation_1);
        ll_axis_1 = view.findViewById(R.id.ll_axis_1);
        ll_counter_1 = view.findViewById(R.id.ll_counter_1);

        ll_degree_2 = view.findViewById(R.id.ll_degree_2);
        ll_deviation_2 = view.findViewById(R.id.ll_deviation_2);
        ll_axis_2 = view.findViewById(R.id.ll_axis_2);
        ll_counter_2 = view.findViewById(R.id.ll_counter_2);


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
        spinner_2_left = view.findViewById(R.id.spinner_2_left);
        spinner_2_right = view.findViewById(R.id.spinner_2_right);
        spinner_1_left_right = view.findViewById(R.id.spinner_1_left_right);

        spinner_deviation_1_left_right = view.findViewById(R.id.spinner_deviation_1_left_right);
        spinner_axis_1_left_right = view.findViewById(R.id.spinner_axis_1_left_right);


        /////***


        spinner_deviation_2_left = view.findViewById(R.id.spinner_deviation_2_left);
        spinner_deviation_2_right = view.findViewById(R.id.spinner_deviation_2_right);
        spinner_axis_2_left = view.findViewById(R.id.spinner_axis_2_left);
        spinner_axis_2_right = view.findViewById(R.id.spinner_axis_2_right);

        tab_slider.setupWithViewPager(pager_slider);

        ///////////////////////spinners/////////////////////


        spinner_2_left.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    degree_2_left = "";
                } else {
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
                if (position == 0) {
                    degree_2_right = "";
                } else {
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
                if (position == 0) {
                    degree_1_left_right = "";
                } else {
                    degree_1_left_right = spinner_1_left_right.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinner_deviation_1_left_right.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    deviation_degree_1_left_right = "";
                } else {
                    deviation_degree_1_left_right = deviationDegreeList.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_axis_1_left_right.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    axis_1_left_right = "";
                } else {
                    axis_1_left_right = axisList.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinner_deviation_2_left.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    deviation_degree_left = "";
                } else {
                    deviation_degree_left = deviationDegreeList.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_deviation_2_right.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    deviation_degree_right = "";
                } else {
                    deviation_degree_right = deviationDegreeList.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinner_axis_2_left.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    axis_left = "";
                } else {
                    axis_left = axisList.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_axis_2_right.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    axis_right = "";
                } else {
                    axis_right = axisList.get(position);
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
                if (checkbox.isChecked()) {
                    similar_eye = 2;
                    ll_1_eye.setVisibility(View.GONE);
                    ll_eye_left_right.setVisibility(View.VISIBLE);

                } else {
                    similar_eye = 1;
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

        ll_add_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckData();
            }
        });


        Bundle bundle = getArguments();
        if (bundle != null) {
            productModel = (ProductDataModel.ProductModel) bundle.getSerializable(TAG);
            UpdateUI(productModel);
        }

    }


    private void UpdateAllSpinnerAdapter(ProductDataModel.ProductModel productModel) {

        degreeList.addAll(productModel.getDegrees().getMyopia());
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(activity, R.layout.spinner_row, degreeList);
        spinner_2_left.setAdapter(arrayAdapter);
        spinner_2_right.setAdapter(arrayAdapter);
        spinner_1_left_right.setAdapter(arrayAdapter);

        deviationDegreeList.addAll(productModel.getDegrees().getDeviation());
        axisList.addAll(productModel.getDegrees().getAxis());

        ArrayAdapter adapter2 = new ArrayAdapter<>(activity, R.layout.spinner_row, deviationDegreeList);
        ArrayAdapter adapter3 = new ArrayAdapter<>(activity, R.layout.spinner_row, axisList);

        spinner_deviation_1_left_right.setAdapter(adapter2);
        spinner_axis_1_left_right.setAdapter(adapter3);
        spinner_deviation_2_left.setAdapter(adapter2);
        spinner_deviation_2_right.setAdapter(adapter2);
        spinner_axis_2_left.setAdapter(adapter3);
        spinner_axis_2_right.setAdapter(adapter3);
    }


    private void UpdateUI(ProductDataModel.ProductModel productModel) {


        if (productModel.getImages().size() == 0) {

        } else if (productModel.getImages().size() == 1) {
            sliderAdapter = new SliderAdapter(productModel.getImages(), activity);
            pager_slider.setAdapter(sliderAdapter);

        } else {
            sliderAdapter = new SliderAdapter(productModel.getImages(), activity);
            pager_slider.setAdapter(sliderAdapter);
            pager_slider.setOffscreenPageLimit(productModel.getImages().size());

            timer = new Timer();
            timerTask = new MyTimerTask();
            timer.scheduleAtFixedRate(timerTask, 6000, 6000);

            for (int i = 0; i < tab_slider.getTabCount() - 1; i++) {
                View view = ((ViewGroup) tab_slider.getChildAt(0)).getChildAt(i);

                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                params.setMargins(4, 0, 4, 0);
            }
        }

        if (productModel.getFeatured() == 0) {
            if (current_language.equals("ar")) {
                tv_name.setText(productModel.getName_ar());


                if (productModel.getBrand() != null) {
                    tv_details.setText(productModel.getDescription_ar() + "\n" + productModel.getBrand().getName_ar() + " " + productModel.getPrice() + " " + getString(R.string.rsa));

                } else {
                    tv_details.setText(productModel.getDescription_ar() + "\n" + " " + productModel.getPrice() + " " + getString(R.string.rsa));

                }
            } else {
                tv_name.setText(productModel.getName_en());

                if (productModel.getBrand() != null) {
                    tv_details.setText(productModel.getDescription_en() + "\n" + productModel.getBrand().getName_en() + " " + productModel.getPrice() + " " + getString(R.string.rsa));

                } else {
                    tv_details.setText(productModel.getDescription_en() + "\n" + " " + productModel.getPrice() + " " + getString(R.string.rsa));

                }

            }

        } else {


            if (current_language.equals("ar")) {
                tv_name.setText(productModel.getName_ar());


                if (productModel.getBrand() != null) {
                    tv_details.setText(productModel.getDescription_ar() + "\n" + productModel.getBrand().getName_ar() + " " + productModel.getPrice() + " " + getString(R.string.rsa));

                } else {
                    tv_details.setText(productModel.getDescription_ar() + "\n" + " " + productModel.getPrice() + " " + getString(R.string.rsa));

                }

            } else {


                tv_name.setText(productModel.getName_en());
                if (productModel.getBrand() != null) {
                    tv_details.setText(productModel.getDescription_en() + "\n" + productModel.getBrand().getName_en() + " " + productModel.getPrice() + " " + getString(R.string.rsa));

                } else {
                    tv_details.setText(productModel.getDescription_en() + "\n" + " " + productModel.getPrice() + " " + getString(R.string.rsa));

                }
            }
        }

        if (productModel.getHas_sizes() == 1) {
            ll_container.setVisibility(View.VISIBLE);
        } else {
            ll_container.setVisibility(View.GONE);

        }


        if (productModel.getDegrees().getMyopia().size() == 0 && productModel.getDegrees().getAxis().size() == 0 && productModel.getDegrees().getDeviation().size() == 0) {
            ll_add_cart.setVisibility(View.GONE);
            ll_container.setVisibility(View.GONE);
            checkbox.setVisibility(View.GONE);
        } else {
            checkbox.setVisibility(View.VISIBLE);

            ll_add_cart.setVisibility(View.VISIBLE);
            ll_container.setVisibility(View.VISIBLE);
            ll_counter_1.setVisibility(View.VISIBLE);
            ll_counter_2.setVisibility(View.VISIBLE);


            if (productModel.getDegrees().getMyopia().size() > 0) {
                ll_degree_1.setVisibility(View.VISIBLE);
                ll_degree_2.setVisibility(View.VISIBLE);

                degreeList.add(getString(R.string.choose2));
                hasDegree = true;
            } else {
                hasDegree = false;
                ll_degree_1.setVisibility(View.GONE);
                ll_degree_2.setVisibility(View.GONE);
            }

            if (productModel.getDegrees().getAxis().size() > 0) {
                ll_axis_1.setVisibility(View.VISIBLE);
                ll_axis_2.setVisibility(View.VISIBLE);

                axisList.add(getString(R.string.choose2));
                hasAxis = true;

            } else {
                ll_axis_1.setVisibility(View.GONE);
                ll_axis_2.setVisibility(View.GONE);

                hasAxis = false;

            }

            if (productModel.getDegrees().getDeviation().size() > 0) {
                ll_deviation_1.setVisibility(View.VISIBLE);
                ll_deviation_2.setVisibility(View.VISIBLE);

                deviationDegreeList.add(getString(R.string.choose2));
                hasDeviation = true;


            } else {
                ll_deviation_1.setVisibility(View.GONE);
                ll_deviation_2.setVisibility(View.GONE);
                hasDeviation = false;

            }

        }




        /*if (productModel.getType() == 2) {
            ll_deviation.setVisibility(View.VISIBLE);
            ll_2_axis_deviation.setVisibility(View.VISIBLE);
        } else {
            ll_deviation.setVisibility(View.GONE);
            ll_2_axis_deviation.setVisibility(View.GONE);
        }*/

        UpdateAllSpinnerAdapter(productModel);


    }

    private void CheckData() {
        if (similar_eye == 1) {


            if (hasDegree && hasAxis && hasDeviation) {
                if (!TextUtils.isEmpty(degree_1_left_right) && !TextUtils.isEmpty(deviation_degree_1_left_right) && !TextUtils.isEmpty(axis_1_left_right)) {
                    AddItemToCart(degree_1_left_right, degree_1_left_right, axis_1_left_right, axis_1_left_right, deviation_degree_1_left_right, deviation_degree_1_left_right);
                } else {

                    if (TextUtils.isEmpty(degree_1_left_right)) {
                        Common.CreateSignAlertDialog(activity, getString(R.string.Choose_deg));

                    } else if (TextUtils.isEmpty(deviation_degree_1_left_right)) {
                        Common.CreateSignAlertDialog(activity, getString(R.string.ch_dev_deg));

                    } else {
                        Common.CreateSignAlertDialog(activity, getString(R.string.ch_ax));

                    }


                }


            } else if (hasDegree && hasAxis && !hasDeviation) {

                if (!TextUtils.isEmpty(degree_1_left_right) && !TextUtils.isEmpty(axis_1_left_right)) {
                    AddItemToCart(degree_1_left_right, degree_1_left_right, axis_1_left_right, axis_1_left_right, "", "");

                } else {
                    if (TextUtils.isEmpty(degree_1_left_right)) {
                        Common.CreateSignAlertDialog(activity, getString(R.string.Choose_deg));

                    } else if (TextUtils.isEmpty(axis_1_left_right)) {
                        Common.CreateSignAlertDialog(activity, getString(R.string.ch_ax));

                    }
                }


            } else if (hasDegree && !hasAxis && hasDeviation) {

                if (!TextUtils.isEmpty(degree_1_left_right) && !TextUtils.isEmpty(deviation_degree_1_left_right)) {

                    AddItemToCart(degree_1_left_right, degree_1_left_right, "", "", deviation_degree_1_left_right, deviation_degree_1_left_right);


                } else {
                    if (TextUtils.isEmpty(degree_1_left_right)) {
                        Common.CreateSignAlertDialog(activity, getString(R.string.Choose_deg));

                    } else if (TextUtils.isEmpty(deviation_degree_1_left_right)) {
                        Common.CreateSignAlertDialog(activity, getString(R.string.ch_dev_deg));

                    }
                }


            } else if (hasDegree && !hasAxis && !hasDeviation) {


                if (!TextUtils.isEmpty(degree_1_left_right)) {

                    AddItemToCart(degree_1_left_right, degree_1_left_right, "", "", "", "");

                } else {

                    if (TextUtils.isEmpty(degree_1_left_right)) {
                        Common.CreateSignAlertDialog(activity, getString(R.string.Choose_deg));

                    }


                }


            } else if (!hasDegree && hasAxis && hasDeviation) {

                if (!TextUtils.isEmpty(deviation_degree_1_left_right) && !TextUtils.isEmpty(axis_1_left_right)) {

                    AddItemToCart("", "", axis_1_left_right, axis_1_left_right, deviation_degree_1_left_right, deviation_degree_1_left_right);

                } else {

                    if (TextUtils.isEmpty(deviation_degree_1_left_right)) {
                        Common.CreateSignAlertDialog(activity, getString(R.string.ch_dev_deg));

                    } else {
                        Common.CreateSignAlertDialog(activity, getString(R.string.ch_ax));

                    }


                }


            } else if (!hasDegree && hasAxis && !hasDeviation) {

                if (!TextUtils.isEmpty(axis_1_left_right)) {
                    AddItemToCart("", "", axis_1_left_right, axis_1_left_right, "", "");
                } else {

                    Common.CreateSignAlertDialog(activity, getString(R.string.ch_ax));


                }


            } else if (!hasDegree && !hasAxis && hasDeviation) {

                if (!TextUtils.isEmpty(deviation_degree_1_left_right)) {
                    AddItemToCart("", "", "", "", deviation_degree_1_left_right, deviation_degree_1_left_right);

                } else {

                    Common.CreateSignAlertDialog(activity, getString(R.string.ch_dev_deg));


                }


            }

        } else if (similar_eye == 2) {


            if (hasDegree && hasAxis && hasDeviation) {
                if (!TextUtils.isEmpty(degree_2_left) && !TextUtils.isEmpty(degree_2_right) && !TextUtils.isEmpty(deviation_degree_left) && !TextUtils.isEmpty(deviation_degree_right) && !TextUtils.isEmpty(axis_left) && !TextUtils.isEmpty(axis_right)) {
                    AddItemToCart(degree_2_left, degree_2_right, axis_left, axis_right, deviation_degree_left, deviation_degree_right);
                } else {

                    if (TextUtils.isEmpty(degree_2_left) || TextUtils.isEmpty(degree_2_right) || (TextUtils.isEmpty(degree_2_left) && TextUtils.isEmpty(degree_2_right))) {
                        Common.CreateSignAlertDialog(activity, getString(R.string.Choose_deg));

                    } else if (TextUtils.isEmpty(deviation_degree_left) || TextUtils.isEmpty(deviation_degree_right) || (TextUtils.isEmpty(deviation_degree_left) && TextUtils.isEmpty(deviation_degree_right))) {
                        Common.CreateSignAlertDialog(activity, getString(R.string.ch_dev_deg));

                    } else if (TextUtils.isEmpty(axis_left) || TextUtils.isEmpty(axis_right) || (TextUtils.isEmpty(axis_left) && TextUtils.isEmpty(axis_right))) {
                        Common.CreateSignAlertDialog(activity, getString(R.string.ch_ax));

                    }


                }


            } else if (hasDegree && hasAxis && !hasDeviation) {

                if (!TextUtils.isEmpty(degree_2_left) && !TextUtils.isEmpty(degree_2_right) && !TextUtils.isEmpty(axis_left) && !TextUtils.isEmpty(axis_right)) {
                    AddItemToCart(degree_2_left, degree_2_right, axis_left, axis_right, "", "");
                } else {

                    if (TextUtils.isEmpty(degree_2_left) || TextUtils.isEmpty(degree_2_right) || (TextUtils.isEmpty(degree_2_left) && TextUtils.isEmpty(degree_2_right))) {
                        Common.CreateSignAlertDialog(activity, getString(R.string.Choose_deg));


                    } else if (TextUtils.isEmpty(axis_left) || TextUtils.isEmpty(axis_right) || (TextUtils.isEmpty(axis_left) && TextUtils.isEmpty(axis_right))) {
                        Common.CreateSignAlertDialog(activity, getString(R.string.ch_ax));

                    }


                }


            } else if (hasDegree && !hasAxis && hasDeviation) {


                if (!TextUtils.isEmpty(degree_2_left) && !TextUtils.isEmpty(degree_2_right) && !TextUtils.isEmpty(deviation_degree_left) && !TextUtils.isEmpty(deviation_degree_right)) {
                    AddItemToCart(degree_2_left, degree_2_right, "", "", deviation_degree_left, deviation_degree_right);
                } else {

                    if (TextUtils.isEmpty(degree_2_left) || TextUtils.isEmpty(degree_2_right) || (TextUtils.isEmpty(degree_2_left) && TextUtils.isEmpty(degree_2_right))) {
                        Common.CreateSignAlertDialog(activity, getString(R.string.Choose_deg));

                    } else if (TextUtils.isEmpty(deviation_degree_left) || TextUtils.isEmpty(deviation_degree_right) || (TextUtils.isEmpty(deviation_degree_left) && TextUtils.isEmpty(deviation_degree_right))) {
                        Common.CreateSignAlertDialog(activity, getString(R.string.ch_dev_deg));

                    }


                }


            } else if (hasDegree && !hasAxis && !hasDeviation) {


                if (!TextUtils.isEmpty(degree_2_left) && !TextUtils.isEmpty(degree_2_right)) {
                    AddItemToCart(degree_2_left, degree_2_right, "", "", "", "");
                } else {

                    Common.CreateSignAlertDialog(activity, getString(R.string.Choose_deg));


                }


            } else if (!hasDegree && hasAxis && hasDeviation) {


                if (!TextUtils.isEmpty(deviation_degree_left) && !TextUtils.isEmpty(deviation_degree_right) && !TextUtils.isEmpty(axis_left) && !TextUtils.isEmpty(axis_right)) {
                    AddItemToCart("", "", axis_left, axis_right, deviation_degree_left, deviation_degree_right);
                } else {

                    if (TextUtils.isEmpty(deviation_degree_left) || TextUtils.isEmpty(deviation_degree_right) || (TextUtils.isEmpty(deviation_degree_left) && TextUtils.isEmpty(deviation_degree_right))) {
                        Common.CreateSignAlertDialog(activity, getString(R.string.ch_dev_deg));

                    } else if (TextUtils.isEmpty(axis_left) || TextUtils.isEmpty(axis_right) || (TextUtils.isEmpty(axis_left) && TextUtils.isEmpty(axis_right))) {
                        Common.CreateSignAlertDialog(activity, getString(R.string.ch_ax));

                    }


                }


            } else if (!hasDegree && hasAxis && !hasDeviation) {


                if (!TextUtils.isEmpty(deviation_degree_right) && !TextUtils.isEmpty(axis_left) && !TextUtils.isEmpty(axis_right)) {
                    AddItemToCart("", "", axis_left, axis_right, "", "");
                } else {

                    Common.CreateSignAlertDialog(activity, getString(R.string.ch_ax));


                }


            } else if (!hasDegree && !hasAxis && hasDeviation) {


                if (!TextUtils.isEmpty(deviation_degree_left) && !TextUtils.isEmpty(deviation_degree_right)) {
                    AddItemToCart("", "", "", "", deviation_degree_left, deviation_degree_right);
                } else {

                    Common.CreateSignAlertDialog(activity, getString(R.string.ch_dev_deg));


                }


            }


        }


    }

    private void AddItemToCart(String left_degree, String right_degree, String left_axis, String right_axis, String left_deviation, String right_deviation) {

        ItemCartModel itemCartModel;
        if (productModel.getFeatured() == 0) {

            if (similar_eye == 1) {
                double total = counter_left_right * productModel.getPrice();

                if (productModel.getImages().size() > 0) {
                    itemCartModel = new ItemCartModel(productModel.getId(), productModel.getImages().get(0), productModel.getName_ar(), productModel.getName_en(), productModel.getPrice(), counter_left_right, total, Tags.ISSIMILAR, left_degree, right_degree, left_deviation, right_deviation, left_axis, right_axis, counter_left_right, counter_left_right, Tags.PRODUCT_TYPE_LENSES, counter_left_right, counter_left_right);

                } else {

                    itemCartModel = new ItemCartModel(productModel.getId(), "", productModel.getName_ar(), productModel.getName_en(), productModel.getPrice(), counter_left_right, total, Tags.ISSIMILAR, left_degree, right_degree, left_deviation, right_deviation, left_axis, right_axis, counter_left_right, counter_left_right, Tags.PRODUCT_TYPE_LENSES, counter_left_right, counter_left_right);

                }
            } else {
                double total = (counter_left * productModel.getPrice()) + (counter_right * productModel.getPrice());
                int total_amount = counter_left + counter_right;
                if (productModel.getImages().size() > 0) {
                    itemCartModel = new ItemCartModel(productModel.getId(), productModel.getImages().get(0), productModel.getName_ar(), productModel.getName_en(), productModel.getPrice(), total_amount, total, Tags.ISSIMILAR, left_degree, right_degree, left_deviation, right_deviation, left_axis, right_axis, counter_left, counter_right, Tags.PRODUCT_TYPE_LENSES, counter_left, counter_right);

                } else {

                    itemCartModel = new ItemCartModel(productModel.getId(), "", productModel.getName_ar(), productModel.getName_en(), productModel.getPrice(), total_amount, total, Tags.ISSIMILAR, left_degree, right_degree, left_deviation, right_deviation, left_axis, right_axis, counter_left, counter_right, Tags.PRODUCT_TYPE_LENSES, counter_left, counter_right);

                }
            }

        } else {


            if (similar_eye == 1) {
                double total = counter_left_right * productModel.getPrice_after_discount();

                if (productModel.getImages().size() > 0) {
                    itemCartModel = new ItemCartModel(productModel.getId(), productModel.getImages().get(0), productModel.getName_ar(), productModel.getName_en(), productModel.getPrice_after_discount(), counter_left_right, total, Tags.ISSIMILAR, left_degree, right_degree, left_deviation, right_deviation, left_axis, right_axis, counter_left_right, counter_left_right, Tags.PRODUCT_TYPE_LENSES, counter_left_right, counter_left_right);

                } else {

                    itemCartModel = new ItemCartModel(productModel.getId(), "", productModel.getName_ar(), productModel.getName_en(), productModel.getPrice_after_discount(), counter_left_right, total, Tags.ISSIMILAR, left_degree, right_degree, left_deviation, right_deviation, left_axis, right_axis, counter_left_right, counter_left_right, Tags.PRODUCT_TYPE_LENSES, counter_left_right, counter_left_right);

                }
            } else {
                double total = (counter_left * productModel.getPrice_after_discount()) + (counter_right * productModel.getPrice_after_discount());
                int total_amount = counter_left + counter_right;
                if (productModel.getImages().size() > 0) {
                    itemCartModel = new ItemCartModel(productModel.getId(), productModel.getImages().get(0), productModel.getName_ar(), productModel.getName_en(), productModel.getPrice_after_discount(), total_amount, total, Tags.ISSIMILAR, left_degree, right_degree, left_deviation, right_deviation, left_axis, right_axis, counter_left, counter_right, Tags.PRODUCT_TYPE_LENSES, counter_left, counter_right);

                } else {

                    itemCartModel = new ItemCartModel(productModel.getId(), "", productModel.getName_ar(), productModel.getName_en(), productModel.getPrice_after_discount(), total_amount, total, Tags.ISSIMILAR, left_degree, right_degree, left_deviation, right_deviation, left_axis, right_axis, counter_left, counter_right, Tags.PRODUCT_TYPE_LENSES, counter_left, counter_right);

                }
            }


        }

        orderCartSingleTone.Add_Update_Item(itemCartModel);
        int total_item_cart = orderCartSingleTone.getItemsCount();
        activity.UpdateCartCounter(total_item_cart);
        CreateAlertDialog();

    }

    public void CreateAlertDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(activity)
                .setCancelable(true)
                .create();

        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_continue_goto_cart, null);
        Button btn_continue = view.findViewById(R.id.btn_continue);
        Button btn_payment = view.findViewById(R.id.btn_payment);

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                activity.removeFragmentLensesDetails_AccessoriesDetails_DisplayFragmentCart();
            }
        });

        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_window_bg);
        dialog.setView(view);
        dialog.show();
    }


    private void increase_2_left_eye_counter() {
        counter_left += 1;
        tv_counter_2_left.setText(String.valueOf(counter_left));

    }

    private void decrease_2_left_eye_counter() {

        if (counter_left > 1) {
            counter_left -= 1;
            tv_counter_2_left.setText(String.valueOf(counter_left));
        }
    }

    private void increase_2_right_eye_counter() {
        counter_right += 1;
        tv_counter_2_right.setText(String.valueOf(counter_right));
    }

    private void decrease_2_right_eye_counter() {
        if (counter_right > 1) {
            counter_right -= 1;
            tv_counter_2_right.setText(String.valueOf(counter_right));
        }
    }

    private void decrease_1_left_right_eye_counter() {
        if (counter_left_right > 1) {
            counter_left_right -= 1;
            tv_counter_1_left_right.setText(String.valueOf(counter_left_right));
        }
    }

    private void increase_1_left_right_eye_counter() {
        counter_left_right += 1;
        tv_counter_1_left_right.setText(String.valueOf(counter_left_right));
    }


    private class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (pager_slider.getCurrentItem() < pager_slider.getAdapter().getCount() - 1) {
                        pager_slider.setCurrentItem(pager_slider.getCurrentItem() + 1);
                    } else {
                        pager_slider.setCurrentItem(0);

                    }
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        if (timer != null) {
            timer.purge();
            timer.cancel();
        }

        if (timerTask != null) {
            timerTask.cancel();
        }
        super.onDestroyView();
    }
}
