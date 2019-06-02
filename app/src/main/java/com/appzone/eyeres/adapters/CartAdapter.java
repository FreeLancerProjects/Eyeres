package com.appzone.eyeres.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appzone.eyeres.R;
import com.appzone.eyeres.activities_fragments.activity_home.fragments.fragment_cart.Fragment_Cart;
import com.appzone.eyeres.models.ItemCartModel;
import com.appzone.eyeres.tags.Tags;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class CartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ITEM_LENSES = 1;
    private final int ITEM_ACCESSORIES = 2;

    private List<ItemCartModel> itemCartModelList;
    private Context context;
    private Fragment_Cart fragment;
    private String current_language;

    public CartAdapter(List<ItemCartModel> itemCartModelList, Context context,Fragment_Cart fragment) {
        this.itemCartModelList = itemCartModelList;
        this.context = context;
        this.fragment = fragment;
        Paper.init(context);
        current_language = Paper.book().read("lang", Locale.getDefault().getLanguage());


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == ITEM_LENSES)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.cart_row, parent, false);
            return new MyHolder(view);
        }else
            {
                View view = LayoutInflater.from(context).inflate(R.layout.cart_row_accessories, parent, false);
                return new MyHolderAccessories(view);
            }


    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyHolder)
        {
            final MyHolder l_holder = (MyHolder) holder;

            ItemCartModel itemCartModel = itemCartModelList.get(position);
            l_holder.BindData(itemCartModel);
            l_holder.image_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragment.Delete(l_holder.getAdapterPosition());
                }
            });

            l_holder.image_increase.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ItemCartModel itemCartModel = itemCartModelList.get(l_holder.getAdapterPosition());
                    int counter = Integer.parseInt(l_holder.tv_amount.getText().toString().trim()) + 1;
                    l_holder.tv_amount.setText(String.valueOf(counter));
                    itemCartModel.setQuantity(counter);
                    double item_total_cost = counter * (itemCartModel.getBase_amount_left() + itemCartModel.getBase_amount_right()) * itemCartModel.getProduct_cost();
                    itemCartModel.setTotal(item_total_cost);

                    l_holder.tv_cost.setText(item_total_cost + " " + context.getString(R.string.rsa));

                    l_holder.tv_left_eye_amount.setText((itemCartModel.getBase_amount_left() * counter) + "");
                    l_holder.tv_right_eye_amount.setText((itemCartModel.getBase_amount_right() * counter) + "");

                    itemCartModel.setLeft_amount((itemCartModel.getBase_amount_left() * counter));
                    itemCartModel.setRight_amount((itemCartModel.getBase_amount_right() * counter));


                    fragment.UpdateList_Increase_Decrease(l_holder.getAdapterPosition(), itemCartModel);
                }
            });

            l_holder.image_decrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (Integer.parseInt(l_holder.tv_amount.getText().toString().trim()) > 1) {
                        ItemCartModel itemCartModel = itemCartModelList.get(l_holder.getAdapterPosition());
                        int counter = Integer.parseInt(l_holder.tv_amount.getText().toString().trim()) - 1;
                        l_holder.tv_amount.setText(String.valueOf(counter));
                        itemCartModel.setQuantity(counter);
                        double item_total_cost = counter * (itemCartModel.getBase_amount_left() + itemCartModel.getBase_amount_right()) * itemCartModel.getProduct_cost();
                        itemCartModel.setTotal(item_total_cost);

                        l_holder.tv_cost.setText(item_total_cost + " " + context.getString(R.string.rsa));

                        l_holder.tv_left_eye_amount.setText((itemCartModel.getBase_amount_left() * counter) + "");
                        l_holder.tv_right_eye_amount.setText((itemCartModel.getBase_amount_right() * counter) + "");

                        itemCartModel.setLeft_amount((itemCartModel.getBase_amount_left() * counter));
                        itemCartModel.setRight_amount((itemCartModel.getBase_amount_right() * counter));

                        fragment.UpdateList_Increase_Decrease(l_holder.getAdapterPosition(), itemCartModel);

                    }


                }
            });
        }else if (holder instanceof MyHolderAccessories)
        {
            final MyHolderAccessories l_holder = (MyHolderAccessories) holder;

            ItemCartModel itemCartModel = itemCartModelList.get(position);
            l_holder.BindData(itemCartModel);
            l_holder.image_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragment.Delete(l_holder.getAdapterPosition());
                }
            });

            l_holder.image_increase.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ItemCartModel itemCartModel = itemCartModelList.get(l_holder.getAdapterPosition());
                    int counter = Integer.parseInt(l_holder.tv_amount.getText().toString().trim()) + 1;
                    l_holder.tv_amount.setText(String.valueOf(counter));
                    itemCartModel.setQuantity(counter);
                    double item_total_cost = counter * itemCartModel.getProduct_cost();
                    itemCartModel.setTotal(item_total_cost);

                    l_holder.tv_cost.setText(item_total_cost + " " + context.getString(R.string.rsa));

                    fragment.UpdateList_Increase_Decrease(l_holder.getAdapterPosition(), itemCartModel);
                }
            });

            l_holder.image_decrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (Integer.parseInt(l_holder.tv_amount.getText().toString().trim()) > 1) {
                        ItemCartModel itemCartModel = itemCartModelList.get(l_holder.getAdapterPosition());
                        int counter = Integer.parseInt(l_holder.tv_amount.getText().toString().trim()) - 1;
                        l_holder.tv_amount.setText(String.valueOf(counter));
                        itemCartModel.setQuantity(counter);
                        double item_total_cost = counter *  itemCartModel.getProduct_cost();
                        itemCartModel.setTotal(item_total_cost);

                        l_holder.tv_cost.setText(item_total_cost + " " + context.getString(R.string.rsa));

                        fragment.UpdateList_Increase_Decrease(l_holder.getAdapterPosition(), itemCartModel);

                    }


                }
            });
        }



    }

    @Override
    public int getItemCount() {
        return itemCartModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private ImageView image,image_delete,image_increase,image_decrease;
        private TextView tv_cost,tv_name,tv_amount,tv_left_eye_amount,tv_right_eye_amount;

        public MyHolder(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            image_delete = itemView.findViewById(R.id.image_delete);
            image_increase = itemView.findViewById(R.id.image_increase);
            image_decrease = itemView.findViewById(R.id.image_decrease);

            tv_cost = itemView.findViewById(R.id.tv_cost);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_amount = itemView.findViewById(R.id.tv_amount);

            tv_left_eye_amount = itemView.findViewById(R.id.tv_left_eye_amount);
            tv_right_eye_amount = itemView.findViewById(R.id.tv_right_eye_amount);

        }

        public void BindData(ItemCartModel itemCartModel) {


            if (current_language.equals("ar"))
            {
                tv_name.setText(itemCartModel.getProduct_name_ar());
            }else
                {
                    tv_name.setText(itemCartModel.getProduct_name_en());

                }
            double total_cost = (itemCartModel.getLeft_amount()+itemCartModel.getRight_amount())*itemCartModel.getProduct_cost();

            tv_amount.setText("1");
            tv_cost.setText(total_cost+" "+ context.getString(R.string.rsa));


            tv_left_eye_amount.setText(String.valueOf(itemCartModel.getLeft_amount()));
            tv_right_eye_amount.setText(String.valueOf(itemCartModel.getRight_amount()));

            new MyAsyncTask().execute(itemCartModel.getProduct_image());
        }

        public class MyAsyncTask extends AsyncTask<String,Void,Bitmap> {
            @Override
            protected Bitmap doInBackground(String... strings) {
                Bitmap bitmap = null;
                try {
                    bitmap = Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL+strings[0])).priority(Picasso.Priority.HIGH).transform(new Transformation() {
                        @Override
                        public Bitmap transform(Bitmap source) {
                            int size = Math.min(source.getWidth(), source.getHeight());
                            int x = (source.getWidth() - size) / 2;
                            int y = (source.getHeight() - size) / 2;
                            Bitmap result = Bitmap.createBitmap(source, x, y, size, size);
                            if (result != source) {
                                source.recycle();
                            }
                            return result;

                        }

                        @Override
                        public String key() {
                            return "square()";
                        }
                    }).get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                image.setImageBitmap(bitmap);
            }
        }

    }
    public class MyHolderAccessories extends RecyclerView.ViewHolder {
        private ImageView image,image_delete,image_increase,image_decrease;
        private TextView tv_cost,tv_name,tv_amount;

        public MyHolderAccessories(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            image_delete = itemView.findViewById(R.id.image_delete);
            image_increase = itemView.findViewById(R.id.image_increase);
            image_decrease = itemView.findViewById(R.id.image_decrease);

            tv_cost = itemView.findViewById(R.id.tv_cost);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_amount = itemView.findViewById(R.id.tv_amount);


        }

        public void BindData(ItemCartModel itemCartModel) {


            if (current_language.equals("ar"))
            {
                tv_name.setText(itemCartModel.getProduct_name_ar());
            }else
            {
                tv_name.setText(itemCartModel.getProduct_name_en());

            }
            double total_cost = itemCartModel.getQuantity()*itemCartModel.getProduct_cost();

            tv_amount.setText(itemCartModel.getQuantity()+"");
            tv_cost.setText(total_cost+" "+ context.getString(R.string.rsa));

            new MyAsyncTask().execute(itemCartModel.getProduct_image());
        }

        public class MyAsyncTask extends AsyncTask<String,Void,Bitmap> {
            @Override
            protected Bitmap doInBackground(String... strings) {
                Bitmap bitmap = null;
                try {
                    bitmap = Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL+strings[0])).priority(Picasso.Priority.HIGH).transform(new Transformation() {
                        @Override
                        public Bitmap transform(Bitmap source) {
                            int size = Math.min(source.getWidth(), source.getHeight());
                            int x = (source.getWidth() - size) / 2;
                            int y = (source.getHeight() - size) / 2;
                            Bitmap result = Bitmap.createBitmap(source, x, y, size, size);
                            if (result != source) {
                                source.recycle();
                            }
                            return result;

                        }

                        @Override
                        public String key() {
                            return "square()";
                        }
                    }).get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                image.setImageBitmap(bitmap);
            }
        }

    }

    @Override
    public int getItemViewType(int position) {
        ItemCartModel itemCartModel = itemCartModelList.get(position);

        if (itemCartModel.getProduct_type()==Tags.PRODUCT_TYPE_LENSES)
        {
            return ITEM_LENSES;

        }else
            {
                return ITEM_ACCESSORIES;
            }

    }
}
