package com.fbo.raspberry.services.impl.parameter;

/**
 * Created by Fred on 04/02/2016.
 */
public enum Color {

    MULTI(0), BLEU(1), VIOLET(60), ROUGE(120), ORANGE(150),JAUNE(180), VERT(240), CYAN(300);

    private Integer code ;

    Color(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
