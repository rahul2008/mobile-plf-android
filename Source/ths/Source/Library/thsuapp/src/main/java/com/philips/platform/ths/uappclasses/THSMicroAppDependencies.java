/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.uappclasses;


import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;
import com.philips.platform.ths.registration.dependantregistration.THSConsumer;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uappframework.uappinput.UappDependencies;


public class THSMicroAppDependencies extends UappDependencies {



    public void setConsentDefinition(ConsentDefinition consentDefinition){
        THSManager.getInstance().setConsentDefinition(consentDefinition);
    }


    public THSMicroAppDependencies(final AppInfraInterface appInfra) {
        super(appInfra);
    }

    public void setThsConsumer(THSConsumer thsConsumer){
        THSManager.getInstance().setThsParentConsumer(thsConsumer);
    }

}
