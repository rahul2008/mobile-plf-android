package com.philips.cdp.registration.myaccount;

import android.view.View;

import com.philips.cdp.registration.BuildConfig;
import com.philips.cdp.registration.CustomRobolectricRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;

/**
 * Created by philips on 2/12/18.
 */
@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class UserDetailPresenterTest {

    @Mock
    MyaDetailContract.View  viewMock;

    UserDetailPresenter myaDetailPresenter;

    @Mock
    UserDataModelProvider userDataModelProviderMock;

    @Mock
    private com.philips.cdp.registration.myaccount.UserDataModel dataModelMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        myaDetailPresenter = new UserDetailPresenter(viewMock);
    }

    @Test
    public void shouldSetUserNameWhenItIsNotEmpty() throws Exception {

        Mockito.when(dataModelMock.getGivenName()).thenReturn("philips");
        Mockito.when(userDataModelProviderMock.getData(DataModelType.USER)).thenReturn(dataModelMock);
        myaDetailPresenter.setUserDetails(userDataModelProviderMock);
        Mockito.verify(viewMock).setUserName("philips");
        Mockito.verify(viewMock).setCircleText("PH");
    }

    @Test
    public void shouldSetUserNameWithFamilyNameWhenBothAreNotEmpty() throws Exception {

        Mockito.when(dataModelMock.getFamilyName()).thenReturn("philips");
        Mockito.when(dataModelMock.getGivenName()).thenReturn("philips");
        Mockito.when(userDataModelProviderMock.getData(DataModelType.USER)).thenReturn(dataModelMock);
        myaDetailPresenter.setUserDetails(userDataModelProviderMock);
        Mockito.verify(viewMock).setUserName("philips philips");
        Mockito.verify(viewMock).setCircleText("PP");
    }

}