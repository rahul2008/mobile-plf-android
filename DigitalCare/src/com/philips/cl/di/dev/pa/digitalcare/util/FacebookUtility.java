package com.philips.cl.di.dev.pa.digitalcare.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
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
import com.philips.cl.di.dev.pa.digitalcare.R;

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
	private static Uri imageFileUri = null;
	private static Uri selectedImageUri = null;
	private final String IMAGE_DIRECTORY_NAME = "DigitalCare";
	private PendingAction pendingAction = PendingAction.NONE;
	private static final int RESULT_OK = -1;
	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 0;
	private static final int MEDIA_TYPE_IMAGE = 7;
	private static final int SELECT_FILE = 1;

	private UiLifecycleHelper mFbUiHelper;

	private EditText editStatus;
	private ImageView popShareImage;
	private boolean canPresentShareDialog = false;
	private boolean canPresentShareDialogWithPhotos = false;
	private ImageView mCamera = null;
	private View myView = null;
	private static boolean mAllowNoSession = false;
	private static boolean isImageAvialable = false;

	private enum PendingAction {
		NONE, POST_PHOTO, POST_STATUS_UPDATE
	}

	public FacebookUtility(Activity activity, Bundle savedInstanceState,
			View view) {
		mActivity = activity;
		myView = view;
		mFbUiHelper = new UiLifecycleHelper(mActivity, sessionCalback);
		mFbUiHelper.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			String name = savedInstanceState
					.getString(PENDING_ACTION_BUNDLE_KEY);
			pendingAction = PendingAction.valueOf(name);
		}

		initViews();

		Session s = openActiveSession(true,
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
	}

	/**
	 * Capturing Camera Image will lauch camera app requrest image capture
	 */
	private void captureImage() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		imageFileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);
		/** Start the image capture Intent */
		mActivity.startActivityForResult(intent,
				CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
	}

	/**
	 * Take Pic from camera or gallery.
	 * 
	 * @param isCapturePic
	 *            true if want take pic.
	 */
	private void pickImage(boolean isCapturePic) {
		if (isCapturePic) {
			captureImage();
		} else {
			Intent intent = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			intent.setType("image/*");
			mActivity.startActivityForResult(
					Intent.createChooser(intent, "Select File"), SELECT_FILE);
		}
	}

	/**
	 * Creating file uri to store image/video
	 * 
	 * @param type
	 *            Type
	 * @return URI
	 */
	public Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}

	/**
	 * Get recorded image or file.
	 * 
	 * @param type
	 *            file type
	 * @return image / video
	 */
	private File getOutputMediaFile(int type) {

		/** External sdcard location */
		File mediaStorageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				IMAGE_DIRECTORY_NAME);

		/** Create the storage directory if it does not exist */
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				ALog.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
						+ IMAGE_DIRECTORY_NAME + " directory");
				return null;
			}
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

	public void onActivityResultFragment(Activity activity, int requestCode,
			int resultCode, Intent data) {

		Session.getActiveSession().onActivityResult(activity, requestCode,
				resultCode, data);
		new Session.OpenRequest(activity);

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
			showShareAlert(null, mAllowNoSession);
			performPublish(PendingAction.POST_STATUS_UPDATE,
					canPresentShareDialog);
		}
	}

	private OnClickListener clickListener = new OnClickListener() {

		public void onClick(View view) {
			int id = view.getId();
			if (id == R.id.fb_post_camera) {
				chooseMediaOptions();
			}
		}
	};

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
