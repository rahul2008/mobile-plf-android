package com.philips.platform.appframework.connectivity;

import android.content.Context;

import com.philips.cdp.registration.User;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by philips on 03/02/17.
 */

@RunWith(MockitoJUnitRunner.class)
public class ConnectivityPresenterTest {

    @Mock
    ConnectivityContract.View view;

    @Mock
    Context context;

    ConnectivityPresenter connectivityPresenter;

    @Mock
    User user;

    @Before
    public void setUp(){
        connectivityPresenter=new ConnectivityPresenter(view,user,context);
    }
    @Test(expected = IllegalArgumentException.class)
    public void setUpApplicance() throws Exception {
        connectivityPresenter.setUpApplicance(null);
    }

    @Test
    public void getDummyUserMomentTest(){
        Assert.assertNotNull(connectivityPresenter.getDummyUserMoment("50"));
    }

}