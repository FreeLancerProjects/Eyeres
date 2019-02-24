package com.appzone.eyeres.singletone;

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
                itemCartModelList.set(pos,itemCartModel);


            }
        }

    }
    private int getItemPosition(ItemCartModel itemCartModel)
    {

        int pos = -1;
        if (itemCartModelList.size() > 0) {

            for (int index = 0 ; index<itemCartModelList.size()-1;index++)
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
            }
        }


    }
    public void Delete_Item(int pos)
    {
        itemCartModelList.remove(pos);

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
}
