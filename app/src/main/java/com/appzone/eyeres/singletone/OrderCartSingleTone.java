package com.appzone.eyeres.singletone;

import android.util.Log;

import com.appzone.eyeres.models.ItemCartModel;

import java.util.ArrayList;
import java.util.List;

public class OrderCartSingleTone {

    private static OrderCartSingleTone instance = null;
    private List<ItemCartModel> itemCartModelList = new ArrayList<>();

    private OrderCartSingleTone() {
    }

    public static synchronized OrderCartSingleTone newInstance() {
        if (instance == null) {
            instance = new OrderCartSingleTone();
        }
        return instance;
    }


    public void Add_Update_Item(ItemCartModel itemCartModel)
    {

        if (itemCartModel!=null)
        {
            if (getItemPosition(itemCartModel) == -1)
            {
                itemCartModelList.add(itemCartModel);

            }else
            {
                int pos = getItemPosition(itemCartModel);
                double oldTotal = itemCartModelList.get(pos).getTotal();
                int oldQuantity = itemCartModelList.get(pos).getQuantity();

                int totalQuantity = oldQuantity+itemCartModel.getQuantity();
                double totalCost = oldTotal+itemCartModel.getTotal();

                itemCartModel.setQuantity(totalQuantity);
                itemCartModel.setTotal(totalCost);

                itemCartModelList.set(pos,itemCartModel);
                Log.e("2","2");


            }
        }

    }
    private int getItemPosition(ItemCartModel itemCartModel)
    {

        int pos = -1;
        if (itemCartModelList.size() > 0) {

            for (int index = 0 ; index<itemCartModelList.size();index++)
            {
                ItemCartModel item = itemCartModelList.get(index);


                if (item.getProduct_id() == itemCartModel.getProduct_id())
                {
                    pos = index;
                    break;
                }

            }
        }
        return pos;
    }

    public void Update_Item(int pos,ItemCartModel itemCartModel)
    {
        if (itemCartModelList.size()>0)
        {
            if (itemCartModel!=null)
            {
                itemCartModelList.set(pos,itemCartModel);
                Log.e("3","3");
            }
        }


    }

    public void Delete_Item(int pos)
    {
        getItemCartModelList().remove(pos);
    }
    public List<ItemCartModel> getItemCartModelList() {
        return itemCartModelList;
    }

    public int getItemsCount() {
        return itemCartModelList.size();
    }

    public void clear() {
        itemCartModelList.clear();


    }

    public void setItemCartModelList(List<ItemCartModel> itemCartModelList)
    {
        this.itemCartModelList.addAll(itemCartModelList);
    }
}
