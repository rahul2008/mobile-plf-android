/* Copyright (c) Koninklijke Philips N.V., 2018
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.pif.chi;

import java.util.List;

public interface ConsentRegistryInterface extends ConsentHandlerInterface {

    void register(List<String> consentType, ConsentHandlerInterface consentHandlerInterface);

    ConsentHandlerInterface getHandler(String consentType);

    void removeHandler(List<String> consentType);
}
