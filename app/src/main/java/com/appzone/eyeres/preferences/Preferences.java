package com.appzone.eyeres.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.appzone.eyeres.models.UserModel;
import com.appzone.eyeres.tags.Tags;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class Preferences {

    private static Preferences instance=null;

    private Preferences() {
    }

    public static Preferences getInstance()
    {
        if (instance==null)
        {
            instance = new Preferences();
        }
        return instance;
    }

    public void create_update_userData(Context context, UserModel userModel)
    {
        SharedPreferences preferences = context.getSharedPreferences("user",Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String userData = gson.toJson(userModel);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user_data",userData);
        editor.apply();
        create_update_session(context, Tags.session_login);

    }

    public UserModel getUserData(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences("user",Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String user_data = preferences.getString("user_data","");
        UserModel userModel = gson.fromJson(user_data,UserModel.class);
        return userModel;
    }

    public void create_update_session(Context context,String session)
    {
        SharedPreferences preferences = context.getSharedPreferences("session",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("state",session);
        editor.apply();

    }

    public String getSession(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences("session",Context.MODE_PRIVATE);
        String session = preferences.getString("state", Tags.session_logout);
        return session;
    }

    public List<String> getAllSearchQueries(Context context)
    {
        List<String> queriesList = new ArrayList<>();
        SharedPreferences preferences = context.getSharedPreferences("query",Context.MODE_PRIVATE);

        String gson_query = preferences.getString("query_gson","");
        if (!TextUtils.isEmpty(gson_query))
        {
            queriesList = new Gson().fromJson(gson_query, new TypeToken<List<String>>(){}.getType());
        }

        return queriesList;
    }

    public void SaveQuery(Context context , String query)
    {
        List<String> queriesList = new ArrayList<>();
        SharedPreferences preferences = context.getSharedPreferences("query",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String gson_query = preferences.getString("query_gson","");

        if (TextUtils.isEmpty(gson_query))
        {
            queriesList.add(query);
            String queryList_Gson = new Gson().toJson(queriesList);
            editor.putString("query_gson",queryList_Gson);
            editor.apply();
        }else
            {
                queriesList = new Gson().fromJson(gson_query,new TypeToken<List<String>>(){}.getType());
                if (queriesList.size()<10)
                {
                    if (!queriesList.contains(query))
                    {
                        queriesList.add(0,query);
                        String queryList_Gson = new Gson().toJson(queriesList);
                        editor.putString("query_gson",queryList_Gson);
                        editor.apply();
                    }

                }else
                    {
                        if (!queriesList.contains(query))
                        {
                            queriesList.set(0,query);
                            String queryList_Gson = new Gson().toJson(queriesList);
                            editor.putString("query_gson",queryList_Gson);
                            editor.apply();
                        }
                    }
            }

    }

    public void Clear(Context context)
    {
        SharedPreferences preferences1 = context.getSharedPreferences("user",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = preferences1.edit();
        editor1.clear();
        editor1.apply();
        SharedPreferences preferences2 = context.getSharedPreferences("session",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor2 = preferences2.edit();
        editor2.clear();
        editor2.apply();
        SharedPreferences preferences3 = context.getSharedPreferences("query",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor3 = preferences3.edit();
        editor3.clear();
        editor3.apply();
    }
}
