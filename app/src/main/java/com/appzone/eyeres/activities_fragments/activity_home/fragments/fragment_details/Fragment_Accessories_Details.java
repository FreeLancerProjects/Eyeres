package com.appzone.eyeres.activities_fragments.activity_home.fragments.fragment_details;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appzone.eyeres.R;
import com.appzone.eyeres.activities_fragments.activity_home.activity.HomeActivity;
import com.appzone.eyeres.adapters.SliderAdapter;
import com.appzone.eyeres.models.ItemCartModel;
import com.appzone.eyeres.models.ProductDataModel;
import com.appzone.eyeres.singletone.OrderCartSingleTone;
import com.appzone.eyeres.tags.Tags;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import io.paperdb.Paper;

public class Fragment_Accessories_Details extends Fragment{
    private static String TAG="productModel";
    private LinearLayout ll_back;
    private TextView tv_name,tv_details,tv_counter;
    private ImageView arrow;
    private Button btn_add_to_cart;
    private FrameLayout fl_decrease,fl_increase;

    private ViewPager pager_slider;
    private TabLayout tab_slider;
    private SliderAdapter sliderAdapter;
    private ProductDataModel.ProductModel productModel;
    private String current_language;
    private HomeActivity activity;
    private Timer timer;
    private TimerTask timerTask;
    private int counter = 1;
    private ScaleAnimation scaleAnimation;
    private OrderCartSingleTone orderCartSingleTone;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_accessories_details,container,false);
        initView(view);
        return view;
    }

    public static Fragment_Accessories_Details newInstance(ProductDataModel.ProductModel productModel)
    {
        Fragment_Accessories_Details fragment_Lenses_details = new Fragment_Accessories_Details();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG,productModel);
        fragment_Lenses_details.setArguments(bundle);
        return fragment_Lenses_details;
    }
    private void initView(View view) {

        orderCartSingleTone =OrderCartSingleTone.newInstance();
        scaleAnimation = new ScaleAnimation(.2f,1.0f,.2f,1.0f,50,50);
        scaleAnimation.setDuration(300);
        activity = (HomeActivity) getActivity();
        arrow = view.findViewById(R.id.arrow);


        Paper.init(activity);
        current_language = Paper.book().read("lang", Locale.getDefault().getLanguage());


        if (current_language.equals("ar"))
        {
            arrow.setImageResource(R.drawable.white_right_arrow);
        }else
            {
                arrow.setImageResource(R.drawable.white_left_arrow);

            }

        ll_back = view.findViewById(R.id.ll_back);
        tv_counter = view.findViewById(R.id.tv_counter);
        btn_add_to_cart = view.findViewById(R.id.btn_add_to_cart);
        fl_decrease = view.findViewById(R.id.fl_decrease);
        fl_increase = view.findViewById(R.id.fl_increase);
        pager_slider = view.findViewById(R.id.pager_slider);
        tab_slider = view.findViewById(R.id.tab_slider);
        tv_name = view.findViewById(R.id.tv_name);
        tv_details = view.findViewById(R.id.tv_details);
        tab_slider.setupWithViewPager(pager_slider);

        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.Back();
            }
        });
        fl_increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fl_decrease.clearAnimation();
                fl_increase.clearAnimation();
                fl_increase.startAnimation(scaleAnimation);

                int c =counter+1;
                UpdateCounter(c);
            }
        });

        fl_decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fl_decrease.clearAnimation();
                fl_increase.clearAnimation();
                fl_decrease.startAnimation(scaleAnimation);

                if (counter>1)
                {
                    int c =counter-1;
                    UpdateCounter(c);
                }

            }
        });

        btn_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckData();
            }
        });
        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            productModel = (ProductDataModel.ProductModel) bundle.getSerializable(TAG);
            UpdateUI(productModel);
        }

    }



    private void UpdateUI(ProductDataModel.ProductModel productModel)
    {
        if (productModel.getImages().size()>0)
        {
            if (productModel.getImages().size()==1)
            {
                sliderAdapter = new SliderAdapter(productModel.getImages(),activity);
                pager_slider.setAdapter(sliderAdapter);

            }else
            {
                sliderAdapter = new SliderAdapter(productModel.getImages(),activity);
                pager_slider.setAdapter(sliderAdapter);
                timer = new Timer();
                timerTask = new MyTimerTask();
                timer.scheduleAtFixedRate(timerTask,6000,6000);

                for (int i = 0;i<tab_slider.getTabCount()-1;i++)
                {
                    View view = ((ViewGroup)tab_slider.getChildAt(0)).getChildAt(i);

                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                    params.setMargins(4,0,4,0);
                }
            }


        }


        if (productModel.getFeatured() == 0)
        {
            if (current_language.equals("ar"))
            {
                tv_name.setText(productModel.getName_ar());

                if (productModel.getBrand()!=null)
                {
                    tv_details.setText(productModel.getDescription_ar()+"\n"+productModel.getBrand().getName_ar()+" "+productModel.getPrice()+" "+getString(R.string.rsa));

                }else
                    {
                        tv_details.setText(productModel.getDescription_ar()+"\n"+productModel.getPrice()+" "+getString(R.string.rsa));

                    }
            }else
            {
                tv_name.setText(productModel.getName_en());
                if (productModel.getBrand()!=null)
                {
                    tv_details.setText(productModel.getDescription_en()+"\n"+productModel.getBrand().getName_en()+" "+productModel.getPrice()+" "+getString(R.string.rsa));

                }else
                    {
                        tv_details.setText(productModel.getDescription_en()+"\n"+productModel.getPrice()+" "+getString(R.string.rsa));

                    }

            }

        }else
            {
                if (current_language.equals("ar"))
                {
                    tv_name.setText(productModel.getName_ar());



                    tv_details.setText(productModel.getDescription_ar()+"\n"+productModel.getBrand().getName_ar()+" "+productModel.getPrice_after_discount()+" "+getString(R.string.rsa));
                }else
                {
                    tv_name.setText(productModel.getName_en());
                    tv_details.setText(productModel.getDescription_en()+"\n"+productModel.getBrand().getName_en()+" "+productModel.getPrice_after_discount()+" "+getString(R.string.rsa));

                }
            }




    }

    private void CheckData() {
        ItemCartModel itemCartModel;
        /////not offer price
        if (productModel.getFeatured() == 0) {
            double total = counter * productModel.getPrice();

            if (productModel.getImages().size() > 0) {
                itemCartModel = new ItemCartModel(productModel.getId(), productModel.getImages().get(0), productModel.getName_ar(), productModel.getName_en(), productModel.getPrice(), counter, total, Tags.ISSIMILAR, "0", "0","0","0","0","0", 0,0, Tags.PRODUCT_TYPE_ACCESSORIES,counter,counter);

            } else {

                itemCartModel = new ItemCartModel(productModel.getId(), "", productModel.getName_ar(), productModel.getName_en(), productModel.getPrice(), counter, total, Tags.ISSIMILAR, "0", "0", "0","0","0","0",0,0, Tags.PRODUCT_TYPE_ACCESSORIES,counter,counter);

            }
        } else {

            double total = counter * productModel.getPrice_after_discount();

            if (productModel.getImages().size() > 0) {
                itemCartModel = new ItemCartModel(productModel.getId(), productModel.getImages().get(0), productModel.getName_ar(), productModel.getName_en(), productModel.getPrice_after_discount(), counter, total, Tags.ISSIMILAR, "0", "0","0","0","0","0", 0, 0, Tags.PRODUCT_TYPE_ACCESSORIES,counter,counter);

            } else {
                itemCartModel = new ItemCartModel(productModel.getId(), "", productModel.getName_ar(), productModel.getName_en(), productModel.getPrice_after_discount(), counter, total, Tags.ISSIMILAR, "0", "0","0","0","0","0", 0, 0, Tags.PRODUCT_TYPE_ACCESSORIES,counter,counter);

            }

        }

        orderCartSingleTone.Add_Update_Item(itemCartModel);
        int total_item_cart = orderCartSingleTone.getItemsCount();
        activity.UpdateCartCounter(total_item_cart);
        CreateAlertDialog();

        //////11111111111111111111

    }


    private void UpdateCounter(int counter)
    {
        this.counter = counter;
        tv_counter.setText(String.valueOf(counter));
    }


    public void CreateAlertDialog()
    {
        final AlertDialog dialog = new AlertDialog.Builder(activity)
                .setCancelable(true)
                .create();

        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_continue_goto_cart,null);
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

        dialog.getWindow().getAttributes().windowAnimations=R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_window_bg);
        dialog.setView(view);
        dialog.show();
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
