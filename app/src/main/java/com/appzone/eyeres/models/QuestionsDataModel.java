package com.appzone.eyeres.models;

import java.io.Serializable;
import java.util.List;

public class QuestionsDataModel implements Serializable {

    private List<QuestionModel> data;
    private Meta meta;

    public List<QuestionModel> getData() {
        return data;
    }

    public Meta getMeta() {
        return meta;
    }

    public class QuestionModel implements Serializable
    {
        private String q_ar;
        private String q_en;
        private String a_ar;
        private String a_en;

        public String getQ_ar() {
            return q_ar;
        }

        public String getQ_en() {
            return q_en;
        }

        public String getA_ar() {
            return a_ar;
        }

        public String getA_en() {
            return a_en;
        }
    }

    public class Meta implements Serializable
    {
        private int current_page;
        private int last_page;

        public int getCurrent_page() {
            return current_page;
        }

        public int getLast_page() {
            return last_page;
        }
    }
}
