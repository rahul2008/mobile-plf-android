
package com.philips.cdp.registration.hsdp;

class HsdpUserInstance {

    private static HsdpUserInstance hsdpUserInstance = new HsdpUserInstance( );

     HsdpUserRecord getHsdpUserRecord() {
         return hsdpUserRecord;
     }

     void setHsdpUserRecord(HsdpUserRecord hsdpUserRecord) {
         this.hsdpUserRecord = hsdpUserRecord;
     }

     private  HsdpUserRecord hsdpUserRecord;

    /* A private Constructor prevents any other
     * class from instantiating.
     */
    private HsdpUserInstance() { }

    /* Static 'instance' method */
    public static HsdpUserInstance getInstance( ) {
        return hsdpUserInstance;
    }
}