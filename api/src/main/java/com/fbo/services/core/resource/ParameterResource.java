package com.fbo.services.core.resource;

import com.fbo.services.core.model.GenericParameter;

/**
 * Created by Fred on 27/06/2015.
 */
public class ParameterResource<T> extends GenericResource<GenericParameter<T>> {

    public ParameterResource() {
    }

    public ParameterResource(GenericParameter<T> content) {
        this(true, content);
    }

    private ParameterResource(boolean editable, GenericParameter<T> content) {
        super(content, editable);
    }

}
