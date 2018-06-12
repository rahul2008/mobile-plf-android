package com.philips.platform.csw.permission.uielement;

import android.view.View;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class LinkSpanTest {

    @Mock
    private LinkSpanClickListener listenerMock;
    @Mock
    private View viewMock;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void givenListenerSet_whenOnClick_thenShouldVerifyOnClick() {
        LinkSpan span = new LinkSpan(listenerMock);

        span.onClick(viewMock);

        verify(listenerMock).onClick();
    }


    @Test
    public void givenListenerNotSet_whenOnClick_thenShouldNotVerifyOnClick() {
        LinkSpan span = new LinkSpan(null);

        span.onClick(viewMock);

        verify(listenerMock, never()).onClick();
    }
}