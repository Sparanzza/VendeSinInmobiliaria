package com.ilerna.vendesininmobiliarias.services;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.ilerna.vendesininmobiliarias.Utils.NotificationUtil;

import java.util.Map;
import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Map<String, String> msgData = remoteMessage.getData();
        String title = msgData.get("title");
        String body = msgData.get("body");
        String sNotificationId = msgData.get("notificationId");

        int notificationId = sNotificationId == null ? new Random().nextInt(99999) : Integer.parseInt(sNotificationId);

        NotificationUtil notificationUtil = new NotificationUtil(getBaseContext());
        NotificationCompat.Builder builder = notificationUtil.getNotification(title == null ? "New Chat Message" : title, body);
        notificationUtil.getManager().notify(notificationId, builder.build());
    }
}
