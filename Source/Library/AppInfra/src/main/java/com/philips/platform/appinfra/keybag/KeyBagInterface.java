package com.philips.platform.appinfra.keybag;


public interface KeyBagInterface {

    String obfuscate(String data, int seed);

    void init(String rawData);
}
