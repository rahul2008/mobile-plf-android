/*----------------------------------------------------------------------------
Copyright(c) Philips Electronics India Ltd
All rights reserved. Reproduction in whole or in part is prohibited without
the written consent of the copyright holder.

Project           : Consumer Care
----------------------------------------------------------------------------*/

package com.philips.cdp.digitalcare.productdetails;

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
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

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

    private static final String PACKAGENAME_ADOBE_READER = "com.adobe.reader";
    private String mHelpManualFileName, mHelpManualUrl;
    private String TAG = DownloadPDFService.class.getSimpleName();

//    private ModelContainer mModelContainer;

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

                //TODO: If PDF app is not installed then click on notification icon leads to crash.
//                File file = new File(Environment.getExternalStorageDirectory(), mHelpManualFileName);
//
//                Intent intentPDF = new Intent(Intent.ACTION_VIEW);
//                intentPDF.setDataAndType(Uri.fromFile(file), "application/pdf");
//                intentPDF.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intentPDF.setPackage(PACKAGENAME_ADOBE_READER);
//                context.startActivity(intentPDF);
//                mModelContainer.getProperty().notifyObserver(PROPERTY_LISTNERS.DOWNLOAD_COMPLETED, mHelpManualFileName);
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
//        mModelContainer = ModelContainer.getInstance();
//        mModelContainer.clearData();

        IntentFilter filter = new IntentFilter();
        filter.addAction("com.philips.cdp.digitalcare.productdetails.services.OPENPDF");
        registerReceiver(receiver, filter);

        createNotification(getApplicationContext());
        if (null != intent) {
            mHelpManualUrl = intent.getStringExtra("HELP_MANUAL_PDF_URL");
            mHelpManualFileName = intent.getStringExtra("HELP_MANUAL_PDF_FILE_NAME");
            DigiCareLogger.i(TAG, "onStartCommand url: " + mHelpManualUrl + " filename: " + mHelpManualFileName);

            new DownloadFile(getApplicationContext()).execute(mHelpManualUrl, mHelpManualFileName);
        }
        return START_STICKY;
    }

    private void createNotification(Context ctx) {
        mNotifyManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(ctx);

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
                DigiCareLogger.i(TAG, "connected");
                // this will be useful so that you can show a typical 0-100% progress bar
                int fileLength = connection.getContentLength();

                // download the file
                BufferedInputStream input = new BufferedInputStream(connection.getInputStream());

                file = new File(Environment.getExternalStorageDirectory(), sUrl[1]);
                OutputStream output = new FileOutputStream(file);

                byte data[] = new byte[1024];
                long total = 0;
                int count, progress = 0;
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
                System.out.println("More than 15s elapsed.");
                return false;
            } catch (Exception e) {
                file.delete();
                showNotification(mContext.getResources().getString(R.string.download_error));
                DigiCareLogger.e(TAG, "Error downloading file " + e.getMessage());
                return false;
            }
            DigiCareLogger.i(TAG, "Download completed");
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
//                mModelContainer.getProperty().notifyObserver(PROPERTY_LISTNERS.DOWNLOAD_COMPLETED, mHelpManualFileName);

                // When the loop is finished, updates the notification
//                showNotification(mContext.getResources().getString(R.string.download_complete));
                showNotification(mContext.getResources().getString(R.string.download_complete));
            }

        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);

            DigiCareLogger.i(TAG, "onProgressUpdate progress: " + progress[0]);
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
