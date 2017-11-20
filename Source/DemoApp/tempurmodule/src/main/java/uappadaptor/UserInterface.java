package com.philips.platform.mya.uappadaptor;

import android.content.Context;

import com.philips.cdp.registration.User;

/**
 * Created by philips on 11/16/17.
 */

public class UserInterface extends DataInterface {

    UserDataModel userDataModel;
    User user;


   public UserInterface(Context context){
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
    }



}
