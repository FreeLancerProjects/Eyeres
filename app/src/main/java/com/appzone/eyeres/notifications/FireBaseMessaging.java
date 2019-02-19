package com.appzone.eyeres.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.appzone.eyeres.R;
import com.appzone.eyeres.activities_fragments.activity_home.activity.HomeActivity;
import com.appzone.eyeres.models.OrderStatusModel;
import com.appzone.eyeres.models.UserModel;
import com.appzone.eyeres.preferences.Preferences;
import com.appzone.eyeres.tags.Tags;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

public class FireBaseMessaging extends FirebaseMessagingService {
    private Preferences preferences = Preferences.getInstance();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Map<String, String> map = remoteMessage.getData();

        for (String key : map.keySet()) {
            Log.e("key :", key + " value :" + map.get(key));
        }

        ManageNotification(map);

    }

    private void ManageNotification(final Map<String, String> map)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            new Handler(Looper.getMainLooper())
                    .postDelayed(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void run() {
                            String session = getSession();

                            if (session.equals(Tags.session_login)) {
                                int user_id = getUserData().getId();
                                int receiver_id = Integer.parseInt(map.get("receiver_id"));
                                if (user_id == receiver_id) {
                                    CreateProfessionalNotification(map);

                                }
                            }


                        }
                    }, 1);
        } else {
            new Handler(Looper.getMainLooper())
                    .postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            String session = getSession();

                            if (session.equals(Tags.session_login)) {
                                int user_id = getUserData().getId();
                                int receiver_id = Integer.parseInt(map.get("receiver_id"));
                                if (user_id == receiver_id) {
                                    CreateNativeNotification(map);

                                }
                            }
                        }
                    }, 1);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void CreateProfessionalNotification(Map<String, String> map) {
        final int notification_type = Integer.parseInt(map.get("notification_type"));

        String sound_path = "android.resource://" + getPackageName() + "/" + R.raw.not;
        String CHANNEL_ID = "my_channel_02";
        CharSequence CHANNEL_NAME = "my_channel_name";
        int IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;
        final NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE);
        channel.setShowBadge(true);
        channel.setSound(Uri.parse(sound_path), new AudioAttributes.Builder()
                .setLegacyStreamType(AudioManager.STREAM_NOTIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
        );

        final NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setChannelId(CHANNEL_ID);
        builder.setContentTitle(getString(R.string.admin));
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setSound(Uri.parse(sound_path));

        Intent intent = new Intent(this,HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("status",notification_type);

        if (notification_type == Tags.accepted_order) {

            builder.setContentText(getString(R.string.accepted));

        } else if (notification_type == Tags.refused_order) {

            builder.setContentText(getString(R.string.refused));


        } else if (notification_type == Tags.finished_order) {

            builder.setContentText(getString(R.string.finished));


        }

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                builder.setLargeIcon(bitmap);
                if (manager!=null)
                {
                    manager.createNotificationChannel(channel);
                    manager.notify(0,builder.build());
                    OrderStatusModel model = new OrderStatusModel(notification_type);
                    EventBus.getDefault().post(model);

                }
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        Picasso.with(this).load(R.mipmap.ic_launcher).into(target);
    }

    private void CreateNativeNotification(Map<String, String> map) {

        final int notification_type = Integer.parseInt(map.get("notification_type"));

        String sound_path = "android.resource://" + getPackageName() + "/" + R.raw.not;

        final NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle(getString(R.string.admin));
        builder.setSmallIcon(R.mipmap.ic_launcher);

        Intent intent = new Intent(this,HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("status",notification_type);

        if (notification_type == Tags.accepted_order) {

            builder.setContentText(getString(R.string.accepted));

        } else if (notification_type == Tags.refused_order) {

            builder.setContentText(getString(R.string.refused));


        } else if (notification_type == Tags.finished_order) {

            builder.setContentText(getString(R.string.finished));


        }

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        builder.setSound(Uri.parse(sound_path));
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                builder.setLargeIcon(bitmap);
                if (manager!=null)
                {
                    manager.notify(0,builder.build());
                    OrderStatusModel model = new OrderStatusModel(notification_type);
                    EventBus.getDefault().post(model);

                }
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        Picasso.with(this).load(R.mipmap.ic_launcher).into(target);
    }


    private String getSession() {
        return preferences.getSession(this);
    }

    private UserModel getUserData() {
        return preferences.getUserData(this);
    }
}
