package com.appzone.eyeres.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appzone.eyeres.R;
import com.appzone.eyeres.activities_fragments.activity_home.fragments.fragment_search.Fragment_Search;

import java.util.List;

public class AutocompleteSearchAdapter extends RecyclerView.Adapter<AutocompleteSearchAdapter.MyHolder> {

    private List<String> queriesList;
    private Context context;
    private Fragment_Search fragment;

    public AutocompleteSearchAdapter(Context context,List<String> queriesList,Fragment_Search fragment) {
        this.queriesList = queriesList;
        this.context = context;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.autocomplete_search_row, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {
        String query =  queriesList.get(position);
        holder.BindData(query);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query =  queriesList.get(holder.getAdapterPosition());


                fragment.setItemDataForSearch(query);

            }
        });



    }

    @Override
    public int getItemCount() {
        return queriesList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private TextView tv_name;


        public MyHolder(View itemView) {
            super(itemView);

            tv_name = itemView.findViewById(R.id.tv_name);


        }

        public void BindData(String query) {

            tv_name.setText(query);



        }
    }
}
