/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.mya.interfaces;


import com.philips.platform.mya.error.MyaError;

import java.io.Serializable;

public interface MyaListener extends Serializable {

    boolean onClickMyaItem(String itemName);

    void onError(MyaError myaError);
}
