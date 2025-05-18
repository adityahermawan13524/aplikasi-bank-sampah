package com.dev.banksampahdigital.firebasenotification;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Vibrator;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.dev.banksampahdigital.LoginRegister.LoginActivity;
import com.dev.banksampahdigital.R;
import com.dev.banksampahdigital.View.history.detail_transaksi_input_sampah;
import com.dev.banksampahdigital.View.main.HomeActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Objects;

public class MessageService extends FirebaseMessagingService {


    private NotificationManager notificationManager;

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);

        updateNewToken(token);


    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);

        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {0, 10, 100, 200};

        vibrator.vibrate(pattern, -1);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "Notification");

//        Intent resultIntent = new Intent(this, HomeActivity.class);
        Intent intent = new Intent(this, LoginActivity.class);
        intent.getStringExtra("username");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_IMMUTABLE);

//        PendingIntent pendingIntent = null;
//        pendingIntent.getActivity(this, 1, resultIntent, PendingIntent.FLAG_IMMUTABLE);
        builder.setContentTitle(Objects.requireNonNull(message.getNotification()).getTitle());
        builder.setContentText(message.getNotification().getBody());
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(message.getNotification().getBody()));
        builder.setAutoCancel(true);
        builder.setSmallIcon(R.drawable.ic_logo);
        builder.setVibrate(pattern);
        builder.setPriority(Notification.PRIORITY_MAX);
        builder.setContentIntent(pendingIntent);

        Log.d("title", message.getNotification().getTitle());
        Log.d("body", message.getNotification().getBody());

        notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String chanelID = "Notification";
            NotificationChannel channel = new NotificationChannel(
                    chanelID, "Coding", NotificationManager.IMPORTANCE_HIGH
            );
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setVibrationPattern(pattern);
            channel.canBypassDnd();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                channel.canBubble();
            }

            notificationManager.createNotificationChannel(channel);
            builder.setChannelId(chanelID);
        }

        notificationManager.notify(100, builder.build());
    }

    private void updateNewToken(String token){

    }
}
