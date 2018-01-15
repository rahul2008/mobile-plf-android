/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.myaplugin.uappadaptor;

import java.io.Serializable;


public interface DataInterface extends Serializable {

   DataModel getData(DataModelType dataModelType);

}
