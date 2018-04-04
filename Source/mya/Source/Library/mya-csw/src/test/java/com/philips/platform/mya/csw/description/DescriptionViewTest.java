package com.philips.platform.mya.csw.description;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.mya.csw.BuildConfig;
import com.philips.platform.mya.csw.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class DescriptionViewTest {

    @Mock
    private FragmentTransaction transactionMock;
    @Mock
    private FragmentManager fragmentManagerMock;

    private LayoutInflater inflaterMock;

    // Subject under test
    private DescriptionView descriptionView;

    @Before
    public void setup() {
        initMocks(this);
        descriptionView = new DescriptionView();

        inflaterMock = LayoutInflater.from(RuntimeEnvironment.application);

        when(fragmentManagerMock.beginTransaction()).thenReturn(transactionMock);
//        when(inflaterMock.inflate(anyInt(), (ViewGroup) any(), anyBoolean())).thenReturn(inflatedViewMock);
//        when(inflatedViewMock.findViewById(anyInt())).thenReturn(inflatedViewMock);
    }

    @Test
    public void test_givenDescriptionView_whenGetTitleResourceId_thenShouldReturnCorrectId() {
        int result = descriptionView.getTitleResourceId();

        assertEquals(R.string.csw_consent_help_label, result);
    }

    @Test
    public void test_givenDescriptionView_whenOnCreateView_thenShouldReturnObject() {
        descriptionView.setArguments(new Bundle());

        View result = descriptionView.onCreateView(inflaterMock, null, null);

        assertNotNull(result);
    }

    @Test
    public void test_givenDescriptionView_whenShow_thenShouldVerifyBeginTransaction() {
        DescriptionView.show(fragmentManagerMock, R.string.csw_consent_help_label, R.id.csw_frame_layout_fragment_container);

        verify(fragmentManagerMock).beginTransaction();
    }

    @Test
    public void test_givenDescriptionView_whenShow_thenShouldVerifyCommitAllowingStateLoss() {
        DescriptionView.show(fragmentManagerMock, R.string.csw_consent_help_label, R.id.csw_frame_layout_fragment_container);

        verify(transactionMock).commitAllowingStateLoss();
    }
}