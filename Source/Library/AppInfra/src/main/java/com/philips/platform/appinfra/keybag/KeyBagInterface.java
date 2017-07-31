package com.philips.platform.appinfra.keybag;


import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public interface KeyBagInterface {

    String obfuscate(String data, int seed);

    void init() throws FileNotFoundException;

    ArrayList<HashMap> getMapForServiceId(String serviceId, URL url);
}
