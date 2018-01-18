/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.mya.interfaces;


import com.philips.platform.mya.error.MyaError;
import com.philips.platform.myaplugin.uappadaptor.DataInterface;
import com.philips.platform.myaplugin.uappadaptor.DataModelType;

public interface MyaListener {

    boolean onClickMyaItem(String itemName);

    DataInterface getDataInterface(DataModelType modelType);

    void onError(MyaError myaError);
}
