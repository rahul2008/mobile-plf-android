package com.philips.platform.mya.csw.permission.adapter;

import android.content.Context;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;

import com.philips.platform.mya.csw.BuildConfig;
import com.philips.platform.mya.csw.R;
import com.philips.platform.mya.csw.permission.HelpClickListener;
import com.philips.platform.mya.csw.utils.PowerRobolectricTest;
import com.philips.platform.mya.csw.utils.ShadowSilenceableSwitch;
import com.philips.platform.mya.csw.utils.TestHelpClickListener;
import com.philips.platform.uid.thememanager.ThemeUtils;
import com.philips.platform.uid.utils.UIDActivity;
import com.philips.platform.uid.view.widget.Switch;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.support.membermodification.MemberMatcher.constructorsDeclaredIn;
import static org.powermock.api.support.membermodification.MemberMatcher.methodsDeclaredIn;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, shadows = {ShadowSilenceableSwitch.class})
@PowerMockIgnore({"javax.xml.*", "org.mockito.*", "org.robolectric.*", "android.*", "com.sun.org.apache.xerces.internal.jaxp.*" })
@PrepareForTest({ThemeUtils.class, Switch.class, SwitchCompat.class})
public class PermissionViewHolderTest extends PowerRobolectricTest {

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    private HelpClickListener listenerMock = new TestHelpClickListener();

    private PermissionViewHolder viewHolder;

    @Before
    public void setUp() {
        initMocks(this);
        PowerMockito.suppress(constructorsDeclaredIn(SwitchCompat.class));
        PowerMockito.suppress(constructorsDeclaredIn(Switch.class));
        mockStatic(ThemeUtils.class);
        when(ThemeUtils.buildColorStateList((Context) any(), anyInt())).thenReturn(null);

        View itemView = LayoutInflater.from(RuntimeEnvironment.application).inflate(R.layout.csw_permission_list_row, null, false);

        viewHolder = new PermissionViewHolder(itemView, itemView.getWidth(), listenerMock, null);
    }

    @Test
    public void test(){
        assertNotNull(viewHolder);
    }
}