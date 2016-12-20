package com.bangjoni.agentpulsa.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.bangjoni.agentpulsa.DetailOrderActivity;
import com.bangjoni.agentpulsa.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class CustomFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "CustomFirebaseMessaging";

    public CustomFirebaseMessagingService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Data: " + remoteMessage.getData());

        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");
        String orderId = remoteMessage.getData().get("order_id");

        Intent resultIntent = new Intent(this, DetailOrderActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity( getApplicationContext(), 0, new Intent(), 0 );

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.cast_ic_notification_small_icon)
                        .setAutoCancel(true)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setContentIntent(resultPendingIntent);

        Notification notification = mBuilder.build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        // Vibrate if vibrate is enabled
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notification.priority = Notification.PRIORITY_HIGH;

        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify((int) System.currentTimeMillis(), notification);

        Intent launchIntent = new Intent(this, DetailOrderActivity.class);
        launchIntent.putExtra("orderId", orderId);
        launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(launchIntent);
    }
}
