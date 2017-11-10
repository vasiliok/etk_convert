package com.openbimmer.src;

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
    Class<?> _clazz = String.class;
    DBType _dbtype = DBType.NONE;

    public SyntaxItem(String dataType) {
        _datatype = dataType;
        _g1 = 0;
        _g2 = 0;
        _g3 = 0;
    }

    public SyntaxItem(String dataType, DBType dbtype) {
        this(dataType);
        _dbtype = dbtype;
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
        _clazz = Integer.class;
        _dbtype = DBType.INT;
    }

    public SyntaxItem(String dataType, String regexp, Integer g1, Integer g2, DBType dbtype) {
        this(dataType, regexp, g1, g2);
        //_clazz = clazz;
        _dbtype = dbtype;
    }

    public SyntaxItem(String dataType, String regexp, Integer g1, Integer g2, Integer g3) {
        this(dataType, regexp, g1, g2);
        _g3 = g3;
    }

    public List<Object> match(String inp) {
        List<Object> res = null;
        String tile;

        if (inp.length() >= _datatype.length() && inp.substring(0, _datatype.length()).equalsIgnoreCase(_datatype))
        {
            if (inp.length() == _datatype.length()) {
                return new ArrayList<Object>();
            }

            tile = inp.substring(_datatype.length());
            Matcher m = _compileRegexp.matcher(tile);

            if (m.matches()) {
                res = new ArrayList<Object>();
                if (_g1 != null) res.add(m.group(_g1));
                if (_g2 != null) res.add(m.group(_g2));
                if (_g3 != null) res.add(m.group(_g3));
            }

        }
        return res;
    }
    public Class<?> getClazz() {
        return _clazz;
    }
}
