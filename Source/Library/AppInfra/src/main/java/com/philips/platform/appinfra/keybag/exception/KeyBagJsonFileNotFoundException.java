package com.philips.platform.appinfra.keybag.exception;

import java.io.FileNotFoundException;


public class KeyBagJsonFileNotFoundException extends FileNotFoundException {

    public KeyBagJsonFileNotFoundException() {
        super("AIKeyBag.json file not found in assets folder");
    }
}
