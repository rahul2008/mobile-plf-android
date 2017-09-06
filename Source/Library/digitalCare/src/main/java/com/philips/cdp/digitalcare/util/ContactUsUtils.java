
package com.philips.cdp.digitalcare.util;

import android.content.Context;
import android.util.Log;

import com.philips.cdp.digitalcare.BuildConfig;
import com.philips.cdp.digitalcare.DigitalCareConfigManager;
import com.philips.cdp.digitalcare.R;

public class ContactUsUtils {

    private static String FACEBOOKURL = "https://www.facebook.com/";
    private static String FACEBOOKINAPPURL = "fb://page/";

    public String serviceDiscoveryFacebookUrl(){

        String serviceDiscoveryUrl = DigitalCareConfigManager.getInstance().getFbUrl();

        if(serviceDiscoveryUrl == null){
            return null;
        } else {
            return serviceDiscoveryUrl;
        }
    }

    public String facebooAppUrl(Context context){

        String facebookPageID = null;
        String facebookAppURL = null;
        String serviceDiscoveryFbURL = serviceDiscoveryFacebookUrl();
        String configFbPageID = context.getString(R.string.facebook_product_pageID);

        if( serviceDiscoveryFbURL!= null){
            String fbSDUrl = serviceDiscoveryFbURL;
            facebookPageID= fbSDUrl.substring(fbSDUrl.lastIndexOf("/") + 1);
        } else if (configFbPageID != null){
            facebookPageID = configFbPageID;
        } else {
            return null;
        }

        facebookAppURL =  FACEBOOKINAPPURL + facebookPageID;

        return facebookAppURL;
    }

    public String facebookWebUrl(Context context){

        String facebookWebUrl = null;
        String serviceDiscoveryFbURL = serviceDiscoveryFacebookUrl();
        String configFbPageID = context.getString(R.string.facebook_product_pageID);

        if( serviceDiscoveryFbURL != null){
            facebookWebUrl = serviceDiscoveryFbURL;
        } else if (configFbPageID != null){
            facebookWebUrl = FACEBOOKURL + context.getString(R.string.facebook_product_pageID);
        }

        return facebookWebUrl;
    }

    public String serviceDiscoveryTwitterUrl(){

        String serviceDiscoveryUrl = DigitalCareConfigManager.getInstance().getTwitterUrl();

        if(serviceDiscoveryUrl == null){
            return null;
        } else {
            return serviceDiscoveryUrl;
        }
    }

    public  String twitterPageName(Context context){

        String twitterPageName = null;

        String serviceDiscoveryTwitterUrl = serviceDiscoveryTwitterUrl();
        String configTwitterPageName = context.getString(R.string.twitter_page);

        if(serviceDiscoveryTwitterUrl != null){
            String twitterPage = serviceDiscoveryTwitterUrl;
            twitterPageName = twitterPage.substring(twitterPage.lastIndexOf("@") + 1);
        }
        else if (configTwitterPageName != null ) {
            twitterPageName = configTwitterPageName;
        }

        return twitterPageName;

    }

    public static String liveChatUrl(Context context){

        String chatLink = null;

        String liveChatApiUrl =  DigitalCareConfigManager.getInstance().getLiveChatUrl();
        String liveChatServiceDiscoveryUrl =  DigitalCareConfigManager.getInstance().getSdLiveChatUrl();
        String liveChatConfigUrl = context.getString(R.string.live_chat_url);

        if( liveChatApiUrl != null ){
            chatLink = liveChatApiUrl;
        }
        else if( liveChatServiceDiscoveryUrl != null ){
            chatLink = liveChatServiceDiscoveryUrl;
        }
        else if ( liveChatConfigUrl != null ) {
            chatLink = liveChatConfigUrl;
        }

        return chatLink;

    }

}

