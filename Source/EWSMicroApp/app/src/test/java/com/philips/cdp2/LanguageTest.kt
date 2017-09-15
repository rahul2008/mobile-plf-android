/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2

import android.content.res.Resources
import com.philips.cdp2.powersleep.BuildConfig
import com.philips.cdp2.powersleep.R
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class, sdk = intArrayOf(25), application = TestApplication::class)
class LanguageTest {

    companion object {
        private val FULL_ENG = "Full"
        private val FULL_DE = "(German_label)Voll"
    }

    private lateinit var resources: Resources

    @Before
    @Throws(Exception::class)
    fun setUp() {
        resources = RuntimeEnvironment.application.resources
    }

    @Test
    @Throws(Exception::class)
    fun `full for english device should be 'Full'`() {
        assertEquals(FULL_ENG, resources.getString(R.string.battery_status_full))
    }

    @Test
    @Config(qualifiers = "nl-NL")
    @Throws(Exception::class)
    fun `full for dutch device should be english`() {
        assertEquals(FULL_ENG, resources.getString(R.string.battery_status_full))
    }

    @Test
    @Config(qualifiers = "de-DE")
    @Throws(Exception::class)
    fun `full for german device should be 'Voll'`() {
        assertEquals(FULL_DE, resources.getString(R.string.battery_status_full))
    }
}
