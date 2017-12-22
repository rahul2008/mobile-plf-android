/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.registration.dependantregistration;

import android.content.Context;
import android.os.Bundle;

import com.americanwell.sdk.entity.SDKLocalDate;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.utility.THSManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class THSDependentPresenterTest {

    THSDependentPresenter mTHSDependentPresenter;

    @Mock
    THSDependantHistoryFragment thsDependantHistoryFragmentMock;

    @Mock
    THSConsumer thsConsumer1;

    @Mock
    THSConsumer getThsConsumer2;

    @Mock
    THSConsumer parentMock;

    @Mock
    Consumer consumerMock;

    @Mock
    Consumer consumer1;

    @Mock
    Consumer consumer2;

    @Mock
    SDKLocalDate dob;

    @Mock
    Context contextMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        List list = new ArrayList();
        list.add(thsConsumer1);
        list.add(getThsConsumer2);
        parentMock.setDependents(list);
        when(parentMock.getDependents()).thenReturn(list);

        THSManager.getInstance().setThsParentConsumer(parentMock);
        THSManager.getInstance().setThsConsumer(thsConsumer1);
        THSManager.getInstance().getThsParentConsumer(contextMock).setConsumer(consumerMock);

        when(thsConsumer1.getHsdpUUID()).thenReturn("111");
        when(getThsConsumer2.getHsdpUUID()).thenReturn("111");

        when(THSManager.getInstance().getThsParentConsumer(contextMock).getConsumer()).thenReturn(consumerMock);

        when(THSManager.getInstance().getThsConsumer(contextMock).getConsumer()).thenReturn(consumerMock);
        when(THSManager.getInstance().getThsConsumer(contextMock).getConsumer()).thenReturn(consumerMock);

        mTHSDependentPresenter = new THSDependentPresenter(thsDependantHistoryFragmentMock);
    }

    @Test
    public void onEvent() throws Exception {
        mTHSDependentPresenter.onEvent(-1);
    }

    @Test
    public void checkIfUserExists() throws Exception {
        mTHSDependentPresenter.checkIfUserExists();
        verify(thsDependantHistoryFragmentMock).addFragment(any(THSBaseFragment.class),anyString(),any(Bundle.class),anyBoolean());
    }

    @Test
    public void checkIfUserExistsWhenUserIsADependent() throws Exception {
        when(THSManager.getInstance().getThsConsumer(contextMock).isDependent()).thenReturn(true);
        when(THSManager.getInstance().getThsConsumer(contextMock).getConsumer().isDependent()).thenReturn(true);
        mTHSDependentPresenter.checkIfUserExists();
        verify(thsDependantHistoryFragmentMock).addFragment(any(THSBaseFragment.class),anyString(),any(Bundle.class),anyBoolean());
    }

    @Test
    public void checkIfUserExistsWhenConsumerIsADependent() throws Exception {
        when(THSManager.getInstance().getThsConsumer(contextMock).isDependent()).thenReturn(true);
        when(THSManager.getInstance().getThsConsumer(contextMock).getConsumer().isDependent()).thenReturn(false);
        mTHSDependentPresenter.checkIfUserExists();
        verify(thsDependantHistoryFragmentMock).addFragment(any(THSBaseFragment.class),anyString(),any(Bundle.class),anyBoolean());
    }

    @Test
    public void updateDependents() throws Exception {
        List list = new ArrayList();
        list.add(consumer1);
        list.add(consumer2);
        when(THSManager.getInstance().getThsParentConsumer(contextMock).getConsumer().getDependents()).thenReturn(list);
        when(consumer1.getSourceId()).thenReturn("111");
        when(consumer2.getSourceId()).thenReturn("111");
        mTHSDependentPresenter.updateDependents();

        assert THSManager.getInstance().getThsParentConsumer(contextMock).getDependents().size() > 1;

    }

    @Test
    public void updateDependentsDoesNotUpdateTheList() throws Exception {
        List list = new ArrayList();
        list.add(consumer1);
        when(consumer1.getFirstName()).thenReturn("Spoorti");
        when(thsConsumer1.getFirstName()).thenReturn("Spoorti");
        list.add(consumer2);
        when(consumer1.getDob()).thenReturn(dob);
        when(consumer1.getGender()).thenReturn(com.americanwell.sdk.entity.consumer.Gender.MALE);
        when(consumer1.getSourceId()).thenReturn("111");
        when(thsConsumer1.getHsdpUUID()).thenReturn("111");
        when(THSManager.getInstance().getThsParentConsumer(contextMock).getConsumer().getDependents()).thenReturn(list);
        mTHSDependentPresenter.updateDependents();

        assert THSManager.getInstance().getThsParentConsumer(contextMock).getDependents().size() == 2;

    }

    @Test
    public void updateDependentsDoesNotUpdatesTheList() throws Exception {
        List list = new ArrayList();
        list.add(consumer1);
        when(consumer1.getFirstName()).thenReturn("Spoorti");
        when(thsConsumer1.getFirstName()).thenReturn("Hallur");
        list.add(consumer2);
        when(consumer1.getDob()).thenReturn(dob);
        when(consumer1.getGender()).thenReturn(com.americanwell.sdk.entity.consumer.Gender.MALE);
        when(consumer1.getSourceId()).thenReturn("111");
        when(thsConsumer1.getHsdpUUID()).thenReturn("1233");
        when(THSManager.getInstance().getThsParentConsumer(contextMock).getConsumer().getDependents()).thenReturn(list);
        mTHSDependentPresenter.updateDependents();

        assert THSManager.getInstance().getThsParentConsumer(contextMock).getDependents().size() > 1;

    }

}