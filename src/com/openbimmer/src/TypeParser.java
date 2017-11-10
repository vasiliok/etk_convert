package com.openbimmer.src;

import com.openbimmer.src.SyntaxItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bacil on 19.09.2017.
 */
enum DBType {
    NONE,
    TINYINT,
    SMALLINT,
    MEDIUMINT,
    INT,
    BIGINT,
    FLOAT,
    DOUBLE,
    DECIMAL,
    NUMERIC,


}
public class TypeParser {
    List<SyntaxItem> listSyntaxItems = new ArrayList<SyntaxItem>();

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


    public TypeParser() {
        listSyntaxItems.add(new SyntaxItem("TINYINT", DBType.TINYINT));
        listSyntaxItems.add(new SyntaxItem("SMALLINT", DBType.SMALLINT));
        listSyntaxItems.add(new SyntaxItem("INTEGER", DBType.INT));
        listSyntaxItems.add(new SyntaxItem("BIGINT", DBType.BIGINT));
        listSyntaxItems.add(new SyntaxItem("NUMERIC", "(\\((\\d+)(,(\\d+))?\\))?", 2, 4, DBType.NUMERIC));
        listSyntaxItems.add(new SyntaxItem("DECIMAL", "(\\((\\d+)(,(\\d+))?\\))?", 2, 4, DBType.DECIMAL));
        listSyntaxItems.add(new SyntaxItem("FLOAT", DBType.FLOAT));
        listSyntaxItems.add(new SyntaxItem("DOUBLE", DBType.DOUBLE));
        listSyntaxItems.add(new SyntaxItem("REAL"));
        listSyntaxItems.add(new SyntaxItem("VARCHAR", "(\\((\\d+)\\))?", 2));
        listSyntaxItems.add(new SyntaxItem("CHAR", "(\\((\\d+)\\))?", 2));
        listSyntaxItems.add(new SyntaxItem("CHAR(*)"));
        listSyntaxItems.add(new SyntaxItem("STRING"));
        listSyntaxItems.add(new SyntaxItem("BINCHAR","(\\((\\d+)\\))?", 2));
        listSyntaxItems.add(new SyntaxItem("BINCHAR(*)"));
        listSyntaxItems.add(new SyntaxItem("BITS", "\\((\\d+)\\)", 1, 0, Integer.class));
        listSyntaxItems.add(new SyntaxItem("BITS(*)", "", 0, 0, Integer.class));
        listSyntaxItems.add(new SyntaxItem("BITS2", "\\((\\d+)\\)", 1, 0, Integer.class));
        listSyntaxItems.add(new SyntaxItem("BOOL", "", 0, 0, Boolean.class));
        listSyntaxItems.add(new SyntaxItem("DATETIME", "\\[(\\w+)(:(\\w+))?\\]", 1, 3, String.class));
        listSyntaxItems.add(new SyntaxItem("DATE"));
        listSyntaxItems.add(new SyntaxItem("TIME"));
        listSyntaxItems.add(new SyntaxItem("TIMESTAMP"));
        listSyntaxItems.add(new SyntaxItem("TIMESPAN", "\\[(\\w+)(:(\\w+))?\\]", 1, 3, String.class));
        listSyntaxItems.add(new SyntaxItem("BLOB"));
        listSyntaxItems.add(new SyntaxItem("CLOB"));

    }
    public void parse(String source) {
        System.out.printf("-- parse(%s)\n", source);
        for (SyntaxItem i: listSyntaxItems) {
            List<Object> r = i.match(source);
            if (r != null) {
                if (r.size() > 0) {
                    System.out.format("Clazz: %s\n", i.getClazz().toString());
                    for (Object e: r) {
                        System.out.format("%s, ", e);
                    }
                    System.out.println();
                }
                System.out.println (i._datatype);
                break;

            }
        }
    }
}
