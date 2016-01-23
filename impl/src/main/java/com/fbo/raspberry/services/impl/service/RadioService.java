package com.fbo.raspberry.services.impl.service;

import com.fbo.media.radio.vlc.VLCRadioPlayer;
import com.fbo.services.core.model.GenericParameter;
import com.fbo.services.core.model.RPiAbstractService;
import com.fbo.services.core.model.ServiceState;
import com.fbo.raspberry.services.impl.parameter.StationParameter;
import com.fbo.raspberry.services.impl.parameter.VolumeParameter;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RadioService extends RPiAbstractService {

    public static final String STATION_PARAMETER_ID = "station";
    public static final String VOLUME_PARAMETER_ID = "volume";

    private final StationParameter stationParameter = new StationParameter();
    private final VolumeParameter volumeParameter = new VolumeParameter();

    private final Map<String, GenericParameter> parameters = new HashMap<>();

    public RadioService() {
        super("Radio", ServiceState.NOT_CONFIGURED);
        parameters.put(STATION_PARAMETER_ID, stationParameter);
        parameters.put(VOLUME_PARAMETER_ID, volumeParameter);
    }

    @Override
    protected void innerEnable() {
        startPlayer();
    }

    @Override
    protected void innerDisable() {
        VLCRadioPlayer.getInstance().stop();
    }

    @Override
    public void updateState() {
        if (getState() == ServiceState.NOT_CONFIGURED && stationParameter.getObjectValue() != null) {
            setState(ServiceState.DISABLED);
        }
        startPlayer();
    }

    private void startPlayer() {
        VLCRadioPlayer.getInstance().setVolume(volumeParameter.getObjectValue());
        VLCRadioPlayer.getInstance().start(stationParameter.getObjectValue());
    }

    @Override
    public Map<String, GenericParameter> getParameters() {
        return parameters;
    }
}
