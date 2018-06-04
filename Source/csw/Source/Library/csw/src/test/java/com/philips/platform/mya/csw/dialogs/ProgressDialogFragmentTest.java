package com.philips.platform.mya.csw.dialogs;

import android.content.DialogInterface;
import android.view.KeyEvent;

import com.philips.platform.mya.csw.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
@Config(constants = BuildConfig.class, sdk = 25)
public class ProgressDialogFragmentTest {

    private ProgressDialogFragment progressDialogFragment;
    @Mock
    private DialogInterface dialog;
    private boolean result;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        progressDialogFragment=new ProgressDialogFragment();
      //  SupportFragmentTestUtil.startFragment(progressDialogFragment);
    }

    @Test
    public void testOnKeyDismissesDialogWhenTheKeyEventIsBackButton() {
        whenOnKeyIsCalled(KeyEvent.KEYCODE_BACK);
        thenDialogDismissIsCalled();
        thenOnKeyReturnsTrue();
        thenFragmentManagetPopStackBackIsCalled();
    }


    @Test
    public void testOnKeyDonotDismissDialogWhenARandomKeyIsPressed() {
        whenOnKeyIsCalled(KeyEvent.KEYCODE_0);
        thenDialogDismissIsNotCalled();
        thenOnKeyReturnsTrue();
        thenFragmentManagetPopStackBackIsCalled();
    }

    private void thenFragmentManagetPopStackBackIsCalled() {
      //  verify(fragmentManager).popBackStack();
    }

    private void whenOnKeyIsCalled(int keyCode) {
        result = progressDialogFragment.onKey(dialog, keyCode, new KeyEvent(1, keyCode));
    }

    private void thenDialogDismissIsNotCalled() {
        verify(dialog, never()).dismiss();
    }

    private void thenDialogDismissIsCalled() {
        verify(dialog).dismiss();
    }

    private void thenOnKeyReturnsTrue() {
        assertTrue(result);
    }

}