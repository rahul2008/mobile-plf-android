package com.philips.cl.di.digitalcare.social;


/**
 * SocialProviderListener is interface for handling custom button handling for social providers.. 
 * 
 * @author : Ritesh.jha@philips.com
 * 
 * @since : 1 June 2015
 */

public interface SocialProviderListener {
	public boolean onSocialProviderItemClicked(String socialProviderItem);
}
