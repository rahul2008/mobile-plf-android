package com.philips.cdp.di.mec.screens.detail

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.philips.cdp.di.ecs.ECSServices
import com.philips.cdp.di.ecs.integration.ECSCallback
import com.philips.cdp.di.ecs.model.products.ECSProduct
import com.philips.cdp.di.mec.integration.MecHolder
import org.junit.Assert
import org.junit.Before
import org.junit.Test


import org.junit.Assert.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.*
import org.mockito.Mockito.times
import org.mockito.Mockito.verify

import java.lang.Exception



//@RunWith(RobolectricTestRunner::class)
@RunWith(JUnit4::class)
class ECSProductDetailRepositoryTest {

    lateinit var mContext: Context

    @Mock
    lateinit var ecsProductDetailViewModel : EcsProductDetailViewModel

    @Mock
    lateinit var  eCSProductDetailRepository : ECSProductDetailRepository

    @Mock
    lateinit var ecsServices: ECSServices

    lateinit var eCSProduct : ECSProduct;

   /* @Captor
    private var argumentCaptor: ArgumentCaptor<ECSCallback<ECSProduct, Exception>>? = null*/

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
       // mContext = InstrumentationRegistry.getInstrumentation().getContext()
        eCSProduct = ECSProduct()
        eCSProduct.code = "HX12345/00"

       /* Mockito.`when`(MecHolder.INSTANCE.eCSServices).thenAnswer {
            return@thenAnswer ecsServices
        }*/
    }



        @Test
        fun getProductDetail_success() {
           /* val argument = ArgumentCaptor.forClass(Person::class.java)
            verify(mock).doSomething(argument.capture())
            assertEquals("John", argument.getValue().getName())*/

            val eCSCallback = ECSCallback::class.java as Class<ECSCallback<ECSProduct, Exception>>
           var argumentCaptor = ArgumentCaptor.forClass(eCSCallback)


          //  eCSProductDetailRepository.getProductDetail(eCSProduct)
                  verify(ecsServices).fetchProductDetails(eCSProduct, argumentCaptor.capture() as ECSCallback<ECSProduct, Exception>);

           // verify(eCSProductDetailRepository, times(1)).(argumentCaptor.capture() as ECSCallback<ECSProduct, Exception>);




            Assert.assertEquals(eCSProduct,  argumentCaptor!!.value.onResponse(eCSProduct))

            argumentCaptor!!.value.onResponse(eCSProduct)
           // argumentCaptor.value.onResponse(this.eCSProduct)
            //verify(ECSCallback)
        }

     /*   @Test
        fun fetchProductReview() {
        }

        @Test
        fun getRatings() {
        }

        @Test
        fun getEcsProductDetailViewModel() {
        }*/


}