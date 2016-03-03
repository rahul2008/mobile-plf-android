package com.philips.pins.shinelib.helper;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.powermock.api.mockito.PowerMockito.mock;

/**
 * Created by 310188215 on 04/04/15.
 */
public class Utility {

    public enum Answers {
        UNSTUBBED_THROWS_EXCEPTION(new Answer<Object>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                throw new RuntimeException("Function not stubbed");
            }
        });

        private final Answer<Object> implementation;

        Answers(Answer<Object> implementation) {
            this.implementation = implementation;
        }

        public Answer<Object> get() {
            return implementation;
        }
    }

    public static <T> T makeThrowingMock(final Class<T> clazz) {

        return mock(clazz, Answers.UNSTUBBED_THROWS_EXCEPTION.get());
    }

}
