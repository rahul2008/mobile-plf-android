package com.philips.platform.mya.csw.permission.uielement;

import android.content.Context;
import android.view.VelocityTracker;
import android.widget.CompoundButton;

import com.philips.platform.mya.csw.BuildConfig;
import com.philips.platform.mya.csw.mock.ContextMock;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

// /@RunWith(RobolectricTestRunner.class)
//@Config(manifest = Config.NONE)
@PrepareForTest(VelocityTracker.class)
@RunWith(MockitoJUnitRunner.class)
public class SilenceableSwitchTest {

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Mock
    private Context contextMock;

    @Mock
    private VelocityTracker velocityTrackerMock;

    @Mock
    private CompoundButton.OnCheckedChangeListener onCheckedChangeListenerMock;

    private SilenceableSwitch sut;

    @Before
    public void setUp() {
        initMocks(this);
        mockStatic(VelocityTracker.class);
        when(VelocityTracker.obtain()).thenReturn(velocityTrackerMock);


        sut = new SilenceableSwitch(new ContextMock());
    }

    @Test
    public void OnCheckedChangedIsNotCalled_WhenChangeListenerIsNull() {
        whenSetOnCheckedChangeListenerIsCalledWith(null);
        thenTheOnCheckedChangedIsNeverCalled();
    }

    private void whenSetOnCheckedChangeListenerIsCalledWith(CompoundButton.OnCheckedChangeListener onCheckedChangeListener) {
        sut.setOnCheckedChangeListener(onCheckedChangeListener);
    }

    private void thenTheOnCheckedChangedIsNeverCalled() {
        verify(onCheckedChangeListenerMock, never()).onCheckedChanged((CompoundButton) any(),anyBoolean());
    }
}