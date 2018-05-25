package com.philips.platform.mya.csw.permission;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.philips.platform.mya.csw.BuildConfig;
import com.philips.platform.mya.csw.R;
import com.philips.platform.mya.csw.permission.adapter.BasePermissionViewHolder;
import com.philips.platform.mya.csw.permission.adapter.PermissionAdapter;
import com.philips.platform.mya.csw.permission.adapter.PermissionViewHolder;
import com.philips.platform.mya.csw.utils.CustomRobolectricRunner;
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
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 25)
@Ignore
public class PermissionAdapterTest {

    private final ConsentDefinition consentDefinition = new ConsentDefinition(1, 2, Collections.singletonList("consentType"), 1);
    private final ConsentView consentView = new ConsentView(consentDefinition);
    private View listItemView;
    private PermissionAdapter permissionAdapter;
    private final ConsentError error = new ConsentError("test error", 123);
    @Mock
    private RecyclerView recyclerView;
    private int onItemRangeChangedPositionStart;
    private int onItemRangeChangedItemCount;
    private ViewGroup viewGroup;
    private Application context;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RuntimeEnvironment.application = spy(RuntimeEnvironment.application);
        context = RuntimeEnvironment.application;
        Resources spiedResources = spy(context.getResources());
        when(context.getResources()).thenReturn(spiedResources);

        permissionAdapter = new PermissionAdapter(Collections.singletonList(consentView), new HelpClickListener() {
            @Override
            public void onHelpClicked(int helpText) {

            }
        });
        permissionAdapter.onAttachedToRecyclerView(new RecyclerView(context));

        //recyclerView.setAdapter(permissionAdapter);
//        permissionAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
//            @Override
//            public void onItemRangeChanged(int positionStart, int itemCount) {
//               onItemRangeChangedPositionStart = positionStart;
//               onItemRangeChangedItemCount = itemCount;
//            }
//        });

        viewGroup = new RelativeLayout(context);
        BasePermissionViewHolder viewHolder = permissionAdapter.onCreateViewHolder(viewGroup, 1);
        when(spiedResources.getText(1)).thenReturn("consent text");
        permissionAdapter.onBindViewHolder(viewHolder, 1);
        when(spiedResources.getText(2)).thenReturn("consent help text");
    }


    @Test
    public void onCreateConsentFailed() {
        whenCreateConsentFailedWithError(error);
        thenConsentViewIsError(true);
        thenConsentViewIsLoading(false);
        thenConsentViewIsEnabled(true);
        thenOnItemRangeChangedIsCalled();
    }

    private void thenOnItemRangeChangedIsCalled() {
        assertEquals(onItemRangeChangedItemCount, 1);
        assertEquals(onItemRangeChangedPositionStart, 0);
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