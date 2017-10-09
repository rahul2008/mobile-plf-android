/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.registration.dependantregistration;

import com.americanwell.sdk.entity.practice.Practice;

//TODO: Review Comment - Spoorti - wrap the Practice Object to PTHObject
public interface OnItemClickListener {
    void onItemClick(THSConsumer thsConsumer);
}
