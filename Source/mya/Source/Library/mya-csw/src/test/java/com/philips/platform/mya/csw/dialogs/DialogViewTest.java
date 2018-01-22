package com.philips.platform.mya.csw.dialogs;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.FragmentActivity;

import com.philips.platform.mya.csw.mock.FragmentActivityMock;
import com.philips.platform.mya.csw.mock.FragmentManagerMock;
import com.philips.platform.mya.csw.mock.FragmentTransactionMock;
import com.philips.platform.uid.thememanager.UIDHelper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(UIDHelper.class)
public class DialogViewTest {

    @Test
    public void onClickingOkayInDialogCallsListeners() {
        dialogView.showDialog(null);
        assertTrue(listener.okayWasClicked);
    }

    @Before
    public void setup() {
//        FragmentActivity mock1 = mock(FragmentActivity.class);
        PowerMockito.mockStatic(UIDHelper.class);


        when(UIDHelper.getPopupThemedContext(any(Context.class))).thenReturn(fragmentActivity);
        dialogView = new DialogView();
        dialogView.addListener(listener);
    }

    private FragmentActivityMock fragmentActivity = new FragmentActivityMock(new FragmentManagerMock(new FragmentTransactionMock()));
    private DialogView dialogView;
    private MockOkayButtonListener listener = new MockOkayButtonListener();


}