package com.example.philips.calculatordemo;

import org.junit.Test;

import static java.lang.Double.NaN;
import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class CalculatorBrainTest {
    CalculatorBrain cb = new CalculatorBrain();
    private static final double DELTA = 1e-15;

    @Test
    public void addTest(){
        Double ans = cb.add(0d,1d);
        assertEquals(1d,ans,DELTA);
    }
    @Test
    public void subTest(){
        Double ans = cb.sub(0d,1d);
        assertEquals(-1d,ans,DELTA);
    }
    @Test
    public void mulTest(){
        Double ans = cb.mul(0d,1d);
        assertEquals(0d,ans,DELTA);
    }
    @Test
    public void divTest(){
        Double ans = cb.div(0d,1d);
        assertEquals(0d,ans,DELTA);
    }
    //throws exception check needed
    @Test
    public void divTestWithException(){
        Double ans = cb.div(0d,0d);
        assertEquals(NaN,ans,DELTA);
    }
}