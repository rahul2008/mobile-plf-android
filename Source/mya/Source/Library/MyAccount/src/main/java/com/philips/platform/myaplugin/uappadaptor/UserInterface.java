package com.philips.platform.myaplugin.uappadaptor;

import android.content.Context;

import com.philips.cdp.registration.handlers.LogoutHandler;

import java.io.Serializable;

public abstract class UserInterface implements DataInterface, Serializable {


    private static final long serialVersionUID = -37048850981108559L;

    @Override
    public abstract DataModel getData(DataModelType dataModelType);

    public abstract boolean isUserLoggedIn(Context context);

    public abstract void logOut(Context context, LogoutHandler logoutHandler);


}
