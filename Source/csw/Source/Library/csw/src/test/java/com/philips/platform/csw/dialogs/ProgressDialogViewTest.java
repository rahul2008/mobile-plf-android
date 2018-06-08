package com.philips.platform.csw.dialogs;


import android.support.v4.app.FragmentActivity;
import android.view.ViewGroup;

import com.philips.platform.csw.mock.FragmentActivityMock;
import com.philips.platform.csw.mock.FragmentManagerMock;
import com.philips.platform.csw.mock.FragmentTransactionMock;
import com.philips.platform.uid.view.widget.AlertDialogFragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class ProgressDialogViewTest {

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void hideDialog_hidesStateless()  {
        givenProgressDialogViewIsShownWith(mockAlertDialogFragment);
        whenHidingDialog();
        thenAlertFragmentDismissAllowingStateLossIsCalled();
    }

    @Test
    public void showDialog_showsStateless()  {
        givenProgressDialogViewIsSetupWith(mockAlertDialogFragment);
        whenShowingDialog();
        thenAlertFragmentShowAllowingStateLossIsCalled();
    }


    private void givenProgressDialogViewIsShownWith(final ProgressDialogFragment givenAlertDialogFragment) {
        givenProgressDialogViewIsSetupWith(givenAlertDialogFragment);
        whenShowingDialog();
    }

    private void givenProgressDialogViewIsSetupWith(final ProgressDialogFragment givenAlertDialogFragment) {
        progressDialogView = new ProgressDialogView() {

            @Override
            protected void setupView(FragmentActivity activity) {
                this.view = mock(ViewGroup.class);
            }

            @Override
            protected void setupAlertDialogFragment(FragmentActivity activity) {
                this.progressDialogFragment = givenAlertDialogFragment;
            }
        };
    }

    private void whenShowingDialog() {
        progressDialogView.showDialog(fragmentActivity);
    }

    private void whenHidingDialog() {
        progressDialogView.hideDialog();
    }

    private void thenAlertFragmentDismissAllowingStateLossIsCalled() {
        verify(mockAlertDialogFragment).dismissAllowingStateLoss();
    }

    private void thenAlertFragmentShowAllowingStateLossIsCalled() {
        verify(mockAlertDialogFragment, times(1)).showAllowingStateLoss(eq(fragmentActivity.getSupportFragmentManager()), eq(AlertDialogFragment.class.getCanonicalName()));
    }

    private FragmentActivityMock fragmentActivity = new FragmentActivityMock(new FragmentManagerMock(new FragmentTransactionMock()));
    private ProgressDialogView progressDialogView;
    @Mock
    private ProgressDialogFragment mockAlertDialogFragment;

}