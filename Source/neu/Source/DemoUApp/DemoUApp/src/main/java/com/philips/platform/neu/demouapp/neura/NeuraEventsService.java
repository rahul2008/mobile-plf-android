package com.philips.platform.neu.demouapp.neura;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.neura.standalonesdk.events.NeuraEvent;
import com.neura.standalonesdk.events.NeuraEventCallBack;
import com.neura.standalonesdk.events.NeuraPushCommandFactory;
import com.philips.platform.neu.demouapp.R;

import java.util.Map;

public class NeuraEventsService extends FirebaseMessagingService {
    String CHANNEL_ID = "my_channel_01";
    @Override
    public void onMessageReceived(RemoteMessage message) {
        final Map data = message.getData();
        Log.i(getClass().getSimpleName(), "Received push");
        NeuraPushCommandFactory.getInstance().isNeuraPush(getApplicationContext(), data, new NeuraEventCallBack() {
            @Override
            public void neuraEventDetected(NeuraEvent event) {
                final String eventText = event != null ? event.toString() : "couldn't parse data";
                Log.i(getClass().getSimpleName(), "received Neura event - " + eventText);
                Handler handler = new Handler(getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                Toast.makeText(getApplicationContext(),"Notification: " + eventText,Toast.LENGTH_SHORT).show();
                    }
                });
                generateNotification(getApplicationContext(), eventText);
            }
        });
    }

    private void generateNotification(Context context, String eventText) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,CHANNEL_ID);

        String appName = "Neura";
        int stringId = context.getApplicationInfo().labelRes;
        if (stringId > 0)
            appName = context.getString(stringId);

        builder.setContentTitle(appName + " detected event")
                .setContentText(eventText)
                .setSmallIcon(R.drawable.neura_sdk_notification_status_icon)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), context.getApplicationInfo().icon))
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setStyle(new NotificationCompat.BigTextStyle().bigText(eventText));
        Notification notification = builder.build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int) System.currentTimeMillis(), notification);
    }
}
