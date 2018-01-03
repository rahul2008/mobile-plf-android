package com.philips.platform.myaplugin.uappadaptor;

import java.io.Serializable;


public interface DataInterface extends Serializable {

   DataModel getData(DataModelType dataModelType);

}
