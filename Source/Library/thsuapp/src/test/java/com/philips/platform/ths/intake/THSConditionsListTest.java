package com.philips.platform.ths.intake;

import com.americanwell.sdk.entity.health.Condition;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;

public class THSConditionsListTest {
    THSCondition THSConditions;

    @Mock
    Condition condition;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        THSConditions = new THSCondition();
        THSConditions.setCondition(condition);
    }

    @Test
    public void getCondition() throws Exception {
        Condition condition = THSConditions.getCondition();
        assertThat(condition).isNotNull();
        assertThat(condition).isInstanceOf(Condition.class);
    }

}