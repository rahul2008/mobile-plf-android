package com.philips.cl.di.dev.pa.cpp;

import java.io.File;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.util.Utils;

public class AppUpdater implements AppUpdateListener{
	
	private CPPController mCppController;
	private Context mContext;
	private int mDownloadPercentage ;
	private NotificationManager mNotificationManager;
	private Notification mNotification;
	private String mFilePath = "";
	
	
	private static final int APP_UPDATE_NOTIFICATION_BUILDER_ID = 128272;
	private final int NOTIFICATION_ID=45;
	
	//TODO: get the string from resources
	public static String NO_STORAGE_AVAILABLE = "External storage not available";
	
	
	private static AppUpdater mAppUpdaterIntance = null;
	private ShowAppUpdateDialogListener mShowAppUpdateDialogListener;
	
	private boolean mAppUpdateAlertShown;
	
	public interface ShowAppUpdateDialogListener{
		public void showAppUpdateDialog();
	}
	
	private AppUpdater(Context context){	
		mContext = context;
		mCppController = CPPController.getInstance(context);
		mCppController.setAppUpdateNotificationListener(this) ;
		mAppUpdateAlertShown=false;
	}
	
	public static synchronized AppUpdater getInstance(Context appContext) {		
		if (null == mAppUpdaterIntance) {
			mAppUpdaterIntance = new AppUpdater(appContext);
		}
		return mAppUpdaterIntance;
	}
	
	public void registerShowAppUpdateDialogListener(ShowAppUpdateDialogListener listener){
		mShowAppUpdateDialogListener = listener;
	}
	
	public void unregisterShowAppUpdateDialogListener(){
		mShowAppUpdateDialogListener = null;
	}
	
	@Override
	public void onAppUpdateAvailable() {
		if(mShowAppUpdateDialogListener!=null && !mAppUpdateAlertShown){
			mShowAppUpdateDialogListener.showAppUpdateDialog();
			mAppUpdateAlertShown=true;
		}
	}

	@Override
	public void onAppUpdateInfoDownloaded() {
        File sdcardWithDirFile = Utils.getExternalStorageDirectory(AppConstants.APP_UPDATE_DIRECTORY);
		
		if (sdcardWithDirFile == null) {
			showAppUpdateFailedNotification(NO_STORAGE_AVAILABLE);
			return;
		}		
		mCppController.startNewAppUpdateDownload();
	}
	
	@Override
	public File createFileForAppUpdateDownload(){
		File sdcardWithDirFile = Utils.getExternalStorageDirectory(AppConstants.APP_UPDATE_DIRECTORY);
		if(sdcardWithDirFile==null){
			return null;
		}
		File outFile = new File(sdcardWithDirFile, AppConstants.APP_UPDATE_FILE_NAME);
		if (outFile != null) {
		mFilePath = outFile.toString();		
		}
		return outFile;
	}

	@Override
	public void onAppUpdateDownloadStart(int percentage) {
		mDownloadPercentage = percentage;
		createAppNotificationBuilder();
	}

	@Override
	public void onAppUpdateDownloadProgress(int percentage) {
		mDownloadPercentage = percentage;
		mNotification.contentView.setProgressBar(R.id.notification_progressbar, 100, mDownloadPercentage, false);        
        mNotification.contentView.setTextViewText(R.id.notification_progressbar_percent, mDownloadPercentage + "%");    
        mNotificationManager.notify(APP_UPDATE_NOTIFICATION_BUILDER_ID, mNotification);		
	}

	@Override
	public void onAppUpdateDownloadComplete() {
		handleAppUpdateDownloadComplete();		
	}

	@Override
	public void onAppUpdateDownloadFailed() {
		handleAppUpdateDownloadFail();
	}
	
	private void handleAppUpdateDownloadComplete() {		
		mNotification.contentView.setProgressBar(R.id.notification_progressbar, 100, 100, false);        
        mNotification.contentView.setTextViewText(R.id.notification_progressbar_percent, mDownloadPercentage + "%");   
		
        mNotificationManager.notify(APP_UPDATE_NOTIFICATION_BUILDER_ID, mNotification);
        mNotificationManager.cancel(APP_UPDATE_NOTIFICATION_BUILDER_ID);
        
		showPackageInstaller();
	}
	
	private void handleAppUpdateDownloadFail() {
		if( mNotificationManager != null ) {
			mNotificationManager.cancel(APP_UPDATE_NOTIFICATION_BUILDER_ID) ;
		}
		showAppUpdateFailedNotification(AppConstants.EMPTY_STRING);
	}
	
	@SuppressWarnings("deprecation")
	private void createAppNotificationBuilder() {
		mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotification = new Notification(R.drawable.purair_icon, 
        		mContext.getString(R.string.app_update_notif_title), System.currentTimeMillis());
        RemoteViews contentView = new RemoteViews(mContext.getPackageName(), R.layout.custom_notification_layout);
        contentView.setProgressBar(R.id.notification_progressbar, 100, mDownloadPercentage, false);        
        contentView.setTextViewText(R.id.notification_progressbar_percent, mDownloadPercentage + "%");        
        mNotification.contentView = contentView;

        PendingIntent dummyIntent = PendingIntent.getActivity(mContext, 0, new Intent(), 0);
        mNotification.contentIntent = dummyIntent;
        mNotificationManager.notify(APP_UPDATE_NOTIFICATION_BUILDER_ID, mNotification);
	}
	
	private void showPackageInstaller() {
		Intent intentInstaller = new Intent(Intent.ACTION_VIEW);
		intentInstaller.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(mFilePath));
		intentInstaller.setDataAndType(uri, "application/vnd.android.package-archive");
		mContext.startActivity(intentInstaller);
	}
	
	public void showAppUpdateFailedNotification(String msg){
		PendingIntent contentIntent = PendingIntent.getActivity(mContext,
				0, new Intent(), 0);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext);
		mBuilder.setSmallIcon(R.drawable.purair_icon); // TODO change notification icon
		mBuilder.setContentTitle(mContext.getString(R.string.app_update_failed));
		mBuilder.setWhen(System.currentTimeMillis());
		mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(msg));
		mBuilder.setContentText(msg);
		mBuilder.setAutoCancel(true);

		mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
	}
	
	public void fetchComponents(String appComponentId){
		mCppController.fetchICPComponents(appComponentId);
	}
	
	public void setAppUpdateStatus(boolean shown){
		mAppUpdateAlertShown = shown;
	}

}


