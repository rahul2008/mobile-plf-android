package com.philips.platform.uappframework.uappadaptor;

import com.philips.platform.uappframework.uappadaptor.listeners.LogoutListener;

import junit.framework.TestCase;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


public class ListenersTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testInvokingLogoutSuccess() {
        SomeClass someClass = new SomeClass();
        LogoutListener logoutListener = mock(LogoutListener.class);
        someClass.invokeLogOutSuccess(logoutListener);
        verify(logoutListener).onLogoutSuccess();
    }





    class SomeClass {

       public void invokeLogOutSuccess(LogoutListener logoutListener) {
           logoutListener.onLogoutSuccess();
       }
    }
}
