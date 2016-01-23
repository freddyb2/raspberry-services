package com.fbo.services.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Map;

/**
 * Created by Fred on 27/06/2015.
 */
public abstract class RPiAbstractService {

    private final String name;

    private ServiceState state;

    protected RPiAbstractService(String name, ServiceState initialState) {
        this.name = name;
        this.state = initialState;
    }

    public final void enable() {
        innerEnable();
        setState(ServiceState.ENABLED);
    }

    public final void disable() {
        innerDisable();
        setState(ServiceState.DISABLED);
    }

    protected abstract void innerEnable();

    protected abstract void innerDisable();

    public abstract void updateState();

    @JsonIgnore
    public abstract Map<String, GenericParameter> getParameters();

    @JsonIgnore
    public ServiceState getState() {
        return state;
    }

    protected void setState(ServiceState state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

}
