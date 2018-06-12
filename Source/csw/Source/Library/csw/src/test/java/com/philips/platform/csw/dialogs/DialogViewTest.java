package com.philips.platform.csw.dialogs;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.philips.platform.csw.mock.FragmentActivityMock;
import com.philips.platform.csw.mock.FragmentManagerMock;
import com.philips.platform.csw.mock.FragmentTransactionMock;
import com.philips.platform.uid.view.widget.AlertDialogFragment;
import com.philips.platform.uid.view.widget.Button;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
public class DialogViewTest {

    private FragmentActivityMock fragmentActivity = new FragmentActivityMock(new FragmentManagerMock(new FragmentTransactionMock()));
    private DialogView dialogView;
    private MockOkayButtonListener listener = new MockOkayButtonListener();

    @Mock
    private Button okButton;
    @Mock
    private AlertDialogFragment alertDialogFragmentMock;

    @Before
    public void setUp() {
        initMocks(this);
    }

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

    @Test
    public void dialogIsShownFirstTimeIsCalled() {
        givenDialogViewIsCreatedWithoutListener();
        whenShowingDialog();
        thenNewDialogIsShownTimes(1);
    }

    @Test
    public void dialogIsOnlyShownOnce() {
        givenDialogViewIsCreatedWithoutListener();
        whenShowingDialog();
        whenShowingDialog();
        thenNewDialogIsShownTimes(1);
    }

    private void thenNewDialogIsShownTimes(int times) {
        verify(alertDialogFragmentMock, times(times)).show((FragmentManager) any(), anyString());
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
                this.alertDialogFragment = alertDialogFragmentMock;
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
              this.alertDialogFragment = alertDialogFragmentMock;
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
}