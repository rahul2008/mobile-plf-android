package com.philips.amwelluapp.intake;

import com.americanwell.sdk.entity.health.Condition;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class PTHConditionsTest {
    PTHConditions pthConditions;

    @Mock
    Condition condition;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        pthConditions = new PTHConditions();
        pthConditions.setCondition(condition);
    }

    @Test
    public void getCondition() throws Exception {
        Condition condition = pthConditions.getCondition();
        assertThat(condition).isNotNull();
        assertThat(condition).isInstanceOf(Condition.class);
    }

}