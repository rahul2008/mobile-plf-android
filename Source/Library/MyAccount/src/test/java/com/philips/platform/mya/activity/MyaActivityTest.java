package com.philips.platform.mya.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.philips.platform.mya.BuildConfig;
import com.philips.platform.mya.MyaConstants;
import com.philips.platform.mya.runner.CustomRobolectricRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.annotation.Config;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.robolectric.Robolectric.buildActivity;

@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 25)
public class MyaActivityTest {

    private Intent intent;
    @Mock
    FragmentManager fragmentManagerMock;
    @Mock
    FragmentTransaction fragmentTransactionMock;
    @Captor
    ArgumentCaptor<Fragment> fragmentArgumentCaptor;
    private MyaActivity activity;

    @Before()
    public void setUp() {
        initMocks(this);
        intent = new Intent();
        intent.putExtra(MyaConstants.MYA_DLS_THEME, 0);
        activity = buildActivity(MyaActivity.class, intent).withIntent(intent).get();
        when(fragmentManagerMock.beginTransaction()).thenReturn(fragmentTransactionMock);
    }

    @Test(expected = NullPointerException.class)
    public void testActivity() {
        activity = buildActivity(MyaActivity.class, intent).withIntent(intent).get();
        MyaActivity spyActivity = Mockito.spy(activity);

        Mockito.doReturn(fragmentManagerMock).when(spyActivity).getSupportFragmentManager();
        spyActivity.onCreate(null);

        verify(fragmentTransactionMock).replace(anyInt(), fragmentArgumentCaptor.capture(), anyString());
    }
}