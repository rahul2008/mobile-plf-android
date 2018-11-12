/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.platform.csw.dialogs;

import android.content.DialogInterface;
import android.view.KeyEvent;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class ProgressDialogFragmentTest {


    @Mock
    private DialogInterface dialog;

    private ProgressDialogFragment progressDialogFragment;
    private boolean result;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        progressDialogFragment=new ProgressDialogFragment();
    }

    @Test
    public void testOnKeyDismissesDialogWhenTheKeyEventIsBackButton() {
        whenOnKeyIsCalled(KeyEvent.KEYCODE_BACK);
        thenDialogDismissIsCalled();
        thenOnKeyReturnsTrue();
    }

    @Test
    public void testOnKeyDonotDismissDialogWhenARandomKeyIsPressed() {
        whenOnKeyIsCalled(KeyEvent.KEYCODE_0);
        thenDialogDismissIsNotCalled();
        thenOnKeyReturnsTrue();
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