package com.philips.cdp.di.iap.screens;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
public class ErrorDialogFragmentTest {
    private Context mContext;
    private ErrorDialogFragment errorDialogFragment;

    @Before
    public void setUp() {
        initMocks(this);
        errorDialogFragment = new ErrorDialogFragment();
        mContext = RuntimeEnvironment.application;
        TestUtils.getStubbedStore();
        TestUtils.getStubbedHybrisDelegate();
    }

    @Test(expected = Exception.class)
    public void shouldDisplayAddressSelectionFragment() {

        SupportFragmentTestUtil.startFragment(errorDialogFragment);
    }

    @Mock
    View viewMock;

    @Mock
    TextView dialogTitleMock;

    @Mock
    TextView dialogDescriptionMock;

    @Mock
    Button okBtnMock;


    /*  TextView dialogTitle = (TextView) v.findViewById(R.id.dialogTitle);
        dialogTitle.setText(bundle.getString(IAPConstant.SINGLE_BUTTON_DIALOG_TITLE));

        final TextView dialogDescription = (TextView) v.findViewById(R.id.dialogDescription);
        dialogDescription.setText(bundle.getString(IAPConstant.SINGLE_BUTTON_DIALOG_DESCRIPTION));

        Button mOkBtn = (Button) v.findViewById(R.id.btn_dialog_ok);*/

    @Test
    public void shouldInitializeViews() throws Exception {

        Bundle bundle=new Bundle();
        errorDialogFragment.setArguments(bundle);
        Mockito.when(viewMock.findViewById(R.id.dialogTitle)).thenReturn(dialogTitleMock);
        Mockito.when(viewMock.findViewById(R.id.dialogDescription)).thenReturn(dialogDescriptionMock);
        Mockito.when(viewMock.findViewById(R.id.btn_dialog_ok)).thenReturn(okBtnMock);
        errorDialogFragment.initializeViews(viewMock);
    }

    @Mock
    FragmentManager fragmentManagerMock;

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