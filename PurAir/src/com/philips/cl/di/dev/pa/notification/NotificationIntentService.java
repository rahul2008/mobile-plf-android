package com.philips.cl.di.dev.pa.notification;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.MainActivity;
import com.philips.cl.di.dev.pa.util.ALog;

public class NotificationIntentService extends IntentService {
	
    public static final String EXTRA_ALERT = "alert";
    public static final String EXTRA_FROM = "from";
    public static final String EXTRA_SOUND = "sound";
    
    
	public static final int NOTIFICATION_ID = 1;
    NotificationCompat.Builder builder;

    public NotificationIntentService() {
        super("NotificationIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  
            /*
             * Filter messages based on message type. Since it is likely that GCM
             * will be extended in the future with new message types, just ignore
             * any message types you're not interested in, or that you don't
             * recognize.
             */
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
            	ALog.e(ALog.NOTIFICATION, "Received notification contains send error - ignoring");
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
            	ALog.e(ALog.NOTIFICATION, "Received notification regarding deleted message on server - ignoring");
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
            	NotificationManager notificationMan = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
                String message = getMessageFromNotification(extras);
            	showNotification(notificationMan, this, message);
                ALog.i(ALog.NOTIFICATION, "Received notification: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        NotificationBroadcastReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    public void showNotification(NotificationManager notificationMan, Context context, String msg) {
    	if(notificationMan==null) return;
    	if(msg==null || msg.isEmpty()) return;
    	
    	Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    	
    	Intent intent=new Intent(context, MainActivity.class);
    	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setSmallIcon(R.drawable.purair_icon); // TODO change notification icon
        mBuilder.setContentTitle(context.getString(R.string.app_name));
        mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(msg));
        mBuilder.setAutoCancel(true);
        mBuilder.setContentText(msg);
        mBuilder.setSound(soundUri);

        mBuilder.setContentIntent(contentIntent);
        notificationMan.notify(NOTIFICATION_ID, mBuilder.build());
        ALog.i(ALog.NOTIFICATION, "Successfully showed notification: " + msg);
    }
    
    public String getMessageFromNotification(Bundle notificationExtras) {
    	if (notificationExtras == null) return null;
    	if (notificationExtras.isEmpty()) return null;
    	
    	String message = notificationExtras.getString(EXTRA_ALERT);
    	if (message == null) return null;
    	if (message.isEmpty()) return null;
    	return message;
    }
}