package com.pins.philips.shinelib.helper;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.powermock.api.mockito.PowerMockito.mock;

/**
 * Created by 310188215 on 04/04/15.
 */
public class Utility {
    public static<T> T makeThrowingMock(final Class<T> clazz) {
        return mock(clazz, new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                throw new RuntimeException("Function not stubbed with doReturn(..).when(" + clazz.getSimpleName() + ")." + invocation.getMethod().getName() + "();");
            }
        });
    }
}
