package com.philips.cl.di.dev.pa.digitalcare.twitter;

import java.io.File;
import java.io.InputStream;

import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.cl.di.dev.pa.digitalcare.R;
import com.philips.cl.di.dev.pa.digitalcare.customview.FontButton;

public class MainActivity extends Activity implements OnClickListener {

	/* Shared preference keys */
	private static final String PREF_NAME = "sample_twitter_pref";
	private static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
	private static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
	private static final String PREF_KEY_TWITTER_LOGIN = "is_twitter_loggedin";
	private static final String PREF_USER_NAME = "twitter_user_name";
	private static final String PREF_CUSTOMER_SUPPORT = " @PhilipsCare ";
	private static String PREF_USERNAME;
	private Activity mActivity = this;
	private Uri mImageCaptureUri;

	/* Any number for uniquely distinguish your request */
	public static final int WEBVIEW_REQUEST_CODE = 100;
	private static final int PICK_FROM_CAMERA = 101;
	private static final int PICK_FROM_FILE = 102;

	private ProgressDialog pDialog;

	private static Twitter twitter;
	private static RequestToken requestToken;

	private static SharedPreferences mSharedPreferences;

	private EditText mShareEditText;
	private FontButton mSendPort, mCancelPort, mSendLand, mCancelLand;
	private TextView userName;
	private ImageView mPhoto;
	private ImageView mSocialLoginIcon = null;
	private CheckBox mCheckBox;
	private File mFile;

	private String consumerKey = null;
	private String consumerSecret = null;
	private String callbackUrl = null;
	private String oAuthVerifier = null;

	private LinearLayout mOptionParent = null;
	private FrameLayout.LayoutParams mParams = null;
	private LinearLayout mFacebookParentPort = null;
	private LinearLayout mFacebookParentLand = null;
	InputMethodManager imm;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		consumerKey = getString(R.string.twitter_consumer_key);
		consumerSecret = getString(R.string.twitter_consumer_secret);
		callbackUrl = getString(R.string.twitter_callback);
		oAuthVerifier = getString(R.string.twitter_oauth_verifier);

		/* Enabling strict mode */
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		/* Setting activity layout file */
		setContentView(R.layout.fragment_facebook_screen);
		mShareEditText = (EditText) findViewById(R.id.share_text);
		mSendPort = (FontButton) findViewById(R.id.facebookSendPort);
		mCancelPort = (FontButton) findViewById(R.id.facebookCancelPort);
		mSendLand = (FontButton) findViewById(R.id.facebookSendLand);
		mCancelLand = (FontButton) findViewById(R.id.facebookCancelLand);
		userName = (TextView) findViewById(R.id.fb_Post_FromHeaderText);
		mPhoto = (ImageView) findViewById(R.id.fb_post_camera);
		mSocialLoginIcon = (ImageView) findViewById(R.id.socialLoginIcon);
		mSocialLoginIcon.setImageDrawable(getResources().getDrawable(
				R.drawable.social_twitter_icon));
		mCheckBox = (CheckBox) findViewById(R.id.fb_Post_CheckBox);
		mCheckBox.setChecked(true);
		mShareEditText.setHint("");

		mOptionParent = (LinearLayout) findViewById(R.id.fbPostContainer);
		mParams = (android.widget.FrameLayout.LayoutParams) mOptionParent
				.getLayoutParams();
		mFacebookParentPort = (LinearLayout) findViewById(R.id.facebookParentPort);
		mFacebookParentLand = (LinearLayout) findViewById(R.id.facebookParentLand);
		imm = (InputMethodManager) this
				.getSystemService(Service.INPUT_METHOD_SERVICE);

		mSendPort.setOnClickListener(this);
		mCancelPort.setOnClickListener(this);

		mSendLand.setOnClickListener(this);
		mCancelLand.setOnClickListener(this);
		mPhoto.setOnClickListener(this);
		mCheckBox.setOnClickListener(this);

		/* Check if required twitter keys are set */
		if (TextUtils.isEmpty(consumerKey) || TextUtils.isEmpty(consumerSecret)) {
			Toast.makeText(this, "Twitter key and secret not configured",
					Toast.LENGTH_SHORT).show();
			return;
		}

		/* Initialize application preferences */
		mSharedPreferences = getSharedPreferences(PREF_NAME, 0);

		boolean isLoggedIn = mSharedPreferences.getBoolean(
				PREF_KEY_TWITTER_LOGIN, false);

		/* if already logged in, then hide login layout and show share layout */
		if (isLoggedIn) {

			String username = mSharedPreferences.getString(PREF_USER_NAME, "");
			userName.setText(getResources().getString(R.string.hello)
					+ username);
			PREF_USERNAME = username;

		} else {
			loginToTwitter();

			Uri uri = getIntent().getData();

			if (uri != null && uri.toString().startsWith(callbackUrl)) {

				String verifier = uri.getQueryParameter(oAuthVerifier);

				try {

					AccessToken accessToken = twitter.getOAuthAccessToken(
							requestToken, verifier);

					long userID = accessToken.getUserId();
					final User user = twitter.showUser(userID);
					final String username = user.getName();

					/* save updated token */
					saveTwitterInfo(accessToken);

					userName.setText(getString(R.string.hello) + username);
					PREF_USERNAME = username;

				} catch (Exception e) {
					Log.e("Failed to login Twitter!!", e.getMessage());
				}
			}
		}
		android.content.res.Configuration config = this.getResources()
				.getConfiguration();
		setViewParams(config);
	}

	@Override
	public void onConfigurationChanged(
			android.content.res.Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		setViewParams(newConfig);
	}

	private void setViewParams(android.content.res.Configuration config) {
		int mLeftRightMarginPort = (int) this.getResources().getDimension(
				R.dimen.activity_margin_port);
		int mLeftRightMarginLand = (int) this.getResources().getDimension(
				R.dimen.activity_margin_land);
		if (config.orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT) {
			mFacebookParentPort.setVisibility(View.VISIBLE);
			mFacebookParentLand.setVisibility(View.GONE);
			mParams.leftMargin = mParams.rightMargin = mLeftRightMarginPort;
		} else {
			mFacebookParentLand.setVisibility(View.VISIBLE);
			mFacebookParentPort.setVisibility(View.GONE);
			mParams.leftMargin = mParams.rightMargin = mLeftRightMarginLand;
		}
		mOptionParent.setLayoutParams(mParams);
	}

	private void saveTwitterInfo(AccessToken accessToken) {

		long userID = accessToken.getUserId();

		User user;
		try {
			user = twitter.showUser(userID);

			String username = user.getName();

			/* Storing oAuth tokens to shared preferences */
			Editor e = mSharedPreferences.edit();
			e.putString(PREF_KEY_OAUTH_TOKEN, accessToken.getToken());
			e.putString(PREF_KEY_OAUTH_SECRET, accessToken.getTokenSecret());
			e.putBoolean(PREF_KEY_TWITTER_LOGIN, true);
			e.putString(PREF_USER_NAME, username);
			e.commit();

		} catch (TwitterException e1) {
			e1.printStackTrace();
		}
	}

	private void loginToTwitter() {
		boolean isLoggedIn = mSharedPreferences.getBoolean(
				PREF_KEY_TWITTER_LOGIN, false);

		if (!isLoggedIn) {
			final ConfigurationBuilder builder = new ConfigurationBuilder();
			builder.setOAuthConsumerKey(consumerKey);
			builder.setOAuthConsumerSecret(consumerSecret);

			final Configuration configuration = builder.build();
			final TwitterFactory factory = new TwitterFactory(configuration);
			twitter = factory.getInstance();

			try {
				requestToken = twitter.getOAuthRequestToken(callbackUrl);

				/**
				 * Loading twitter login page on webview for authorization Once
				 * authorized, results are received at onActivityResult
				 * */
				final Intent intent = new Intent(this, WebViewActivity.class);
				intent.putExtra(WebViewActivity.EXTRA_URL,
						requestToken.getAuthenticationURL());
				startActivityForResult(intent, WEBVIEW_REQUEST_CODE);

			} catch (TwitterException e) {
				e.printStackTrace();
			}
		} else {

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == Activity.RESULT_OK
				&& requestCode == WEBVIEW_REQUEST_CODE) {
			String verifier = data.getExtras().getString(oAuthVerifier);
			try {
				AccessToken accessToken = twitter.getOAuthAccessToken(
						requestToken, verifier);

				long userID = accessToken.getUserId();
				final User user = twitter.showUser(userID);
				String username = user.getName();

				saveTwitterInfo(accessToken);

				if (username != null)
					userName.setText(MainActivity.this.getResources()
							.getString(R.string.hello) + username);

			} catch (Exception e) {
				Log.e("Twitter Login Failed", e.getMessage());
			}
		}

		Bitmap bitmap = null;
		String path = "";

		if (requestCode == PICK_FROM_FILE) {
			mImageCaptureUri = data.getData();
			path = getRealPathFromURI(mImageCaptureUri); // from Gallery

			if (path == null)
				path = mImageCaptureUri.getPath(); // from File Manager

			if (path != null){
				bitmap = BitmapFactory.decodeFile(path);
			mFile = new File(path);
			}
		}

		if (requestCode == PICK_FROM_CAMERA && resultCode == Activity.RESULT_OK) {
			path = mImageCaptureUri.getPath();
			bitmap = BitmapFactory.decodeFile(path);
			mFile = new File(path);
		}

		mPhoto.setImageBitmap(bitmap);

		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.fb_Post_CheckBox:
			if (!((CheckBox) v).isChecked()) {
				mShareEditText.setText("");
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				if (imm != null) {
					imm.toggleSoftInput(0,
							InputMethodManager.HIDE_IMPLICIT_ONLY);
				}

			} else if (((CheckBox) v).isChecked()) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				if (imm != null) {
					imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
				}
			}

			break;

		case R.id.facebookCancelPort:
			finish();
			break;

		case R.id.facebookCancelLand:
			finish();
			break;

		case R.id.fb_post_camera:
			chooseImage();
			dialog.show();

			break;

		case R.id.facebookSendPort:
			final String status = mShareEditText.getText().toString();

			if (status.trim().length() > 0) {
				{
					String tweet = PREF_CUSTOMER_SUPPORT + status;
					new updateTwitterStatus().execute(tweet);

				}
			} else {
				Toast.makeText(this, "Message is empty!!", Toast.LENGTH_SHORT)
						.show();
			}
			break;

		case R.id.facebookSendLand:
			final String status1 = mShareEditText.getText().toString();

			if (status1.trim().length() > 0) {
				{
					String tweet = PREF_CUSTOMER_SUPPORT + status1;
					new updateTwitterStatus().execute(tweet);

				}
			} else {
				Toast.makeText(this, "Message is empty!!", Toast.LENGTH_SHORT)
						.show();
			}
			break;

		}
	}

	AlertDialog dialog;

	private void chooseImage() {
		final String[] items = new String[] { "Take Photo", "Choose from Library",
				"Cancel" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.select_dialog_item, items);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle("Select Image");
		builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				if (item == 0) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					File file = new File(Environment
							.getExternalStorageDirectory(), "tmp_avatar_"
							+ String.valueOf(System.currentTimeMillis())
							+ ".jpg");
					mImageCaptureUri = Uri.fromFile(file);

					try {
						intent.putExtra(
								android.provider.MediaStore.EXTRA_OUTPUT,
								mImageCaptureUri);
						intent.putExtra("return-data", true);

						startActivityForResult(intent, PICK_FROM_CAMERA);
					} catch (Exception e) {
						e.printStackTrace();
					}

					dialog.cancel();
				} else if (item == 1) {
					Intent intent = new Intent();

					intent.setType("image/*");
					intent.setAction(Intent.ACTION_GET_CONTENT);

					startActivityForResult(Intent.createChooser(intent,
							"Complete action using"), PICK_FROM_FILE);
				} else {
					dialog.dismiss();
				}
			}
		});

		dialog = builder.create();
	}

	// Select Imagepath
	private String getRealPathFromURI(Uri contentUri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(contentUri, proj, null, null, null);

		if (cursor == null)
			return null;

		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

		cursor.moveToFirst();

		return cursor.getString(column_index);
	}

	class updateTwitterStatus extends AsyncTask<String, String, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage("Posting to twitter...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected Void doInBackground(String... args) {

			String status = args[0];
			try {
				ConfigurationBuilder builder = new ConfigurationBuilder();
				builder.setOAuthConsumerKey(consumerKey);
				builder.setOAuthConsumerSecret(consumerSecret);

				// Access Token
				String access_token = mSharedPreferences.getString(
						PREF_KEY_OAUTH_TOKEN, "");
				// Access Token Secret
				String access_token_secret = mSharedPreferences.getString(
						PREF_KEY_OAUTH_SECRET, "");

				AccessToken accessToken = new AccessToken(access_token,
						access_token_secret);
				Twitter twitter = new TwitterFactory(builder.build())
						.getInstance(accessToken);

				// Update status
				StatusUpdate statusUpdate = new StatusUpdate(status);
				
		
				/*InputStream is = getResources().openRawResource(
						R.drawable.lakeside_view);*/
				
				statusUpdate.setMedia(mFile);

				twitter4j.Status response = twitter.updateStatus(statusUpdate);

				Log.d("Status", response.getText());

			} catch (TwitterException e) {
				Log.d("Failed to post!", e.getMessage());
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			/* Dismiss the progress dialog after sharing */
			pDialog.dismiss();

			Toast.makeText(MainActivity.this, "Posted to Twitter!",
					Toast.LENGTH_SHORT).show();

			// Clearing EditText field
			mShareEditText.setText("");
		}

	}

}