package com.philips.cdp2.commlib.appliance;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.appliance.DICommApplianceFactory;
import com.philips.cdp.dicommclient.communication.CommunicationStrategy;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.context.BleTransportContext;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class MixerFactory implements DICommApplianceFactory<Mixer> {
    @NonNull
    private final BleTransportContext bleTransportContext;

    public MixerFactory(@NonNull BleTransportContext bleTransportContext) {
        this.bleTransportContext = bleTransportContext;
    }

    @Override
    public boolean canCreateApplianceForNode(NetworkNode networkNode) {
        return Mixer.MODELNAME.equals(networkNode.getModelName());
    }

    @Override
    public Mixer createApplianceForNode(NetworkNode networkNode) {
        if (canCreateApplianceForNode(networkNode)) {
            final CommunicationStrategy communicationStrategy = bleTransportContext.createCommunicationStrategyFor(networkNode);

            return new Mixer(networkNode, communicationStrategy);
        }
        return null;
    }

    @Override
    public Set<String> getSupportedModelNames() {
        return Collections.unmodifiableSet(new HashSet<String>() {{
            add(Mixer.MODELNAME);
        }});
    }
}
