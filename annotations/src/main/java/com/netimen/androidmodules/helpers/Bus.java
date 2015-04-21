package com.netimen.androidmodules.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides type-safe communication between submodules. Consists of event and request systems.
 * <p>
 * {@link #event(Object)} is used to notify about some event and to pass some data via it's parameter. There can be any number of listeners of event of certain type
 * <p>
 * {@link #request(com.netimen.androidmodules.helpers.Bus.Request)} is used to retrieve some data from another submodule. There can be ONLY ONE processor of request of certain type.
 */
@SuppressWarnings("UnusedDeclaration")
public class Bus {

    private final Map<Class<?>, RequestProcessor> requestProcessors = new HashMap<>();
    private final Map<Class<?>, List<EventListener>> eventListeners = new HashMap<>();

    /**
     * it's better not to call this method directly, but to use {@link com.netimen.androidmodules.annotations.Request} instead
     */
    public <RESULT, R extends Request<RESULT>> void register(Class<R> requestClass, RequestProcessor<RESULT, R> processor) {
        requestProcessors.put(requestClass, processor);
    }

    @SuppressWarnings("unchecked")
    public <RESULT, R extends Request<RESULT>> RESULT request(R request) {
        final RequestProcessor<RESULT, R> processor = requestProcessors.get(request.getClass());
        return processor == null ? request.defaultResult() : processor.process(request);
    }

    ///

    /**
     * it's better not to call this method directly, but to use {@link com.netimen.androidmodules.annotations.Event} instead
     */
    public <E> void register(Class<E> eventClass, EventListener<E> listener) {
        List<EventListener> listeners = eventListeners.get(eventClass);
        if (listeners == null) {
            listeners = new ArrayList<>();
            eventListeners.put(eventClass, listeners);
        }
        listeners.add(listener);
    }

    @SuppressWarnings("unchecked")
    public <E> void event(E event) {
        final List<EventListener> listeners = eventListeners.get(event.getClass());
        if (listeners != null)
            for (EventListener listener : listeners)
                listener.onEvent(event);
    }

    ///

    public abstract static class Request<R> {
        protected R defaultResult() {
            return null;
        }
    }

    @SuppressWarnings("WeakerAccess")
    public abstract static class BooleanRequest extends Request<Boolean> {

        @Override
        protected Boolean defaultResult() {
            return false;
        }
    }

    public interface RequestProcessor<RESULT, R extends Request<RESULT>> {
        public RESULT process(R request);
    }

    ///

    public interface EventListener<E> {
        void onEvent(E event);
    }

}
