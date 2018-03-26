package com.philips.platform.ths.uappclasses;

/**
 * Created by philips on 10/16/17.
 */
public interface THSCompletionProtocol {


    /**
     * Did exit ths.
     *
     * @param thsExitType the ths exit type
     */
    void didExitTHS(THSExitType thsExitType);

    /**
     * The enum Ths exit type.
     */
    public enum THSExitType {
        /**
         * Visit successful ths exit type.
         */
        visitSuccessful, /**
         * Visit unsuccessful ths exit type.
         */
        visitUnsuccessful, /**
         * Other ths exit type.
         */
        Other};
}
