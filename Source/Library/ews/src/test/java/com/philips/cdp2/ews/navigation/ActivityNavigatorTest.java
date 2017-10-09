package com.philips.cdp2.ews.navigation;

import android.content.Context;

import com.philips.cdp2.ews.troubleshooting.base.BaseDialogActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(BaseDialogActivity.class)
public class ActivityNavigatorTest {

    private ActivityNavigator subject;

    @Mock private Context mockContext;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        subject = new ActivityNavigator(mockContext);

        mockStatic(BaseDialogActivity.class);
    }

    @Test
    public void itShouldShowDialogActivity() throws Exception {
        String fragmentName = "fragmentName";
        subject.showFragment(fragmentName);

        verifyStatic();
        BaseDialogActivity.startActivity(mockContext, fragmentName);
    }
}