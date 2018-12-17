/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.cdp.registration.myaccount;

import java.io.Serializable;

public abstract class UserInterface implements DataInterface, Serializable {


    private static final long serialVersionUID = -37048850981108559L;

    @Override
    public abstract DataModel getData(DataModelType dataModelType);

}
