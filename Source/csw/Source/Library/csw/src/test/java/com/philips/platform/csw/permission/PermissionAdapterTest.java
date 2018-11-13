/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.platform.csw.permission;

import android.app.Fragment;
import android.support.v7.widget.RecyclerView;

import com.philips.platform.csw.permission.adapter.PermissionAdapter;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mock;

@RunWith(RobolectricTestRunner.class)
@Ignore
public class PermissionAdapterTest {

    List<ConsentView> consentViewList = new ArrayList<>();
    private final ConsentDefinition consentDefinition = new ConsentDefinition(1, 2, Collections.singletonList("consentType"), 1);
    private final ConsentDefinition consentDefinition2 = new ConsentDefinition(3, 4, Collections.singletonList("otherConsentType"), 1);
    private final ConsentView consentView = new ConsentView(consentDefinition);
    private final ConsentView consentView2 = new ConsentView(consentDefinition2);
    private PermissionAdapter permissionAdapter;
    private final ConsentError error = new ConsentError("test error", 123);
    private final ConsentError versionMismatchError = new ConsentError("blah error", 1252);
    private ConsentError noInternetError = new ConsentError("crap error", 2);
    private Fragment mockFragment;

    @Mock
    private RecyclerView recyclerView;

    private int onItemRangeChangedPositionStart;
    private int onItemRangeChangedItemCount;

    @Before
    public void setup() {
        consentViewList.add(consentView);
        consentViewList.add(consentView2);

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
    public void onGetConsentsSuccess() {
        whenRetrieveConsents();
        thenItemRangeWasNotified(consentViewList.size() + 1);
        thenItemListWasUpdatedWithinView();
    }

    @Test
    public void onGetConsentsFailed() {
        whenRetrieveConsentsFailedWith(error);
        thenItemRangeWasNotified(2);
        thenConsentViewIsError(false);
        thenConsentViewIsLoading(false);
        thenConsentViewIsOnline(true);
    }

    @Test
    public void onGetConsentsFailed_OnNoNetwork_SetsOnlineFalse() {
        whenRetrieveConsentsFailedWith(noInternetError);
        thenItemRangeWasNotified(2);
        thenConsentViewIsError(false);
        thenConsentViewIsLoading(false);
        thenConsentViewIsOnline(false);
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
        thenConsentViewIsOnline(true);
        thenItemChangeWasNotified();
    }

    @Test
    public void onCreateConsentFailed_DisablesToggleView_onNoInternetError() {
        givenConsentViewEnabledIs(true);
        whenCreateConsentFailedWithError(noInternetError);
        thenConsentViewIsError(true);
        thenConsentViewIsLoading(false);
        thenConsentViewIsEnabled(true);
        thenConsentViewIsOnline(false);
        thenItemChangeWasNotified();
    }

    @Test
    public void onCreateConsentSuccess() {
        whenCreateConsentSuccessfulWithStatus(false);
        thenConsentViewIsError(false);
        thenConsentViewIsLoading(false);
        thenConsentViewIsEnabled(true);
        thenConsentViewIsOnline(true);
        thenItemChangeWasNotified();
    }


    private void givenConsentViewEnabledIs(boolean isEnabled) {
        consentView.setEnabledFlag(isEnabled);
    }

    private void whenCreateConsentFailedWithError(ConsentError consentError) {
        permissionAdapter.onCreateConsentFailed(1, consentError);
    }

    private void whenCreateConsentSuccessfulWithStatus(boolean status) {
        permissionAdapter.onCreateConsentSuccess(1, status);
    }

    private void whenRetrieveConsents() {
        permissionAdapter.onGetConsentRetrieved(consentViewList);
    }

    private void whenRetrieveConsentsFailedWith(ConsentError error) {
        permissionAdapter.onGetConsentFailed(error);
    }

    private void thenItemChangeWasNotified() {
        assertEquals(1, onItemRangeChangedPositionStart);
        assertEquals(1, onItemRangeChangedItemCount);
    }


    private void thenItemRangeWasNotified(int expectedRangeCount) {
        assertEquals(expectedRangeCount, onItemRangeChangedItemCount);
        assertEquals(1, onItemRangeChangedPositionStart);
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

    private void thenConsentViewIsOnline(boolean expectedOnlineStatus) {
        assertEquals(expectedOnlineStatus, consentView.isOnline());
    }

    private void thenItemListWasUpdatedWithinView() {
        assertEquals(consentViewList.size(), permissionAdapter.getConsentViews().size());
        for(ConsentView consentView: permissionAdapter.getConsentViews()){
            assertTrue(consentViewList.contains(consentView));
        }
    }
}