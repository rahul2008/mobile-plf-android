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
    public void testOnKeyDismissesDialog() {
        whenOnKeyIsCalled();
        thenDialogDismissIsCalled();
        thenOnKeyReturnsTrue();
        thenFragmentManagetPopStackBackIsCalled();
    }

    private void thenFragmentManagetPopStackBackIsCalled() {
      //  verify(fragmentManager).popBackStack();
    }

    private void whenOnKeyIsCalled() {
        result = progressDialogFragment.onKey(dialog, 1, new KeyEvent(1,1));
    }

    private void thenDialogDismissIsCalled() {
        verify(dialog).dismiss();
    }

    private void thenOnKeyReturnsTrue() {
        assertTrue(result);
    }

}