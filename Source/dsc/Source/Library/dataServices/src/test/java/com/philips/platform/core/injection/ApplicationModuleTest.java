package com.philips.platform.core.injection;

import android.content.Context;
import android.os.Handler;

import com.philips.platform.appinfra.AppInfraInterface;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

public class ApplicationModuleTest {

    @InjectMocks
    private ApplicationModule module;

    @Mock
    private Context mockContext;

    @Mock
    private AppInfraInterface appInfra;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        module = new ApplicationModule(mockContext, appInfra);
    }

    @Test
    public void ShouldReturnContextWhenAsked() {
        assertThat(module.providesContext()).isNotNull();
        assertThat(module.providesContext()).isInstanceOf(Context.class);
    }


    @Test
    public void ShouldReturnHandler_WhenProvideHandlerIsCalled() {
        final Handler handler = module.providesHandler();
        assertThat(handler).isNotNull();
        assertThat(handler).isInstanceOf(Handler.class);
    }

    @Test
    public void ShouldReturnSharedPreferences_WhenProvidesSharedPreferencesIsCalled() {
        module.provideSharedPreferences();
    }
}