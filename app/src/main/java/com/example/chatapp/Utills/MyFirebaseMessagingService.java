package com.example.chatapp.Utills;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.chatapp.ChatActivity;
import com.example.chatapp.R;
import com.example.chatapp.ViewFriendActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull  RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String title=remoteMessage.getNotification().getTitle();
        String body=remoteMessage.getNotification().getBody();

        NotificationCompat.Builder builder=new NotificationCompat.Builder(getApplicationContext(),"CHAT");
        builder.setContentTitle(title);
        builder.setContentText(body);
        builder.setSmallIcon(R.drawable.ic_baseline_message_24);
        Intent intent = null;

        if(remoteMessage.getData().get("type").equalsIgnoreCase("sms"))
        {
            intent=new Intent(this,ChatActivity.class);
            intent.putExtra("OtherUserID",remoteMessage.getData().get("userID"));


        }

       if(remoteMessage.getData().get("type").equalsIgnoreCase("Request"))
        {
            intent=new Intent(this, ViewFriendActivity.class);
            intent.putExtra("userKey",remoteMessage.getData().get("userID"));
            ;
        }

        PendingIntent pendingIntent=PendingIntent.getActivity(this,101,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        NotificationManager manager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(123,builder.build());
    }
}
