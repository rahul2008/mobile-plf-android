/*
 *  Copyright (c) Koninklijke Philips N.V., 2017
 *  All rights are reserved. Reproduction or dissemination
 *  in whole or in part is prohibited without the prior written
 *  consent of the copyright holder.
 */

package com.philips.platform.appframework.connectivitypowersleep;

import com.philips.platform.appframework.connectivity.appliance.RefAppBleReferenceAppliance;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AllSessionsProviderFactoryTest {

    @Mock
    RefAppBleReferenceAppliance refAppBleReferenceAppliance;


    @Test
    public void createAllSessionProvider_Not_Null() throws Exception {
        AllSessionsProviderFactory allSessionsProviderFactory=new AllSessionsProviderFactory();
        Assert.assertNotNull(allSessionsProviderFactory.createAllSessionProvider(refAppBleReferenceAppliance));
    }

}