package com.philips.pins.shinelib.framework;

import junit.framework.TestCase;

public class BleUUIDCreatorTest extends TestCase {
    public void testSomeUUIDS() {
        assertEquals("00001811-0000-1000-8000-00805F9B34FB", BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x1811));
        assertEquals("00000000-0000-1000-8000-00805F9B34FB", BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x0));
        assertEquals("0000FFFF-0000-1000-8000-00805F9B34FB", BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0xFFFF));
    }
}