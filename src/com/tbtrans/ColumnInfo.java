package com.tbtrans;

/**
 * Created by bacil on 13.09.2017.
 */

public class ColumnInfo  implements ColumnInfoInterface{
    String _name;
    String _type;
    boolean _notnull = false;
    String _default_value;
    int _ckey = 0;
    int _cpos = 0;
    ResultMath _rm;
    String _translateType;

    public int getCPos() {
        return _cpos;
    }
    public ResultMath getResultMatch() { return _rm; }
    public String getName() {
        return _name;
    }
    public String getType() {
        return _type;
    }
    public int getCKey() {
        return _ckey;
    }
    public boolean isNotNull() {
        return _notnull;
    }


    public ColumnInfo(String name, String orig_type, ResultMath rm, Boolean notnull, String default_value, Integer ckey, Integer cpos) {
        _name = name;
        _rm = rm;
        _type = orig_type;
        _notnull = notnull;
        _default_value = default_value;
        _ckey = ckey;
        _cpos = cpos;
    }

    public ColumnInfo(String name, String type) {
        _name = name;
        _type = type;
    }
}
