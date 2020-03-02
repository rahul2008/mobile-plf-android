/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.mec.integration

import android.content.Context

import com.philips.platform.uappframework.uappinput.UappSettings

/**
 * MECSettings class is used to initialize basic settings for MEC. Right now MEC doesn’t have any settings to be initialized. So only default initialization of MECSettings is required to be passed while creating MECInterface object.
 * @since 1.0.0
 */
class MECSettings
/**
 * @param applicationContext
 */
(applicationContext: Context) : UappSettings(applicationContext)
