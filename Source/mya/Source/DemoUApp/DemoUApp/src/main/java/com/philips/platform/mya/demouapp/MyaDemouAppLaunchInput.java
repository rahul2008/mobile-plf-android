package com.philips.platform.mya.demouapp;


import com.philips.cdp.registration.User;
import com.philips.platform.pif.DataInterface.USR.UserDataInterface;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;

public class MyaDemouAppLaunchInput extends UappLaunchInput {

    private UserDataInterface userDataInterface;
    private User user;

    public MyaDemouAppLaunchInput(UserDataInterface userDataInterface, User user) {
        this.userDataInterface = userDataInterface;
        this.user = user;
    }

    public UserDataInterface getUserDataInterface() {
        return userDataInterface;
    }

    public User getUser() {
        return user;
    }
}
