/*----------------------------------------------------------------------------
Copyright(c) Philips Electronics India Ltd
All rights reserved. Reproduction in whole or in part is prohibited without
the written consent of the copyright holder.

Project           : ConsumerCare
----------------------------------------------------------------------------
Copyright (c) 2016 Philips. All rights reserved.
*/

package com.philips.cdp.digitalcare.productdetails;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import com.philips.cdp.digitalcare.DigitalCareConfigManager;
import com.philips.cdp.digitalcare.R;
import com.philips.cdp.digitalcare.util.DigiCareLogger;

import java.io.File;
import java.util.Locale;

public class DownloadAndShowPDFHelper {

    private static final String PACKAGENAME_ADOBE_READER = "com.adobe.reader";
    private String TAG = DownloadAndShowPDFHelper.class.getSimpleName();
//    private static final String URL_PDF_MANUAL_EN_NEW = "http://download.p4c.philips.com/files/h/hd8977_01/hd8977_01_dfu_eng.pdf";

    private Context mContext;

    private AlertDialog.Builder mAlertDialog;

    private static String mHelpManualFileName, mHelpManualUrl;

    public void downloadAndOpenPDFManual(final Context pContext, String urlPDF, String pdfName, boolean isConnected) {
        if (pContext == null) {
            DigiCareLogger.e(TAG, "Failed to open PDF manual - context is null");
        }

        mContext = pContext;
        mHelpManualUrl = urlPDF;

        if (null != mContext) {
//            mHelpManualUrl = getURLHelpManualPDFFile(mContext);

//            if (isAquacleanEnabled(mContext)) {
//                mHelpManualFileName = mContext.getString(R.string.help_manual_aquaclean_pdf);
//            } else {
//                mHelpManualFileName = mContext.getString(R.string.help_manual_pdf);
//            }

            mHelpManualFileName = pdfName;

            File file = new File(Environment.getExternalStorageDirectory(), mHelpManualFileName);
            if (file.exists()) {
                openManualPDFInAcrobatReader(mContext, mHelpManualFileName);
            } else {
                if (isConnected) {

                    Intent downloadService = new Intent(mContext, DownloadPDFService.class);
                    downloadService.putExtra("HELP_MANUAL_PDF_URL", mHelpManualUrl);
                    downloadService.putExtra("HELP_MANUAL_PDF_FILE_NAME", mHelpManualFileName);
                    mContext.startService(downloadService);

                    mAlertDialog = new AlertDialog.Builder(mContext);
                    Locale locale = DigitalCareConfigManager.getInstance().getLocaleMatchResponseWithCountryFallBack();
                    String language = null;
                    if(locale != null){
                        language = locale.getLanguage();
                    }
                    String message = String.format(mContext.getString(R.string.ManualDownLoadConfirmMessage), language);
                    mAlertDialog.setMessage(message);
                    mAlertDialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent downloadService = new Intent(mContext, DownloadPDFService.class);
                            downloadService.putExtra("HELP_MANUAL_PDF_URL", mHelpManualUrl);
                            downloadService.putExtra("HELP_MANUAL_PDF_FILE_NAME", mHelpManualFileName);
                            mContext.startService(downloadService);
                        }
                    });
//                    mAlertDialog.setOnDialogClickListener(mContext);
//                    mAlertDialog.setDialogId(AppConstants.OK_RESEND_DIALOG);
//                    mAlertDialog.setAlertDialogType(XAlertDialog.DIALOG_TYPE.TWO);
                } else {
                    mAlertDialog = new AlertDialog.Builder(mContext);
                    mAlertDialog.setMessage(mContext.getResources().getString(
                            R.string.no_internet));
                }
//                mAlertDialog.show();
            }
        }
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            DigiCareLogger.d(TAG, "BroadcastReceiver DownloadAndShowPDFHelper");
            if (action.equals("com.philips.cdp.digitalcare.productdetails.services.OPENPDF")) {
                openManualPDFInAcrobatReader(context, mHelpManualFileName);
            }
        }
    };

    public void openManualPDFInAcrobatReader(Context ctx, String manualFilename) {
        if (manualFilename == null)
            return;

        File file = new File(Environment.getExternalStorageDirectory(), manualFilename);
        try {
            DigiCareLogger.d(TAG, " manualUrl " + manualFilename);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "application/pdf");
            intent.setPackage(PACKAGENAME_ADOBE_READER);
            ctx.startActivity(intent);
            DigiCareLogger.d(TAG, "Opening Manual PDF in Adobe Reader - " + manualFilename);
        } catch (Exception e) {
            DigiCareLogger.e(TAG, e.getMessage());
            showAcrobatReaderNotInstalledDialog(ctx);
        }
    }

    private void showAcrobatReaderNotInstalledDialog(final Context ctx) {
        DigiCareLogger.d(TAG, "Showing Adobe reader not installed dialog");

        mAlertDialog = new AlertDialog.Builder(mContext);
        mAlertDialog.setMessage("showAcrobatReaderNotInstalledDialog testing");

        mAlertDialog.setPositiveButton("showAcrobatReaderNotInstalledDialog testing", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showAcrobatReaderInGooglePlay(ctx);
            }
        });
        mAlertDialog.show();
    }

    private void showAcrobatReaderInGooglePlay(final Context ctx) {
        DigiCareLogger.d(TAG, "Showing Adobe Reader in Google Play");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=" + PACKAGENAME_ADOBE_READER));
        ctx.startActivity(intent);
    }
}
