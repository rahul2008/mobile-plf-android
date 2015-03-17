package com.philips.cl.di.digitalcare.social.twitter;

import java.io.File;

import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.philips.cl.di.digitalcare.R;
import com.philips.cl.di.digitalcare.social.PostCallback;
import com.philips.cl.di.digitalcare.util.DLog;

/**
 * Background Task for posting Twitter tweets along with Product
 *              Image.
 * @author naveen@philips.com
 * @since 11/feb/2015
 * 
 */
public class TwitterPost extends AsyncTask<String, String, Void> {

	private String TAG = TwitterPost.class.getSimpleName();
	private Context mContext = null;
	private String mConsumerKey = null;
	private String mConsumerSecret = null;
	private File mFile = null;
	private PostCallback mPostCallback = null;
	private static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
	private static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
	public static final String PREF_NAME = "sample_twitter_pref";

	public TwitterPost(Context c, File f, PostCallback callback) {
		mContext = c;
		mFile = f;
		mPostCallback = callback;
		DLog.d(TAG, "TwitterPost Constructor");
	}

	protected Void doInBackground(String... args) {
		DLog.d(TAG, "Twitter POst DoIN backGround++");
		String status = args[0];
		try {
			mConsumerKey = mContext.getString(R.string.twitter_consumer_key);
			mConsumerSecret = mContext
					.getString(R.string.twitter_consumer_secret);
			ConfigurationBuilder builder = new ConfigurationBuilder();
			builder.setOAuthConsumerKey(mConsumerKey);
			builder.setOAuthConsumerSecret(mConsumerSecret);

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

			DLog.d(TAG, "Twitter Response" + response.getText());
			if (mPostCallback != null)
				mPostCallback.onTaskCompleted();

		} catch (TwitterException e) {
			mPostCallback.onTaskFailed();
			DLog.d(TAG, "Failed to post : " + e);

		}
		return null;
	}

}
