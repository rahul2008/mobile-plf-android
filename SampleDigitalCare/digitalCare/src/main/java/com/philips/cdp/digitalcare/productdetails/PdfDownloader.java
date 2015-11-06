package com.philips.cdp.digitalcare.productdetails;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Description :
 * Project : PRX Common Component.
 * Created by naveen@philips.com on 06-Nov-15.
 */
class PdfDownloader extends AsyncTask<String, String, String> {

    private Activity mContext = null;
    private String mManualPath = null;

    public PdfDownloader(Activity context) {
        this.mContext = context;
    }


    ProgressDialog mProgressDialog = null;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage("Downloading file..");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    @Override
    protected String doInBackground(String... aurl) {
        int count;

        try {

            URL url = new URL(aurl[0]);
            URLConnection connection = url.openConnection();
            connection.connect();

            int lenghtOfFile = connection.getContentLength();

            InputStream input = new BufferedInputStream(url.openStream());
            File sdcard = Environment.getExternalStorageDirectory();
            mManualPath = sdcard.getAbsolutePath() + "/" + "Philips_Manual";
            OutputStream output = new FileOutputStream(mManualPath);

            byte data[] = new byte[1024];

            long total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;
                publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                output.write(data, 0, count);
            }

            output.flush();
            output.close();
            input.close();
        } catch (Exception e) {
        }
        return null;

    }

    protected void onProgressUpdate(String... progress) {
        mProgressDialog.setProgress(Integer.parseInt(progress[0]));
    }

    @Override
    protected void onPostExecute(String result) {
        if (mProgressDialog.isShowing()) {
            Uri path = Uri.fromFile(new File(mManualPath));
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(path, "application/pdf");

            try {
                mContext.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                try {
                    mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.adobe.reader")));
                } catch (android.content.ActivityNotFoundException anfe) {
                    mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + "com.adobe.reader")));
                }
            }
            mProgressDialog.dismiss();
        }
    }
}