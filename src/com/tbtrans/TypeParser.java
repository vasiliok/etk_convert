package com.tbtrans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bacil on 19.09.2017.
 */
public class TypeParser {
    List<SyntaxItem> listSyntaxItems = new ArrayList<SyntaxItem>();

    public List<SyntaxItem> getListSyntaxItems() {
        return listSyntaxItems;
    }

    public TypeParser() {
        listSyntaxItems.add(new SyntaxItem("TINYINT", DBType.TINYINT));
        listSyntaxItems.add(new SyntaxItem("SMALLINT", DBType.SMALLINT));
        listSyntaxItems.add(new SyntaxItem("INTEGER", DBType.INT));
        listSyntaxItems.add(new SyntaxItem("BIGINT", DBType.BIGINT));

        listSyntaxItems.add(new SyntaxItem("NUMERIC",
                "(\\((\\d+)(,(\\d+))?\\))?", 2, 4,
                (Integer precision, Integer scale) -> {return String.format("DECIMAL(%d, %d)", precision, scale);} ));

        listSyntaxItems.add(new SyntaxItem("DECIMAL",
                "(\\((\\d+)(,(\\d+))?\\))?", 2, 4,
                (Integer precision, Integer scale) -> {return String.format("DECIMAL(%d, %d)", precision, scale);} ));

        listSyntaxItems.add(new SyntaxItem("FLOAT", DBType.FLOAT));
        listSyntaxItems.add(new SyntaxItem("DOUBLE", DBType.DOUBLE));
        listSyntaxItems.add(new SyntaxItem("REAL", DBType.REAL));
        listSyntaxItems.add(new SyntaxItem("VARCHAR", "(\\((\\d+)\\))?", 2, DBType.VARCHAR, "\""));
        listSyntaxItems.add(new SyntaxItem("CHAR", "(\\((\\d+)\\))?", 2, DBType.CHAR, "\""));
        listSyntaxItems.add(new SyntaxItem("CHAR(*)", DBType.CHAR));
        listSyntaxItems.add(new SyntaxItem("STRING", DBType.CHAR));
        listSyntaxItems.add(new SyntaxItem("BINCHAR","(\\((\\d+)\\))?", 2, DBType.BINARY));
        listSyntaxItems.add(new SyntaxItem("BINCHAR(*)", DBType.BINARY));
        listSyntaxItems.add(new SyntaxItem("BITS", "\\((\\d+)\\)", 1, DBType.BIT));
        listSyntaxItems.add(new SyntaxItem("BITS(*)", DBType.BIT));
        listSyntaxItems.add(new SyntaxItem("BITS2", "\\((\\d+)\\)", 1, DBType.BIT));
        listSyntaxItems.add(new SyntaxItem("BOOL", DBType.BOOL));
        listSyntaxItems.add(new SyntaxItem("DATETIME[yy:ms]", DBType.TIMESTAMP));
        //listSyntaxItems.add(new SyntaxItem("DATETIME", "\\[(\\w+)(:(\\w+))?\\]", 1, 3, DBType.TIMESTAMP));
        listSyntaxItems.add(new SyntaxItem("DATE", DBType.DATE));
        listSyntaxItems.add(new SyntaxItem("TIME", DBType.TIME));
        listSyntaxItems.add(new SyntaxItem("TIMESTAMP", DBType.TIMESTAMP));
        listSyntaxItems.add(new SyntaxItem("TIMESPAN", "\\[(\\w+)(:(\\w+))?\\]", 1, 3, DBType.TIME));
        listSyntaxItems.add(new SyntaxItem("BLOB", DBType.BLOB));
        listSyntaxItems.add(new SyntaxItem("CLOB"));
    }

    public SyntaxItem findSyntaxItem(String c) {
        for (SyntaxItem k : listSyntaxItems) {
            if (k.match1(c)) {
                return k;
            }
        }
        return null;
    }

    public String parseEx(String src) throws Exception {
        ResultMath lo;
        for (SyntaxItem k: listSyntaxItems) {
            lo = k.match(src);
            if (lo != null && lo.getMatches()) {
                return k.getTranslated();
            }
        }
        return null;
    }

}
