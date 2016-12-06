package com.philips.platform.datasync.moments;

import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.events.MomentChangeEvent;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Collections;

import retrofit.RetrofitError;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by sangamesh on 30/11/16.
 */
public class MomentsMonitorTest {


    @Mock
    private MomentsDataSender momentsDataSenderMock;

    @Mock
    private MomentChangeEvent momentChangeEventMock;

    @Mock
    private MomentsDataFetcher momentsDataFetcherMock;

    @Mock
    private Moment momentMock;

    private MomentsMonitor momentsMonitor;

    @Before
    public void setUp() {
        initMocks(this);

        momentsMonitor = new MomentsMonitor(momentsDataSenderMock, momentsDataFetcherMock);
    }

    @Test
    public void ShouldCallSaveMoments_WhenSyncMomentsIsCalled() throws Exception {
        when(momentChangeEventMock.getMoment()).thenReturn(momentMock);

        momentsMonitor.onEventAsync(momentChangeEventMock);

        verify(momentsDataSenderMock).sendDataToBackend(Collections.singletonList(momentMock));

    }

    @Test
    public void ShouldFetchMoment_WhenMomentConflictOccursDuringSend() throws Exception {
        when(momentChangeEventMock.getMoment()).thenReturn(momentMock);
        when(momentsDataSenderMock.sendDataToBackend(Collections.singletonList((momentMock)))).thenReturn(true);

        momentsMonitor.onEventAsync(momentChangeEventMock);

        RetrofitError retrofitError = verify(momentsDataFetcherMock).fetchDataSince(any(DateTime.class));

    }

}