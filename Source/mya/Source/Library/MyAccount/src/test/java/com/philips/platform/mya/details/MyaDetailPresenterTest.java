/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.details;

import android.content.Context;

import com.philips.platform.mya.BuildConfig;
import com.philips.platform.mya.runner.CustomRobolectricRunner;
import com.philips.platform.myaplugin.uappadaptor.DataModelType;
import com.philips.platform.myaplugin.uappadaptor.UserDataModel;
import com.philips.platform.myaplugin.user.UserDataModelProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Date;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 25)
public class MyaDetailPresenterTest {

    private MyaDetailContract.View view;
    private Context context;
    private MyaDetailPresenter myaDetailPresenter;

    @Before
    public void setup() {
        initMocks(this);
        view = mock(MyaDetailContract.View.class);
        context = RuntimeEnvironment.application;
        when(view.getContext()).thenReturn(context);
        myaDetailPresenter = new MyaDetailPresenter(view);
    }

    @Test
    public void testSetUserDetails() {
        UserDataModelProvider userDataModelProvider = mock(UserDataModelProvider.class);
        UserDataModel userDataModel = new UserDataModel();
        userDataModel.setEmail("some_email");
        userDataModel.setGivenName("some_name");
        userDataModel.setFamilyName("some_family_name");
        userDataModel.setMobileNumber("98278383939");
        userDataModel.setGender("male");
        Date birthday = new Date();
        userDataModel.setBirthday(birthday);
        userDataModel.setEmail("some_email");
        when(userDataModelProvider.getData(DataModelType.USER)).thenReturn(userDataModel);
        myaDetailPresenter.setUserDetails(userDataModelProvider);
        verify(view).setGender(userDataModel.getGender());
        verify(view).setMobileNumber(userDataModel.getMobileNumber());
        verify(view).setEmail(userDataModel.getEmail());
        verify(view).setDateOfBirth(userDataModel.getBirthday());
        verify(view).handleArrowVisibility(userDataModel.getEmail(), userDataModel.getMobileNumber());
        verify(view).setUserName(userDataModel.getGivenName().concat(" ").concat(userDataModel.getFamilyName()));
        verify(view).setCircleText(String.valueOf(userDataModel.getGivenName().charAt(0)).toUpperCase().concat(String.valueOf(userDataModel.getFamilyName().charAt(0))).toUpperCase());
        userDataModel.setFamilyName(null);
        when(userDataModelProvider.getData(DataModelType.USER)).thenReturn(userDataModel);
        myaDetailPresenter.setUserDetails(userDataModelProvider);
        verify(view).setCircleText("SO");
    }


}