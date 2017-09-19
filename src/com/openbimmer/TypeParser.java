package com.openbimmer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bacil on 19.09.2017.
 */
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
        listSyntaxItems.add(new SyntaxItem("TINYINT"));
        listSyntaxItems.add(new SyntaxItem("SMALLINT"));
        listSyntaxItems.add(new SyntaxItem("INTEGER"));
        listSyntaxItems.add(new SyntaxItem("BIGINT"));
        listSyntaxItems.add(new SyntaxItem("NUMERIC", "(\\((\\d+)(,(\\d+))?\\))?", 2, 4));
        listSyntaxItems.add(new SyntaxItem("DECIMAL", "(\\((\\d+)(,(\\d+))?\\))?", 2, 4));
        listSyntaxItems.add(new SyntaxItem("FLOAT"));
        listSyntaxItems.add(new SyntaxItem("DOUBLE"));
        listSyntaxItems.add(new SyntaxItem("REAL"));
        listSyntaxItems.add(new SyntaxItem("VARCHAR", "(\\((\\d+)\\))?", 2));
        listSyntaxItems.add(new SyntaxItem("CHAR", "(\\((\\d+)\\))?", 2));
        listSyntaxItems.add(new SyntaxItem("CHAR(*)"));
        listSyntaxItems.add(new SyntaxItem("STRING"));
        listSyntaxItems.add(new SyntaxItem("BINCHAR","(\\((\\d+)\\))?", 2));
        listSyntaxItems.add(new SyntaxItem("BINCHAR(*)"));
        listSyntaxItems.add(new SyntaxItem("BITS", "\\((\\d+)\\)", 1));
        listSyntaxItems.add(new SyntaxItem("BITS(*)"));
        listSyntaxItems.add(new SyntaxItem("BITS2", "\\((\\d+)\\)", 1));
        listSyntaxItems.add(new SyntaxItem("BOOL"));
        listSyntaxItems.add(new SyntaxItem("DATETIME", "\\[(\\w+)(:(\\w+))?\\]", 1,3));
        listSyntaxItems.add(new SyntaxItem("DATE"));
        listSyntaxItems.add(new SyntaxItem("TIME"));
        listSyntaxItems.add(new SyntaxItem("TIMESTAMP"));
        listSyntaxItems.add(new SyntaxItem("TIMESPAN", "\\[(\\w+)(:(\\w+))?\\]", 1, 3));
        listSyntaxItems.add(new SyntaxItem("BLOB"));
        listSyntaxItems.add(new SyntaxItem("CLOB"));

    }
    public void parse(String source) {
        for (SyntaxItem i: listSyntaxItems) {
            i.match(source);
        }
    }
}
