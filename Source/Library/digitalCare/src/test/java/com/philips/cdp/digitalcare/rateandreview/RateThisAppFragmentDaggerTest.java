package com.philips.cdp.digitalcare.rateandreview;

import android.view.View;

import com.philips.cdp.digitalcare.TestFragment;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

/**
 * Created by philips on 7/10/17.
 */
@RunWith(CustomRobolectricRunner.class)
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*", "com.philips.cdp.ohc.utility.ui.*"})
public class RateThisAppFragmentDaggerTest {

  /* @Rule
   public PowerMockRule powerMockRule=new PowerMockRule();*/

   private RateThisAppFragment _rateThisAppFragment;
   private View _rootView;

    @Before
    public void setUp(){
        _rateThisAppFragment=new RateThisAppFragment();
        SupportFragmentTestUtil.startFragment(_rateThisAppFragment, TestFragment.class);
        _rootView=_rateThisAppFragment.getView();
    }

    @Test
    public void testOnCreateView(){
        Assert.assertNotNull(_rootView);
    }

}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme