package com.philips.platform.appframework;

import android.content.Context;
import android.test.ApplicationTestCase;

import com.philips.platform.appframework.introscreen.WelcomePresenter;
import com.philips.platform.modularui.statecontroller.UIBasePresenter;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest extends ApplicationTestCase<AppFrameworkApplication> {
    Context context;
    UIBasePresenter presenter;

    public ExampleUnitTest(Class<AppFrameworkApplication> applicationClass) {
        super(AppFrameworkApplication.class);
    }

    public ExampleUnitTest() {
        super(AppFrameworkApplication.class);
    }
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Before
    public void setUp() throws Exception{
        super.setUp();
        presenter = new WelcomePresenter();
       /* createApplication();

        context = getApplication();
        SharedPreferenceUtility.getInstance().Initialize(context);
        setContext(context);*/
    }

    @Test
    public void testClickTest(){
        context = mock(Context.class);
        AppFrameworkApplication appFrameworkApplication = mock(AppFrameworkApplication.class);
            when(context.getApplicationContext()).thenReturn(appFrameworkApplication);
            presenter.onClick(R.id.start_registration_button,context);
             //assertEquals();
    }
}