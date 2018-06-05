package com.philips.platform.mya.csw.permission;

import com.philips.platform.mya.csw.R;
import com.philips.platform.mya.csw.dialogs.ConfirmDialogView;
import com.philips.platform.mya.csw.dialogs.DialogView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PermissionFragment.class)
public class PermissionFragmentTest {

    @Mock
    private PermissionPresenter permissionPresenter;

    @Mock
    private PermissionContract.View mockView;

    private PermissionFragment permissionFragment;

    @Before
    public void setup() {
        initMocks(this);
        permissionFragment = new PermissionFragment();
        permissionFragment.setPresenter(permissionPresenter);
    }

    @Test
    public void onResume_setsResourceIdOnParentFragment() {
        whenResuming();
        thenSetResourceIdIsInvokedWith(R.string.csw_privacy_settings);
    }

    @Test
    public void onResume_fetchesConsentDefinitions() {
        whenResuming();
        thenConsentDefinitionsAreFetched();
    }

    @Test
    public void onPause_hidesConfirmationDialogIfAny() throws Exception {
        PowerMockito.whenNew(ConfirmDialogView.class).withNoArguments()
                .thenReturn(this.confirmDialogView);
        permissionFragment.showConfirmRevokeConsentDialog(null, null);
        permissionFragment.onPause();
        verify(confirmDialogView).hideDialog();
    }

    @Test
    public void onPause_doesNotHideConfirmationDialogIfNull() {
        permissionFragment.onPause();
        verify(confirmDialogView, never()).hideDialog();
    }

    @Test
    public void onPause_doesNotDisplayErrorDialogView() {
        permissionFragment.onPause();
        permissionFragment.showErrorDialog(false, 1, 1);
        thenErrorIsNotShown(false, 1, 1);
    }

    @Test
    public void getDialogViewWithGoBackReturnsSameInstance() {
        DialogView dialogView1 = permissionFragment.getDialogView(true);
        DialogView dialogView2 = permissionFragment.getDialogView(true);
        assertEquals(dialogView1, dialogView2);
    }

    @Test
    public void getDialogViewWithoutGoingBackCreatesNewInstance() {
        DialogView dialogView1 = permissionFragment.getDialogView(false);
        DialogView dialogView2 = permissionFragment.getDialogView(false);
        assertNotEquals(dialogView1, dialogView2);
    }

    private void whenResuming() {
        permissionFragment.onResume();
    }

    private void thenSetResourceIdIsInvokedWith(int expectedResourceId) {
        assertEquals(expectedResourceId, permissionFragment.getTitleResourceId());
    }

    private void thenConsentDefinitionsAreFetched() {
        verify(permissionPresenter).fetchConsentStates(null);
    }

    private void thenErrorIsNotShown(boolean goBack, int titleRes, int error) {
        verify(mockView, never()).showErrorDialog(goBack, titleRes, error);
    }

    @Mock
    private ConfirmDialogView confirmDialogView;
}