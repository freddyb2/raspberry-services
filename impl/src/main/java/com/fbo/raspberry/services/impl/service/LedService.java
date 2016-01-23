package com.fbo.raspberry.services.impl.service;

import com.fbo.services.core.model.GenericParameter;
import com.fbo.services.core.model.RPiAbstractService;
import com.fbo.services.core.model.ServiceState;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Fred on 27/06/2015.
 */
public class LedService extends RPiAbstractService {

    public static final String TEST_WS2801_PATH = "/home/pi/bcm2835/bcm2835-1.42/examples/spi/ws2801_spi_tester";
    public static final String STOP_WS2801_PATH = "/home/pi/bcm2835/bcm2835-1.42/examples/spi/stop";

    private final Map<String, GenericParameter> parameterMap = new HashMap<>();

    private Optional<Process> processHolder = Optional.empty();

    public LedService() {
        super("LED lights", ServiceState.DISABLED);
    }

    @Override
    public Map<String, GenericParameter> getParameters() {
        return parameterMap;
    }

    @Override
    protected void innerEnable() {
        System.out.println("Put the lights on...");
        try {
            Process p = Runtime.getRuntime().exec(TEST_WS2801_PATH);
            processHolder = Optional.of(p);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void innerDisable() {
        System.out.println("Put the lights off...");
        processHolder.ifPresent(process -> {
            process.destroy();
            try {
                Process p = Runtime.getRuntime().exec(STOP_WS2801_PATH);
                p.waitFor();
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        });
        processHolder = Optional.empty();
    }


    @Override
    public void updateState() {
        //Nothing to do
    }
}
