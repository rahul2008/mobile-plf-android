/**
 * SocialProviderListener is interface for handling custom button handling for social providers..
 *
 * @author : Ritesh.jha@philips.com
 * @since : 1 June 2015
 *
 *  Copyright (c) 2016 Philips. All rights reserved.
 */

package com.philips.cdp.digitalcare.social;




public interface SocialProviderListener {

    /**
     * Retuns the click event onPressing the respective SocialProvider Button.
     *
     * @param socialProviderItem
     * @return
     */
    public boolean onSocialProviderItemClicked(String socialProviderItem);
}
