package com.philips.platform.appinfra.keybag;

import com.philips.platform.appinfra.AppInfraInstrumentation;

public class KeyBagManagerTest extends AppInfraInstrumentation {


    private KeyBagInterface keyBagInterface;

    private String rawData = "{\n" +
            "  \"appinfra.localtesting\" : [\n" +
            "    {\"clientId\":\"4c73b365\"},\n" +
            "    {\"clientId\":\"2d35b548bb\"},\n" +
            "    {\"clientId\":\"ecbb6a284f\"},\n" +
            "    {\"new\":\"49967f42cf\",\"clientId\":\"d68cc08550\"},\n" +
            "    {\"new\":\"e88cf71dab\",\"clientId\":\"89c821b20a\"}\n" +
            "  ],\n" +
            "  \"userreg.janrain.cdn\" : [\n" +
            "    {\"clientId\":\"c9e153c5\"},\n" +
            "    {\"clientId\":\"0ce51950ec\"},\n" +
            "    {\"clientId\":\"d0f1cfb3aa\"},\n" +
            "    {\"new\":\"b1f9f948ec\",\"clientId\":\"02cc3dd7da\"},\n" +
            "    {\"new\":\"e6e173c5da\",\"clientId\":\"b83d533301\"}\n" +
            "  ]\n" +
            "}";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        keyBagInterface = new KeyBagManager();
        keyBagInterface.init(rawData);
    }

    public void testObfuscate() {
        String obfuscate = keyBagInterface.obfuscate("Raja Ram Mohan Roy", 0XAEF7);
        assertEquals(keyBagInterface.obfuscate(obfuscate, 0XAEF7), "Raja Ram Mohan Roy");
        assertFalse(keyBagInterface.obfuscate(obfuscate, 0XAEF7).equals("Raja Ram Mohan Roy xxx"));
    }

}