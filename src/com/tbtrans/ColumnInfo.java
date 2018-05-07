package com.tbtrans;

/**
 * Created by bacil on 13.09.2017.
 */

public class ColumnInfo {
    String _name;
    String _type;
    boolean _notnull = false;
    String _default_value;
    int _ckey = 0;
    int _cpos = 0;
    SyntaxItem _syntaxItem;
    String _translateType;

    public int getCPos() {
        return _cpos;
    }
    public SyntaxItem getSyntaxItem() {
        return _syntaxItem;
    }
    public String getName() {
        return _name;
    }
    public String getType() {
        return _type;
    }
    public int isKey() {
        return _ckey;
    }
    public boolean isNotNull() {
        return _notnull;
    }

    public String getTransType() {
        return _translateType;
    }

    public ColumnInfo(String name, String tr_type, String orig_type, Boolean notnull, String default_value, Integer ckey, Integer cpos) {
        _name = name;
        //_syntaxItem = si;
        _translateType = tr_type;
        _type = orig_type;
        _notnull = notnull;
        _default_value = default_value;
        _ckey = ckey;
        _cpos = cpos;

    }

    public String translateType() throws Exception {
        ResultMath r = _syntaxItem.match(_type);
        if (r != null && r.getMatches()) {
            String translate = _syntaxItem.getTranslated();
            return translate;
        }
        return null;
    }
    public ColumnInfo(String name, String type) {
        _name = name;
        _type = type;

    }
}
