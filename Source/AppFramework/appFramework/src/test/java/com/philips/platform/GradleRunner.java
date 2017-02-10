package com.philips.platform;


import org.junit.runners.model.InitializationError;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.manifest.AndroidManifest;
import org.robolectric.manifest.BroadcastReceiverData;

import java.util.ArrayList;
import java.util.List;

public class GradleRunner extends RobolectricTestRunner {

    public GradleRunner(final Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected AndroidManifest getAppManifest(org.robolectric.annotation.Config config) {
        AndroidManifest manifest = super.getAppManifest(config);

        List<BroadcastReceiverData> broadcastReceivers = manifest.getBroadcastReceivers();
        List<BroadcastReceiverData> removeList = new ArrayList<>();

        for (BroadcastReceiverData receiverData : broadcastReceivers) {
            if (isDeletePackage(receiverData.getClassName())) {
                removeList.add(receiverData);
            }
        }
        broadcastReceivers.removeAll(removeList);
        return manifest;
    }

    private boolean isDeletePackage(String className) {
        if (className.contains("NetworkStateReceiver")) {
            return true;
        } else
            return false;
    }
}
