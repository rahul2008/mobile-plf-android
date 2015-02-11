package com.philips.cl.di.dev.pa.digitalcare.twitter;

import java.io.File;

import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.philips.cl.di.dev.pa.digitalcare.R;
import com.philips.cl.di.dev.pa.digitalcare.util.ALog;

/**
 * @description Background Task for posting Twitter tweets along with Product Image.
 * @author 310190678
 * @since 11/feb/2015
 *
 */
public class TwitterPost extends AsyncTask<String, String, Void> {

	private String TAG = TwitterPost.class.getSimpleName();
	private ProgressDialog mDialog = null;
	private Context mContext = null;
	private String consumerKey = null;
	private String consumerSecret = null;
	private File mFile = null;

	private static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
	private static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
	public static final String PREF_NAME = "sample_twitter_pref";

	public TwitterPost(Context c, File f) {
		mContext = c;
		mFile = f;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		mDialog = new ProgressDialog(mContext);
		mDialog.setMessage("Posting to PhilipsCare...");
		mDialog.setIndeterminate(false);
		mDialog.setCancelable(false);
		mDialog.show();
	}

	protected Void doInBackground(String... args) {

		String status = args[0];
		try {
			consumerKey = mContext.getString(R.string.twitter_consumer_key);
			consumerSecret = mContext
					.getString(R.string.twitter_consumer_secret);
			ConfigurationBuilder builder = new ConfigurationBuilder();
			builder.setOAuthConsumerKey(consumerKey);
			builder.setOAuthConsumerSecret(consumerSecret);

			SharedPreferences mSharedPreferences = null;
			mSharedPreferences = mContext.getSharedPreferences(PREF_NAME, 0);
			String access_token = mSharedPreferences.getString(
					PREF_KEY_OAUTH_TOKEN, "");
			String access_token_secret = mSharedPreferences.getString(
					PREF_KEY_OAUTH_SECRET, "");

			AccessToken accessToken = new AccessToken(access_token,
					access_token_secret);
			Twitter twitter = new TwitterFactory(builder.build())
					.getInstance(accessToken);
			StatusUpdate statusUpdate = new StatusUpdate(status);
			if (mFile != null)
				statusUpdate.setMedia(mFile);

			twitter4j.Status response = twitter.updateStatus(statusUpdate);

			Log.d(TAG, "Twitter Response" +response.getText());

		} catch (TwitterException e) {
			Log.d(TAG, "Failed to post : "+ e);
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {

		ALog.d(TAG, "Posted to Twitter");
		mDialog.dismiss();
		mFile = null;

	}

}
