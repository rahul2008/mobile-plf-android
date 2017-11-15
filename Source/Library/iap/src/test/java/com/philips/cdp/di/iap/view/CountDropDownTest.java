package com.philips.cdp.di.iap.view;

import android.app.Application;
import android.content.Context;
import android.view.View;

import com.philips.cdp.di.iap.BuildConfig;
import com.philips.cdp.di.iap.CustomRobolectricRunner;
import com.philips.cdp.di.iap.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by philips on 9/22/17.
 */
@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class CountDropDownTest {

    private Application mContext;
    @Mock
    View viewMock;

    @Mock
    Context contextMock;
    @Mock
    CountDropDown.CountUpdateListener countUpdateListenerMock;

    CountDropDown countDropDown;

    CountDropDown.CountAdapter countAdapter;

    @Before
    public void setUp() {
        initMocks(this);
        mContext = RuntimeEnvironment.application;
        Mockito.when(viewMock.getContext()).thenReturn(contextMock);
        countDropDown=new CountDropDown(viewMock,mContext, Mockito.anyInt(),Mockito.anyInt(),countUpdateListenerMock);
        countAdapter=countDropDown.new CountAdapter(mContext, R.layout.uikit_simple_list_image_text,Mockito.anyListOf(RowItem.class));
    }

    @Test(expected = NullPointerException.class)
    public void shouldCreatePopUp() throws Exception {
        countDropDown.createPopUp(v, data.getQuantity());

    }
}