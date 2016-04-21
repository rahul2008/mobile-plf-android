package com.philips.cdp.di.iap.model;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
//@RunWith(MockitoJUnitRunner.class)
public class PaymentRequestTest {
//    @Mock
//    private Store mStore;
//
//    @Test
//    public void testRequestMethodIsPOST() {
//        PaymentRequest request = new PaymentRequest(mStore, null, null);
//        assertEquals(Request.Method.POST, request.getMethod());
//    }
//
//    @Test
//    public void testQueryParamsIsNotNull() {
//        PaymentRequest mockPaymentRequest = Mockito.mock(PaymentRequest.class);
//        Mockito.when(mockPaymentRequest.getUrl()).thenReturn(NetworkConstants.BASE_URL + "orders/5165165165/pay");
//        assertNotNull(mockPaymentRequest.requestBody());
//    }
//
//    @Test
//    public void testQueryParamsHasBody() {
//        PaymentRequest mockPaymentRequest = Mockito.mock(PaymentRequest.class);
//        Mockito.when(mockPaymentRequest.getUrl()).thenReturn(NetworkConstants.BASE_URL + "orders/5165165165/pay");
//        Map<String, String> params = new HashMap<String, String>();
//
//        assertEquals(mockPaymentRequest.requestBody(), params);
//    }
//
//    @Test
//    public void testTestingUriIsNotNull() {
//        PaymentRequest mockPaymentRequest = Mockito.mock(PaymentRequest.class);
//        IAPConfiguration iapConfiguration = Mockito.mock(IAPConfiguration.class);
//        CartModelContainer.getInstance().setIapConfiguration(iapConfiguration);
//        Mockito.when(CartModelContainer.getInstance().getIapConfiguration().getHostport()).thenReturn("tst.pl.shop.philips.com");
//        Mockito.when(CartModelContainer.getInstance().getIapConfiguration().getSite()).thenReturn("US_Tuscany");
//        Mockito.when(mockPaymentRequest.getUrl()).thenReturn(NetworkConstants.BASE_URL + "orders/5165165165/pay");
//        assertNotNull(mockPaymentRequest.getUrl());
//    }
//
//    @Test
//    public void testTestingUrilIsForPaymentRequest() {
//        PaymentRequest mockPaymentRequest = Mockito.mock(PaymentRequest.class);
//        Mockito.when(mockPaymentRequest.getUrl()).thenReturn(NetworkConstants.BASE_URL + "orders/5165165165/pay");
//        assertEquals(mockPaymentRequest.getUrl(), NetworkConstants.BASE_URL + "orders/5165165165/pay");
//    }
//
//    @Test
//    public void parseResponseShouldBeOfPaymentRequestDataType() {
//        PaymentRequest request = new PaymentRequest(mStore, null, null);
//        PaymentRequest mockPaymentRequest = Mockito.mock(PaymentRequest.class);
//        Mockito.when(mockPaymentRequest.getUrl()).thenReturn(NetworkConstants.BASE_URL + "orders/5165165165/pay");
//
//        String str = TestUtils.readFile(PaymentRequestTest.class, "payment_url.txt");
//        Object response = request.parseResponse(str);
//        assertEquals(response.getClass(), MakePaymentData.class);
//    }
}