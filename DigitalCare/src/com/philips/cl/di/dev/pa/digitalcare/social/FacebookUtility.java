package com.philips.cl.di.dev.pa.digitalcare.social;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.OpenRequest;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.philips.cl.di.dev.pa.digitalcare.util.ALog;

/*
 *	FacebookUtility will help to provide options for facebook utility.
 * 
 * Author : Ritesh.jha@philips.com
 * 
 * Creation Date : 5 Feb 2015
 */

public class FacebookUtility {
	private static Activity mActivity = null;

	private static final String TAG = "ContactUsFragment";

	private final String PERMISSION = "publish_actions";
	// private Uri selectedImageUri = null;
	private final String EMAIL = "email";
	private final String BIRTHDAY = "user_birthday";
	private final String HOMETOWN = "user_hometown";
	private final String LOCATION = "user_location";
	private final String PENDING_ACTION_BUNDLE_KEY = "com.facebook.samples.hellofacebook:PendingAction";
	private PendingAction pendingAction = PendingAction.NONE;

	private UiLifecycleHelper mFbUiHelper;

	private EditText editStatus;
	private ImageView popShareImage;
	private boolean canPresentShareDialog = false;
	private boolean canPresentShareDialogWithPhotos = false;
	private static boolean isImageAvialable = false;
	private static FacebookUtility mFacebook = null;

	private static enum PendingAction {
		NONE, POST_PHOTO, POST_STATUS_UPDATE
	}

	public static FacebookUtility getSingleInstance(Activity activity,
			Bundle savedInstanceState) {
		if (mFacebook == null) {
			mFacebook = new FacebookUtility(activity, savedInstanceState);
			return mFacebook;
		}
		return mFacebook;
	}

	private FacebookUtility(Activity activity, Bundle savedInstanceState) {
		mActivity = activity;
		mFbUiHelper = new UiLifecycleHelper(mActivity, sessionCalback);
		mFbUiHelper.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			String name = savedInstanceState
					.getString(PENDING_ACTION_BUNDLE_KEY);
			pendingAction = PendingAction.valueOf(name);
		}
	}

	public Session Authenticate() {

		return openActiveSession(true,
				Arrays.asList(EMAIL, BIRTHDAY, HOMETOWN, LOCATION), null);
	}

	/**
	 * Facebook Session
	 */

	public void onActivityResultFragment(Activity activity, int requestCode,
			int resultCode, Intent data) {

		Session.getActiveSession().onActivityResult(activity, requestCode,
				resultCode, data);
		new Session.OpenRequest(activity);
	}

	public void performPublishAction() {
		if (isImageAvialable) {
			isImageAvialable = false;
			ALog.i(TAG, "FacebookUtility performPublishAction POST_PHOTO");
			performPublish(PendingAction.POST_PHOTO,
					canPresentShareDialogWithPhotos);
		} else {
			ALog.i(TAG,
					"FacebookUtility performPublishAction POST_STATUS_UPDATE");
			performPublish(PendingAction.POST_STATUS_UPDATE,
					canPresentShareDialog);
		}
	}

	@SuppressWarnings("static-method")
	private boolean hasPublishPermission() {
		Session session = Session.getActiveSession();
		return session != null
				&& session.getPermissions().contains("publish_actions");
	}

	/**
	 * perform publish action of facebook sharing.
	 * 
	 * @param action
	 * @param allowNoSession
	 */
	private void performPublish(PendingAction action, boolean allowNoSession) {
		Session session = Session.getActiveSession();
		if (session != null) {
			pendingAction = action;
			if (hasPublishPermission()) {
				handlePendingAction();
				return;
			} else if (session.isOpened()) {
				// We need to get new permissions, then complete the action when
				// we get called back.
				try {
					session.requestNewPublishPermissions(new Session.NewPermissionsRequest(
							mActivity, PERMISSION));
				} catch (UnsupportedOperationException exception) {
					ALog.i(TAG,
							" UnsupportedOperationException "
									+ exception.getMessage());
				}

				return;
			} else {
				openActiveSession(true,
						Arrays.asList(EMAIL, BIRTHDAY, HOMETOWN, LOCATION),
						null);
			}
		} else if (allowNoSession) {
			pendingAction = action;
			handlePendingAction();
		}
	}

	private void shareImage() {
		Request request = Request.newUploadPhotoRequest(
				Session.getActiveSession(),
				((BitmapDrawable) popShareImage.getDrawable()).getBitmap(),
				new Request.Callback() {

					@Override
					public void onCompleted(Response response) {
						String posted = "Shared Image Successful to Facebook";
						ALog.d(TAG, posted);
						Toast.makeText(mActivity, posted, Toast.LENGTH_LONG)
								.show();
					}
				});

		Bundle params = request.getParameters();
		params.putString("message", editStatus.getText().toString());
		request.setParameters(params);
		request.executeAsync();
	}

	/**
	 * Handle pending action.
	 */
	private void handlePendingAction() {
		PendingAction previouslyPendingAction = pendingAction;
		pendingAction = PendingAction.NONE;

		switch (previouslyPendingAction) {
		case POST_PHOTO:
			shareImage();
			break;
		case POST_STATUS_UPDATE:
			shareStatusUpdate();
			break;
		default:
			break;
		}
	}

	/**
	 * Open and set FB session.
	 * 
	 * @param activity
	 *            Activity
	 * @param allowLoginUI
	 * @param permissions
	 * @param callback
	 * @return Session object.
	 */
	private Session openActiveSession(boolean allowLoginUI,
			List<String> permissions, StatusCallback callback) {
		OpenRequest openRequest = new OpenRequest(mActivity)
				.setPermissions(permissions).setCallback(callback)
				.setLoginBehavior(SessionLoginBehavior.SUPPRESS_SSO);
		session = new Session.Builder(mActivity).build();
		if (SessionState.CREATED_TOKEN_LOADED.equals(session.getState())
				|| allowLoginUI) {
			Session.setActiveSession(session);
			session.openForRead(openRequest);
			return session;
		}
		return null;
	}

	Session session = null;

	public Session isSessionAvailable() {
		return session;
	}

	/**
	 * Sharing text in facebook.
	 */
	private void shareStatusUpdate() {
		Request request = Request.newStatusUpdateRequest(
				Session.getActiveSession(), "Session", new Request.Callback() {

					@Override
					public void onCompleted(Response response) {
						ALog.d(TAG,
								"Shared Status Successful to Facebook 'response'"
										+ response);
					}
				});
		Bundle params = request.getParameters();
		params.putString("message", editStatus.getText().toString());
		request.setParameters(params);
		request.executeAsync();
	}

	/**
	 * onPause of fragment.
	 */
	public void onPause() {
		mFbUiHelper.onPause();
	}

	/**
	 * onResume of fragment.
	 */
	public void onDestroy() {
		mFbUiHelper.onDestroy();
	}

	/**
	 * onSaveInstanceState fragment.
	 * 
	 * @param outState
	 *            Bundle Object
	 */
	public void onSaveInstanceState(Bundle outState) {
		Session session = Session.getActiveSession();
		Session.saveSession(session, outState);
		mFbUiHelper.onSaveInstanceState(outState);
		outState.putString(PENDING_ACTION_BUNDLE_KEY, pendingAction.name());
	}

	public void onResume() {
		ALog.i(TAG,
				"onResume contactUsFragment   Session.getActiveSession() : "
						+ Session.getActiveSession());
		mFbUiHelper.onResume();
	}

	private Session.StatusCallback sessionCalback = new Session.StatusCallback() {

		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			if (pendingAction != PendingAction.NONE
					&& (exception instanceof FacebookOperationCanceledException || exception instanceof FacebookAuthorizationException)) {
				ALog.i(TAG, "Facebook session failure");
				pendingAction = PendingAction.NONE;
			} else if (state == SessionState.OPENED_TOKEN_UPDATED) {
				handlePendingAction();
			}
		}
	};
}
