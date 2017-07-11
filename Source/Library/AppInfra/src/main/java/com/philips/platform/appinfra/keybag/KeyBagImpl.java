package com.philips.platform.appinfra.keybag;

public class KeyBagImpl implements KeyBagInterface {


    @Override
    public void obfuscate() {
        new KeyBagLib().lfsrMain();
    }
}
