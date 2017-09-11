package com.philips.cdp.digitalcare.util;

import android.content.Context;
import android.provider.Settings;

import com.philips.cdp.digitalcare.BuildConfig;
import com.philips.cdp.digitalcare.DigitalCareConfigManager;
import com.philips.cdp.digitalcare.R;
import com.philips.cdp.digitalcare.activity.DigitalCareActivity;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Created by philips on 8/22/17.
 */

@RunWith(CustomRobolectricRunnerCC.class)
@Config(constants = BuildConfig.class)
public class ContactUsUtilsRTest {

    private ContactUsUtils utils;
    private Context context;

    @Before
    public void setUp() {
        context = RuntimeEnvironment.application.getApplicationContext();
        utils = new ContactUsUtils();
    }

    @Test
    public void testServiceDiscoveryFacebookUrl(){
        String fbURL = "https://www.facebook.com/177538492375159";
        DigitalCareConfigManager.getInstance().setFbUrl(fbURL);
        assertEquals("Value is Same", fbURL, utils.serviceDiscoveryFacebookUrl());
    }

    @Test
    public void testServiceDiscoveryFacebookUrlNull(){
        String fbURL = null;
        DigitalCareConfigManager.getInstance().setFbUrl(fbURL);
        Assert.assertNull(utils.serviceDiscoveryFacebookUrl());
    }

    @Test
    public void testServiceDiscoveryFacebookAppUrl(){
        String fbURL = "fb://page/177538492375159";
        DigitalCareConfigManager.getInstance().setFbUrl(fbURL);
        assertEquals("Value is Same", fbURL, utils.facebooAppUrl(context));
    }

    @Ignore
    @Test
    public void testConfigFacebookUrl(){
        String fbURL = "fb://page/177538492375159";
        DigitalCareConfigManager.getInstance().setFbUrl(null);
        assertEquals("Value is Same", fbURL, utils.facebooAppUrl(context));
    }

    @Test
    public void testServiceDiscoveryFacebookWebUrl(){
        String fbURL = "https://www.facebook.com/177538492375159";
        DigitalCareConfigManager.getInstance().setFbUrl(fbURL);
        assertEquals("Value is Same", fbURL, utils.facebookWebUrl(context));
    }

    @Ignore
    @Test
    public void testConfigFacebookWebUrl(){
        String fbURL = "https://www.facebook.com/177538492375159";
        DigitalCareConfigManager.getInstance().setFbUrl(null);
        assertEquals("Value is Same", fbURL, utils.facebookWebUrl(context));
    }

    @Test
    public void testServiceDiscoveryTwitterUrl(){
        String twitterURL = "https://twitter.com/@PhilipsCare";
        DigitalCareConfigManager.getInstance().setTwitterUrl(twitterURL);
        assertEquals("Value is Same", twitterURL, utils.serviceDiscoveryTwitterUrl());
    }

    @Test
    public void testServiceDiscoveryTwitterUrlNull(){
        String twitterURL = null;
        DigitalCareConfigManager.getInstance().setTwitterUrl(twitterURL);
        Assert.assertNull(utils.serviceDiscoveryTwitterUrl());
    }

    @Test
    public void testServiceDiscoveryTwitterPage(){
        String twitterURL = "https://twitter.com/@PhilipsCare";
        String twitterPage = "PhilipsCare";
        DigitalCareConfigManager.getInstance().setTwitterUrl(twitterURL);
        assertEquals("Value is Same", twitterPage, utils.twitterPageName(context));
    }

    @Ignore
    @Test
    public void testConfigTwitterPage(){
        String twitterPage = "PhilipsCare";
        DigitalCareConfigManager.getInstance().setTwitterUrl(null);
        //System.out.print("****TwitterPage****"+context.getString(R.string.twitter_page));
        assertEquals("Value is Same", twitterPage, utils.twitterPageName(context));
    }

    @Test
    public void testServiceDiscoveryLivechatUrl(){
        String livechatURL = "https://apps.caas.com/Philips/chat/?provider=philips&language=de&country=DE&group=PERSONAL_CARE&category=TOOTHBRUSH_HEADS_CA&sub-category=DIAMONDCLEAN_SU2&ctn=PARAM_CTN";
        DigitalCareConfigManager.getInstance().setSdLiveChatUrl(livechatURL);
        assertEquals("Value is Same", livechatURL, DigitalCareConfigManager.getInstance().getSdLiveChatUrl());
    }

    @Test
    public void testServiceDiscoveryLivechatNull(){
        String livechatURL = null;
        DigitalCareConfigManager.getInstance().setSdLiveChatUrl(livechatURL);
        Assert.assertNull(DigitalCareConfigManager.getInstance().getSdLiveChatUrl());
    }

    @Test
    public void testServiceDiscoveryLivechatUrlResponse(){
        String livechatURL = "https://apps.caas.com/Philips/chat/?provider=philips&language=de&country=DE&group=PERSONAL_CARE&category=TOOTHBRUSH_HEADS_CA&sub-category=DIAMONDCLEAN_SU2&ctn=PARAM_CTN";
        DigitalCareConfigManager.getInstance().setLiveChatUrl(null);
        DigitalCareConfigManager.getInstance().setSdLiveChatUrl(livechatURL);
        assertEquals("Value is Same", livechatURL, ContactUsUtils.liveChatUrl(context));
    }

    @Test
    public void testAPILivechatUrl(){
        String livechatURL = "https://apps.caas.com/Philips/chat/?provider=philips&language=de&country=DE&group=PERSONAL_CARE&category=TOOTHBRUSH_HEADS_CA&sub-category=DIAMONDCLEAN_SU2&ctn=PARAM_CTN";
        DigitalCareConfigManager.getInstance().setLiveChatUrl(livechatURL);
        DigitalCareConfigManager.getInstance().setSdLiveChatUrl(null);
        assertEquals("Value is Same", livechatURL, ContactUsUtils.liveChatUrl(context));
    }

    @Test
    public void testConfigLivechatUrl(){
        String livechatURL = "https://ph-india.livecom.net/5g/ch/?___________________________________________________________=&aid=WuF95jlNIAA%3D&gid=3&skill=undefined&tag=PHILIPS_GEN_GR&cat=&chan=LWC;LVC;LVI&fields=&customattr=Group%3APHILIPS_GEN_GR%3B%20Category%3A%3B%20Sub-category%3A%3B%20CTN%3A%3B%20Country%3AIN%3B%20Language%3AEN&sID=1mOYTHel%2BAI%3D&cID=uENOfpmJKAA%3D&lcId=SMS_IN_EN&url=http%3A%2F%2Fwww.support.philips.com%2Fsupport%2Fcontact%2Ffragments%2Fchat_now_fragment.jsp%3FparentId%3DPB_IN_1%26userCountry%3Din%26userLanguage%3Den&ref=http%3A%2F%2Fwww.support.philips.com%2Fsupport%2Fcontact%2Fcontact_page.jsp%3FuserLanguage%3Den%26userCountry%3Din";
        DigitalCareConfigManager.getInstance().setLiveChatUrl(null);
        DigitalCareConfigManager.getInstance().setSdLiveChatUrl(null);
        assertEquals("Value is Same", livechatURL, ContactUsUtils.liveChatUrl(context));
    }

}
