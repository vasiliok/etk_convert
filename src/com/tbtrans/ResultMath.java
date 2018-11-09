package com.tbtrans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bacil on 12.01.2018.
 */
public class ResultMath {
    List<String> _groups = null;
    Boolean _matches = null;
    SyntaxItem _si;
    public ResultMath(SyntaxItem si, boolean matches) {
        _si = si;
        _matches = matches;
        _groups = new ArrayList<>();
    }
    public boolean getMatches() {
        return _matches;
    }
//    public setMatches(boolean matches) {
//        _matches = matches;
//    }
    public void addGroup(String v) {
        _groups.add(v);
    }
    public int groupCount() {
        return _groups.size();
    }
    public String getGroup(int i) {
        return _groups.get(i);
    }
}
