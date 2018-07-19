
package com.philips.cdp.registration.hsdp;

class HsdpUserInstance {

    private static HsdpUserInstance hsdpUserInstance = new HsdpUserInstance( );

     HsdpUserRecordV2 getHsdpUserRecordV2() {
         return hsdpUserRecordV2;
     }

     void setHsdpUserRecordV2(HsdpUserRecordV2 hsdpUserRecordV2) {
         this.hsdpUserRecordV2 = hsdpUserRecordV2;
     }

     private HsdpUserRecordV2 hsdpUserRecordV2;

    /* A private Constructor prevents any other
     * class from instantiating.
     */
    private HsdpUserInstance() { }

    /* Static 'instance' method */
    public static HsdpUserInstance getInstance( ) {
        if(hsdpUserInstance==null){
            return new HsdpUserInstance();
        }
        return hsdpUserInstance;
    }
}