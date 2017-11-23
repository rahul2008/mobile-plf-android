/*
 *  Copyright (c) Koninklijke Philips N.V., 2017
 *  All rights are reserved. Reproduction or dissemination
 *  in whole or in part is prohibited without the prior written
 *  consent of the copyright holder.
 */

package com.philips.platform.appframework.connectivitypowersleep;

import org.junit.Assert;
import org.junit.Test;

public class ConnectivityHelperTest {
    @Test
    public void calculateDeepSleepScore() throws Exception {
        Assert.assertEquals(95,new ConnectivityHelper().calculateDeepSleepScore(6950712));
    }

}