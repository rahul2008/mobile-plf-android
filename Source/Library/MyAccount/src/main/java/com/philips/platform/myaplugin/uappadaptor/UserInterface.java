package com.philips.platform.myaplugin.uappadaptor;

import java.io.Serializable;

/**
 * Created by philips on 11/16/17.
 */

public abstract class UserInterface implements DataInterface, Serializable {


    @Override
    public abstract DataModel getData(DataModelType dataModelType);
}
