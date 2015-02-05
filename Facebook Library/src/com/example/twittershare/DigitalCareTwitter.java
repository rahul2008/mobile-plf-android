package com.example.twittershare;

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
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DigitalCareTwitter extends Activity {

	/* Shared preference keys */
	private static final String PREF_NAME = "sample_twitter_pref";
	private static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
	private static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
	private static final String PREF_KEY_TWITTER_LOGIN = "is_twitter_loggedin";
	private static final String PREF_USER_NAME = "twitter_user_name";

	/* Any number for uniquely distinguish your request */
	public static final int WEBVIEW_REQUEST_CODE = 100;
	private static final int SELECT_PICTURE = 101;

	private ProgressDialog pDialog;
	private Activity mActivity;

	private static Twitter twitter;
	private static RequestToken requestToken;

	private static SharedPreferences mSharedPreferences;

	private EditText mShareEditText;
	private TextView userName;
	private Button mPost;
	private ImageView mPhoto, mPhotoClose;

	private String consumerKey = null;
	private String consumerSecret = null;
	private String callbackUrl = null;
	private String oAuthVerifier = null;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	consumerKey = getString(R.string.twitter_consumer_key);
		consumerSecret = getString(R.string.twitter_consumer_secret);
		callbackUrl = getString(R.string.twitter_callback);
		oAuthVerifier = getString(R.string.twitter_oauth_verifier);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

	setContentView(R.layout.fragment_twitter_post);

		mShareEditText = (EditText) findViewById(R.id.Twitter_Post_TestComposingEditer);
		userName = (TextView) findViewById(R.id.Twitter_Post_FromHeaderText);
		mPost = (Button) findViewById(R.id.Twitter_Post_Send);
		mPhoto = (ImageView) findViewById(R.id.Twitter_Post_camera);
		mPhotoClose = (ImageView) findViewById(R.id.Twitter_Post_camera_close);

	mPost.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				final String status = "@neymarjr "
						+ mShareEditText.getText().toString();

				if (status.trim().length() > 0) {
					new updateTwitterStatus().execute(status);
				} else {
					Toast.makeText(mActivity, "Message is empty!!",
							Toast.LENGTH_SHORT).show();
				}

			}
		});

		mPhoto.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent pickIntent = new Intent();
				pickIntent.setType("image/*");
				pickIntent.setAction(Intent.ACTION_GET_CONTENT);

				Intent takePhotoIntent = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);

				String pickTitle = "Select or take a new Picture"; // Or get
																	// from
																	// strings.xml
				Intent chooserIntent = Intent.createChooser(pickIntent,
						pickTitle);
				chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
						new Intent[] { takePhotoIntent });

				startActivityForResult(chooserIntent, SELECT_PICTURE);
			}
		});

		

		if (TextUtils.isEmpty(consumerKey) || TextUtils.isEmpty(consumerSecret)) {
			Toast.makeText(this, "Twitter key and secret not configured",
					Toast.LENGTH_SHORT).show();
			return;
		}
		mSharedPreferences = getSharedPreferences(PREF_NAME, 0);

		boolean isLoggedIn = mSharedPreferences.getBoolean(
				PREF_KEY_TWITTER_LOGIN, false);
		if (isLoggedIn) {
			
			  /*loginLayout.setVisibility(View.GONE);
		shareLayout.setVisibility(View.VISIBLE);*/
			 

			String username = mSharedPreferences.getString(PREF_USER_NAME, "");
			userName.setText("From @" + username);

		} else {
			
			/* * loginLayout.setVisibility(View.VISIBLE);
			 * shareLayout.setVisibility(View.GONE);*/
			 

			Uri uri = getIntent().getData();

			if (uri != null && uri.toString().startsWith(callbackUrl)) {

				String verifier = uri.getQueryParameter(oAuthVerifier);

				try {
					AccessToken accessToken = twitter.getOAuthAccessToken(
							requestToken, verifier);
					long userID = accessToken.getUserId();
					final User user = twitter.showUser(userID);
					final String username = user.getName();

					saveTwitterInfo(accessToken);

					
				/*	 * loginLayout.setVisibility(View.GONE);
					 * shareLayout.setVisibility(View.VISIBLE);*/
					 
					userName.setText("From @" + username);

				} catch (Exception e) {
					Log.e("Failed to login Twitter!!", e.getMessage());
				}
			}

		}
	}

	private void saveTwitterInfo(AccessToken accessToken) {

		long userID = accessToken.getUserId();

		User user;
		try {
			user = twitter.showUser(userID);

			String username = user.getName();
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case WEBVIEW_REQUEST_CODE:

				String verifier = data.getExtras().getString(oAuthVerifier);
				try {
					AccessToken accessToken = twitter.getOAuthAccessToken(
							requestToken, verifier);

					long userID = accessToken.getUserId();
					final User user = twitter.showUser(userID);
					String username = user.getName();

					saveTwitterInfo(accessToken);

					/*
					 * loginLayout.setVisibility(View.GONE);
					 * shareLayout.setVisibility(View.VISIBLE);
					 */
			//		userName.setText("From @" + username);

				} catch (Exception e) {
					Log.e("Twitter Login Failed", e.getMessage());
				}

				break;
				
				
			case SELECT_PICTURE:
				  if(data != null
							&& data.getData() != null)
							{


				Uri _uri = data.getData();

				if (_uri != null) {
					Cursor cursor = getContentResolver()
							.query(_uri,
									new String[] { android.provider.MediaStore.Images.ImageColumns.DATA },
									null, null, null);
					cursor.moveToFirst();

					// Link to the image
					final String imageFilePath = cursor.getString(0);
					cursor.close();

	//				mPhoto.setImageURI(Uri.parse(imageFilePath));
				} else if (data.getAction().equals(MediaStore.ACTION_IMAGE_CAPTURE)) {
					Toast.makeText(mActivity, "Camera", Toast.LENGTH_SHORT).show();
				}}
			

			default:
				break;
			}
		}

	/*	if (resultCode == Activity.RESULT_OK
				&& requestCode == WEBVIEW_REQUEST_CODE) {
		} else if (resultCode == Activity.RESULT_OK
				&& requestCode == SELECT_PICTURE &&  {}*/

		super.onActivityResult(requestCode, resultCode, data);
	}

	/*public void onClick(View v) {
		switch (v.getId()) {
		
		  case R.id.btn_login: loginToTwitter(); break; case R.id.btn_share:
		  final String status = mShareEditText.getText().toString();
		  
		  if (status.trim().length() > 0) { new
		  updateTwitterStatus().execute(status); } else { Toast.makeText(this,
		  "Message is empty!!", Toast.LENGTH_SHORT).show(); } break; }
		 
		}
	}*/

    private ProgressDialog pDoalog;

	class updateTwitterStatus extends AsyncTask<String, String, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(DigitalCareTwitter.this);
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
				String access_token = mSharedPreferences.getString(
						PREF_KEY_OAUTH_TOKEN, "");
				String access_token_secret = mSharedPreferences.getString(
						PREF_KEY_OAUTH_SECRET, "");

				AccessToken accessToken = new AccessToken(access_token,
						access_token_secret);
				Twitter twitter = new TwitterFactory(builder.build())
						.getInstance(accessToken);

				// Update status
				StatusUpdate statusUpdate = new StatusUpdate(status);
			InputStream is = getResources().openRawResource(
						R.drawable.lakeside_view);
				statusUpdate.setMedia("test.jpg", is);

				twitter4j.Status response = twitter.updateStatus(statusUpdate);

				Log.d("Status", response.getText());

			} catch (TwitterException e) {
				Log.d("Failed to post!", e.getMessage());
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			pDialog.dismiss();

			Toast.makeText(DigitalCareTwitter.this, "Posted to Twitter!",
					Toast.LENGTH_SHORT).show();
			mShareEditText.setText("");
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

			/*
			 * loginLayout.setVisibility(View.GONE);
			 * shareLayout.setVisibility(View.VISIBLE);
			 */
		}
	}

}
