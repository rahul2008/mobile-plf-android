package com.philips.platform.appinfra.keybag;


import java.net.URL;
import java.util.HashMap;

public interface KeyBagInterface {

    String obfuscate(String data, int seed);

    void init(String rawData);

    HashMap<String, String> getMapForServiceId(String serviceId, URL url);
}
