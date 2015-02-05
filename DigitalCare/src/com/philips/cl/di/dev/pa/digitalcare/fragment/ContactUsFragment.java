package com.philips.cl.di.dev.pa.digitalcare.fragment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
import com.philips.cl.di.dev.pa.digitalcare.customview.FontButton;
import com.philips.cl.di.dev.pa.digitalcare.util.ALog;

/*
 *	ContactUsFragment will help to provide options to contact Philips.
 * 
 * Author : Ritesh.jha@philips.com
 * 
 * Creation Date : 19 Jan 2014
 */
public class ContactUsFragment extends BaseFragment {
	private LinearLayout mConactUsParent = null;
	private FrameLayout.LayoutParams mParams = null;
	private FontButton mFacebook = null;
	private FontButton mChat = null;
	private FontButton mCallPhilips = null;

	private static final String TAG = "ContactUsFragment";

	private final String PERMISSION = "publish_actions";
	// private Uri selectedImageUri = null;
	private final String EMAIL = "email";
	private final String BIRTHDAY = "user_birthday";
	private final String HOMETOWN = "user_hometown";
	private final String LOCATION = "user_location";
	private final String PENDING_ACTION_BUNDLE_KEY = "com.facebook.samples.hellofacebook:PendingAction";
	private Uri imageFileUri = null;
	private Uri selectedImageUri = null;
	private final String IMAGE_DIRECTORY_NAME = "DigitalCare";
	private PendingAction pendingAction = PendingAction.NONE;
	private final int RESULT_OK = -1;
	private final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
	private final int MEDIA_TYPE_IMAGE = 7;
	private final int SELECT_FILE = 2;

	private UiLifecycleHelper mFbUiHelper;

	private AlertDialog shareDialog;
	private EditText editStatus;
	private ImageView popShareImage;
	private Button popShare;
	private boolean canPresentShareDialog = false;

	private enum PendingAction {
		NONE, POST_PHOTO, POST_STATUS_UPDATE
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mFbUiHelper = new UiLifecycleHelper(getActivity(), sessionCalback);
		mFbUiHelper.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			String name = savedInstanceState
					.getString(PENDING_ACTION_BUNDLE_KEY);
			pendingAction = PendingAction.valueOf(name);
		}

		View view = inflater.inflate(R.layout.fragment_contact_us, container,
				false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mConactUsParent = (LinearLayout) getActivity().findViewById(
				R.id.contactUsParent);
		mChat = (FontButton) getActivity().findViewById(R.id.contactUsChat);
		mFacebook = (FontButton) getActivity().findViewById(
				R.id.socialLoginFacebookBtn);
		mCallPhilips = (FontButton) getActivity().findViewById(
				R.id.contactUsCall);

		mFacebook.setOnClickListener(actionBarClickListener);
		mChat.setOnClickListener(actionBarClickListener);
		mCallPhilips.setOnClickListener(actionBarClickListener);

		mParams = (FrameLayout.LayoutParams) mConactUsParent.getLayoutParams();
		Configuration config = getResources().getConfiguration();
		setViewParams(config);
	}

	// public void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// /** For opening FB session */
	// ALog.i("testing", "onActivityResult resultCode : " + resultCode);
	// // new Session.OpenRequest(getActivity());
	// }

	public void onActivityResultFragment(int requestCode, int resultCode,
			Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
				previewCapturedImage();
			} else if (requestCode == SELECT_FILE && data != null) {
				selectedImageUri = data.getData();
				String filePath = getFilePath(selectedImageUri);
				BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
				showShareAlert(
						BitmapFactory.decodeFile(filePath, btmapOptions), true);

			}
		}
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
		getActivity().startActivityForResult(intent,
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
			getActivity().startActivityForResult(
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

	public String getFilePath(Uri uri) {
		String[] projection = { MediaColumns.DATA };
		Cursor cursor = getActivity().getContentResolver().query(uri,
				projection, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
		cursor.moveToFirst();
		String returnString = cursor.getString(column_index);
		cursor.close();

		return returnString;
	}

	@Override
	public void onConfigurationChanged(Configuration config) {
		super.onConfigurationChanged(config);
		setViewParams(config);
	}

	private OnClickListener actionBarClickListener = new OnClickListener() {

		public void onClick(View view) {
			int id = view.getId();
			if (id == R.id.contactUsChat) {
				showFragment(new ChatFragment());
			} else if (id == R.id.contactUsCall) {
				callPhilips();
			} else if (id == R.id.socialLoginFacebookBtn) {
				Session s = openActiveSession(getActivity(), true,
						Arrays.asList(EMAIL, BIRTHDAY, HOMETOWN, LOCATION),
						null);
//				showShareAlert(null, mAllowNoSession);
				pickImage(false);
			}
		}
	};

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

		AlertDialog.Builder myBuilder;
		myBuilder = new AlertDialog.Builder(getActivity());
		myBuilder.setInverseBackgroundForced(true);
		myBuilder.setCancelable(true);
		LayoutInflater myLayoutInflater = getActivity().getLayoutInflater();
		View myView = myLayoutInflater.inflate(R.layout.share_action_layout,
				null);

		popShareImage = (ImageView) myView.findViewById(R.id.share_image);
		editStatus = (EditText) myView.findViewById(R.id.share_text);
		popShareImage.setImageBitmap(bitmap);
		popShare = (Button) myView.findViewById(R.id.btn_post);
		Button popCancel = (Button) myView.findViewById(R.id.btn_cancel);
		editStatus.setText(getActivity().getResources().getString(
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
				popShare.setEnabled(true);
				// }
			}
		});

		popShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// if (isImageAvialable) {
				// performPublish(PendingAction.POST_PHOTO,
				// canPresentShareDialogWithPhotos);
				// } else {
				performPublish(PendingAction.POST_STATUS_UPDATE,
						canPresentShareDialog);
				// }
			}
		});

		popCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				shareDialog.dismiss();
			}
		});

		shareDialog = myBuilder.create();
		shareDialog.setView(myView, 0, 0, 0, 0);
		shareDialog.show();
	}

	@SuppressWarnings("static-method")
	private boolean hasPublishPermission() {
		Session session = Session.getActiveSession();
		return session != null
				&& session.getPermissions().contains("publish_actions");
	}

	private static boolean mAllowNoSession = false;

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
							getActivity(), PERMISSION));
				} catch (UnsupportedOperationException exception) {
					ALog.i(TAG,
							" UnsupportedOperationException "
									+ exception.getMessage());
				}

				return;
			} else {
				openActiveSession(getActivity(), true,
						Arrays.asList(EMAIL, BIRTHDAY, HOMETOWN, LOCATION),
						null);
			}
		} else if (allowNoSession) {
			pendingAction = action;
			handlePendingAction();
		}
	}

	private void shareImage() {

		shareDialog.dismiss();
		Request request = Request.newUploadPhotoRequest(
				Session.getActiveSession(),
				((BitmapDrawable) popShareImage.getDrawable()).getBitmap(),
				new Request.Callback() {

					@Override
					public void onCompleted(Response response) {
						ALog.d(TAG, "Shared Image Successful to Facebook");
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
	public Session openActiveSession(Activity activity, boolean allowLoginUI,
			List<String> permissions, StatusCallback callback) {
		OpenRequest openRequest = new OpenRequest(activity)
				.setPermissions(permissions).setCallback(callback)
				.setLoginBehavior(SessionLoginBehavior.SUPPRESS_SSO);
		Session session = new Session.Builder(activity).build();
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

		shareDialog.dismiss();
		ALog.i(TAG,
				"Session.getActiveSession() : " + Session.getActiveSession());
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
		super.onPause();
		mFbUiHelper.onPause();
	}

	/**
	 * onResume of fragment.
	 */
	public void onDestroy() {
		super.onDestroy();
		mFbUiHelper.onDestroy();
	}

	/**
	 * onSaveInstanceState fragment.
	 * 
	 * @param outState
	 *            Bundle Object
	 */
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Session session = Session.getActiveSession();
		Session.saveSession(session, outState);
		mFbUiHelper.onSaveInstanceState(outState);
		outState.putString(PENDING_ACTION_BUNDLE_KEY, pendingAction.name());
	}

	@Override
	public void onResume() {
		super.onResume();
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

	private void callPhilips() {
		Intent myintent = new Intent(Intent.ACTION_CALL);
		myintent.setData(Uri.parse("tel:" + "9986202179"));
		myintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(myintent);

	};

	private void setViewParams(Configuration config) {
		if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
			mParams.leftMargin = mParams.rightMargin = mLeftRightMarginPort;
		} else {
			mParams.leftMargin = mParams.rightMargin = mLeftRightMarginLand;
		}
		mConactUsParent.setLayoutParams(mParams);
	}
}
