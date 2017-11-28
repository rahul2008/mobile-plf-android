package com.philips.platform.myaplugin.user;

import android.content.Context;

import com.philips.cdp.registration.User;
import com.philips.platform.myaplugin.uappadaptor.DataModel;
import com.philips.platform.myaplugin.uappadaptor.DataModelType;
import com.philips.platform.myaplugin.uappadaptor.UserDataModel;
import com.philips.platform.myaplugin.uappadaptor.UserInterface;

import java.io.Serializable;


public class UserDataModelProvider extends UserInterface implements Serializable {

    //UserInterface userInterface;

    private UserDataModel userDataModel;
    private transient User user;


    public UserDataModelProvider(Context context){
        if(userDataModel==null){
            userDataModel=new UserDataModel();
        }

        user = new User(context);


    }

    @Override
    public DataModel getData(DataModelType dataModelType) {
        if(userDataModel==null){
            userDataModel=new UserDataModel();
        }

        fillUserData();
        return userDataModel;
    }

    public void  fillUserData(){
        userDataModel.setName(user.getDisplayName());
        userDataModel.setBirthday(user.getDateOfBirth());
        userDataModel.setEmail(user.getEmail());
        userDataModel.setAccessToken(user.getDisplayName());
        userDataModel.setGivenName(user.getGivenName());
        userDataModel.setBirthday(user.getDateOfBirth());
        userDataModel.setEmailVerified(user.isEmailVerified());
        userDataModel.setMobileNumber(user.getMobile());
        userDataModel.setMobileVerified(user.isMobileVerified());
        userDataModel.setGender(user.getGender().toString());
        userDataModel.setVerified(user.isTermsAndConditionAccepted());
        userDataModel.setFamilyName(user.getFamilyName());
    }




}
