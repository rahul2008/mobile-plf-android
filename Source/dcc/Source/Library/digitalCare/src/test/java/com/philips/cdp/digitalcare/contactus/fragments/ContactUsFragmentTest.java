
/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.digitalcare.contactus.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.view.View;

import com.philips.cdp.digitalcare.ConsumerProductInfo;
import com.philips.cdp.digitalcare.DigitalCareConfigManager;
import com.philips.cdp.digitalcare.R;
import com.philips.cdp.digitalcare.homefragment.DigitalCareBaseFragment;
import com.philips.cdp.digitalcare.util.DigitalCareTestMock;
import com.philips.cdp.digitalcare.util.Utils;
import com.philips.cdp.prxclient.datamodels.summary.Data;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.util.HashMap;

import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.when;

@Ignore
@RunWith(RobolectricTestRunner.class)
@PrepareForTest(DigitalCareConfigManager.class)
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*", "org.apache.xerces", "javax.xml.*", "org.xml.sax.*", "org.w3c.dom.*",  "org.springframework.context.*", "org.apache.log4j.*"})
public class ContactUsFragmentTest {

    private ContactUsFragment fragment;

    private View rootView;

    private Context context;

    @Rule
    public PowerMockRule powerMockRule=new PowerMockRule();

    @Mock
    private DigitalCareConfigManager mockDigitalCareConfigManager;

    @Mock
    AppTaggingInterface mockAppTaggingInterface;

    @Mock
    SharedPreferences mMockSharedPreferences;

    @Mock
    SharedPreferences mMockBrokenSharedPreferences;

    @Mock
    SharedPreferences.Editor mMockEditor;

    @Mock
    SharedPreferences.Editor mMockBrokenEditor;

    @Mock
    Utils mockUtils;

    @Mock
    HashMap map;

    private DigitalCareBaseFragment digitalCareBaseFragmentspy;

    private ContactUsFragment fragmentSpy;

    private ConsumerProductInfo consumerProductInfo;

    @Before
    public void setUp(){
        Robolectric.buildActivity(DigitalCareTestMock.class).create().get();
        MockitoAnnotations.initMocks(this);
        fragment = new ContactUsFragment();
        context = RuntimeEnvironment.application.getApplicationContext();
        PowerMockito.mockStatic(DigitalCareConfigManager.class);
        when(DigitalCareConfigManager.getInstance()).thenReturn(mockDigitalCareConfigManager);
        when(DigitalCareConfigManager.getInstance().getTaggingInterface()).thenReturn(mockAppTaggingInterface);
        digitalCareBaseFragmentspy=spy(fragment);
        fragmentSpy = spy(fragment);
//        SupportFragmentTestUtil.startFragment(fragment,DigitalCareTestMock.class);
        consumerProductInfo = new ConsumerProductInfo();
        consumerProductInfo.setCtn("AqvaShaver");
        consumerProductInfo.setCatalog("Shaver_2234");
        consumerProductInfo.setGroup("http://www.philips.com");
        consumerProductInfo.setProductReviewUrl("http://www.philips.com");
        consumerProductInfo.setSubCategory("Philips");
        consumerProductInfo.setSector("Philips");

        Data data = new Data();
        data.setCtn("sdfsa");
        data.setProductTitle("asfdsa");
        data.setDomain("dfadq");
        data.setDtn("Sdcfsd");

        consumerProductInfo.setCategory(data.getDtn());
        consumerProductInfo.setCategory(data.getCtn());
        consumerProductInfo.setCategory(data.getProductTitle());
        consumerProductInfo.setCategory(data.getProductTitle());

        DigitalCareConfigManager.getInstance().setConsumerProductInfo(consumerProductInfo);
        when(mockDigitalCareConfigManager.getConsumerProductInfo()).thenReturn(consumerProductInfo);
        rootView=fragment.getView();
    }

    @After
    public void tearDown() throws Exception{
        fragment = null;
        digitalCareBaseFragmentspy = null;
        mockDigitalCareConfigManager = null;
        mockAppTaggingInterface = null;
        rootView = null;
        context = null;
    }

    @Test
    public void testInitView() throws Exception {
        when(mockDigitalCareConfigManager.getConsumerProductInfo()).thenReturn(consumerProductInfo);
        Assert.assertNotNull(rootView);
    }

    @Test
    public void testContactUsChat(){
        digitalCareBaseFragmentspy=spy(fragment);
        digitalCareBaseFragmentspy.isInternetAvailable=true;
        digitalCareBaseFragmentspy.getView().findViewById(R.id.contactUsChat).performClick();
    }

    @Test
    public void testContactUsCall(){
        digitalCareBaseFragmentspy=spy(fragment);
        digitalCareBaseFragmentspy.isInternetAvailable=true;
        digitalCareBaseFragmentspy.getView().findViewById(R.id.contactUsCall).performClick();
    }

/*
   @Test
    public void testisContactNumberCachedFalse(){
        digitalCareBaseFragmentspy=spy(fragment);
        Mockito.when(fragmentSpy.isSimAvailable()).thenReturn(false);
        when(mMockSharedPreferences.getString(USER_SELECTED_PRODUCT_CTN_HOURS, "sfcsdfdw"))
                .thenReturn("sfcsdfdw");
        when(mMockSharedPreferences.getString(USER_SELECTED_PRODUCT_CTN_CALL, anyString()))
                .thenReturn("");
        when(mMockEditor.commit()).thenReturn(true);
        when(mMockSharedPreferences.edit()).thenReturn(mMockEditor);
        digitalCareBaseFragmentspy.getView().findViewById(R.id.contactUsCall).performClick();
    }*/

    @Test
    public void testNoPhoneCallFunctionality(){
        digitalCareBaseFragmentspy=spy(fragment);
        Mockito.when(fragmentSpy.isContactNumberCached()).thenReturn(false);
        digitalCareBaseFragmentspy.isInternetAvailable=true;
        Mockito.when(mockUtils.isSimAvailable(context)).thenReturn(true);
        Mockito.when(mockUtils.isTelephonyEnabled(context)).thenReturn(true);
        digitalCareBaseFragmentspy.getView().findViewById(R.id.contactUsCall).performClick();
    }
/*
   @Test
    public void testPhoneCallFunctionality(){
        digitalCareBaseFragmentspy=spy(fragment);
        Mockito.when(fragmentSpy.isContactNumberCached()).thenReturn(false);
        digitalCareBaseFragmentspy.isInternetAvailable=true;
        Mockito.when(mockUtils.isTelephonyEnabled(context)).thenReturn(false);
        Mockito.when(mockUtils.isSimAvailable(context)).thenReturn(true);
        digitalCareBaseFragmentspy.getView().findViewById(R.id.contactUsSocialProvideButtonsParent).performClick();
    }*/



    @Test
    public void testPhoneCallFunctionality(){
        digitalCareBaseFragmentspy=spy(fragment);
        digitalCareBaseFragmentspy.isInternetAvailable=true;
        digitalCareBaseFragmentspy.getView().findViewById(R.id.contactUsSocialProvideButtonsParent).performClick();
    }


    @Test
    public void testGetActionbarTitle(){
        String title = fragment.getActionbarTitle();
        Assert.assertNotNull(title);
    }

    @Test
    public void testSetPreviousPageName(){
        String previousPageName = fragment.setPreviousPageName();
        Assert.assertNotNull(previousPageName);
    }

    @Test
    public void testonConfigurationChanged(){
        Configuration configuration = new Configuration();
        fragment.onConfigurationChanged(configuration);
    }

    @Test
    public void testDestroyMethod(){
        fragment.onDestroy();
    }
}
