package com.philips.platform.core.events;

import java.util.List;

public class GetPairedDevicesResponseEvent extends Event{
    List<String> mPairedDevices;

    public GetPairedDevicesResponseEvent(List<String> pairedDevices) {
        mPairedDevices = pairedDevices;
    }

    public List<String> getPairedDevices() {
        return mPairedDevices;
    }
}
