package com.fbo.raspberry.services.impl.service;

import com.fbo.raspberry.services.impl.parameter.HeureParameter;
import com.fbo.raspberry.services.impl.parameter.MinuteParameter;
import com.fbo.raspberry.services.impl.parameter.StationParameter;
import com.fbo.raspberry.services.impl.parameter.VolumeParameter;
import com.fbo.services.core.model.GenericParameter;
import com.fbo.services.core.model.RPiAbstractService;
import com.fbo.services.core.model.ServiceState;

import java.util.*;

/**
 * Created by Fred on 27/10/2015.
 */
public class ReveilService extends RPiAbstractService {

    public static final long ONE_DAY_IN_MILLIS = 24 * 60 * 60 * 1000;

    private final RadioService radioService;
    private final LedService ledService;

    public static final String STATION_PARAMETER_ID = "station";
    public static final String HEURE_PARAMETER_ID = "heure";
    public static final String MINUTE_PARAMETER_ID = "minute";

    private final HeureParameter heureParameter = new HeureParameter();
    private final MinuteParameter minuteParameter = new MinuteParameter();
    private final StationParameter stationParameter = new StationParameter();

    private final Map<String, GenericParameter> parameters = new HashMap<>();

    private final Timer timer = new Timer("Timer pour le réveil");
    private TimerTask timerTask;

    public ReveilService(RadioService radioService, LedService ledService) {
        super("Réveil", ServiceState.NOT_CONFIGURED);
        this.radioService = radioService;
        this.ledService = ledService;
        parameters.put(HEURE_PARAMETER_ID, heureParameter);
        parameters.put(MINUTE_PARAMETER_ID, minuteParameter);
        parameters.put(STATION_PARAMETER_ID, stationParameter);
    }

    @Override
    protected void innerEnable() {
        updateTimer(ServiceState.ENABLED);
    }

    @Override
    protected void innerDisable() {
        updateTimer(ServiceState.DISABLED);
    }

    @Override
    public void updateState() {
        if (stationParameter.getObjectValue() != null) {
            if (getState()==ServiceState.ENABLED){
                disable();
            }
            enable();
        }
    }

    private void updateTimer(ServiceState serviceState) {
        switch (serviceState) {
            case ENABLED:
                timerTask = buildTimerTask();
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, heureParameter.getObjectValue());
                calendar.set(Calendar.MINUTE, minuteParameter.getObjectValue());
                calendar.set(Calendar.SECOND, 0);
                if (calendar.before(Calendar.getInstance())) {
                    calendar.roll(Calendar.DATE, true);
                }
                timer.schedule(timerTask, calendar.getTime(), ONE_DAY_IN_MILLIS);
                System.out.println("Réveil programmé : " + calendar.getTime());
                break;
            case DISABLED:
                if (timerTask != null) {
                    timerTask.cancel();
                    timerTask = null;
                    timer.purge();
                    System.out.println("Réveil déprogrammé");
                }
                break;
        }
    }

    private TimerTask buildTimerTask() {
        return new TimerTask() {
            @Override
            public void run() {
                // On allume la radio
                radioService.getParameters().get(RadioService.STATION_PARAMETER_ID).setValue(stationParameter.getObjectValue().name());
                radioService.getParameters().get(RadioService.VOLUME_PARAMETER_ID).setValue(new VolumeParameter().getObjectValue().toString()); // Set volume to default (100)
                radioService.enable();
                // On allume les LEDs
                ledService.enable();
            }
        };
    }

    @Override
    public Map<String, GenericParameter> getParameters() {
        return parameters;
    }

}
