package com.philips.platform.myaplugin.uappadaptor;

import com.philips.cdp.registration.handlers.LogoutHandler;

import java.io.Serializable;

public abstract class UserInterface implements DataInterface, Serializable {


    @Override
    public abstract DataModel getData(DataModelType dataModelType);

    public abstract boolean isUserLoggedIn();

    public abstract void logOut(LogoutHandler logoutHandler);


}
