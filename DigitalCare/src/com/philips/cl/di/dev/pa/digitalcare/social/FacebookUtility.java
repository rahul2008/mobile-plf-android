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
 * FacebookUtility will help to provide options for facebook utility.
 * 
 * Author : Ritesh.jha@philips.com
 * 
 * Creation Date : 5 Feb 2015
 */

public class FacebookUtility {
	private static Activity mActivity = null;

	private static final String TAG = "ContactUsFragment";

	private final static String PERMISSION = "publish_actions";
	// private Uri selectedImageUri = null;
<<<<<<< HEAD:DigitalCare/src/com/philips/cl/di/dev/pa/digitalcare/util/FacebookUtility.java
	private final static String EMAIL = "email";
	private final static String BIRTHDAY = "user_birthday";
	private final static String HOMETOWN = "user_hometown";
	private final static String LOCATION = "user_location";
	private final static String PENDING_ACTION_BUNDLE_KEY = "com.facebook.samples.hellofacebook:PendingAction";
	private static Uri imageFileUri = null;
	private static Uri selectedImageUri = null;
	private final static String IMAGE_DIRECTORY_NAME = "DigitalCare";
=======
	private final String EMAIL = "email";
	private final String BIRTHDAY = "user_birthday";
	private final String HOMETOWN = "user_hometown";
	private final String LOCATION = "user_location";
	private final String PENDING_ACTION_BUNDLE_KEY = "com.facebook.samples.hellofacebook:PendingAction";
>>>>>>> 6d07ff46305162add796c5120e119b2ce506cc87:DigitalCare/src/com/philips/cl/di/dev/pa/digitalcare/social/FacebookUtility.java
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
<<<<<<< HEAD:DigitalCare/src/com/philips/cl/di/dev/pa/digitalcare/util/FacebookUtility.java

		initViews();

		openActiveSession(true,
				Arrays.asList(EMAIL, BIRTHDAY, HOMETOWN, LOCATION), null);
		showShareAlert(null, mAllowNoSession); // post only text on FB
	}

	private void initViews() {
		popShareImage = (ImageView) myView.findViewById(R.id.share_image);
		editStatus = (EditText) myView.findViewById(R.id.share_text);

		mCamera = (ImageView) myView.findViewById(R.id.fb_post_camera);
		mCamera.setOnClickListener(clickListener);
		editStatus.setText(mActivity.getResources().getString(
				R.string.SocialSharingPostTemplateText));
		editStatus.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence pEnteredText, int start,
					int before, int count) {
				/** Do nothing... */
			}

			@Override
			public void beforeTextChanged(CharSequence pEnteredText, int start,
					int count, int after) {
				/** Do nothing... */
			}

			@Override
			public void afterTextChanged(Editable s) {
				// if (s.length() == 0 && bitmap == null) {
				// popShare.setEnabled(false);
				// } else {
				// popSharePort.setEnabled(true);
				// }
			}
		});
	}

	private void previewCapturedImage() {
		try {
			Bitmap cameraImage = BitmapFactory.decodeFile(imageFileUri
					.getPath());
			showShareAlert(cameraImage, true);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
=======
>>>>>>> 6d07ff46305162add796c5120e119b2ce506cc87:DigitalCare/src/com/philips/cl/di/dev/pa/digitalcare/social/FacebookUtility.java
	}

	public Session Authenticate() {

		return openActiveSession(true,
				Arrays.asList(EMAIL, BIRTHDAY, HOMETOWN, LOCATION), null);
	}

	/**
	 * Facebook Session
	 */
<<<<<<< HEAD:DigitalCare/src/com/philips/cl/di/dev/pa/digitalcare/util/FacebookUtility.java
	private File getOutputMediaFile(int type) {

		/** External sdcard location */
		File mediaStorageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				IMAGE_DIRECTORY_NAME);

		/** Create the storage directory if it does not exist */
		if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
			ALog.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
					+ IMAGE_DIRECTORY_NAME + " directory");
			return null;
		}

		/** Create a media file name */
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
				Locale.getDefault()).format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "IMG_" + timeStamp + ".jpg");
		} else {
			return null;
		}

		return mediaFile;
	}

	public String getFilePath(Activity activity, Uri uri) {
		String[] projection = { MediaColumns.DATA };
		Cursor cursor = activity.getContentResolver().query(uri, projection,
				null, null, null);
		ALog.i(TAG, "cursor : " + cursor);
		int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
		cursor.moveToFirst();
		String returnString = cursor.getString(column_index);
		cursor.close();

		return returnString;
	}
=======
>>>>>>> 6d07ff46305162add796c5120e119b2ce506cc87:DigitalCare/src/com/philips/cl/di/dev/pa/digitalcare/social/FacebookUtility.java

	public void onActivityResultFragment(Activity activity, int requestCode,
			int resultCode, Intent data) {

		Session.getActiveSession().onActivityResult(activity, requestCode,
				resultCode, data);
		new Session.OpenRequest(activity);
<<<<<<< HEAD:DigitalCare/src/com/philips/cl/di/dev/pa/digitalcare/util/FacebookUtility.java

		if (resultCode == RESULT_OK) {
			if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
				isImageAvialable = true;
				previewCapturedImage();
			} else if (requestCode == SELECT_FILE && data != null) {
				isImageAvialable = true;
				selectedImageUri = data.getData();
				String filePath = getFilePath(activity, selectedImageUri);
				BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
				showShareAlert(
						BitmapFactory.decodeFile(filePath, btmapOptions), true);
			}
		}
	}

	private void chooseMediaOptions() {
		AlertDialog.Builder builderSingle = new AlertDialog.Builder(mActivity);
		builderSingle.setTitle("Select One Option:-");
		final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
				mActivity, android.R.layout.select_dialog_singlechoice);
		arrayAdapter.add("Camera");
		arrayAdapter.add("Phone Memory");
		builderSingle.setNegativeButton("cancel",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

		builderSingle.setAdapter(arrayAdapter,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case CAMERA_CAPTURE_IMAGE_REQUEST_CODE:
							pickImage(true);
							break;
						case SELECT_FILE:
							pickImage(false);
							break;
						default:
							break;
						}
					}
				});
		builderSingle.show();
	}

	/**
	 * Alert Dialog for FB post with image and text.
	 * 
	 * @param bitmap
	 *            Bitmap Object.
	 * @param isImageAvialable
	 *            true is Imageview is available.
	 */
	public void showShareAlert(final Bitmap bitmap,
			final boolean isImageAvialable) {
		if (bitmap != null) {
			popShareImage.setVisibility(View.VISIBLE);
			popShareImage.setImageBitmap(bitmap);
			popShareImage.bringToFront();

			mCamera.setVisibility(View.GONE);
		}
=======
>>>>>>> 6d07ff46305162add796c5120e119b2ce506cc87:DigitalCare/src/com/philips/cl/di/dev/pa/digitalcare/social/FacebookUtility.java
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
