package com.philips.cl.di.dev.digitalcare.social;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.text.TextUtils;

import com.philips.cl.di.dev.digitalcare.R;
import com.philips.cl.di.dev.digitalcare.util.DLog;

public class TwitterConnect {

	private static final String TAG = TwitterConnect.class.getSimpleName();
	private static TwitterConnect mTwitterObject = null;
	private static Activity mContext = null;
	private TwitterAuth mTwitterAuth = null;
	private String consumerKey = null;
	private String consumerSecret = null;
	private String callbackUrl = null;
	private String oAuthVerifier = null;
	private Twitter twitter = null;
	private RequestToken requestToken = null;
	private SharedPreferences mSharedPreferences = null;
	private String verifier = null;

	public static final String PREF_NAME = "sample_twitter_pref";
	private static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
	private static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
	private static final String PREF_KEY_TWITTER_LOGIN = "is_twitter_loggedin";
	public static final String PREF_USER_NAME = "twitter_user_name";

	public static final int WEBVIEW_REQUEST_CODE = 100;

	private TwitterConnect() {
	}

	public static TwitterConnect getInstance(Activity activity) {
		mContext = activity;
		if (mTwitterObject == null)
			mTwitterObject = new TwitterConnect();
		return mTwitterObject;
	}

	public static TwitterConnect getInstance() {
		return mTwitterObject;
	}

	public void initSDK(TwitterAuth auth) {

		this.mTwitterAuth = auth;
		consumerKey = mContext.getString(R.string.twitter_consumer_key);
		consumerSecret = mContext.getString(R.string.twitter_consumer_secret);
		callbackUrl = mContext.getString(R.string.twitter_callback);
		oAuthVerifier = mContext.getString(R.string.twitter_oauth_verifier);

		if (TextUtils.isEmpty(consumerKey) || TextUtils.isEmpty(consumerSecret)) {
			DLog.d(TAG, "Invalid ConsumerKey & ConsumerSecreat key");
			return;
		}
		mSharedPreferences = mContext.getSharedPreferences(PREF_NAME, 0);
		boolean isLoggedIn = mSharedPreferences.getBoolean(
				PREF_KEY_TWITTER_LOGIN, false);

		if (isLoggedIn) {
			DLog.d(TAG, "Already LoggedIn");

			mTwitterAuth.onTwitterLoginSuccessful();

		} else {
			DLog.d(TAG, "Logging inti Twitter");

			Thread mLoginThread = new Thread(new Runnable() {

				@Override
				public void run() {
					loginToTwitter();

				}
			});
			mLoginThread.setName("Twitter Login");
			mLoginThread.setPriority(Thread.MAX_PRIORITY);
			mLoginThread.start();

			Uri uri = mContext.getIntent().getData();

			if (uri != null && uri.toString().startsWith(callbackUrl)) {

				String verifier = uri.getQueryParameter(oAuthVerifier);

				try {

					AccessToken accessToken = twitter.getOAuthAccessToken(
							requestToken, verifier);

					saveTwitterInformation(accessToken);
					mTwitterAuth.onTwitterLoginSuccessful();

				} catch (Exception e) {
					DLog.e("Failed to login Twitter!!", e.getMessage());
					mTwitterAuth.onTwitterLoginFailed();
				}
			}
		}

	}

	private void saveTwitterInformation(AccessToken accessToken) {

		DLog.d(TAG, "Save Twitter Information");
		long userID = accessToken.getUserId();
		DLog.d(TAG, "USer ID : " + userID);

		User user;
		try {
			user = twitter.showUser(userID);
			DLog.d(TAG, "User : " + user.toString());

			String username = user.getName();

			/* Storing oAuth tokens to shared preferences */
			Editor e = mSharedPreferences.edit();
			e.putString(PREF_KEY_OAUTH_TOKEN, accessToken.getToken());
			e.putString(PREF_KEY_OAUTH_SECRET, accessToken.getTokenSecret());
			e.putBoolean(PREF_KEY_TWITTER_LOGIN, true);
			e.putString(PREF_USER_NAME, username);
			e.commit();
			mContext.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					mTwitterAuth.onTwitterLoginSuccessful();

				}
			});

		} catch (TwitterException e1) {
			e1.printStackTrace();
			mTwitterAuth.onTwitterLoginFailed();
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

				final Intent intent = new Intent(mContext,
						WebViewActivity.class);
				intent.putExtra(WebViewActivity.EXTRA_URL,
						requestToken.getAuthenticationURL());
				mContext.startActivityForResult(intent, WEBVIEW_REQUEST_CODE);

			} catch (TwitterException e) {
				e.printStackTrace();
			}
		}
	}

	public void onActivityResult(Intent data) {

		DLog.d(TAG, "Received Twitter session from DigitalCare Activity");
		if (data != null)
			verifier = data.getExtras().getString(oAuthVerifier);
		DLog.d(TAG, "Verifier : " + verifier);

		new Thread(new Runnable() {

			@Override
			public void run() {
				AccessToken accessToken;
				try {
					accessToken = twitter.getOAuthAccessToken(requestToken,
							verifier);
					DLog.d(TAG, "AccessToken : " + accessToken);
					if (accessToken != null)
						saveTwitterInformation(accessToken);
				} catch (Exception e) {
					DLog.e("Twitter Login Failed", "" + e);
					mTwitterAuth.onTwitterLoginFailed();
				}
			}
		}).start();
	}
}
