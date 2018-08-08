package com.philips.platform.catk;

import android.content.Context;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.consentmanager.ConsentManagerInterface;
import com.philips.platform.pif.chi.ConsentHandlerInterface;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;


@RunWith(PowerMockRunner.class)
@PrepareForTest({ConsentsClient.class})
public class CatkInitializerTest {

    @Mock
    private ConsentsClient consentsClientMock;
    @Mock
    private ConsentManagerInterface consentManagerMock;
    @Mock
    private Context contextMock;
    @Mock
    private AppInfraInterface appInfraMock;


    private CatkInitializer initializer;
    private CatkInputs inputs;

    @Before
    public void setUp() {
        initMocks(this);

        when(appInfraMock.getConsentManager()).thenReturn(consentManagerMock);
        mockStatic(ConsentsClient.class);
        when(ConsentsClient.getInstance()).thenReturn(consentsClientMock);

        initializer = new CatkInitializer();
        inputs = buildInputs(appInfraMock, contextMock);
    }

    @Test(expected = NullPointerException.class)
    public void givenNullInputs_whenInitCatk_thenShouldThrowNullPointer() {
        initializer.initCatk(null);
    }

    @Test
    public void givenValidInputs_whenInitCatk_thenShouldCallInitConsentClient() {
        initializer.initCatk(inputs);

        verify(consentsClientMock).init((CatkInputs) any());
    }

    @Test
    public void givenValidInputs_whenInitCatk_thenShouldRegisterConsentHandler() {
        initializer.initCatk(inputs);

        verify(consentManagerMock).registerHandler((List<String>)any(), (ConsentHandlerInterface) any());
    }

    @Test
    public void givenValidInputs_whenInitCatk_thenShouldRegisterConsentHandlerWithParams() {
        initializer.initCatk(inputs);

        List<String> expected = Arrays.asList("moment", "coaching", "binary", "research", "analytics", "devicetaggingclickstream");
        verify(consentManagerMock).registerHandler(eq(expected), (ConsentHandlerInterface) any());
    }

    private CatkInputs buildInputs(AppInfraInterface appInfra, Context context) {
        return new CatkInputs.Builder()
                    .setAppInfraInterface(appInfra)
                    .setContext(context)
                    .build();
    }
}