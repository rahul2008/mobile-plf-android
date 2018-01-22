package com.philips.platform.mya.csw.dialogs;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;

import com.philips.platform.mya.csw.mock.FragmentActivityMock;
import com.philips.platform.mya.csw.mock.FragmentManagerMock;
import com.philips.platform.mya.csw.mock.FragmentTransactionMock;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.view.widget.Button;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
@PrepareForTest(UIDHelper.class)
public class DialogViewTest {

    @Test
    public void showDialogSetsCustomListenerForButton() {
        givenDialogViewIsCreatedWithCustomListener(listener);
        whenShowingDialog();
        thenListenerSetOnOkayButtonIs(listener);
    }

    @Test
    public void showDialogSetsDefaultListenerForButton() {
        givenDialogViewIsCreatedWithoutListener();
        whenShowingDialog();
        thenDefaultListenerIsSetOnOkayButton();
    }

    private void givenDialogViewIsCreatedWithoutListener() {
        dialogView = new DialogView(){
            @Override
            protected void setupView(FragmentActivity activity) {

            }

            @Override
            protected void setupAlertDialogFragment(FragmentActivity activity) {

            }

            @Override
            protected Button getOkButton() {
                return okButton;
            }

            @Override
            protected void showButton(FragmentActivity activity){

            }
        };
    }

    private void givenDialogViewIsCreatedWithCustomListener(final OnClickListener listener) {
        dialogView = new DialogView(listener) {
          @Override
          protected void setupView(FragmentActivity activity) {

          }

          @Override
          protected void setupAlertDialogFragment(FragmentActivity activity) {

          }

          @Override
          protected Button getOkButton() {
              return okButton;
          }

          @Override
          protected void showButton(FragmentActivity activity){

          }
        };
    }
    private void whenShowingDialog() {
        dialogView.showDialog(fragmentActivity);
    }



    private void thenListenerSetOnOkayButtonIs(OnClickListener listener) {
        verify(okButton).setOnClickListener(listener);
    }



    private void thenDefaultListenerIsSetOnOkayButton() {
        ArgumentCaptor<OnClickListener> captor = ArgumentCaptor.forClass(OnClickListener.class);
        verify(okButton).setOnClickListener(captor.capture());
        assertFalse(captor.getValue() instanceof MockOkayButtonListener);
        assertTrue(captor.getValue() instanceof OnClickListener);
    }

    private FragmentActivityMock fragmentActivity = new FragmentActivityMock(new FragmentManagerMock(new FragmentTransactionMock()));
    private DialogView dialogView;
    private MockOkayButtonListener listener = new MockOkayButtonListener();

    @Mock
    private Button okButton;
}