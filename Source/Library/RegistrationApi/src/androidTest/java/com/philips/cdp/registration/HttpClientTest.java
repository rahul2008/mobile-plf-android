/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration;

import android.support.v4.util.Pair;
import android.test.ActivityInstrumentationTestCase2;

import com.philips.cdp.registration.ui.traditional.RegistrationActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 310202337 on 11/23/2015.
 */
public class HttpClientTest extends ActivityInstrumentationTestCase2<RegistrationActivity> {

    public HttpClientTest() {
        super(RegistrationActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty("dexmaker.dexcache", getInstrumentation()
                .getTargetContext().getCacheDir().getPath());

    }

    public void testHttpClientcallGet(){
        HttpClient httpClient = new HttpClient();
     //   String s = httpClient.callGet("https://www.google.com","Ase345689fhf!@");
        //assertEquals("",s);
     //   assertNotNull(s);
    }

    public void testHttpClientPost(){
      /*  HttpClient httpClient = new HttpClient();
        DefaultHttpClient defaultHttpClient = httpClient.getHttpClient();
        List<org.apache.http.NameValuePair> nameValuePair = new ArrayList<org.apache.http.NameValuePair>();
        nameValuePair.add(new BasicNameValuePair("productSerialNumber","rt234556778777"));
        nameValuePair
                .add(new BasicNameValuePair("purchaseDate", "2015-12-02"));

        nameValuePair
                .add(new BasicNameValuePair("registrationChannel","MS81376"));

        httpClient.postData("https://acc.philips.co.uk/prx/registration/B2C/ru%5FRU/CONSUMER/products/HD8969/09.register.type.product?",nameValuePair,"4ttk7eznansrzdvd");*/
        HttpClient httpClient = new HttpClient();
        Pair p1 = new Pair("productSerialNumber","rt234556778777");
        Pair p2 = new Pair("purchaseDate","2015-12-02");
        Pair p3 = new Pair("registrationChannel","MS81376");
        List<Pair<String,String>> al = new ArrayList<Pair<String,String>>();
        al.add(p1);
        al.add(p2);
        al.add(p3);
        httpClient.callPost("https://acc.philips.co.uk/prx/registration/B2C/de_DE/CONSUMER/products/HD8978/01.register.type.product?",al,"swa7ud5vjx75cqq2");
    }
}
