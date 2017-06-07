/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.tagging;

import android.app.Activity;
import android.content.BroadcastReceiver;

import java.util.Map;


/**
 * The interface Ai app tagging interface.
 */
public interface AppTaggingInterface {

	/**
	 * Create instance for component ai app tagging interface.
	 * This method to be used by all component to get their respective tagging
	 *
	 * @param componentId      the component id
	 * @param componentVersion the component version
	 * @return the appinfra app tagging interface
	 */
	public AppTaggingInterface createInstanceForComponent(String componentId,
	                                                      String componentVersion);

	/**
	 * Sets previous page.
	 *
	 * @param previousPage the previous page
	 */
	public void setPreviousPage(String previousPage);

	/**
	 * Gets privacy consent.
	 *
	 * @return the privacy consent
	 */
	public PrivacyStatus getPrivacyConsent();

	/**
	 * Sets privacy consent.
	 *
	 * @param privacyStatus the privacy status
	 */
	public void setPrivacyConsent(PrivacyStatus privacyStatus);

	/**
	 * Track page with info.
	 *
	 * @param pageName the page name
	 * @param key      the key
	 * @param value    the value
	 */
	public void trackPageWithInfo(String pageName, String key, String value);

	/**
	 * Track page with info.
	 *
	 * @param pageName  the page name
	 * @param paramDict set of key/value pairs to be added to the tracking entry
	 */
	public void trackPageWithInfo(String pageName, Map<String, String> paramDict);

	/**
	 * Track action with info.
	 *
	 * @param pageName the page name
	 * @param key      the key
	 * @param value    the value
	 */
	public void trackActionWithInfo(String pageName, String key, String value);

	/**
	 * Track action with info.
	 *
	 * @param pageName  the page name
	 * @param paramDict set of key/value pairs to be added to the tracking entry
	 */
	public void trackActionWithInfo(String pageName, Map<String, String> paramDict);

	/**
	 * Collect LifeCycle info.
	 *
	 * @param context   the page name
	 * @param paramDict set of key/value pairs to be added to the tracking entry
	 */
	public void collectLifecycleInfo(Activity context, Map<String, Object> paramDict);

	/**
	 * Collect LifeCycle info.
	 *
	 * @param context the page name
	 */
	public void collectLifecycleInfo(Activity context);

	/**
	 * Pause LifeCycle info.
	 */
	public void pauseLifecycleInfo();

	/**
	 * Track Video started with a videoName.
	 *
	 * @param videoName user friendly name of video being played.
	 */
	void trackVideoStart(String videoName);

	/**
	 * Track Video End with a videoName.
	 *
	 * @param videoName user friendly name of video being ended.
	 */
	void trackVideoEnd(String videoName);

	/**
	 * Track social sharing with social media like facebook, twitter, mail etc…
	 *
	 * @param medium     SocialMedium=enum:{Facebook, Twitter, Mail, AirDrop}
	 * @param sharedItem sharedItem is the object being shared
	 */
	void trackSocialSharing(SocialMedium medium, String sharedItem);

	/**
	 * Track URL destination
	 *
	 * @param url url destination.
	 */
	void trackLinkExternal(String url);

	/**
	 * Track file being downloaded.
	 *
	 * @param filename String filename.
	 */
	void trackFileDownload(String filename);

	/**
	 * Track Timed Action Start.
	 *
	 * @param actionStart String filename.
	 */
	void trackTimedActionStart(String actionStart);

	/**
	 * Track Timed Action end.
	 *
	 * @param actionEnd String filename.
	 */
	void trackTimedActionEnd(String actionEnd);

	/**
	 * get Privacy Consent For SensitiveData.
	 *
	 * @return returns consent value true or false
	 */
	boolean getPrivacyConsentForSensitiveData();

	/**
	 * sets Privacy Consent For SensitiveData.
	 *
	 * @param valueContent String filename.
	 */
	void setPrivacyConsentForSensitiveData(boolean valueContent);

	/**
	 * @return an String value containing the tracking identifier
	 * @brief Retrieves the analytics tracking identifier
	 * @note This method can cause a blocking network call and should not be used from a UI thread.
	 */
	String getTrackingIdentifier();

	/**
	 * UnRegister for the Tagging data .
	 * @param receiver BroadcastReceiver
	 */
	void unregisterTaggingData(BroadcastReceiver receiver);

	/**
	 * Register for the Tagging data.
	 *
	 * @param receiver BroadcastReceiver
	 */
	void registerTaggingData(BroadcastReceiver receiver);

	/**
	 * The enum Privacy status.
	 */
	enum PrivacyStatus {
		OPTIN, OPTOUT, UNKNOWN
	}

	enum SocialMedium {
		Facebook("facebook"),
		Twitter("twitter"),
		Mail("mail"),
		AirDrop("airdrop");
		private final String socialMedium;

		SocialMedium(String socialMedium) {
			this.socialMedium = socialMedium;
		}

		public String toString() {
			return this.socialMedium;
		}
	}
}


