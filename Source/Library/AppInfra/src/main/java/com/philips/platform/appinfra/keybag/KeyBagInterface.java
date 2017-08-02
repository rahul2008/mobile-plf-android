package com.philips.platform.appinfra.keybag;


import java.io.FileNotFoundException;
import java.util.Map;

public interface KeyBagInterface {

    void init() throws FileNotFoundException;

    Map getMapForServiceId(String serviceId);
}
