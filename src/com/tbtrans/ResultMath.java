package com.tbtrans;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.regex.Matcher;

/**
 * Created by bacil on 12.01.2018.
 */
public class ResultMath {
    Boolean _is_match = false;
    Matcher _matcher;
    SyntaxItem _si;
    public ResultMath(Boolean ismatch) {
        _is_match = ismatch;
    }
    public ResultMath(SyntaxItem si, Matcher matcher) {
        this(matcher == null ? true : matcher.matches());
        _si = si;
        _matcher = matcher;
    }
    public SyntaxItem getSyntaxItem() {
        return _si;
    }
    public String translate() throws Exception {
        if (_matcher == null) {
            return _si.getDBType().toString();
        }
        if (_si.getTranslateCB() != null) {
            if (_si.getParamCount() == 2) {
                return _si.getTranslateCB().apply(Integer.parseInt(_matcher.group(_si.getG1())), Integer.parseInt(_matcher.group(_si.getG2())));
            }
        }
        else {
            if (_si.getParamCount() == 1) {
                return String.format("%s(%s)", _si.getDBType().toString(), _matcher.group(_si.getG1()));
            }
            else if (_si.getParamCount() == 2) {
                return String.format("%s(%s,%s)", _si.getDBType().toString(), _matcher.group(_si.getG1()), _matcher.group(_si.getG2()));
            }
            else if (_si.getParamCount() == 0) {
                return _si.getDBType().toString();
            }
        }
        throw new Exception("parse error");
    }
    public Boolean isMatch() {
        return _is_match;
    }
}
