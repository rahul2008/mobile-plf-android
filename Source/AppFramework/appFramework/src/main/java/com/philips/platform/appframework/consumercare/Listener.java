/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.consumercare;

/**
 * @author: ritesh.jha@philips.com
 * @since: June 30, 2016
 */

import java.util.ArrayList;

public interface Listener {

    void updateList(ArrayList<String> productList);
}
