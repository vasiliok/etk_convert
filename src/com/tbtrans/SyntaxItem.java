package com.tbtrans;

import java.util.Objects;
import java.util.function.BiFunction;
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
    Integer _paramCount;
    String _translateResult;
    Matcher _matcher;

    public BiFunction<Integer, Integer, String> getTranslateCB() {
        return _translateCB;
    }

    BiFunction<Integer, Integer, String> _translateCB;

    public DBType getDBType() {
        return _dbtype;
    }

    public String getWrap() {
        return _wrap;
    }

    DBType _dbtype = DBType.NONE;

    ResultMath _resultMath = null;
    String _wrap;

    public SyntaxItem(String dataType) {
        _datatype = dataType;
        _g1 = 0;
        _g2 = 0;
        _g3 = 0;
        _dbtype = DBType.NONE;
        _paramCount = 0;
        _translateResult = null;
    }

    public SyntaxItem(String dataType, DBType dbtype, String wrap) {
        this(dataType);
        _dbtype = dbtype;
        _wrap = wrap;
    }

    // new SyntaxItem("TINYINT", DBType.TINYINT)
    public SyntaxItem(String dataType, DBType dbtype) {
        this(dataType,  dbtype, "");
    }

    public SyntaxItem(String dataType, String regexp, Integer g1, String wrap) {
        this(dataType);
        _compileRegexp = Pattern.compile(regexp);
        _g1 = g1;
        _paramCount =1;
        _wrap = wrap;
    }
    public SyntaxItem(String dataType, String regexp, Integer g1) {
        this(dataType, regexp, g1, "");
    }

    public SyntaxItem(String dataType, String regexp, Integer g1, DBType dbtype, String wrap) {
        this(dataType, regexp, g1, wrap);
        _paramCount =1;
        _dbtype = dbtype;
    }

    public SyntaxItem(String dataType, String regexp, Integer g1, DBType dbtype) {
        this(dataType, regexp, g1, dbtype, "");
    }

    public SyntaxItem(String dataType, String regexp, Integer g1, Integer g2, String wrap) {
        this(dataType, regexp, g1, wrap);
        _g2 = g2;
        _paramCount =2;
    }

    public SyntaxItem(String dataType, String regexp, Integer g1, Integer g2, DBType dbtype, String wrap) {
        this(dataType, regexp, g1, g2, wrap);
        _dbtype = dbtype;
    }
    public SyntaxItem(String dataType, String regexp, Integer g1, Integer g2, DBType dbtype) {
        this(dataType, regexp, g1, g2, "");
        _dbtype = dbtype;
    }

    public SyntaxItem(String dataType, String regexp, Integer g1, Integer g2, BiFunction<Integer, Integer, String> cb) {
        this(dataType, regexp, g1, g2, "");
        _translateCB = cb;
    }

    public Integer getParamCount() {
        return _paramCount;
    }

    public SyntaxItem(String dataType, String regexp, Integer g1, Integer g2, Integer g3, String wrap) {
        this(dataType, regexp, g1, g2, wrap);
        _g3 = g3;
        _paramCount = 3;
    }

    // todo:
    // Return a object that could give opportunity check for matching string and take substring group
    //
    // inp string databse type for matching
    public ResultMath match(String inp) {
        String tile;
        ResultMath res = new ResultMath(this, false);
        if (inp.length() >= _datatype.length() && inp.substring(0, _datatype.length()).equalsIgnoreCase(_datatype))
        {
            if (inp.length() == _datatype.length()) {
                _resultMath = new ResultMath(this, true);
                return _resultMath;
            }

            if (_compileRegexp == null) {
                _resultMath = new ResultMath(this, false);
                return _resultMath;
            }
            tile = inp.substring(_datatype.length());
            Matcher m = _compileRegexp.matcher(tile);

            if (m.matches()) {
                res = new ResultMath(this, true);
                if (_g1 != 0) res.addGroup(m.group(_g1));
                if (_g2 != 0) res.addGroup(m.group(_g2));
                if (_g3 != 0) res.addGroup(m.group(_g3));
            }
            else {
                res = new ResultMath(this, false);
            }
        }
        _resultMath = res;
        return res;
    }

    public boolean match1(String inp) {
        String tile;
        if (inp.length() >= _datatype.length() && inp.substring(0, _datatype.length()).equalsIgnoreCase(_datatype))
        {
            setSource(inp);
            if (inp.length() == _datatype.length()) {
                return true;
            }
            if (_compileRegexp == null) {
                return false;
            }
            tile = inp.substring(_datatype.length());
            _matcher = _compileRegexp.matcher(tile);

            return _matcher.matches();
        }
        return false;
    }
    public String translate() throws Exception {
        if (_matcher == null) {
            return _dbtype.toString();
        }
        if (_translateCB != null) {
            if (_paramCount == 2) {
                return _translateCB.apply(Integer.parseInt(_matcher.group(_g1)), Integer.parseInt(_matcher.group(_g2)));
            }
        }
        else {
            if (_paramCount == 1) {
                return String.format("%s(%s)", _dbtype.toString(), _matcher.group(_g1));
            }
            else if (_paramCount == 2) {
                return String.format("%s(%s,%s)", _dbtype.toString(), _matcher.group(_g1), _matcher.group(_g2));
            }
            else if (_paramCount == 0) {
                return _dbtype.toString();
            }
        }
        throw new Exception("parse error");
    }

    String getTranslated() throws Exception {
        Objects.requireNonNull(_resultMath);
        if (_translateCB != null) {
            if (_paramCount == 2) {
                return _translateCB.apply(Integer.parseInt(_resultMath.getGroup(0)), Integer.parseInt(_resultMath.getGroup(1)));
            }
        }
        else {
            if (_paramCount == 1) {
                return String.format("%s(%s)", _dbtype.toString(), _resultMath.getGroup(0));
            }
            else if (_paramCount == 2) {
                return String.format("%s(%s,%s)", _dbtype.toString(), _resultMath.getGroup(0), _resultMath.getGroup(1));
            }
            else if (_paramCount == 0) {
                return _dbtype.toString();
            }
            else {
                throw new Exception("parse error");
            }
        }
        return new String();
    }
    String getSource() {
        return _source;
    }
    void setSource(String s) {
        _source = s;
    }


}
