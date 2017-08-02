package com.philips.platform.appinfra.keybag;


import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

public interface KeyBagInterface {

    void init() throws FileNotFoundException;

    ArrayList<HashMap> getMapForServiceId(String serviceId);
}
