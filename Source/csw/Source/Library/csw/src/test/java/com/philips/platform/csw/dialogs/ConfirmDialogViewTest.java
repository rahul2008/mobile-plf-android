package com.philips.platform.csw.dialogs;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import com.philips.platform.csw.R;
import com.philips.platform.uid.view.widget.AlertDialogFragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ConfirmDialogViewTest {

    @Mock
    private View viewMock;
    @Mock
    private Button buttonMock;
    @Mock
    private Context contextMock;
    @Mock
    private FragmentActivity activityMock;
    @Mock
    private AlertDialogFragment alertMock;

    private ConfirmDialogView confirmDialog;

    @Before
    public void setUp() {
        initMocks(this);
        confirmDialog = createSUT();
    }

    @Test
    public void givenDialogSetup_whenShowDialog_thenShouldUseFragmentManager() {
        confirmDialog.setupDialog(
                new ConfirmDialogTextResources(R.string.mya_csw_consent_revoked_confirm_title, R.string.mya_csw_consent_revoked_confirm_descr, R.string.mya_csw_consent_revoked_confirm_btn_ok, R.string.mya_csw_consent_revoked_confirm_btn_cancel));

        confirmDialog.showDialog(activityMock);

        verify(activityMock).getSupportFragmentManager();
    }

    @Test(expected = IllegalStateException.class)
    public void givenDialogNotSetup_whenShowDialog_thenShouldThrowException() {
        confirmDialog.showDialog(activityMock);
    }

    @Test
    public void hideDialogue_callsDismissOnAlertFragment() {
        givenDialogueIsShown();
        confirmDialog.hideDialog();
        verify(alertMock).dismiss();
    }

    @Test
    public void hideDialogue_DoesNotcallDismissWhenAlertFragmentIsNull() {
        confirmDialog.hideDialog();
        verify(alertMock, never()).dismiss();
    }

    private void givenDialogueIsShown() {
        confirmDialog.setupDialog(
                new ConfirmDialogTextResources(R.string.mya_csw_consent_revoked_confirm_title, R.string.mya_csw_consent_revoked_confirm_descr, R.string.mya_csw_consent_revoked_confirm_btn_ok, R.string.mya_csw_consent_revoked_confirm_btn_cancel));
        confirmDialog.showDialog(activityMock);
    }

    private ConfirmDialogView createSUT() {
        when(viewMock.findViewById(eq(R.id.mya_csw_confirm_dialog_button_ok))).thenReturn(buttonMock);
        when(viewMock.findViewById(eq(R.id.mya_csw_confirm_dialog_button_cancel))).thenReturn(buttonMock);

        return new ConfirmDialogView() {
            @Override
            protected Context getStyledContext(FragmentActivity activity) {
                return contextMock;
            }

            @Override
            protected void setupView(FragmentActivity activity, Context popupThemedContext) {
                view = viewMock;
            }

            @Override
            protected void populateViews() {
                // NOP
            }


            @Override
            protected AlertDialogFragment createDialogFragment(AlertDialogFragment.Builder builder) {
                return alertMock;
            }
        };
    }
}