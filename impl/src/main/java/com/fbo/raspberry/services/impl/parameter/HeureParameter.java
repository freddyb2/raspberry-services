package com.fbo.raspberry.services.impl.parameter;

import com.fbo.services.core.model.Bounds;
import com.fbo.services.core.model.GenericParameter;

/**
 * Created by Fred on 27/10/2015.
 */
public class HeureParameter extends GenericParameter<Integer> {

    public static final int MIN_HOUR = 0;
    public static final int MAX_HOUR = 24;
    public static final int DEFAULT_HOUR = 7;

    public HeureParameter() {
        super("Heure", new Bounds<>(MIN_HOUR, MAX_HOUR), DEFAULT_HOUR);
    }
}