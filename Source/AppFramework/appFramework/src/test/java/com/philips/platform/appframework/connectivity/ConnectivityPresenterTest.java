package com.philips.platform.appframework.connectivity;

import android.content.Context;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

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

    @Before
    public void setUp(){
        connectivityPresenter=new ConnectivityPresenter(view,context);
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