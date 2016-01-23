package com.fbo.raspberry.services.impl.controller;

import com.fbo.raspberry.services.impl.service.LedService;
import com.fbo.raspberry.services.impl.service.RadioService;
import com.fbo.raspberry.services.impl.service.ReveilService;
import com.fbo.services.core.model.ParameterValue;
import com.fbo.services.core.model.RPiAbstractService;
import com.fbo.services.core.resource.LinksResource;
import com.fbo.services.core.resource.ParameterResource;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static com.fbo.services.core.model.GenericParameter.PARAMETER_PREFIX_NAME;

@Controller
@RequestMapping("/services")
public class ServiceController {

    public static final String BACK_LINK = Link.REL_PREVIOUS;
    public static final String SELF_LINK = Link.REL_SELF;

    private final Map<String, RPiAbstractService> services = new HashMap<>();

    public ServiceController() {
        RadioService radioService = new RadioService();
        LedService ledService = new LedService();
        services.put("radio", radioService);
        services.put("led", ledService);
        services.put("reveil", new ReveilService(radioService, ledService));
    }

    @RequestMapping(method = RequestMethod.GET)
    public HttpEntity<LinksResource> showAllServices() {
        String ipAdress = getIpAdress();
        LinksResource serviceResource = new LinksResource("RaspberryPi " + ipAdress);
        services.forEach((id, service) -> serviceResource.add(linkTo(ServiceController.class).slash(id).withRel(service.getName())));
        return new ResponseEntity<>(serviceResource, HttpStatus.OK);
    }

    private String getIpAdress() {
        final String defaultResult = "";
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface intf = networkInterfaces.nextElement();
                if (intf.isUp() && !intf.isLoopback()) {
                    return intf.getInterfaceAddresses().stream().findFirst()
                            .map(addr -> addr.getAddress() + "/" + addr.getNetworkPrefixLength()).orElse(defaultResult);
                }
            }
        } catch (SocketException e1) {
            e1.printStackTrace();
        }
        return defaultResult;
    }

    @RequestMapping(value = "/{serviceId}", method = RequestMethod.GET)
    public HttpEntity<LinksResource> show(@PathVariable() String serviceId) {
        RPiAbstractService service = services.get(serviceId);
        LinksResource linksResource = new LinksResource(service.getName());
        if (service.getParameters().size() > 0) {
            linksResource.add(linkTo(methodOn(ServiceController.class).showParameters(serviceId)).withRel("Configuration"));
        }
        switch (service.getState()) {
            case ENABLED:
                linksResource.add(linkTo(methodOn(ServiceController.class).disable(serviceId)).withRel("Disable"));
                break;
            case DISABLED:
                linksResource.add(linkTo(methodOn(ServiceController.class).enable(serviceId)).withRel("Enable"));
                break;
        }
        linksResource.add(linkTo(methodOn(ServiceController.class).showAllServices()).withRel(BACK_LINK));
        return new ResponseEntity<>(linksResource, HttpStatus.OK);
    }


    @RequestMapping(value = "/{serviceId}/enable", method = RequestMethod.GET)
    public HttpEntity<LinksResource> enable(@PathVariable String serviceId) {
        return doActionOnService(serviceId, RPiAbstractService::enable);
    }

    @RequestMapping(value = "/{serviceId}/disable", method = RequestMethod.GET)
    public HttpEntity<LinksResource> disable(@PathVariable String serviceId) {
        return doActionOnService(serviceId, RPiAbstractService::disable);
    }

    @RequestMapping(value = "/{serviceId}/configuration", method = RequestMethod.GET)
    public HttpEntity<LinksResource> showParameters(@PathVariable String serviceId) {
        RPiAbstractService service = services.get(serviceId);
        LinksResource linksResource = new LinksResource(String.format("%s configuration", service.getName()));
        service.getParameters().forEach((id, parameter) -> linksResource.add(linkTo(methodOn(ServiceController.class).showParameter(serviceId, id)).withRel(PARAMETER_PREFIX_NAME + parameter.getName())));
        linksResource.add(linkTo(methodOn(ServiceController.class).show(serviceId)).withRel(BACK_LINK));
        return new ResponseEntity<>(linksResource, HttpStatus.OK);
    }


    @RequestMapping(value = "/{serviceId}/configuration/{parameterId}", method = RequestMethod.GET)
    public HttpEntity<ParameterResource> showParameter(@PathVariable String serviceId, @PathVariable String parameterId) {
        ParameterResource parameterResource = services.get(serviceId).getParameters().get(parameterId).toResource();
        parameterResource.add(linkTo(methodOn(ServiceController.class).showParameter(serviceId, parameterId)).withRel(SELF_LINK));
//        parameterResource.add(linkTo(methodOn(ServiceController.class).showParameters(serviceId)).withRel(BACK_LINK));
        return new ResponseEntity<>(parameterResource, HttpStatus.OK);
    }

    @RequestMapping(value = "/{serviceId}/configuration/{parameterId}", method = RequestMethod.PUT)
    public ResponseEntity setParameter(@PathVariable String serviceId, @PathVariable String parameterId, @RequestBody ParameterValue parameterValue) {
        RPiAbstractService service = services.get(serviceId);
        service.getParameters().get(parameterId).setValue(parameterValue.getStrValue());
        service.updateState();
        return new ResponseEntity(HttpStatus.OK);
    }

    private HttpEntity<LinksResource> doActionOnService(String serviceId, Consumer<RPiAbstractService> consumer) {
        consumer.accept(services.get(serviceId));
        return show(serviceId);
    }


}
