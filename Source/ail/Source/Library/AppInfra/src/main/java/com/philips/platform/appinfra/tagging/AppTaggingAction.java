/* Copyright (c) Koninklijke Philips N.V. 2018
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.tagging;


public interface AppTaggingAction {

    void trackSuccessAction(String category, String message);
    void trackErrorAction(String category, String message);
}
