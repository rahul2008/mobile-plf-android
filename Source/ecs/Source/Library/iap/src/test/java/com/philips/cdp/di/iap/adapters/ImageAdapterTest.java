package com.philips.cdp.di.iap.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by philips on 9/27/17.
 */
public class ImageAdapterTest {

    ImageAdapter imageAdapter;

    @Mock
    Context contextMock;

    ArrayList<String> assets;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        assets=new ArrayList<>();
        assets.add("url1");
        imageAdapter=new ImageAdapter(contextMock,assets);
    }

    @Test
    public void getCount() throws Exception {
     assertEquals(assets.size(),imageAdapter.getCount());
    }

    @Mock
    View viewMock;

    @Mock
    LinearLayout linearLayoutMock;
    @Test
    public void isViewFromObject() throws Exception {
       imageAdapter.isViewFromObject(viewMock,linearLayoutMock);
    }

    @Mock
    ViewGroup viewGroupMock;

    @Test(expected = NullPointerException.class)
    public void instantiateItem() throws Exception {
       imageAdapter.instantiateItem(viewGroupMock,0);
    }

    @Test
    public void destroyItem() throws Exception {
     imageAdapter.destroyItem(viewGroupMock,0,linearLayoutMock);
    }

    @Test
    public void getPageTitle() throws Exception {

       assertEquals("url1",imageAdapter.getPageTitle(0));
    }

}