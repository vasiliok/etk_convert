package com.openbimmer.src;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by bacil on 13.09.2017.
 */
public class ColumnType {
//    "TINYINT",
//    | SMALLINT
//    | INTEGER
//    | BIGINT
//    | NUMERIC [(Precision [,Scale])]
//            | DECIMAL [(Precision [,Scale])]
//            | FLOAT
//    13
//            14
//    CHAPTER 2.
//            | DOUBLE
//    | REAL
//    | VARCHAR [(Precision)]
//            | CHAR [(Precision)]
//            | CHAR(*)
//    | STRING
//    | BINCHAR [(Precision)]
//            | BINCHAR (*)
//    | BITS (Precision)
//    | BITS (*)
//    | BITS2 (Precision)
//    | BITS2 (*)
//    | BOOL
//    | DATETIME Range
//    | DATE
//    | TIME
//    | TIMESTAMP
//    | TIMESPAN Range
//    | INTERVAL StartSpec [ TO EndSpec ]
//            | BLOB
//    | CLOB
    public enum Type {
        Integer ("numeric"),
        String ("char"),
        Datetime ("datetime");

         Type(String numeric) {
        }
    };
    Integer _length;
    Type _type;
    String _format;

    public ColumnType() {


    }

    public ColumnType(Type type, Integer length, String fmt) {
        _type = type;
        _length = length;
        _format = fmt;
    }
    public ColumnType(String type) {
        Pattern p = Pattern.compile("(\\w+)((\\([\\d\\,\\*]?\\))|(\\[(\\w+:\\w+)\\]))");
        Matcher m = p.matcher(type);
        if (m.matches()) {
            int c = m.groupCount();
            String name = m.group(1);
            if (name.equals("datetime")) {
                new ColumnType(Type.Datetime, 0, m.group(6));
            }
            else {
                new ColumnType(Type.Datetime, 0, m.group(6));
            }

            String g1 =m.group(1);
            String g2 =m.group(2);
        }


    }
}
