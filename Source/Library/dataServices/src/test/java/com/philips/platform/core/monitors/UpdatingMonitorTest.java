package com.philips.platform.core.monitors;

import android.support.annotation.NonNull;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.datatypes.Moment;
import com.philips.platform.core.dbinterfaces.DBDeletingInterface;
import com.philips.platform.core.dbinterfaces.DBFetchingInterface;
import com.philips.platform.core.dbinterfaces.DBUpdatingInterface;
import com.philips.platform.core.events.MomentUpdateRequest;
import com.philips.platform.datasync.consent.ConsentsClient;
import com.philips.platform.datasync.consent.ConsentsMonitor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by sangamesh on 01/12/16.
 */
public class UpdatingMonitorTest {

    @Mock
    DBUpdatingInterface dbUpdatingInterface;

    @Mock
    DBDeletingInterface dbDeletingInterface;

    @Mock
    MomentUpdateRequest momentUpdateRequestmock;

    @Mock
    Moment momentMock;

    @Mock
    DBFetchingInterface dbFetchingInterface;

    UpdatingMonitor updatingMonitor;

    @Mock
    private Eventing eventingMock;

    @Before
    public void setUp() {
        initMocks(this);

        updatingMonitor = new UpdatingMonitor(dbUpdatingInterface, dbDeletingInterface, dbFetchingInterface);
        updatingMonitor.start(eventingMock);
    }

    @Test
    public void shouldDeleteUpdateAndPostMoment_whenMomentUpdateRequestIsCalled() throws Exception {

        when(momentUpdateRequestmock.getMoment()).thenReturn(momentMock);
        updatingMonitor.onEventAsync(momentUpdateRequestmock);
        verify(momentMock).setSynced(false);
    }
}