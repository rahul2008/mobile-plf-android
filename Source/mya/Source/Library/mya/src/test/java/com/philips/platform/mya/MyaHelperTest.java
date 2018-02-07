package com.philips.platform.mya;


import com.philips.platform.mya.catk.CatkInputs;
import com.philips.platform.mya.chi.ConsentConfiguration;
import com.philips.platform.mya.chi.ConsentHandlerInterface;
import com.philips.platform.mya.chi.datamodel.ConsentDefinition;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import edu.emory.mathcs.backport.java.util.Collections;

public class MyaHelperTest {
    private MyaHelper subject = MyaHelper.getInstance();
    private List<ConsentConfiguration> configurations = new ArrayList<>();
    @Mock
    private ConsentHandlerInterface handler1;
    @Mock
    private ConsentHandlerInterface handler2;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = CatkInputs.InvalidInputException.class)
    public void itShouldThrowExceptionWhenSettingConfigurationsWithDuplicateTypes_in_single_configuration() {
        givenConfigurationsWithTypes(handler1, "moment", "moment");
        whenSettingConfiguration();
    }

    @Test(expected = CatkInputs.InvalidInputException.class)
    public void itShouldThrowExceptionWhenSettingConfigurationsWithDuplicateTypes_in_multiple_configurations() {
        givenConfigurationsWithTypes(handler1, "moment", "consent");
        givenConfigurationsWithTypes(handler2, "moment", "coaching");
        whenSettingConfiguration();
    }

    @Test
    public void itShouldNotThrowExceptionWhenSettingConfigurationWithUniquesDuplicateTypes() {
        givenConfigurationsWithTypes(handler1, "moment", "consent");
        givenConfigurationsWithTypes(handler2, "coaching", "marketing");
        whenSettingConfiguration();
    }

    @Test
    public void itShouldNotThrowExceptionWhenSettingConfigurationWithoutTypes() {
        givenConfigurationsWithTypes(handler1);
        givenConfigurationsWithTypes(handler2);
        whenSettingConfiguration();
    }

    private void givenConfigurationsWithTypes(ConsentHandlerInterface handler, String... types) {
        List<ConsentDefinition> definitions = new ArrayList<>();
        for (String type : types) {
            definitions.add(createDefinitionsWithType(type));
        }
        configurations.add(new ConsentConfiguration(definitions, handler));
    }

    private void whenSettingConfiguration() {
        subject.setConfigurations(configurations);
    }

    private ConsentDefinition createDefinitionsWithType(String type) {
        return new ConsentDefinition("text:" + type, "help:" + type, Collections.singletonList(type), 0);
    }

}