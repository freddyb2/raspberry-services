package com.fbo.raspberry.services.impl.parameter;

import com.fbo.services.core.model.Bounds;
import com.fbo.services.core.model.GenericParameter;

/**
 * Created by Fred on 27/10/2015.
 */
public class MinuteParameter extends GenericParameter<Integer> {

    public static final int MIN_MINUTE = 0;
    public static final int MAX_MINUTE = 60;

    public MinuteParameter() {
        super("Minute", new Bounds<>(MIN_MINUTE, MAX_MINUTE), MIN_MINUTE);
    }
}