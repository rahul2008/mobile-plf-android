package com.philips.platform.catk;

import com.philips.cdp.registration.User;
import com.philips.cdp.registration.handlers.RefreshLoginSessionHandler;
import com.philips.cdp.registration.handlers.RefreshUserHandler;
import com.philips.platform.catk.listener.RefreshTokenListener;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;
import static org.mockito.Mockito.never;

/**
 * Created by philips on 7/25/18.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 25)
public class RefreshTokenHandlerTest {

    @Mock
    User mockUser;

    private static final int STATE_NONE = 0;
    private static final int STATE_TOKEN_REFRESHING = 1;
    private static final int STATE_TOKEN_REFRESH_SUCCESSFUL = 2;
    private static final int STATE_TOKEN_REFRESH_FAILED = 3;


    RefreshTokenHandler refreshTokenHandler;

    @Mock
    RefreshTokenListener mockRefreshTokenListener;

    @Mock
    RefreshTokenListener mockedAnotherTokenListener;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        refreshTokenHandler = new RefreshTokenHandler(mockUser);
    }

    @Test
    public void shouldrefreshLoginSessionWhenStateIsNone() throws Exception {
        refreshTokenHandler.refreshState.set(STATE_NONE);

        refreshTokenHandler.refreshToken(mockRefreshTokenListener);
        Assert.assertEquals(refreshTokenHandler.refreshState.get(), STATE_TOKEN_REFRESHING);
        Mockito.verify(mockUser).refreshLoginSession(refreshTokenHandler);

    }

    @Test
    public void shouldCallOnRefreshSuccessWhenStateIsRefreshing() throws Exception {
        refreshTokenHandler.refreshState.set(STATE_TOKEN_REFRESHING);
        refreshTokenHandler.refreshToken(mockRefreshTokenListener);
        Assert.assertEquals(refreshTokenHandler.refreshTokenListeners.size(), 1);
    }

    @Test
    public void shouldCallOnRefreshSuccessWhenStateIsRefreshSuccessfull() throws Exception {
        refreshTokenHandler.refreshToken(mockRefreshTokenListener);

        //Post another request
        refreshTokenHandler.refreshToken(mockedAnotherTokenListener);

        refreshTokenHandler.onRefreshLoginSessionSuccess();

        Mockito.verify(mockRefreshTokenListener).onRefreshSuccess();
        Mockito.verify(mockedAnotherTokenListener).onRefreshSuccess();
        Assert.assertEquals(refreshTokenHandler.refreshState.get(), STATE_NONE);

    }

    @Test
    public void shouldNeverRefreshLoginSessionWhenStateIsInProgress() throws Exception {

        refreshTokenHandler.refreshToken(mockRefreshTokenListener);

        //Post another request
        refreshTokenHandler.refreshToken(mockedAnotherTokenListener);

        refreshTokenHandler.onRefreshLoginSessionFailedWithError(123);

        Mockito.verify(mockRefreshTokenListener).onRefreshFailed(123);
        Mockito.verify(mockedAnotherTokenListener).onRefreshFailed(123);
        Assert.assertEquals(refreshTokenHandler.refreshState.get(), STATE_NONE);
    }

    @Test
    public void onRefreshLoginSessionSuccess() throws Exception {
    }

    @Test
    public void onRefreshLoginSessionFailedWithError() throws Exception {
    }

    @Test
    public void onRefreshLoginSessionInProgress() throws Exception {
    }

}