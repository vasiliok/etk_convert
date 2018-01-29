package com.tbtrans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bacil on 19.09.2017.
 */
public class TypeParser {
    List<SyntaxItem> listSyntaxItems = new ArrayList<SyntaxItem>();

    public TypeParser() {
        listSyntaxItems.add(new SyntaxItem("TINYINT", DBType.TINYINT));
        listSyntaxItems.add(new SyntaxItem("SMALLINT", DBType.SMALLINT));
        listSyntaxItems.add(new SyntaxItem("INTEGER", DBType.INT));
        listSyntaxItems.add(new SyntaxItem("BIGINT", DBType.BIGINT));

        listSyntaxItems.add(new SyntaxItem("NUMERIC",
                "(\\((\\d+)(,(\\d+))?\\))?", 2, 4,
                (Integer precision, Integer scale) -> {return String.format("Decimal(%d, %d)", precision, scale);} ));

        listSyntaxItems.add(new SyntaxItem("DECIMAL",
                "(\\((\\d+)(,(\\d+))?\\))?", 2, 4,
                (Integer precision, Integer scale) -> {return String.format("Decimal(%d, %d)", precision, scale);} ));

        listSyntaxItems.add(new SyntaxItem("FLOAT", DBType.FLOAT));
        listSyntaxItems.add(new SyntaxItem("DOUBLE", DBType.DOUBLE));
        listSyntaxItems.add(new SyntaxItem("REAL", DBType.REAL));
        listSyntaxItems.add(new SyntaxItem("VARCHAR", "(\\((\\d+)\\))?", 2, DBType.VARCHAR));
        listSyntaxItems.add(new SyntaxItem("CHAR", "(\\((\\d+)\\))?", 2, DBType.CHAR));
        listSyntaxItems.add(new SyntaxItem("CHAR(*)", DBType.CHAR));
        listSyntaxItems.add(new SyntaxItem("STRING", DBType.CHAR));
        listSyntaxItems.add(new SyntaxItem("BINCHAR","(\\((\\d+)\\))?", 2, DBType.BINARY));
        listSyntaxItems.add(new SyntaxItem("BINCHAR(*)", DBType.BINARY));
        listSyntaxItems.add(new SyntaxItem("BITS", "\\((\\d+)\\)", 1, DBType.BIT));
        listSyntaxItems.add(new SyntaxItem("BITS(*)", DBType.BIT));
        listSyntaxItems.add(new SyntaxItem("BITS2", "\\((\\d+)\\)", 1, DBType.BIT));
        listSyntaxItems.add(new SyntaxItem("BOOL", DBType.BOOL));
        listSyntaxItems.add(new SyntaxItem("DATETIME", "\\[(\\w+)(:(\\w+))?\\]", 1, 3, DBType.DATETIME));
        listSyntaxItems.add(new SyntaxItem("DATE", DBType.DATE));
        listSyntaxItems.add(new SyntaxItem("TIME", DBType.TIME));
        listSyntaxItems.add(new SyntaxItem("TIMESTAMP", DBType.TIMESTAMP));
        listSyntaxItems.add(new SyntaxItem("TIMESPAN", "\\[(\\w+)(:(\\w+))?\\]", 1, 3, DBType.TIME));
        listSyntaxItems.add(new SyntaxItem("BLOB", DBType.BLOB));
        listSyntaxItems.add(new SyntaxItem("CLOB"));
    }

    public SyntaxItem parse(String source) {
        System.out.printf("-- parse(%s)\n", source);

        ResultMath r = null;
        for (SyntaxItem i: listSyntaxItems) {
            r = i.match(source);
            if (r != null) {
                if (r.groupCount() > 0) {
                    for (Integer k = 0; k < r.groupCount(); k++) {
                        System.out.format("%s, ", r.getGroup(k));
                    }

                    return i;
                    //System.out.format("DBType: %s\n", i.getDBType().toString());
//                    for (Object e: r) {
//                        System.out.format("%s, ", e);
//                    }
                    //System.out.println();
                }
                System.out.println (i._datatype);
                return i;
            }
        }
        if (r == null) {
            System.out.println("Parse error");
        }
        return null;
    }
}
