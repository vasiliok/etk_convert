package com.tbtrans;

/**
 * Created by bacil on 21.03.2018.
 */
public class Constraint {
    String _name;
    String _text;
    public Constraint(String name, String text) {
        _name = name;
        _text = text;
    }
    public String getName() {
        return _name;
    }
    public String getText() {
        return _text;
    }
}
