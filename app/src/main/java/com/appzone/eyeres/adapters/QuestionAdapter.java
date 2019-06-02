package com.appzone.eyeres.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appzone.eyeres.R;
import com.appzone.eyeres.models.QuestionsDataModel;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class QuestionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final int ITEM_DATA = 1;
    private final int ITEM_LOAD = 2;
    private List<QuestionsDataModel.QuestionModel> questionModelList;
    private Context context;
    private String current_language;

    public QuestionAdapter(List<QuestionsDataModel.QuestionModel> questionModelList, Context context) {
        this.questionModelList = questionModelList;
        this.context = context;
        Paper.init(context);
        current_language = Paper.book().read("lang", Locale.getDefault().getLanguage());

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==ITEM_DATA)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.question_row, parent, false);
            return new MyHolder(view);
        }else
            {
                View view = LayoutInflater.from(context).inflate(R.layout.load_more_progress, parent, false);
                return new LoadMoreHolder(view);
            }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyHolder)
        {
            MyHolder myHolder = (MyHolder) holder;
            QuestionsDataModel.QuestionModel questionModel = questionModelList.get(position);
            myHolder.BindData(questionModel);

        }else
            {
                LoadMoreHolder loadMoreHolder = (LoadMoreHolder) holder;
                loadMoreHolder.progressBar.setIndeterminate(true);
            }

    }

    @Override
    public int getItemCount() {
        return questionModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private TextView tv_q, tv_a;

        public MyHolder(View itemView) {
            super(itemView);

            tv_q = itemView.findViewById(R.id.tv_q);
            tv_a = itemView.findViewById(R.id.tv_a);


        }

        public void BindData(QuestionsDataModel.QuestionModel questionModel)
        {
            if (current_language.equals("ar"))
            {
                tv_q.setText(questionModel.getQ_ar());
                tv_a.setText(questionModel.getA_ar());
            }else
                {
                    tv_q.setText(questionModel.getQ_en());
                    tv_a.setText(questionModel.getA_en());
                }

        }

    }

    public class LoadMoreHolder extends RecyclerView.ViewHolder {
        private ProgressBar progressBar;
        public LoadMoreHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progBar);
            progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        }
    }

    @Override
    public int getItemViewType(int position) {
        QuestionsDataModel.QuestionModel questionModel = questionModelList.get(position);


        if (questionModel==null)
        {
            return ITEM_LOAD;

        }else
            {
                return ITEM_DATA;


            }
    }
}
