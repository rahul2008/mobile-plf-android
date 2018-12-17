package com.philips.platform.appinfra.aikm.exception;

import java.io.FileNotFoundException;
import java.io.Serializable;


public class AIKMJsonFileNotFoundException extends FileNotFoundException implements Serializable {

    private static final long serialVersionUID = 2134370637279312198L;

    public AIKMJsonFileNotFoundException() {
        super("AIKMap.json file not found in assets folder");
    }
}
