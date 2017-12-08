package com.philips.platform.ths.intake;

import com.americanwell.sdk.entity.health.Condition;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

public class THSConditionsTest {
    THSConditionsList thsConditions;

    @Mock
    List<Condition> Conditions;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        thsConditions = new THSConditionsList();
        thsConditions.setConditions(Conditions);
    }

    @Test
    public void getConditions() throws Exception {
        List<Condition> conditions = thsConditions.getConditions();
        assertNotNull(conditions);
        assertThat(conditions).isInstanceOf(List.class);
    }
}