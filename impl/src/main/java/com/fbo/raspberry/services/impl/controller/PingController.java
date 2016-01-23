package com.fbo.raspberry.services.impl.controller;

import com.fbo.services.core.resource.LinksResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Fred on 23/01/2016.
 */
@Controller
@RequestMapping("/ping")
public class PingController {

    @RequestMapping(method = RequestMethod.GET)
    public HttpEntity<LinksResource> answerPing() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
