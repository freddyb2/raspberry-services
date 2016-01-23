package com.fbo.services.core.resource;

/**
 * Created by Fred on 27/06/2015.
 */
public interface Resourceable<R extends GenericResource> {

    R toResource();
}
