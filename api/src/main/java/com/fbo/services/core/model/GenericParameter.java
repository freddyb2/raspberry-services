package com.fbo.services.core.model;

import com.fbo.services.core.resource.ParameterResource;
import com.fbo.services.core.resource.Resourceable;

import java.util.function.Function;

/**
 * Created by Fred on 27/06/2015.
 */
public class GenericParameter<T> implements Resourceable<ParameterResource<T>> {


    public static final String PARAMETER_PREFIX_NAME = "*";

    private String name;
    private Bounds<T> bounds;
    private T[] possibleValues;
    private ConstraintKind constraintKind;
    private Function<String, T> transformer;
    private T objectValue;

    public GenericParameter() {
    }

    private GenericParameter(String name, Bounds<T> bounds, T[] possibleValues, ConstraintKind constraintKind, Function<String, T> transformer, T objectValue) {
        this.name = name;
        this.bounds = bounds;
        this.possibleValues = possibleValues;
        this.constraintKind = constraintKind;
        this.transformer = transformer;
        this.objectValue = objectValue;
    }

    protected GenericParameter(String name, Bounds<T> bounds, T value) {
        this(name, bounds, null, ConstraintKind.INT_BOUNDS, null, value);
    }

    protected GenericParameter(String name, T[] possibleValues, Function<String, T> transformer, T value) {
        this(name, null, possibleValues, ConstraintKind.ENUM, transformer, value);
    }

    public T getObjectValue() {
        return objectValue;
    }

    private void setObjectValue(Object value) {
        this.objectValue = (T) value;
    }

    public void setValue(String value) {
        Object newValue;
        switch (constraintKind) {
            case INT_BOUNDS:
                newValue = Integer.valueOf(value);
                break;
            case ENUM:
                newValue = transformer.apply(value);
                break;
            default:
                newValue = value;
        }
        setObjectValue(newValue);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bounds<T> getBounds() {
        return bounds;
    }

    public void setBounds(Bounds<T> bounds) {
        this.bounds = bounds;
    }

    public T[] getPossibleValues() {
        return possibleValues;
    }

    public void setPossibleValues(T[] possibleValues) {
        this.possibleValues = possibleValues;
    }

    public ConstraintKind getConstraintKind() {
        return constraintKind;
    }

    public void setConstraintKind(ConstraintKind constraintKind) {
        this.constraintKind = constraintKind;
    }

    @Override
    public final ParameterResource<T> toResource() {
        return new ParameterResource(this);
    }
}
