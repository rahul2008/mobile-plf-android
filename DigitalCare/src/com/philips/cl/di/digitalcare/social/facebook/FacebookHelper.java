package com.philips.cl.di.digitalcare.social.facebook;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Intent;

import com.facebook.Session;
import com.facebook.Session.OpenRequest;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.philips.cl.di.digitalcare.util.DLog;

public class FacebookHelper {

	private static Activity mActivity = null;
	private static FacebookHelper mFacebookHelper = null;
	private String TAG = FacebookHelper.class.getSimpleName();

	private FacebookHelper() {
	};

	public static FacebookHelper getInstance(final Activity activity) {
		mActivity = activity;

		if (mFacebookHelper == null)
			mFacebookHelper = new FacebookHelper();
		return mFacebookHelper;
	}

	public static FacebookHelper getInstance() {
		if (mFacebookHelper == null)
			mFacebookHelper = new FacebookHelper();
		return mFacebookHelper;
	}

	public Session openSession(boolean allowLoginUI, List<String> permissions,
			StatusCallback callback) {
		OpenRequest openRequest = new OpenRequest(mActivity)
				.setPermissions(permissions).setCallback(callback)
				.setLoginBehavior(SessionLoginBehavior.SUPPRESS_SSO);
		Session session = new Session.Builder(mActivity).build();
		if (SessionState.CREATED_TOKEN_LOADED.equals(session.getState())
				|| allowLoginUI) {
			Session.setActiveSession(session);
			session.openForRead(openRequest);
			return session;
		}
		return null;
	}

	Session.StatusCallback mCallback = new Session.StatusCallback() {

		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			DLog.d(TAG, "Call method called with state " + session.getState());
		}
	};

	public void onFaceBookCallback(int requestCode, int resultCode, Intent data) {
		DLog.d(TAG, "result code is : .." + resultCode);

		Session.getActiveSession().onActivityResult(mActivity, requestCode,
				resultCode, data);

	}

	private List<String> getPermissions() {
		return Arrays.asList("email", "user_birthday", "user_hometown",
				"user_location");
	}

	public void openFacebookSession() {
		openSession(true, getPermissions(), mCallback);
	}

	/*
	 * public void onFaceBookCallback(int requestCode, int resultCode, Intent
	 * data) { DLog.d(TAG, "result code is : .." + resultCode);
	 * 
	 * Session.getActiveSession().onActivityResult(mActivity, requestCode,
	 * resultCode, data);
	 * 
	 * }
	 * 
	 * public void open(Activity a) { Session.openActiveSession(a, true, new
	 * Session.StatusCallback() {
	 * 
	 * // callback when session changes state
	 * 
	 * @Override public void call(Session session, SessionState state, Exception
	 * exception) { if (session.isOpened()) {
	 * 
	 * // make request to the /me API Request.newMeRequest(session, new
	 * Request.GraphUserCallback() {
	 * 
	 * // callback after Graph API response with user object
	 * 
	 * @Override public void onCompleted(GraphUser user, Response response) { if
	 * (user != null) { DLog.d("Second..", "name : "+ user.getName()); } }
	 * }).executeAsync(); } } }); }
	 */

}
