package com.philips.cl.di.dev.pa.util;

import android.os.Handler;
import android.os.Looper;

public class InternetConnectionHandler implements AsyncTaskCompleteListenere {

	private static final int MAX_NO_OF_REQUESTS = 5 ;
	private boolean internetAlreadyChecked ;
	private int responseCount ;
	private UrlConnectionRunnable urlConnectionRunnable ;
	private Handler handler;
	private InternetConnectionHandler myInstance ;
	private InternetConnectionListener listener;
	
	public InternetConnectionHandler(InternetConnectionListener listener, Handler handler) {
		myInstance = this;
		this.listener = listener ;
		this.handler = handler;
	}

	// Send 5 requests with a gap interval of 2secs
	public void checkInternetConnection() {
		resetInternetConnectionCheckParams() ;
		urlConnectionRunnable = new UrlConnectionRunnable() ;
		for(int index = 0 ; index < MAX_NO_OF_REQUESTS ; index ++) {
			handler.postDelayed(urlConnectionRunnable, index * 2000) ;			
		}
	}

	private void resetInternetConnectionCheckParams() {
		internetAlreadyChecked = false;
		responseCount = 0 ;
	}

	// Runnable for sending internet connection check requests
	private class UrlConnectionRunnable implements Runnable {
		@Override
		public void run() {
			new URLExistAsyncTask().testConnection(myInstance);
		}
	}

	@Override
	public void onTaskComplete(boolean internetAvailable) {
		if( internetAlreadyChecked) {
			return ;
		}
		responseCount++;
		if ( internetAvailable ) {
			internetAlreadyChecked = true ;
			if( handler != null ) {
				handler.removeCallbacks(urlConnectionRunnable) ;
			}
			listener.internetStatus(true) ;
		}
		else if( responseCount == MAX_NO_OF_REQUESTS ) {
			if( handler != null ) {
				handler.removeCallbacks(urlConnectionRunnable) ;
			}
			internetAlreadyChecked = true ;
			responseCount = 0 ;
			listener.internetStatus(false) ;
		}
		
	}

	

}
