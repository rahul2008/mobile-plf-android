package com.philips.cdp.digitalcare.rateandreview;

import android.view.View;
import android.widget.Button;

import com.philips.cdp.digitalcare.DigitalCareConfigManager;
import com.philips.cdp.digitalcare.R;
import com.philips.cdp.digitalcare.homefragment.DigitalCareBaseFragment;
import com.philips.cdp.digitalcare.productdetails.model.ViewProductDetailsModel;
import com.philips.cdp.digitalcare.util.CustomRobolectricRunnerDigitalCare;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.Robolectric;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by philips on 7/10/17.
 */
@RunWith(CustomRobolectricRunnerDigitalCare.class)
@PrepareForTest(DigitalCareConfigManager.class)
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*", "com.philips.cdp.ohc.utility.ui.*"})
public class RateThisAppFragmentTest {

    @Mock
    private DigitalCareConfigManager mockDigitalCareConfigManager;

    @Rule
    public PowerMockRule powerMockRule=new PowerMockRule();

    private RateThisAppFragment fragment;

    @Mock
    private DigitalCareBaseFragment mockDigitalCareBaseFragment;

    @Mock
    RateThisAppFragmentPresenter  rateThisAppFragmentPresenter;
    private ViewProductDetailsModel mockViewProductDetailsModel;

    private DigitalCareBaseFragment digitalCareBaseFragmentspy;


    View rootView;
    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);


        fragment = new RateThisAppFragment();

        mockViewProductDetailsModel = new ViewProductDetailsModel();
        mockViewProductDetailsModel.setProductName("Maqsood");
        mockViewProductDetailsModel.setCtnName("2234");
        mockViewProductDetailsModel.setProductImage("http://www.philips.com");
        mockViewProductDetailsModel.setProductInfoLink("http://www.philips.com");
        mockViewProductDetailsModel.setDomain("Philips");

        PowerMockito.mockStatic(DigitalCareConfigManager.class);
        when(DigitalCareConfigManager.getInstance()).thenReturn(mockDigitalCareConfigManager);
        when(mockDigitalCareConfigManager.getViewProductDetailsData()).thenReturn(mockViewProductDetailsModel);
        digitalCareBaseFragmentspy=spy(fragment);

        SupportFragmentTestUtil.startFragment(fragment,DigitalCareTestMock.class);
        rootView=fragment.getView();
        Robolectric.buildActivity(DigitalCareTestMock.class).create().get();
    }

    @Test
    public void shouldNotBeNull() throws Exception
    {
        Assert.assertNotNull(rootView);
    }


    @Test
    public void testProductSelectionInit_PRX() throws Exception{

       // Mockito.doNothing().when(mockDigitalCareConfigManager).getViewProductDetailsData();
        //DigitalCareConfigManager.getInstance().setViewProductDetailsData(productDetailsModel);
    }


    @Test
    public void test_onPRXProductPageReceived_onProductLinkNull(){

        ViewProductDetailsModel viewProductDetailsModel=new ViewProductDetailsModel();
        viewProductDetailsModel.setProductInfoLink(null);
        fragment.onPRXProductPageReceived(viewProductDetailsModel);


        Button mRatePhilipsBtn=(Button) rootView.findViewById(
                R.id.tellus_PhilipsReviewButton);
        Assert.assertEquals(View.GONE,mRatePhilipsBtn.getVisibility());
    }


    @Test
    public void test_onPRXProductPageReceived_onProductLinNotNull(){

        ViewProductDetailsModel viewProductDetailsModel=new ViewProductDetailsModel();
        viewProductDetailsModel.setProductInfoLink("adfs");
        fragment.onPRXProductPageReceived(viewProductDetailsModel);

        Button mRatePhilipsBtn=(Button) rootView.findViewById(
                R.id.tellus_PhilipsReviewButton);
        Assert.assertEquals(View.VISIBLE,mRatePhilipsBtn.getVisibility());
    }

    @Test
    public void test_OnCLick(){
        SupportFragmentTestUtil.startFragment(digitalCareBaseFragmentspy, DigitalCareTestMock.class);
       //  Mockito.doNothing().when(digitalCareBaseFragmentspy).showFragment(fragment);
        digitalCareBaseFragmentspy.isInternetAvailable=true;
        digitalCareBaseFragmentspy.getView().findViewById(R.id.tellus_PhilipsReviewButton).performClick();

    }


}