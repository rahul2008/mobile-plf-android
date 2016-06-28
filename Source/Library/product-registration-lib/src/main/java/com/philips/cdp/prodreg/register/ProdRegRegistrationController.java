package com.philips.cdp.prodreg.register;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ProdRegRegistrationController {

    private ProdRegProcessController.ProcessControllerCallBacks processControllerCallBacks;

    public ProdRegRegistrationController(final ProdRegProcessController.ProcessControllerCallBacks processControllerCallBacks) {
        this.processControllerCallBacks = processControllerCallBacks;
    }
}
