package com.fbo.raspberry.services.impl.parameter;

import com.fbo.services.core.model.GenericParameter;

/**
 * Created by Fred on 03/02/2016.
 */
public class ColorParameter extends GenericParameter<Color> {

    public ColorParameter() {
        super("Couleur", Color.values(), Color::valueOf, Color.MULTI);
    }
}