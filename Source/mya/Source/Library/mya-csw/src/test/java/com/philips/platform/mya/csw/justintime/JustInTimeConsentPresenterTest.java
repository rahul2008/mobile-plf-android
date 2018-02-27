package com.philips.platform.mya.csw.justintime;

import com.philips.platform.mya.csw.justintime.spy.ViewSpy;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JustInTimeConsentPresenterTest {
    @Test
    public void setsPresenterOnView() {
        ViewSpy view = new ViewSpy();
        JustInTimeConsentPresenter presenter = new JustInTimeConsentPresenter(view);
        assertEquals(presenter, view.presenter);
    }
}