package com.fbo.raspberry.services.impl.parameter;

import com.fbo.services.core.model.Bounds;
import com.fbo.services.core.model.GenericParameter;

/**
 * Created by Fred on 27/06/2015.
 */
public class VolumeParameter extends GenericParameter<Integer> {

    public static final int MIN_VOLUME = 0;
    public static final int MAX_VOLUME = 100;

    public VolumeParameter() {
        super("Volume", new Bounds<>(MIN_VOLUME, MAX_VOLUME), MAX_VOLUME);
    }
}
