package com.fbo.services.core.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;

import java.util.Collections;
import java.util.List;

/**
 * Created by Fred on 27/06/2015.
 */
public class GenericResource<T> extends ResourceSupport {

    private boolean editable;
    private T content;

    public GenericResource() {
        this(null, false);
    }

    public GenericResource(T content, boolean editable) {
        super();
        this.content = content;
        this.editable = editable;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    @Override
    @JsonProperty("links")
    public List<Link> getLinks() {
        return super.getLinks();
    }

}
