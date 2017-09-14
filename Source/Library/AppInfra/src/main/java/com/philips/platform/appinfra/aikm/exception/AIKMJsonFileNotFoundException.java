package com.philips.platform.appinfra.aikm.exception;

import java.io.FileNotFoundException;


public class AIKMJsonFileNotFoundException extends FileNotFoundException {

    public AIKMJsonFileNotFoundException() {
        super("AIKMap.json file not found in assets folder");
    }
}
