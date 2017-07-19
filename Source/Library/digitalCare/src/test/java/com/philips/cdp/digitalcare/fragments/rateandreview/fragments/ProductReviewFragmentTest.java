package com.philips.cdp.digitalcare.fragments.rateandreview.fragments;

import android.content.res.Configuration;
import android.view.View;
import android.widget.ProgressBar;

import com.philips.cdp.digitalcare.DigitalCareConfigManager;
import com.philips.cdp.digitalcare.R;
import com.philips.cdp.digitalcare.homefragment.DigitalCareBaseFragment;
import com.philips.cdp.digitalcare.util.CustomRobolectricRunnerDigitalCare;
import com.philips.cdp.digitalcare.util.DigitalCareTestMock;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.configuration.MockitoConfiguration;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.Robolectric;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(CustomRobolectricRunnerDigitalCare.class)
@PrepareForTest(DigitalCareConfigManager.class)
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*"})
public class ProductReviewFragmentTest extends MockitoConfiguration{

    private ProductReviewFragment fragment;

    private View rootView;

    @Rule
    public PowerMockRule powerMockRule=new PowerMockRule();

    @Mock
    private DigitalCareConfigManager mockDigitalCareConfigManager;

    @Mock
    AppTaggingInterface mockAppTaggingInterface;

    private DigitalCareBaseFragment digitalCareBaseFragmentspy;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        fragment = new ProductReviewFragment();
        PowerMockito.mockStatic(DigitalCareConfigManager.class);
        when(DigitalCareConfigManager.getInstance()).thenReturn(mockDigitalCareConfigManager);
        when(DigitalCareConfigManager.getInstance().getTaggingInterface()).thenReturn(mockAppTaggingInterface);
        digitalCareBaseFragmentspy=spy(fragment);
        SupportFragmentTestUtil.startFragment(fragment,DigitalCareTestMock.class);
        rootView=fragment.getView();
        Robolectric.buildActivity(DigitalCareTestMock.class).create().get();
    }

    @Test
    public void testViewNotNull() throws Exception {
        Assert.assertNotNull(rootView);
    }

    @Test
    public void testLoadProductpageNull() throws Exception {
        when(mockDigitalCareConfigManager.getProductReviewUrl()).thenReturn("http://www.philips.com");
        fragment.loadProductpage(null);
        ProgressBar mProgressBar =(ProgressBar) rootView.findViewById(
                R.id.common_webview_progress);
        Assert.assertEquals(View.VISIBLE,mProgressBar.getVisibility());
    }

    @Test
    public void testLoadProductpageNotNull() throws Exception {
        when(mockDigitalCareConfigManager.getProductReviewUrl()).thenReturn("http://www.philips.com");
        fragment.loadProductpage("Philips");
        ProgressBar mProgressBar =(ProgressBar) rootView.findViewById(
                R.id.common_webview_progress);
        Assert.assertEquals(View.VISIBLE,mProgressBar.getVisibility());
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

