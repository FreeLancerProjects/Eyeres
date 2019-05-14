package com.appzone.eyeres.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.MyHolder> {

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
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.details_product_row, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {
        OrderDataModel.ProductModel productModel = productModelList.get(position);
        holder.BindData(productModel);



    }

    @Override
    public int getItemCount() {
        return productModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView tv_cost,tv_name,tv_amount,tv_discount;

        public MyHolder(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            tv_cost = itemView.findViewById(R.id.tv_cost);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_amount = itemView.findViewById(R.id.tv_amount);
            tv_discount = itemView.findViewById(R.id.tv_discount);

        }

        public void BindData(OrderDataModel.ProductModel productModel) {

            if (current_language.equals("ar"))
            {
                tv_name.setText(productModel.getName_ar());
                tv_discount.setBackgroundResource(R.drawable.offer_bg_left);
            }else
                {
                    tv_discount.setBackgroundResource(R.drawable.offer_bg_left);
                    tv_name.setText(productModel.getName_en());

                }

            tv_amount.setText(String.valueOf(productModel.getQuantity()));

            if (productModel.getFeatured()==0)
            {
                tv_discount.setVisibility(View.GONE);
                tv_cost.setText(productModel.getPrice()+" "+ context.getString(R.string.rsa));

            }else
                {
                    tv_discount.setText(productModel.getDiscount_percentage()+"%");
                    tv_discount.setVisibility(View.VISIBLE);
                    tv_cost.setText(productModel.getPrice_after_discount()+" "+ context.getString(R.string.rsa));


                }

                if (productModel.getImages().size()>0)
                {

                    new MyAsyncTask().execute(productModel.getImages().get(0));
                }
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
}
