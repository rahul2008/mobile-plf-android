package com.philips.platform.ths.providerdetails;

import android.content.Context;
import android.util.AttributeSet;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;

public class THSExpandableHeightGridViewTest {

    THSExpandableHeightGridView thsExpandableHeightGridView;

    @Mock
    Context context;

    @Mock
    AttributeSet attrsMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        thsExpandableHeightGridView = new THSExpandableHeightGridView(context);
    }

    @Test
    public void isExpanded() throws Exception {
        boolean expanded = thsExpandableHeightGridView.isExpanded();
        assertFalse(expanded);
    }

    @Test
    public void isExpandedWithDefinedStyleAndAttribute() throws Exception {
        thsExpandableHeightGridView = new THSExpandableHeightGridView(context, attrsMock,0);
        boolean expanded = thsExpandableHeightGridView.isExpanded();
        assertFalse(expanded);
    }

    @Test
    public void onMeasureExpandedFalse() throws Exception {
        thsExpandableHeightGridView.onMeasure(100,100);
    }

    @Test(expected = NullPointerException.class)
    public void onMeasureExpandedTrue() throws Exception {
        thsExpandableHeightGridView.setExpanded(true);
        thsExpandableHeightGridView.onMeasure(100,100);
    }
}