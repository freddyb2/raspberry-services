package com.fbo.raspberry.services.impl.parameter;

import com.fbo.media.radio.RadioStation;
import com.fbo.services.core.model.GenericParameter;


/**
 * Created by Fred on 27/06/2015.
 */
public class StationParameter extends GenericParameter<RadioStation> {

    public StationParameter() {
        super("Station", RadioStation.values(), RadioStation::valueOf, null);
    }


}
