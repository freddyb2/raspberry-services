package com.fbo.services.core.model;

/**
 * Created by Fred on 27/06/2015.
 */
public class Bounds<T> {
    public static final String SEPARATOR = ";";

    private T min;
    private T max;

    public Bounds() {
    }

    public Bounds(T min, T max) {
        this.min = min;
        this.max = max;
    }

    public T getMin() {
        return min;
    }

    public void setMin(T min) {
        this.min = min;
    }

    public T getMax() {
        return max;
    }

    public void setMax(T max) {
        this.max = max;
    }

    @Override
    public String toString() {
        return String.format("%s%s%s", min.toString(), SEPARATOR, max.toString());
    }
}
