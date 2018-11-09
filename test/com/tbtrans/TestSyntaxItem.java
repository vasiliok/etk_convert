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
    public void batchtest() throws Exception {
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
            if (e.getKey().equalsIgnoreCase("DATETIME[yy:ms]")) {
                int a=1;
            }
            String translated =  typeParser.parseEx(e.getKey());
            //String translated = parsed_si.getTranslated();
            assertEquals(
                    String.format("%s:%s", e.getKey(), e.getValue()),
                    e.getValue(),
                    translated
            );

            System.out.printf("%s\n", translated);

//--
//            ResultMath lo = null;
//            for (SyntaxItem k: listSyntaxItems) {
//                lo = k.match(e.getKey());
//                if (lo != null && lo.getMatches()) {
//                    String translate = k.getTranslated();
//                    assertEquals(String.format("%s:%s", e.getKey(), e.getValue()),e.getValue(), translate);
//                    System.out.printf("%s\n", translate);
//                }
//            }
//--
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
            SyntaxItem si = typeParser.findSyntaxItem(e.getKey());
            assertNotNull(si);

            //String translated = parsed_si.getTranslated();
            assertEquals(
                    String.format("%s:%s", e.getKey(), e.getValue()),
                    e.getValue(),
                    si.translate()
            );

            System.out.printf("%s\n", si.translate());

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
        ResultMath lo = null;
        SyntaxItem si = new SyntaxItem("char");
        lo = si.match("char");
        assertTrue(lo != null);
        SyntaxItem si_char = new SyntaxItem("CHAR", "(\\((\\d+)\\))?", 2, DBType.CHAR);
        assertEquals(DBType.CHAR, si_char.getDBType());
        lo = si_char.match("char(3)");
        assertNotNull(lo);
        assertEquals(1, lo.groupCount());
        assertEquals("3", lo.getGroup(0));

        lo = si_char.match("char(32)");
        assertNotNull(lo);
        assertEquals(1, lo.groupCount());
        assertEquals("32", lo.getGroup(0));

        SyntaxItem si_numeric =
                new SyntaxItem(
                        "NUMERIC",
                        "(\\((\\d+)(,(\\d+))?\\))?", 2, 4,
                        (Integer precision, Integer scale) -> {return String.format("Decimal(%d,%d)", precision, scale);}
        );

        lo = null;
        lo = si_numeric.match("numeric(3,1)");
        assertNotNull(lo);
        assertEquals(2, lo.groupCount());
        assertEquals("Decimal(3,1)", si_numeric.getTranslated());

        SyntaxItem si_vchar = new SyntaxItem("VARCHAR", "(\\((\\d+)\\))?", 2, DBType.VARCHAR);
        lo = null;
        lo = si_vchar.match("VARCHAR(3)");
        assertNotNull(lo);
        assertEquals(1, lo.groupCount());
        assertEquals("VARCHAR(3)", si_vchar.getTranslated());
//        SyntaxItem si_varchar_1dp = new SyntaxItem("varchar(9)");
//        SyntaxItem si_varchar_2dp = new SyntaxItem("varchar(10)");
//        SyntaxItem si_varchar_3dp = new SyntaxItem("varchar(420)");
//        SyntaxItem si_varchar_4dp = new SyntaxItem("varchar(1024)");

//        varchar(10)
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
}
