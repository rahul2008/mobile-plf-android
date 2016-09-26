package cdp.philips.com.mydemoapp.utility;

import android.os.Handler;
import dagger.internal.Factory;
import de.greenrobot.event.EventBus;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated("dagger.internal.codegen.ComponentProcessor")
public final class EventingImpl_Factory implements Factory<EventingImpl> {
  private final Provider<EventBus> eventBusProvider;
  private final Provider<Handler> handlerProvider;

  public EventingImpl_Factory(Provider<EventBus> eventBusProvider, Provider<Handler> handlerProvider) {  
    assert eventBusProvider != null;
    this.eventBusProvider = eventBusProvider;
    assert handlerProvider != null;
    this.handlerProvider = handlerProvider;
  }

  @Override
  public EventingImpl get() {  
    return new EventingImpl(eventBusProvider.get(), handlerProvider.get());
  }

  public static Factory<EventingImpl> create(Provider<EventBus> eventBusProvider, Provider<Handler> handlerProvider) {  
    return new EventingImpl_Factory(eventBusProvider, handlerProvider);
  }
}

