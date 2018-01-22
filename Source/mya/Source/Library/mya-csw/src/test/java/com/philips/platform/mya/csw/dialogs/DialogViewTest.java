package com.philips.platform.mya.csw.dialogs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.philips.platform.mya.csw.mock.FragmentActivityMock;
import com.philips.platform.mya.csw.mock.FragmentManagerMock;
import com.philips.platform.mya.csw.mock.FragmentTransactionMock;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.view.widget.Button;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
@PrepareForTest(UIDHelper.class)
public class DialogViewTest {

    @Test
    public void onClickingOkayInDialogCallsListeners() {
        givenDialogViewIsCreatedWithCustomListener(listener);
        whenShowingDialog();
        thenOnClickListenerSetOnOkayButtonIs(listener);
    }


    private void givenDialogViewIsCreatedWithCustomListener(final View.OnClickListener listener) {
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



    private void thenOnClickListenerSetOnOkayButtonIs(View.OnClickListener listener) {
        verify(okButton).setOnClickListener(listener);
    }


    private FragmentActivityMock fragmentActivity = new FragmentActivityMock(new FragmentManagerMock(new FragmentTransactionMock()));
    private DialogView dialogView;
    private MockOkayButtonListener listener = new MockOkayButtonListener();

    @Mock
    private Button okButton;
}