/*----------------------------------------------------------------------------
Copyright(c) Philips Electronics India Ltd
All rights reserved. Reproduction in whole or in part is prohibited without
the written consent of the copyright holder.

Project           : Consumer Care
----------------------------------------------------------------------------*/

package com.philips.cdp.digitalcare.productdetails;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.philips.cdp.digitalcare.R;
import com.philips.cdp.digitalcare.util.DigiCareLogger;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;

public class DownloadPDFService extends Service {

    private String mHelpManualFileName;
    private String TAG = DownloadPDFService.class.getSimpleName();

    private int NOTIFICATION_ID = 1;

    private NotificationManager mNotifyManager;
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            DigiCareLogger.d(TAG, "BroadcastReceiver DownloadPDFService");
            if (action.equals("com.philips.cdp.digitalcare.productdetails.services.OPENPDF")) {
                mNotifyManager.cancel(NOTIFICATION_ID);

                if(mHelpManualFileName == null){
                    return;
                }
            }
        }
    };
    private NotificationCompat.Builder mBuilder;

    public DownloadPDFService() {
        super();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        IntentFilter filter = new IntentFilter();
        filter.addAction("com.philips.cdp.digitalcare.productdetails.services.OPENPDF");
        registerReceiver(receiver, filter);

        createNotification(getApplicationContext());
        if (null != intent) {
            String mHelpManualUrl = intent.getStringExtra("HELP_MANUAL_PDF_URL");
            mHelpManualFileName = intent.getStringExtra("HELP_MANUAL_PDF_FILE_NAME");
            DigiCareLogger.v(TAG, "onStartCommand url: " + mHelpManualUrl + " filename: " + mHelpManualFileName);

            new DownloadFile(getApplicationContext()).execute(mHelpManualUrl, mHelpManualFileName);
        }
        return START_STICKY;
    }


    private void createNotification(Context ctx) {
        mNotifyManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        String channelId = "channel-01";
        String channelName = "Channel Name";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            mNotifyManager.createNotificationChannel(mChannel);
            mBuilder = new NotificationCompat.Builder(ctx, channelId);
        } else {
            mBuilder = new NotificationCompat.Builder(ctx);
        }

        Intent newintent = new Intent();
        newintent.setAction("com.philips.cdp.digitalcare.productdetails.services.OPENPDF");
        PendingIntent pIntent = PendingIntent.getBroadcast(ctx, 0, newintent, 0);

        mBuilder.setContentTitle(ctx.getResources().getString(R.string.app_name))
                .setContentText(ctx.getResources().getString(R.string.download_ticker))
                .setContentIntent(pIntent)
                .setSmallIcon(getNotificationIcon());

    }

    private int getNotificationIcon() {
        return R.drawable.consumercare_contact_us;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private class DownloadFile extends AsyncTask<String, Integer, Boolean> {
        private static final int id = 1;
        private static final int TIMEOUT_VALUE = 15000;
        File file = null;
        private Context mContext;
        private int mCurrentProgress;

        public DownloadFile(Context pContext) {
            this.mContext = pContext;
        }

        @Override
        protected Boolean doInBackground(String... sUrl) {
            try {
                URL url = new URL(sUrl[0]);
                URLConnection connection = url.openConnection();
                connection.setConnectTimeout(TIMEOUT_VALUE);
                connection.setReadTimeout(TIMEOUT_VALUE);
                connection.connect();
             /*   DigiCareLogger.i(TAG, "connected");*/
                // this will be useful so that you can show a typical 0-100% progress bar
                int fileLength = connection.getContentLength();

                // download the file
                BufferedInputStream input = new BufferedInputStream(connection.getInputStream());

                file = new File(Environment.getExternalStorageDirectory(), sUrl[1]);
                OutputStream output = new FileOutputStream(file);

                byte data[] = new byte[1024];
                long total = 0;
                int count, progress;
                while ((count = input.read(data)) > 0) {
                    total += count;
                    output.write(data, 0, count);
                    // publishing the progress....
                    progress = (int) (total * 100 / fileLength);
                    if (progress > mCurrentProgress) {
                        publishProgress(progress);
                        mCurrentProgress = progress;
                    }
                }
                output.flush();
                output.close();
                input.close();
            } catch (SocketTimeoutException e) {
                file.delete();
                showNotification(mContext.getResources().getString(R.string.download_error));
                DigiCareLogger.e(TAG, "More than 15s elapsed.");
                return false;
            } catch (Exception e) {
                if(file!=null){
                    file.delete();
                }
                showNotification(mContext.getResources().getString(R.string.download_error));
                DigiCareLogger.e(TAG, "Error downloading file " + e.getMessage());
                return false;
            }
            return true;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showNotification(mContext.getResources().getString(R.string.download_ticker));
            mCurrentProgress = 0;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            showNotification(mContext.getResources().getString(R.string.download_error));
        }

        @Override
        protected void onPostExecute(Boolean b) {
            super.onPostExecute(b);

            if (mCurrentProgress == 100) {
                showNotification(mContext.getResources().getString(R.string.download_complete));
            }

        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);

            mBuilder.setContentText(mContext.getResources().getString(R.string.download_ticker));
            mBuilder.setContentInfo(progress[0] + "%");
            mBuilder.setProgress(100, progress[0], false);
            mNotifyManager.notify(id, mBuilder.build());
        }


        private void showNotification(String ticker) {
            mBuilder.setContentText(ticker)
                    .setProgress(0, 0, false);
            mNotifyManager.notify(id, mBuilder.build());
        }
    }
}
