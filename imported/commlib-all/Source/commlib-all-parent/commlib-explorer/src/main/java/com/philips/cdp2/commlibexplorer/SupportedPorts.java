/*
 * © 2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlibexplorer;

import android.support.v4.app.Fragment;

import com.philips.cdp.dicommclient.port.DICommPort;
import com.philips.cdp.dicommclient.port.common.PairingPort;
import com.philips.cdp2.commlibexplorer.appliance.PropertyPort;
import com.philips.cdp2.commlibexplorer.fragments.PairingPortFragment;
import com.philips.cdp2.commlibexplorer.fragments.PortDetailFragment;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SupportedPorts {

    private Map<Class<? extends DICommPort>, Class<? extends Fragment>> supportedPorts = new HashMap<>();

    public SupportedPorts() {
        supportedPorts.put(PropertyPort.class, PortDetailFragment.class);
        supportedPorts.put(PairingPort.class, PairingPortFragment.class);
    }

    public Fragment getFragmentFor(Class<? extends DICommPort> clazz) {
        Class<? extends Fragment> fragmentClass = supportedPorts.get(clazz);

        try {
            return fragmentClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            return null;
        }
    }

    public Set<Class<? extends DICommPort>> getSupportedPorts() {
        return supportedPorts.keySet();
    }
}
