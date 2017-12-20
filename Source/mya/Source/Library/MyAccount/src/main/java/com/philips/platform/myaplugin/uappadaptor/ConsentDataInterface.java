package com.philips.platform.myaplugin.uappadaptor;

import java.io.Serializable;

public abstract class ConsentDataInterface implements DataInterface, Serializable {


    private static final long serialVersionUID = 5696616151107980955L;

    @Override
    public abstract DataModel getData(DataModelType dataModelType);
}
