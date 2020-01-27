/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.screens;

import android.os.Bundle;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.TestUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class CvvCvcDialogFragmentTest {
    private CvvCvcDialogFragment cvvCvcDialogFragment;

    @Mock
    private View viewMock;

    @Mock
    private EditText editTextCvvDigitsMock;

    @Mock
    private Button buttonContinueMock;

    @Mock
    private Button buttonnotNowMock;

    @Mock
    private TextWatcher textWatcherMock;

    @Before
    public void setUp() {
        initMocks(this);
        cvvCvcDialogFragment = new CvvCvcDialogFragment();
        TestUtils.getStubbedStore();
        TestUtils.getStubbedHybrisDelegate();
    }

    @Test(expected = RuntimeException.class)
    public void shouldDisplayAddressSelectionFragment() {
//        SupportFragmentTestUtil.startFragment(cvvCvcDialogFragment);
    }

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