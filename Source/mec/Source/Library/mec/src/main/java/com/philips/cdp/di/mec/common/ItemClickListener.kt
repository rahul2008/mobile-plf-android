/* Copyright (c) Koninklijke Philips N.V., 2020

 * All rights are reserved. Reproduction or dissemination

 * in whole or in part is prohibited without the prior written

 * consent of the copyright holder.

 */
package com.philips.cdp.di.mec.common

import java.io.Serializable

interface ItemClickListener : Serializable{

      fun onItemClick(item:Any)
}