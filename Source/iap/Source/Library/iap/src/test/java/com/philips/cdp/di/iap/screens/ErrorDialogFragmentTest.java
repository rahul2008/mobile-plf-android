/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.screens;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
public class ErrorDialogFragmentTest {

    @Mock
    private View viewMock;

    @Mock
    private TextView dialogTitleMock;

    @Mock
    private TextView dialogDescriptionMock;

    @Mock
    private Button okBtnMock;

    @Mock
    private FragmentManager fragmentManagerMock;

    private ErrorDialogFragment errorDialogFragment;


    @Before
    public void setUp() {
        initMocks(this);
        errorDialogFragment = new ErrorDialogFragment();
        TestUtils.getStubbedStore();
        TestUtils.getStubbedHybrisDelegate();
    }

    @Test(expected = Exception.class)
    public void shouldDisplayAddressSelectionFragment() {
        SupportFragmentTestUtil.startFragment(errorDialogFragment);
    }

    @Test
    public void shouldInitializeViews() throws Exception {

        Bundle bundle=new Bundle();
        errorDialogFragment.setArguments(bundle);
        Mockito.when(viewMock.findViewById(R.id.dialogTitle)).thenReturn(dialogTitleMock);
        Mockito.when(viewMock.findViewById(R.id.dialogDescription)).thenReturn(dialogDescriptionMock);
        Mockito.when(viewMock.findViewById(R.id.btn_dialog_ok)).thenReturn(okBtnMock);
        errorDialogFragment.initializeViews(viewMock);
    }

    @Test
    public void shouldReturnVisibleFragment() throws Exception {
        errorDialogFragment.getVisibleFragment(fragmentManagerMock);
    }

    @Test(expected = NullPointerException.class)
    public void shouldDestroyFragmentAndDismissProgressBar() throws Exception {
        errorDialogFragment.onDestroyView();
    }

    @Test
    public void shouldDetachFragmentAndDismissProgressBar() throws Exception {
        errorDialogFragment.onDetach();
    }
}