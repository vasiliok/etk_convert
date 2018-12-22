package com.tbtrans;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by bacil on 27.11.2017.
 */
public class TestSyntaxItem {
//    public void testSICondition() {
//        SyntaxItem si_char = new SyntaxItem("CHAR", "(\\((\\d+)\\))?", 2, DBType.CHAR);
//        assertEquals(DBType.CHAR, si_char.getDBType());
//        lo = si_char.match("char(3)");
//        assertNotNull(lo);
//        assertEquals(1, lo.size());
//        assertEquals("3", lo.get(0));
//    }


    @Test
    public void batchtest1() throws Exception {
        TypeParser typeParser = new TypeParser();

        List<SyntaxItem> listSyntaxItems = new ArrayList<SyntaxItem>();

//        listSyntaxItems.add(new SyntaxItem("TINYINT", DBType.TINYINT));
//        listSyntaxItems.add(new SyntaxItem("SMALLINT", DBType.SMALLINT));
//        listSyntaxItems.add(new SyntaxItem("INTEGER", DBType.INT));
//        listSyntaxItems.add(new SyntaxItem("BIGINT", DBType.BIGINT));
//
//        listSyntaxItems.add(new SyntaxItem("NUMERIC",
//                "(\\((\\d+)(,(\\d+))?\\))?", 2, 4,
//                (Integer precision, Integer scale) -> {return String.format("DECIMAL(%d,%d)", precision, scale);} ));
//
//        listSyntaxItems.add(new SyntaxItem("DECIMAL",
//                "(\\((\\d+)(,(\\d+))?\\))?", 2, 4,
//                (Integer precision, Integer scale) -> {return String.format("Decimal(%d, %d)", precision, scale);} ));
//
//        listSyntaxItems.add(new SyntaxItem("FLOAT", DBType.FLOAT));
//        listSyntaxItems.add(new SyntaxItem("DOUBLE", DBType.DOUBLE));
//        listSyntaxItems.add(new SyntaxItem("REAL", DBType.REAL));
//        listSyntaxItems.add(new SyntaxItem("VARCHAR", "(\\((\\d+)\\))?", 2, DBType.VARCHAR));
//        listSyntaxItems.add(new SyntaxItem("CHAR", "(\\((\\d+)\\))?", 2, DBType.CHAR));
//        listSyntaxItems.add(new SyntaxItem("CHAR(*)", DBType.CHAR));
//        listSyntaxItems.add(new SyntaxItem("STRING", DBType.CHAR));
//        listSyntaxItems.add(new SyntaxItem("BINCHAR","(\\((\\d+)\\))?", 2, DBType.BINARY));
//        listSyntaxItems.add(new SyntaxItem("BINCHAR(*)", DBType.BINARY));
//        listSyntaxItems.add(new SyntaxItem("BITS", "\\((\\d+)\\)", 1, DBType.BIT));
//        listSyntaxItems.add(new SyntaxItem("BITS(*)", DBType.BIT));
//        listSyntaxItems.add(new SyntaxItem("BITS2", "\\((\\d+)\\)", 1, DBType.BIT));
//        listSyntaxItems.add(new SyntaxItem("BOOL", DBType.BOOL));
//        listSyntaxItems.add(new SyntaxItem("DATETIME[yy:ms]", DBType.TIMESTAMP));
//        //listSyntaxItems.add(new SyntaxItem("DATETIME", "\\[(\\w+)(:(\\w+))?\\]", 1, 3, DBType.DATETIME));
//        listSyntaxItems.add(new SyntaxItem("DATE", DBType.DATE));
//        listSyntaxItems.add(new SyntaxItem("TIME", DBType.TIME));
//        listSyntaxItems.add(new SyntaxItem("TIMESTAMP", DBType.TIMESTAMP));
//        listSyntaxItems.add(new SyntaxItem("TIMESPAN", "\\[(\\w+)(:(\\w+))?\\]", 1, 3, DBType.TIME));
//        listSyntaxItems.add(new SyntaxItem("BLOB", DBType.BLOB));
//        listSyntaxItems.add(new SyntaxItem("CLOB"));

        Map<String, String> input_map = new HashMap<String, String>();
        input_map.put("VARCHAR(10)", "VARCHAR(10)");
        input_map.put("VARCHAR(420)", "VARCHAR(420)");
        input_map.put("VARCHAR(1024)", "VARCHAR(1024)");
        input_map.put("NUMERIC(3,1)", "DECIMAL(3, 1)");
        input_map.put("NUMERIC(14,1)", "DECIMAL(14, 1)");
        input_map.put("INTEGER", "INT");
        input_map.put("CHAR(*)", "CHAR");
        input_map.put("CHAR(4)", "CHAR(4)");
        input_map.put("CHAR(45)", "CHAR(45)");
        input_map.put("TINYINT", "TINYINT");
        input_map.put("BLOB", "BLOB");
        input_map.put("BOOL", "BOOL");
        input_map.put("SMALLINT", "SMALLINT");
        input_map.put("DATETIME[yy:ms]", "TIMESTAMP");

        for (Map.Entry<String, String> e : input_map.entrySet()) {
            System.out.printf("%s->", e.getKey());
            if (e.getKey().equalsIgnoreCase("CHAR(*)")) {
                int a=1;
            }
            ResultMath rm = typeParser.findSyntaxItem(e.getKey());
            //assertNotNull(si);

            //String translated = parsed_si.getTranslated();
            assertEquals(
                    String.format("%s:%s", e.getKey(), e.getValue()),
                    e.getValue(),
                    rm.translate()
            );

            System.out.printf("%s\n", rm.translate());

        }



//        varchar(420)
//        varchar(1024)
//        numeric(3,1)
//        numeric(14,0)
//        integer
//        char(*)
//        char(4)
//        char(44)
//        tinyint
//                blob
//        bool
//                smallint
//        datetime[yy:ms]


    }

    @Test
    public void first() throws Exception {
        SyntaxItem si = new SyntaxItem("char");
        assertTrue(si.match("char").isMatch());
        SyntaxItem si_char = new SyntaxItem("CHAR", "(\\((\\d+)\\))?", 2, DBType.CHAR);
        assertEquals(DBType.CHAR, si_char.getDBType());
        ResultMath rm_char_3 = si_char.match("char(3)");
        assertTrue(rm_char_3.isMatch());
        assertEquals("3", rm_char_3._matcher.group(2));

        ResultMath rm_char_32 = si_char.match("char(32)");
        assertTrue(rm_char_32.isMatch());
        assertEquals("32", rm_char_32._matcher.group(2));

        SyntaxItem si_numeric =
                new SyntaxItem(
                        "NUMERIC",
                        "(\\((\\d+)(,(\\d+))?\\))?", 2, 4,
                        (Integer precision, Integer scale) -> {return String.format("Decimal(%d,%d)", precision, scale);}
        );

        assertTrue(si_numeric.match("numeric(3,1)").isMatch());
        assertEquals("Decimal(3,1)", si_numeric.match("numeric(3,1)").translate());

        SyntaxItem si_vchar = new SyntaxItem("VARCHAR", "(\\((\\d+)\\))?", 2, DBType.VARCHAR);
        assertTrue(si_vchar.match("VARCHAR(3)").isMatch());
        assertEquals("VARCHAR(3)", si_vchar.match("VARCHAR(3)").translate());
    }
}
