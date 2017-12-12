/*
 *  Copyright (c) Koninklijke Philips N.V., 2017
 *  All rights are reserved. Reproduction or dissemination
 *  in whole or in part is prohibited without the prior written
 *  consent of the copyright holder.
 */

package com.philips.platform.appframework.connectivitypowersleep;

import com.philips.platform.appframework.connectivity.appliance.RefAppBleReferenceAppliance;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class SessionProviderFactoryTest {

    @Mock
    RefAppBleReferenceAppliance refAppBleReferenceAppliance;

    @Mock
    SessionProvider.Callback callback;


    @Test
    public void createBleSessionProvider() throws Exception {
        assertNotNull(new SessionProviderFactory().createBleSessionProvider(refAppBleReferenceAppliance,1,callback));
    }

}