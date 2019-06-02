package com.appzone.eyeres.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appzone.eyeres.R;
import com.appzone.eyeres.models.OrderDataModel;
import com.appzone.eyeres.tags.Tags;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class OrderDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ITEM_LENSES = 1;
    private final int ITEM_ACCESSORIES = 2;
    private List<OrderDataModel.ProductModel> productModelList;
    private Context context;
    private String current_language;

    public OrderDetailsAdapter(List<OrderDataModel.ProductModel> productModelList, Context context) {
        this.productModelList = productModelList;
        this.context = context;
        Paper.init(context);
        current_language = Paper.book().read("lang", Locale.getDefault().getLanguage());

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == ITEM_LENSES) {
            View view = LayoutInflater.from(context).inflate(R.layout.details_product_row, parent, false);
            return new MyHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.details_product_row_accessories, parent, false);
            return new MyHolderAccessories(view);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyHolder) {
            MyHolder myHolder = (MyHolder) holder;
            OrderDataModel.ProductModel productModel = productModelList.get(position);
            myHolder.BindData(productModel);
        } else if (holder instanceof MyHolderAccessories) {
            MyHolderAccessories myHolder = (MyHolderAccessories) holder;
            OrderDataModel.ProductModel productModel = productModelList.get(position);
            myHolder.BindData(productModel);
        }


    }

    @Override
    public int getItemCount() {
        return productModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView tv_cost, tv_name, tv_amount, tv_discount, tv_left_eye_amount, tv_right_eye_amount;

        public MyHolder(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            tv_cost = itemView.findViewById(R.id.tv_cost);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_amount = itemView.findViewById(R.id.tv_amount);
            tv_discount = itemView.findViewById(R.id.tv_discount);
            tv_left_eye_amount = itemView.findViewById(R.id.tv_left_eye_amount);
            tv_right_eye_amount = itemView.findViewById(R.id.tv_right_eye_amount);

        }

        public void BindData(OrderDataModel.ProductModel productModel) {

            if (current_language.equals("ar")) {
                tv_name.setText(productModel.getName_ar());
                tv_discount.setBackgroundResource(R.drawable.offer_bg_left);
            } else {
                tv_discount.setBackgroundResource(R.drawable.offer_bg_left);
                tv_name.setText(productModel.getName_en());

            }

            tv_amount.setText(String.valueOf(productModel.getQuantity()));
            tv_left_eye_amount.setText(String.valueOf(productModel.getLeft_amount()));
            tv_right_eye_amount.setText(String.valueOf(productModel.getRight_amount()));

            if (productModel.getFeatured() == 0) {
                double total_cost = (productModel.getLeft_amount()+productModel.getRight_amount())*productModel.getPrice();

                tv_discount.setVisibility(View.GONE);
                tv_cost.setText(total_cost + " " + context.getString(R.string.rsa));

            } else {
                double total_cost = (productModel.getLeft_amount()+productModel.getRight_amount())*productModel.getPrice_after_discount();

                tv_discount.setText(productModel.getDiscount_percentage() + "%");
                tv_discount.setVisibility(View.VISIBLE);
                tv_cost.setText(total_cost + " " + context.getString(R.string.rsa));


            }

            if (productModel.getImages().size() > 0) {

                new MyAsyncTask().execute(productModel.getImages().get(0));
            }


        }

        public class MyAsyncTask extends AsyncTask<String, Void, Bitmap> {
            @Override
            protected Bitmap doInBackground(String... strings) {
                Bitmap bitmap = null;
                try {
                    bitmap = Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL + strings[0])).priority(Picasso.Priority.HIGH).transform(new Transformation() {
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
        private ImageView image;
        private TextView tv_cost, tv_name, tv_amount, tv_discount;

        public MyHolderAccessories(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            tv_cost = itemView.findViewById(R.id.tv_cost);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_amount = itemView.findViewById(R.id.tv_amount);
            tv_discount = itemView.findViewById(R.id.tv_discount);

        }

        public void BindData(OrderDataModel.ProductModel productModel) {

            if (current_language.equals("ar")) {
                tv_name.setText(productModel.getName_ar());
                tv_discount.setBackgroundResource(R.drawable.offer_bg_left);
            } else {
                tv_discount.setBackgroundResource(R.drawable.offer_bg_left);
                tv_name.setText(productModel.getName_en());

            }

            tv_amount.setText(String.valueOf(productModel.getQuantity()));

            if (productModel.getFeatured() == 0) {
                tv_discount.setVisibility(View.GONE);
                double total_cost = productModel.getQuantity()*productModel.getPrice();

                tv_cost.setText(total_cost + " " + context.getString(R.string.rsa));

            } else {
                double total_cost = productModel.getQuantity()*productModel.getPrice_after_discount();

                tv_discount.setText(productModel.getDiscount_percentage() + "%");
                tv_discount.setVisibility(View.VISIBLE);
                tv_cost.setText(total_cost + " " + context.getString(R.string.rsa));


            }

            if (productModel.getImages().size() > 0) {

                new MyAsyncTask().execute(productModel.getImages().get(0));
            }


        }

        public class MyAsyncTask extends AsyncTask<String, Void, Bitmap> {
            @Override
            protected Bitmap doInBackground(String... strings) {
                Bitmap bitmap = null;
                try {
                    bitmap = Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL + strings[0])).priority(Picasso.Priority.HIGH).transform(new Transformation() {
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
        OrderDataModel.ProductModel productModel = productModelList.get(position);

        if (productModel.getLeft_amount() == 0 && productModel.getRight_amount() == 0) {
            return ITEM_ACCESSORIES;

        } else {
            return ITEM_LENSES;
        }

    }


}
