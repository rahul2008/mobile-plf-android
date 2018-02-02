package com.philips.platform.mya.csw.dialogs;

import android.support.v4.app.FragmentActivity;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.philips.platform.mya.csw.mock.FragmentActivityMock;
import com.philips.platform.mya.csw.mock.FragmentManagerMock;
import com.philips.platform.mya.csw.mock.FragmentTransactionMock;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.view.widget.AlertDialogFragment;
import com.philips.platform.uid.view.widget.Button;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;

@RunWith(PowerMockRunner.class)
@PrepareForTest(UIDHelper.class)
public class DialogViewTest {


    @Test
    public void onClickingOkayCustomListenerIsCalled() {
        givenDialogViewIsCreatedWithCustomListener(listener);
        whenShowingDialog();
        andOkayButtonIsClicked();
        thenCustomListenerIsInvoked(listener);
    }

    @Test
    public void onClickingOkayCustomListenerIsNotCalled() {
        givenDialogViewIsCreatedWithoutListener();
        whenShowingDialog();
        andOkayButtonIsClicked();
        thenCustomListenerIsNotInvoked(listener);
    }


    private void givenDialogViewIsCreatedWithoutListener() {
        dialogView = new DialogView(){
            @Override
            protected void setupView(FragmentActivity activity) {
                this.view = mock(ViewGroup.class);
            }

            @Override
            protected Button getOkButton() {
                return okButton;
            }

            @Override
            protected void setupAlertDialogFragment(FragmentActivity activity) {
                this.alertDialogFragment = mock(AlertDialogFragment.class);
            }

            @Override
            protected void setupTitleAndText(String title, String body) {

            }

        };
    }

    private void givenDialogViewIsCreatedWithCustomListener(final OnClickListener listener) {
        dialogView = new DialogView(listener) {

          @Override
          protected void setupView(FragmentActivity activity) {
              this.view = mock(ViewGroup.class);
          }

          @Override
          protected Button getOkButton() {
              return okButton;
          }

          @Override
          protected void setupAlertDialogFragment(FragmentActivity activity) {
              this.alertDialogFragment = mock(AlertDialogFragment.class);
          }

          @Override
          protected void setupTitleAndText(String title, String body) {

          }

        };
    }

    private void whenShowingDialog() {
        dialogView.showDialog(fragmentActivity, "title", "body");
    }


    private void andOkayButtonIsClicked() {
        dialogView.onClick(okButton);
    }


    private void thenCustomListenerIsInvoked(MockOkayButtonListener listener) {
        assertTrue(listener.listenerInvoked);
    }

    private void thenCustomListenerIsNotInvoked(MockOkayButtonListener listener) {
        assertFalse(listener.listenerInvoked);
    }

    private FragmentActivityMock fragmentActivity = new FragmentActivityMock(new FragmentManagerMock(new FragmentTransactionMock()));
    private DialogView dialogView;
    private MockOkayButtonListener listener = new MockOkayButtonListener();

    @Mock
    private Button okButton;
}