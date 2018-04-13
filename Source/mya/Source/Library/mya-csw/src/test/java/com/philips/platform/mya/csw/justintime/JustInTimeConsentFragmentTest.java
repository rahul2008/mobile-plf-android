package com.philips.platform.mya.csw.justintime;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.mya.csw.BuildConfig;
import com.philips.platform.mya.csw.R;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.util.Collections;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class JustInTimeConsentFragmentTest {

    @Mock
    private AppInfra appInfraMock;
    @Mock
    private JustInTimeWidgetHandler handlerMock;
    @Mock
    private AppTaggingInterface taggingMock;

    private JustInTimeConsentFragment fragment;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        when(appInfraMock.getTagging()).thenReturn(taggingMock);
        when(taggingMock.createInstanceForComponent(anyString(), anyString())).thenReturn(taggingMock);

        JustInTimeConsentDependencies.appInfra = appInfraMock;
        JustInTimeConsentDependencies.consentDefinition = buildConsentDefinition();
        JustInTimeConsentDependencies.completionListener = handlerMock;
        JustInTimeConsentDependencies.textResources = buildTextResources();

        fragment = JustInTimeConsentFragment.newInstance(1);
        SupportFragmentTestUtil.startFragment(fragment);
    }

    @Test
    public void givenFragmentStarted_whenStarted_thenShouldNotCrashDuringStart() {
        assertNotNull(fragment);
    }

    private JustInTimeTextResources buildTextResources() {
        JustInTimeTextResources resources = new JustInTimeTextResources();
        resources.acceptTextRes = R.string.mya_csw_justintime_accept;
        resources.rejectTextRes = R.string.mya_csw_justintime_accept;
        resources.titleTextRes = R.string.mya_csw_justintime_title;
        resources.userBenefitsTitleRes = R.string.mya_csw_justintime_user_benefits_title;
        resources.userBenefitsDescriptionRes = R.string.mya_csw_justintime_user_benefits_description;
        return resources;
    }

    private ConsentDefinition buildConsentDefinition() {
        return new ConsentDefinition(
                R.string.csw_consent_help_label,
                R.string.csw_consent_help_label,
                Collections.singletonList("moment"),
                1);
    }
}