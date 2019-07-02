package com.appzone.eyeres.activities_fragments.activity_home.fragments.fragment_cart;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appzone.eyeres.R;
import com.appzone.eyeres.activities_fragments.activity_home.activity.HomeActivity;
import com.appzone.eyeres.adapters.CartAdapter;
import com.appzone.eyeres.models.ItemCartModel;
import com.appzone.eyeres.preferences.Preferences;
import com.appzone.eyeres.singletone.OrderCartSingleTone;
import com.appzone.eyeres.tags.Tags;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Fragment_Cart extends Fragment{
    private TextView tv_cost;
    private LinearLayout ll_empty_cart,ll_cost_container;
    private CardView card_continue,card_back;
    private ImageView arrow1,arrow2;
    private RecyclerView recView;
    private RecyclerView.LayoutManager manager;
    private CartAdapter adapter;
    private HomeActivity activity;
    private String current_language;
    private OrderCartSingleTone orderCartSingleTone;
    private List<ItemCartModel> itemCartModelList;
    private double total_cost=0.0;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cart,container,false);
        initView(view);
        return view;
    }

    public static Fragment_Cart newInstance()
    {
        return new Fragment_Cart();
    }
    private void initView(View view)
    {
        activity = (HomeActivity) getActivity();
        Paper.init(activity);
        current_language = Paper.book().read("lang",Locale.getDefault().getLanguage());

        orderCartSingleTone = OrderCartSingleTone.newInstance();
        arrow1 = view.findViewById(R.id.arrow1);
        arrow2 = view.findViewById(R.id.arrow2);

        if (current_language.equals("ar"))
        {
            arrow1.setImageResource(R.drawable.white_right_arrow);
            arrow2.setImageResource(R.drawable.white_left_arrow);

        }else
        {
            arrow1.setImageResource(R.drawable.white_left_arrow);
            arrow2.setImageResource(R.drawable.white_right_arrow);

        }
        ll_empty_cart = view.findViewById(R.id.ll_empty_cart);
        ll_cost_container = view.findViewById(R.id.ll_cost_container);

        card_continue = view.findViewById(R.id.card_continue);
        card_back = view.findViewById(R.id.card_back);


        tv_cost = view.findViewById(R.id.tv_cost);

        recView = view.findViewById(R.id.recView);
        manager = new LinearLayoutManager(getActivity());
        recView.setLayoutManager(manager);
        recView.setHasFixedSize(true);
        recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
        recView.setDrawingCacheEnabled(true);
        recView.setItemViewCacheSize(25);



        card_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.Back();
            }
        });
        card_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.SaveItemsData(itemCartModelList,total_cost);
                activity.DisplayFragmentClientData(total_cost);
            }
        });

        updateUI();

    }
    private void updateUI()
    {
        itemCartModelList = new ArrayList<>();
        itemCartModelList.addAll(orderCartSingleTone.getItemCartModelList());




        if (itemCartModelList.size()>0)
        {
            ll_empty_cart.setVisibility(View.GONE);
            ll_cost_container.setVisibility(View.VISIBLE);
            adapter = new CartAdapter(itemCartModelList,activity,this);
            recView.setAdapter(adapter);

        }else
            {
                ll_empty_cart.setVisibility(View.VISIBLE);
                ll_cost_container.setVisibility(View.GONE);
            }

            UpdateCost(getOrderItemsCost(itemCartModelList));

    }
    private void UpdateCost(double cost)
    {
        this.total_cost = cost;
        tv_cost.setText(getString(R.string.products_cost)+" "+cost+" "+getString(R.string.rsa));
    }
    private double getOrderItemsCost(List<ItemCartModel> itemCartModelList)
    {
        double cost = 0.0;
        for (ItemCartModel itemCartModel : itemCartModelList)
        {
            if (itemCartModel.getProduct_type() == Tags.PRODUCT_TYPE_LENSES)
            {
                cost += (itemCartModel.getLeft_amount()+itemCartModel.getRight_amount())*itemCartModel.getProduct_cost();

            }else
                {
                    cost += itemCartModel.getQuantity()*itemCartModel.getProduct_cost();

                }
        }

        return cost;
    }

    public void Delete(int pos)
    {
        Log.e("pos",pos+"_");
        itemCartModelList.remove(pos);
        adapter.notifyItemRemoved(pos);
        orderCartSingleTone.Delete_Item(pos);
        if (itemCartModelList.size()==0)
        {
            ll_empty_cart.setVisibility(View.VISIBLE);
            ll_cost_container.setVisibility(View.GONE);
            clearCart();
        }
        activity.UpdateCartCounter(itemCartModelList.size());

    }

    public void UpdateList_Increase_Decrease(int pos,ItemCartModel itemCartModel)
    {

        itemCartModelList.set(pos,itemCartModel);
        orderCartSingleTone.Update_Item(pos,itemCartModel);
        UpdateCost(getOrderItemsCost(itemCartModelList));
    }

    public void clearCart()
    {
        itemCartModelList.clear();
        orderCartSingleTone.clear();
        Preferences.getInstance().clear_cart(activity);
        ll_empty_cart.setVisibility(View.VISIBLE);
        ll_cost_container.setVisibility(View.GONE);

    }
}
