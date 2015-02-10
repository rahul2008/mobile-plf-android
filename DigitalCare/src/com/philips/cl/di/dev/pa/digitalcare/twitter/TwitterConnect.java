package com.philips.cl.di.dev.pa.digitalcare.twitter;

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
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;

import com.philips.cl.di.dev.pa.digitalcare.R;
import com.philips.cl.di.dev.pa.digitalcare.util.ALog;

public class TwitterConnect {

	private static TwitterConnect mTwitterObject = null;
	private static Activity mContext;
	private static final String TAG = TwitterConnect.class.getSimpleName();
	private TwitterAuth mTwitterAuth = null;

	private TwitterConnect() {
	}

	/* Shared preference keys */
	public static final String PREF_NAME = "sample_twitter_pref";
	private static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
	private static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
	private static final String PREF_KEY_TWITTER_LOGIN = "is_twitter_loggedin";
	public static final String PREF_USER_NAME = "twitter_user_name";

	private String consumerKey = null;
	private String consumerSecret = null;
	private String callbackUrl = null;
	private String oAuthVerifier = null;

	public static final int WEBVIEW_REQUEST_CODE = 100;

	public static TwitterConnect getInstance(Activity activity) {
		mContext = activity;
		if (mTwitterObject != null)
			return mTwitterObject;
		else {
			mTwitterObject = new TwitterConnect();
			return mTwitterObject;
		}
	}

	public static TwitterConnect getInstance() {
		if (mTwitterObject != null)
			return mTwitterObject;
		else {
			mTwitterObject = new TwitterConnect();
			return mTwitterObject;
		}
	}

	private static SharedPreferences mSharedPreferences;

	public void initSDK(TwitterAuth auth) {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		this.mTwitterAuth = auth;
		consumerKey = mContext.getString(R.string.twitter_consumer_key);
		consumerSecret = mContext.getString(R.string.twitter_consumer_secret);
		callbackUrl = mContext.getString(R.string.twitter_callback);
		oAuthVerifier = mContext.getString(R.string.twitter_oauth_verifier);

		if (TextUtils.isEmpty(consumerKey) || TextUtils.isEmpty(consumerSecret)) {
			ALog.d(TAG, "Invalid ConsumerKey & ConsumerSecreat key");
			return;
		}
		mSharedPreferences = mContext.getSharedPreferences(PREF_NAME, 0);
		boolean isLoggedIn = mSharedPreferences.getBoolean(
				PREF_KEY_TWITTER_LOGIN, false);

		if (isLoggedIn) {
			ALog.d(TAG, "Already LoggedIn");

			mTwitterAuth.onTwitterLoginSuccessful();

		} else {
			ALog.d(TAG, "Logging inti Twitter");
			loginToTwitter();

			Uri uri = mContext.getIntent().getData();

			if (uri != null && uri.toString().startsWith(callbackUrl)) {

				String verifier = uri.getQueryParameter(oAuthVerifier);

				try {

					AccessToken accessToken = twitter.getOAuthAccessToken(
							requestToken, verifier);

					saveTwitterInformation(accessToken);
					mTwitterAuth.onTwitterLoginSuccessful();

				} catch (Exception e) {
					Log.e("Failed to login Twitter!!", e.getMessage());
					mTwitterAuth.onTwitterLoginFailed();
				}
			}
		}

	}

	private static Twitter twitter;
	private static RequestToken requestToken;

	private void saveTwitterInformation(AccessToken accessToken) {

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
			mTwitterAuth.onTwitterLoginSuccessful();

		} catch (TwitterException e1) {
			e1.printStackTrace();
			mTwitterAuth.onTwitterLoginFailed();
		}
	}

	/**
	 * Authenticate to Twitter Network.
	 */
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
		} else {

		}
	}

	public void onActivityResult(Intent data) {
		String verifier = data.getExtras().getString(oAuthVerifier);
		try {
			AccessToken accessToken = twitter.getOAuthAccessToken(requestToken,
					verifier);
			saveTwitterInformation(accessToken);

		} catch (Exception e) {
			Log.e("Twitter Login Failed", e.getMessage());
		}
	}

}
