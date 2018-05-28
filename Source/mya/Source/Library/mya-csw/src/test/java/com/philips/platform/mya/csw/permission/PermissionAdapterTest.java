package com.philips.platform.mya.csw.permission;

import android.app.Fragment;
import android.support.v7.widget.RecyclerView;

import com.philips.platform.mya.csw.BuildConfig;
import com.philips.platform.mya.csw.permission.adapter.PermissionAdapter;
import com.philips.platform.pif.chi.ConsentError;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Collections;

import static junit.framework.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mock;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 25)
@Ignore
public class PermissionAdapterTest {

    private final ConsentDefinition consentDefinition = new ConsentDefinition(1, 2, Collections.singletonList("consentType"), 1);
    private final ConsentView consentView = new ConsentView(consentDefinition);
    private PermissionAdapter permissionAdapter;
    private final ConsentError error = new ConsentError("test error", 123);
    private final ConsentError versionMismatchError = new ConsentError("test error", 1252);
    private Fragment mockFragment;

    @Mock
    private RecyclerView recyclerView;

    private int onItemRangeChangedPositionStart;
    private int onItemRangeChangedItemCount;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RuntimeEnvironment.application = spy(RuntimeEnvironment.application);

        permissionAdapter = new PermissionAdapter(Collections.singletonList(consentView), new HelpClickListener() {
            @Override
            public void onHelpClicked(int helpText) {

            }
        });

        recyclerView.setAdapter(permissionAdapter);
        permissionAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
               onItemRangeChangedPositionStart = positionStart;
               onItemRangeChangedItemCount = itemCount;
            }
        });

        mockFragment = mock(Fragment.class);
        when(mockFragment.getString(anyInt())).thenReturn("consent text");
    }


    @Test
    public void onCreateConsentFailed() {
        givenConsentViewEnabledIs(true);
        whenCreateConsentFailedWithError(error);
        thenConsentViewIsError(true);
        thenConsentViewIsLoading(false);
        thenConsentViewIsEnabled(true);
        thenItemChangeWasNotified();
    }

    @Test
    public void onCreateConsentFailed_DisablesToggleView_onVersionMismatchError() {
        givenConsentViewEnabledIs(true);
        whenCreateConsentFailedWithError(versionMismatchError);
        thenConsentViewIsError(true);
        thenConsentViewIsLoading(false);
        thenConsentViewIsEnabled(false);
        thenItemChangeWasNotified();
    }


    private void givenConsentViewEnabledIs(boolean isEnabled) {
        consentView.setEnabledFlag(isEnabled);
    }

    private void thenItemChangeWasNotified() {
        assertEquals(1, onItemRangeChangedPositionStart);
        assertEquals(1, onItemRangeChangedItemCount);
    }

    private void thenConsentViewIsEnabled(boolean expectedValue) {
        assertEquals(expectedValue, consentView.isEnabled());
    }

    private void thenConsentViewIsLoading(boolean expectedValue) {
        assertEquals(expectedValue, consentView.isLoading());
    }

    private void thenConsentViewIsError(boolean expectedValue) {
        assertEquals(expectedValue, consentView.isError());
    }

    private void whenCreateConsentFailedWithError(ConsentError consentError) {
        permissionAdapter.onCreateConsentFailed(1, consentError);
    }

}