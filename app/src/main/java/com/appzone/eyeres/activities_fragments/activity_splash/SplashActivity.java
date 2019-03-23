package com.appzone.eyeres.activities_fragments.activity_splash;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.appzone.eyeres.R;
import com.appzone.eyeres.activities_fragments.activity_home.activity.HomeActivity;
import com.appzone.eyeres.activities_fragments.activity_sign_in.activity.SignInActivity;
import com.appzone.eyeres.local_manager.LocalManager;
import com.appzone.eyeres.models.UserModel;
import com.appzone.eyeres.preferences.Preferences;
import com.appzone.eyeres.singletone.UserSingleTone;
import com.appzone.eyeres.tags.Tags;

import java.util.Locale;

import io.paperdb.Paper;

public class SplashActivity extends AppCompatActivity {

    private FrameLayout fl;
    private Preferences preferences;
    private UserSingleTone userSingleTone;
    private String current_language;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalManager.updateResources(newBase,LocalManager.getLanguage(newBase)));

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Paper.init(this);
        current_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        preferences = Preferences.getInstance();
        userSingleTone = UserSingleTone.getInstance();
        fl = findViewById(R.id.fl);
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.fade);
        fl.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                String session = preferences.getSession(SplashActivity.this);

                if (session.equals(Tags.session_login))
                {
                    UserModel userModel = preferences.getUserData(SplashActivity.this);
                    userSingleTone.setUserModel(userModel);
                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                    if (current_language.equals("ar"))
                    {
                        overridePendingTransition(R.anim.from_right,R.anim.to_left);


                    }else
                    {
                        overridePendingTransition(R.anim.from_left,R.anim.to_right);


                    }

                }else
                    {
                        Intent intent = new Intent(SplashActivity.this, SignInActivity.class);
                        startActivity(intent);
                        finish();
                        if (current_language.equals("ar"))
                        {
                            overridePendingTransition(R.anim.from_right,R.anim.to_left);


                        }else
                        {
                            overridePendingTransition(R.anim.from_left,R.anim.to_right);


                        }
                    }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
