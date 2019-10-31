package com.philips.cdp.di.mec.screens.catalog

import android.content.Context
import android.os.Bundle
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.philips.cdp.di.mec.integration.MECListener
import com.philips.cdp.di.mec.utils.MECConstant
import com.philips.platform.uappframework.listener.ActionBarListener
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.support.v4.SupportFragmentController
import java.util.ArrayList

@RunWith(RobolectricTestRunner::class)
class MECProductCatalogFragmentTest {
    lateinit var mECProductCatalogFragment :MECProductCatalogFragment
    lateinit var mContext :Context


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        mContext = getInstrumentation().getContext()
    }

    @Test
    fun shouldDisplayProduct(){

        val bundle = Bundle()
        val list = ArrayList<String>()
        list.add("HX8332/11")
        bundle.putStringArrayList(MECConstant.CATEGORISED_PRODUCT_CTNS, list)
        //mECProductCatalogFragment.createInstance(bundle)
        mECProductCatalogFragment = MECProductCatalogFragment()
       // mECProductCatalogFragment.setActionBarListener(Mockito.mock(ActionBarListener.class), mockIAPListener))
        SupportFragmentController.of<MECProductCatalogFragment>(mECProductCatalogFragment).create().start().resume()
        assertNotNull(mECProductCatalogFragment)
    }
}