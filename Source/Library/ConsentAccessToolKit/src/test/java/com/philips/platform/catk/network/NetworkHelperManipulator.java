package com.philips.platform.catk.network;


public class NetworkHelperManipulator {

    public static void setInstance(NetworkHelper networkHelper) {
        NetworkHelper.setInstance(networkHelper);
    }

    public static void setNetworkController(NetworkController networkController) {
        NetworkHelper.getInstance().setNetworkController(networkController);
    }

}
