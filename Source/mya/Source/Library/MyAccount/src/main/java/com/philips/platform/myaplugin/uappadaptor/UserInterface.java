package com.philips.platform.myaplugin.uappadaptor;

import android.content.Context;

import com.philips.cdp.registration.handlers.LogoutHandler;

import java.io.Serializable;

public abstract class UserInterface implements DataInterface, Serializable {


    @Override
    public abstract DataModel getData(DataModelType dataModelType);

    public abstract boolean isUserLoggedIn(Context context);

    public abstract void logOut(Context context, LogoutHandler logoutHandler);


}
