package com.philips.cdp.di.mec.screens.detail

import androidx.lifecycle.MutableLiveData
import com.bazaarvoice.bvandroidsdk.BulkRatingsResponse
import com.bazaarvoice.bvandroidsdk.ConversationsException
import com.philips.cdp.di.mec.common.MecError
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class MECDetailBulkRatingConversationsDisplayCallbackTest {


    lateinit var mECDetailBulkRatingConversationsDisplayCallback : MECDetailBulkRatingConversationsDisplayCallback

    @Mock
    lateinit var ecsProductDetailViewModelMock: EcsProductDetailViewModel

    @Mock
    lateinit var responseMock: BulkRatingsResponse

    @Mock
    lateinit var mutableLiveDataMock : MutableLiveData<BulkRatingsResponse>

    @Mock
    lateinit var exceptionMock: ConversationsException

    @Mock
    lateinit var mutableLiveDataMecErrorMock : MutableLiveData<MecError>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        Mockito.`when`(ecsProductDetailViewModelMock.bulkRatingResponse).thenReturn(mutableLiveDataMock)
        Mockito.`when`(ecsProductDetailViewModelMock.mecError).thenReturn(mutableLiveDataMecErrorMock)
        mECDetailBulkRatingConversationsDisplayCallback = MECDetailBulkRatingConversationsDisplayCallback(ecsProductDetailViewModelMock)
    }

    @Test
    fun onSuccess() {
        mECDetailBulkRatingConversationsDisplayCallback.onSuccess(responseMock)
        assertNotNull(ecsProductDetailViewModelMock.bulkRatingResponse)

        //TODO
        //assertNotNull(ecsProductDetailViewModelMock.bulkRatingResponse.value)
    }

    @Test
    fun onFailure() {
        mECDetailBulkRatingConversationsDisplayCallback.onFailure(exceptionMock)
        assertNotNull(ecsProductDetailViewModelMock.mecError)
        //assertNotNull(ecsProductDetailViewModelMock.mecError.value)
    }
}