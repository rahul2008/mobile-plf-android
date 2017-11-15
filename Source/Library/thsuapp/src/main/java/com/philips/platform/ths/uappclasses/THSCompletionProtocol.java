package com.philips.platform.ths.uappclasses;

/**
 * Created by philips on 10/16/17.
 */

public interface THSCompletionProtocol {


    void didExitTHS(THSExitType thsExitType);

    public enum THSExitType {visitSuccessful,visitUnsuccessful,Other};
}
