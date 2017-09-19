package com.openbimmer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by bacil on 19.09.2017.
 */
public class SyntaxItem {
    Integer _g1, _g2, _g3;
    String _regexp;
    Pattern _compileRegexp;
    String _datatype;
    String _source;
    public SyntaxItem(String dataType) {
        _datatype = dataType;
    }
    public SyntaxItem(String dataType, String regexp, Integer g1) {
        this(dataType);
        _regexp = regexp;
        _g1 = g1;
        _compileRegexp = Pattern.compile(_regexp);

        }
    public SyntaxItem(String dataType, String regexp, Integer g1, Integer g2) {
        this(dataType, regexp, g1);
        _g2 = g2;
    }
    public SyntaxItem(String dataType, String regexp, Integer g1, Integer g2, Integer g3) {
        this(dataType, regexp, g1, g2);
        _g3 = g3;
    }
    public List<Object> match(String inp) {
        inp.substring(1, _datatype.length()).equalsIgnoreCase(_datatype)
        Matcher m = _compileRegexp.matcher(inp);
        List<Object> res = new ArrayList<Object>();
        if (m.matches()) {
            if (_g1 != null) res.add(m.group(_g1));
            if (_g2 != null) res.add(m.group(_g2));
            if (_g3 != null) res.add(m.group(_g3));
        }
}
