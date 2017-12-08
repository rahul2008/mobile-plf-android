package com.philips.cdp.registration.dao;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.cdp.registration.RegistrationApiInstrumentationBase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;


public class ConsumerArrayTest extends RegistrationApiInstrumentationBase {

    @Mock
    ConsumerArray consumerArray;

    @Mock
    Context context;

    @Before
    public void setUp() throws Exception {
              super.setUp();
        assertNotNull(consumerArray.getInstance());
        consumerArray = consumerArray.getInstance();
        context = getInstrumentation().getTargetContext();
    }

    @Test
    public void testGetInstance() throws Exception {

        assertNotNull(consumerArray);
        consumerArray.getInstance();
        List<ConsumerInterest> listConsumerInterest = new ArrayList<>();
        consumerArray.setConsumerArraylist(listConsumerInterest);
        assertNotNull(consumerArray.getConsumerArraylist());

    }


    @Before
    @Test
    public void testSetConsumerArraylist() throws Exception {
        List<ConsumerInterest> listConsumerInterest = new List<ConsumerInterest>() {
            @Override
            public void add(int location, ConsumerInterest object) {

            }

            @Override
            public boolean add(ConsumerInterest object) {
                return false;
            }

            @Override
            public boolean addAll(int location, Collection<? extends ConsumerInterest> collection) {
                return false;
            }

            @Override
            public boolean addAll(Collection<? extends ConsumerInterest> collection) {
                return false;
            }

            @Override
            public void clear() {

            }

            @Override
            public boolean contains(Object object) {
                return false;
            }

            @Override
            public boolean containsAll(Collection<?> collection) {
                return false;
            }

            @Override
            public ConsumerInterest get(int location) {
                return null;
            }

            @Override
            public int indexOf(Object object) {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @NonNull
            @Override
            public Iterator<ConsumerInterest> iterator() {
                return null;
            }

            @Override
            public int lastIndexOf(Object object) {
                return 0;
            }

            @Override
            public ListIterator<ConsumerInterest> listIterator() {
                return null;
            }

            @NonNull
            @Override
            public ListIterator<ConsumerInterest> listIterator(int location) {
                return null;
            }

            @Override
            public ConsumerInterest remove(int location) {
                return null;
            }

            @Override
            public boolean remove(Object object) {
                return false;
            }

            @Override
            public boolean removeAll(Collection<?> collection) {
                return false;
            }

            @Override
            public boolean retainAll(Collection<?> collection) {
                return false;
            }

            @Override
            public ConsumerInterest set(int location, ConsumerInterest object) {
                return null;
            }

            @Override
            public int size() {
                return 0;
            }

            @NonNull
            @Override
            public List<ConsumerInterest> subList(int start, int end) {
                return null;
            }

            @NonNull
            @Override
            public Object[] toArray() {
                return new Object[0];
            }

            @NonNull
            @Override
            public <T> T[] toArray(T[] array) {
                return null;
            }
        };
        consumerArray.setConsumerArraylist(listConsumerInterest);

        assertEquals(listConsumerInterest,consumerArray.getConsumerArraylist());
    }
}