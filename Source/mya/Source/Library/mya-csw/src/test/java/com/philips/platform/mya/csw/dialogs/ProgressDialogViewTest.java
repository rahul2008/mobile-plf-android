package com.philips.platform.mya.csw.dialogs;


import android.support.v4.app.FragmentActivity;
import android.view.ViewGroup;

import com.philips.platform.mya.csw.mock.FragmentActivityMock;
import com.philips.platform.mya.csw.mock.FragmentManagerMock;
import com.philips.platform.mya.csw.mock.FragmentTransactionMock;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class ProgressDialogViewTest {

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void hideDialog() throws Exception {
        givenProgressDialogViewIsShownWith(mockAlertDialogFragment);
        whenHidingDialog();
        thenAlertFragmentDismissAllowingStateLossIsCalled();
    }

    private void givenProgressDialogViewIsShownWith(final ProgressDialogFragment givenAlertDialogFragment) {
        progressDialogView = new ProgressDialogView() {

            @Override
            protected void setupView(FragmentActivity activity) {
                this.view = mock(ViewGroup.class);
            }

            @Override
            protected void setupAlertDialogFragment(FragmentActivity activity) {
                this.progressDialogFragment = givenAlertDialogFragment;
            }

            @Override
            public void showDialog(FragmentActivity activity) {
                setupAlertDialogFragment(activity);
                this.isDialogShown = true;
            }

        };
        progressDialogView.showDialog(fragmentActivity);
    }

    private void whenHidingDialog() {
        progressDialogView.hideDialog();
    }

    private void thenAlertFragmentDismissAllowingStateLossIsCalled() {
        verify(mockAlertDialogFragment).dismissAllowingStateLoss();
    }

    private FragmentActivityMock fragmentActivity = new FragmentActivityMock(new FragmentManagerMock(new FragmentTransactionMock()));
    private ProgressDialogView progressDialogView;
    @Mock
    private ProgressDialogFragment mockAlertDialogFragment;

}