/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.appliance;

import com.philips.cdp.dicommclient.networknode.NetworkNode;

public abstract class DICommApplianceFactory<T> {

	public abstract boolean canCreateApplianceForNode(NetworkNode networkNode);
	public abstract T createApplianceForNode(NetworkNode networkNode);

}
