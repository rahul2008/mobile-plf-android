package com.philips.cdp.di.mec.screens.detail

import androidx.lifecycle.MutableLiveData
import com.bazaarvoice.bvandroidsdk.ConversationsException
import com.bazaarvoice.bvandroidsdk.ReviewResponse
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
class MECReviewConversationsDisplayCallbackTest {

    lateinit var mecReviewConversationsDisplayCallback: MECReviewConversationsDisplayCallback

    @Mock
    lateinit var ecsProductDetailViewModelMock: EcsProductDetailViewModel

    @Mock
    lateinit var exceptionMock: ConversationsException

    @Mock
    lateinit var mutableLiveDataMecErrorMock : MutableLiveData<MecError>

    @Mock
    lateinit var mutableLiveDataMock : MutableLiveData<ReviewResponse>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        Mockito.`when`(ecsProductDetailViewModelMock.review).thenReturn(mutableLiveDataMock)
        Mockito.`when`(ecsProductDetailViewModelMock.mecError).thenReturn(mutableLiveDataMecErrorMock)
        mecReviewConversationsDisplayCallback = MECReviewConversationsDisplayCallback(ecsProductDetailViewModelMock)
    }

    @Test
    fun onSuccess() {

        val reviewResponse = ReviewResponse()
        mecReviewConversationsDisplayCallback.onSuccess(reviewResponse)
        assertNotNull(ecsProductDetailViewModelMock.review)

        //TODO
        //assertNotNull(ecsProductDetailViewModelMock.bulkRatingResponse.value)
    }

    @Test
    fun onFailure() {
        mecReviewConversationsDisplayCallback.onFailure(exceptionMock)
        assertNotNull(ecsProductDetailViewModelMock.mecError)
        //TODO
        //assertNotNull(ecsProductDetailViewModelMock.mecError.value)
    }
}