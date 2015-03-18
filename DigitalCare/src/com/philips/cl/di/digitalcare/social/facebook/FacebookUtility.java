package com.philips.cl.di.digitalcare.social.facebook;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
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
import com.facebook.model.GraphUser;
import com.philips.cl.di.digitalcare.util.DLog;

/**
 * @description: FacebookUtility will help to provide options for facebook
 *               utility.
 * @author: ritesh.jha@philips.com
 * @since: Feb 5, 2015
 */
public class FacebookUtility {
	private static Activity mActivity = null;

	private static final String TAG = FacebookUtility.class.getSimpleName();

	private final static String PERMISSION = "publish_actions";
	private final static String EMAIL = "email";
	private final static String BIRTHDAY = "user_birthday";
	private final static String HOMETOWN = "user_hometown";
	private final static String LOCATION = "user_location";
	private final static String PENDING_ACTION_BUNDLE_KEY = "com.facebook.samples.hellofacebook:PendingAction";
	private static Bitmap mImageToUpload = null;
	private PendingAction pendingAction = PendingAction.NONE;
	private String mContentDescription = null;
	private UiLifecycleHelper mFbUiHelper;
	private boolean canPresentShareDialog = false;
	private boolean canPresentShareDialogWithPhotos = false;
	private FBAccountCallback mFaceBookAccCallback = null;

	private enum PendingAction {
		NONE, POST_PHOTO, POST_STATUS_UPDATE
	}

	public FacebookUtility(Activity activity, Bundle savedInstanceState,
			View view) {
		mActivity = activity;
		mFbUiHelper = new UiLifecycleHelper(mActivity, sessionCalback);
		mFbUiHelper.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			String name = savedInstanceState
					.getString(PENDING_ACTION_BUNDLE_KEY);
			pendingAction = PendingAction.valueOf(name);
		}

		openActiveSession(true,
				Arrays.asList(EMAIL, BIRTHDAY, HOMETOWN, LOCATION), null);
	}

	protected void setImageToUpload(Bitmap imageToUpload) {
		mImageToUpload = imageToUpload;
	}

	private Bitmap getImageToUpload() {
		return mImageToUpload;
	}

	public void onActivityResultFragment(Activity activity, int requestCode,
			int resultCode, Intent data) {

		Session.getActiveSession().onActivityResult(activity, requestCode,
				resultCode, data);
		new Session.OpenRequest(activity);

	}

	public void performPublishAction(String description,
			FBAccountCallback callback) {
		this.mContentDescription = description;
		this.mFaceBookAccCallback = callback;
		if (getImageToUpload() != null) {
			DLog.i(TAG, "FacebookUtility performPublishAction POST_PHOTO");
			performPublish(PendingAction.POST_PHOTO,
					canPresentShareDialogWithPhotos);
		} else {
			DLog.i(TAG,
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

				try {
					session.requestNewPublishPermissions(new Session.NewPermissionsRequest(
							mActivity, PERMISSION));
				} catch (UnsupportedOperationException exception) {
					DLog.i(TAG,
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
		DLog.i(TAG, "FacebookUtility shareImage image : " + mImageToUpload);
		Request request = Request.newUploadPhotoRequest(
				Session.getActiveSession(), mImageToUpload,
				new Request.Callback() {

					@Override
					public void onCompleted(Response response) {
						String posted = "Shared Image Successful to Facebook";
						DLog.d(TAG, posted);
						Toast.makeText(mActivity, posted, Toast.LENGTH_LONG)
								.show();
					}
				});

		Bundle params = request.getParameters();
		params.putString("message", mContentDescription);
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
	public Session openActiveSession(boolean allowLoginUI,
			List<String> permissions, StatusCallback callback) {
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

	/**
	 * Sharing text in facebook.
	 */
	private void shareStatusUpdate() {
		Request request = Request.newStatusUpdateRequest(
				Session.getActiveSession(), "Session", new Request.Callback() {

					@Override
					public void onCompleted(Response response) {
						DLog.d(TAG,
								"Shared Status Successful to Facebook 'response'"
										+ response);
					}
				});
		Bundle params = request.getParameters();
		params.putString("message", mContentDescription);
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
		DLog.i(TAG,
				"onResume contactUsFragment   Session.getActiveSession() : "
						+ Session.getActiveSession());
		mFbUiHelper.onResume();
	}

	private Session.StatusCallback sessionCalback = new Session.StatusCallback() {

		@Override
		public void call(Session session, SessionState state,
				Exception exception) {

			if (session.isOpened()) {

				Request mRequestName = Request.newMeRequest(session,
						new Request.GraphUserCallback() {

							@Override
							public void onCompleted(GraphUser user,
									Response response) {

								DLog.d(TAG,
										"User Name is : " + user.getFirstName());
								if (user.getFirstName() != null && mFaceBookAccCallback != null) {
									mFaceBookAccCallback.setName(user
											.getFirstName());
								}
							}
						});

				mRequestName.executeAsync();
			}

			if (pendingAction != PendingAction.NONE
					&& (exception instanceof FacebookOperationCanceledException || exception instanceof FacebookAuthorizationException)) {
				DLog.i(TAG, "Facebook session failure");
				pendingAction = PendingAction.NONE;
			} else if (state == SessionState.OPENED_TOKEN_UPDATED) {
				handlePendingAction();
			}
		}
	};

}
