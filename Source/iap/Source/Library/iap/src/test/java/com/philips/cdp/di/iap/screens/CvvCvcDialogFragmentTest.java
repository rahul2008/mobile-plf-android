package com.philips.cdp.di.iap.screens;

import android.content.Context;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.philips.cdp.di.iap.BuildConfig;
import com.philips.cdp.di.iap.CustomRobolectricRunner;
import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.TestUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 25)
public class CvvCvcDialogFragmentTest {
    private Context mContext;
    private CvvCvcDialogFragment cvvCvcDialogFragment;

    @Mock
    View viewMock;

    @Before
    public void setUp() {
        initMocks(this);
        cvvCvcDialogFragment = new CvvCvcDialogFragment();
        mContext = RuntimeEnvironment.application;
        TestUtils.getStubbedStore();
        TestUtils.getStubbedHybrisDelegate();
    }

    @Test(expected = RuntimeException.class)
    public void shouldDisplayAddressSelectionFragment() {

        SupportFragmentTestUtil.startFragment(cvvCvcDialogFragment);
    }

    @Mock
    EditText editTextCvvDigitsMock;

    @Mock
    Button buttonContinueMock;

    @Mock
    Button buttonnotNowMock;

    @Mock
    TextWatcher textWatcherMock;

    /* final Button continueBtn = (Button) view.findViewById(R.id.continue_btn);
        final Button notNowBtn = (Button) view.findViewById(R.id.not_now_btn);*/

    @Test
    public void shouldInitializeViews() throws Exception {
        Bundle bundle=new Bundle();
        cvvCvcDialogFragment.setArguments(bundle);
        Mockito.when(viewMock.findViewById(R.id.et_cvv_digits)).thenReturn(editTextCvvDigitsMock);
        Mockito.when(viewMock.findViewById(R.id.continue_btn)).thenReturn(buttonContinueMock);
        Mockito.when(viewMock.findViewById(R.id.not_now_btn)).thenReturn(buttonnotNowMock);
        editTextCvvDigitsMock.addTextChangedListener(textWatcherMock);
        cvvCvcDialogFragment.initializeViews(viewMock);
    }
}