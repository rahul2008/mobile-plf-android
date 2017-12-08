package com.philips.pins.shinelib.wrappers;

import com.philips.pins.shinelib.ResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.capabilities.SHNCapabilityConfigEnergyIntake;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.Set;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anySet;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class SHNCapabilityConfigEnergyIntakeWrapperTest extends SHNCapabilityWrapperTestBase {

    private static final SHNCapabilityConfigEnergyIntake.MealType TEST_MEAL_TYPE = SHNCapabilityConfigEnergyIntake.MealType.LUNCH;
    private static final SHNResult TEST_RESULT = SHNResult.SHNErrorBluetoothDisabled;

    @Mock
    private SHNCapabilityConfigEnergyIntake.MealConfiguration mealConfigurationMock;

    @Mock
    private SHNCapabilityConfigEnergyIntake capabilityMock;

    @Mock
    private ResultListener<Set<SHNCapabilityConfigEnergyIntake.MealType>> mealTypeListenerMock;

    @Mock
    private ResultListener<SHNCapabilityConfigEnergyIntake.MealConfiguration> mealConfigurationListenerMock;

    @Mock
    private SHNResultListener shnResultListenerMock;

    @Mock
    private Set<SHNCapabilityConfigEnergyIntake.MealType> mealTypeSetMock;

    @Captor
    private ArgumentCaptor<ResultListener> resultListenerArgumentCaptor;

    SHNCapabilityConfigEnergyIntakeWrapper capabilityWrapper;

    @Before
    public void setUp() {
        initMocks(this);

        capabilityWrapper = new SHNCapabilityConfigEnergyIntakeWrapper(capabilityMock, internalHandlerMock, userHandlerMock);
    }

    private void callGetSupportedMealTypesOnInternalHandler() {
        capabilityWrapper.getSupportedMealTypes(mealTypeListenerMock);
        verify(capabilityMock, never()).getSupportedMealTypes(any(ResultListener.class));

        captureInternalHandlerRunnable().run();
    }

    private void callSetMealConfigurationOnInternalHandler() {
        capabilityWrapper.setMealConfiguration(mealConfigurationMock, TEST_MEAL_TYPE, shnResultListenerMock);
        verify(capabilityMock, never()).setMealConfiguration(
                any(SHNCapabilityConfigEnergyIntake.MealConfiguration.class),
                any(SHNCapabilityConfigEnergyIntake.MealType.class),
                any(SHNResultListener.class));

        captureInternalHandlerRunnable().run();
    }

    private void callGetMealConfigurationOnInternalHandler() {
        capabilityWrapper.getMealConfiguration(TEST_MEAL_TYPE, mealConfigurationListenerMock);
        verify(capabilityMock, never()).getMealConfiguration(
                any(SHNCapabilityConfigEnergyIntake.MealType.class),
                any(ResultListener.class));

        captureInternalHandlerRunnable().run();
    }

    @Test
    public void whenGetSupportedMealTypesIsCalled_ItShouldPostTheFunctionCallOnTheInternalHandler() {
        callGetSupportedMealTypesOnInternalHandler();

        verify(capabilityMock).getSupportedMealTypes(isA(ResultListener.class));
    }

    @Test
    public void whenSetMealConfigurationIsCalled_ItShouldPostTheFunctionCallOnTheInternalHandler() {
        callSetMealConfigurationOnInternalHandler();

        verify(capabilityMock).setMealConfiguration(eq(mealConfigurationMock), eq(TEST_MEAL_TYPE), isA(SHNResultListener.class));
    }

    @Test
    public void whenGetMealConfigurationIsCalled_ItShouldPostTheFunctionCallOnTheInternalHandler() {
        callGetMealConfigurationOnInternalHandler();

        verify(capabilityMock).getMealConfiguration(eq(TEST_MEAL_TYPE), isA(ResultListener.class));
    }

    @Test
    public void whenGetSupportedMealTypesIsCalled_ItPostsResultsOnTheUserHandler() {
        callGetSupportedMealTypesOnInternalHandler();

        ArgumentCaptor<ResultListener> internalResultListenerCaptor = ArgumentCaptor.forClass(ResultListener.class);
        verify(capabilityMock).getSupportedMealTypes(internalResultListenerCaptor.capture());
        internalResultListenerCaptor.getValue().onActionCompleted(mealTypeSetMock, TEST_RESULT);

        verify(mealTypeListenerMock, never()).onActionCompleted(anySet(), any(SHNResult.class));

        captureUserHandlerRunnable().run();

        verify(mealTypeListenerMock).onActionCompleted(mealTypeSetMock, TEST_RESULT);
    }

    @Test
    public void whenSetMealConfigurationIsCalled_ItPostsResultsOnTheUserHandler() {
        callSetMealConfigurationOnInternalHandler();

        ArgumentCaptor<SHNResultListener> internalResultListenerCaptor = ArgumentCaptor.forClass(SHNResultListener.class);
        verify(capabilityMock).setMealConfiguration(eq(mealConfigurationMock), eq(TEST_MEAL_TYPE), internalResultListenerCaptor.capture());
        internalResultListenerCaptor.getValue().onActionCompleted(TEST_RESULT);

        verify(shnResultListenerMock, never()).onActionCompleted(any(SHNResult.class));

        captureUserHandlerRunnable().run();

        verify(shnResultListenerMock).onActionCompleted(TEST_RESULT);
    }

    @Test
    public void whenGetMealConfigurationIsCalled_ItPostsResultsOnTheUserHandler() {
        callGetMealConfigurationOnInternalHandler();

        ArgumentCaptor<ResultListener> internalResultListenerCaptor = ArgumentCaptor.forClass(ResultListener.class);
        verify(capabilityMock).getMealConfiguration(eq(TEST_MEAL_TYPE), internalResultListenerCaptor.capture());
        internalResultListenerCaptor.getValue().onActionCompleted(mealConfigurationMock, TEST_RESULT);

        verify(mealConfigurationListenerMock, never()).onActionCompleted(
                any(SHNCapabilityConfigEnergyIntake.MealConfiguration.class),
                any(SHNResult.class));

        captureUserHandlerRunnable().run();

        verify(mealConfigurationListenerMock).onActionCompleted(mealConfigurationMock, TEST_RESULT);
    }
}