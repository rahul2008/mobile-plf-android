package com.philips.pins.shinelib.statemachine;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.stubbing.Answer;

import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class StateMachineTest {

    private StateMachine<State> machine;

    @Mock
    private State stateMock;

    @Mock
    private State stateMock2;

    @Mock
    private StateChangedListener<State> listenerMock;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        initMocks(this);
        machine = new StateMachine<>();
        machine.addStateListener(listenerMock);
    }

    @Test
    public void whenAStateIsSet_thenOnEnterIsCalledOnThatState() {

        machine.setState(stateMock);

        verify(stateMock).onEnter();
    }

    @Test
    public void givenAStateIsSet_whenAnotherStateIsSet_thenOnExitIsCalledOnOldState() {
        machine.setState(stateMock);

        machine.setState(stateMock2);

        verify(stateMock).onExit();
    }

    @Test
    public void givenAStateListenerIsSetAndMachineAlreadyHasState_whenANewStateIsSet_thenTheListenerIsNotifiedWithOldAndNewState() {
        machine.setState(stateMock);

        machine.setState(stateMock2);

        verify(listenerMock).onStateChanged(stateMock, stateMock2);
    }

    @Test
    public void givenAStateMachineWithAListener_whenAStateIsChanged_thenTheListenerGetsNotified() {

        machine.setState(stateMock);

        verify(listenerMock).onStateChanged(null, stateMock);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void givenAStateMachineWithAListener_whenAnotherListenerIsAddedAndStateIsChanged_bothAreNotified() {

        final StateChangedListener<State> listenerMock2 = mock(StateChangedListener.class);
        machine.addStateListener(listenerMock2);
        machine.setState(stateMock);

        verify(listenerMock).onStateChanged(null, stateMock);
        verify(listenerMock2).onStateChanged(null, stateMock);
    }

    @Test
    public void givenAStateMachineWithAListener_whenTheSameListenerIsAddedAgainAndStateIsChanged_thenTheListenerIsNotifiedOnce() {

        machine.addStateListener(listenerMock);
        machine.setState(stateMock);

        verify(listenerMock).onStateChanged(null, stateMock);
    }

    @Test
    public void givenAStateMachineWithAListener_whenTheListenerIsRemovedAndStateIsChanged_thenTheListenerIsNotNotified() {

        machine.removeStateListener(listenerMock);
        machine.setState(stateMock);

        verify(listenerMock, times(0)).onStateChanged(nullable(State.class), nullable(State.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void givenAStateMachineWithAListener_whenStateIsChangedAndListenerIsRemovedDuringNotification_thenNoExceptionIsThrown() {
        final StateChangedListener<State> listenerMock2 = mock(StateChangedListener.class);
        machine.addStateListener(listenerMock2);

        doAnswer((Answer<Void>) invocation -> {
            machine.removeStateListener(listenerMock);
            return null;
        }).when(listenerMock).onStateChanged(nullable(State.class), nullable(State.class));
        machine.setState(stateMock);

        //shouldn't have thrown
    }
}