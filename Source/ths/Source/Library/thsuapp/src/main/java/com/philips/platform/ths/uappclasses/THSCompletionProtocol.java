package com.philips.platform.ths.uappclasses;

/**
 *
 * An interface to get THS exit callback.
 * It is responsibility of proposition to implement this interface at entering points of THS coco if they need a callback upon THS exit.
 * This callback also send THS exit tag to analytics internally.
 *
 */
public interface THSCompletionProtocol {


    /**
     * This method will be be called when control comes back from THS coco to proposition provided proposition has implemented this interface.
     *  thsExitType will notify proposition one of the THS exit reason
     * @param thsExitType the ths exit type
     */
    void didExitTHS(THSExitType thsExitType);

    /**
     * The enum Ths exit type.
     */
    public enum THSExitType {
        /**
         * Visit successful ths exit type.
         * When the video call is successful
         */
        visitSuccessful, /**
         * Visit unsuccessful ths exit type.
         * When the video call is unsuccessful
         */
        visitUnsuccessful, /**
         * Other ths exit type.
         * Exit from non video visit screens
         */
        Other};
}
