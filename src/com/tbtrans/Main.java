package com.tbtrans;

import com.tbtrans.generators.Mysql;
import com.tbtrans.generators.Postgress;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

/*
TODO:


 */
public class Main {
    public static String getTableDefinition(Connection conn) throws Exception {
        // Query systable
        TypeParser typeParser = new TypeParser();

        Statement stmt = conn.createStatement();

        Map<String, TableInfo> tableMap = new HashMap<String, TableInfo>();
        ResultSet table_resuslt = stmt.executeQuery("select tname, ttype, tbl.segno, cnstrt.segno, constraintname, attributes, constrainttext from systable tbl " +
                "left join sysconstraint cnstrt on cnstrt.segno=tbl.segno");
        //
        // example result
        // tname | ttype | tbl.segno | cnstrt.segno | constraintname | attributes | constrainttext
        //-------+-------+-----------+--------------+----------------+------------+----------------------------
        // w_abk | R     | 63        | 63           | w_abk_KEY      | IMMEDIATE  | PRIMARY KEY IS abk_kuerzel
        //
        while(table_resuslt.next()) {
            Object tname = table_resuslt.getObject("tname");
            String ttype = table_resuslt.getObject("ttype").toString();
            Object cnstrnt_name = table_resuslt.getObject("constraintname");
            Object cnstrnt_text = table_resuslt.getObject("constrainttext");
            if (ttype.equals("R") && !cnstrnt_name.toString().isEmpty()) {
                TableInfo table_info = tableMap.getOrDefault(tname.toString(), new TableInfo(tname.toString()));
                table_info.appendConstraint(new Constraint(cnstrnt_name.toString(), cnstrnt_text.toString()));
                tableMap.put(tname.toString(),  table_info);
            }
        }

        Map<String, List<ColumnInfo>> tablesInfo = new HashMap<String, List<ColumnInfo>>();
        ResultSet rset = stmt.executeQuery (
                "select tname, ttype, cname, cpos, ctype, defaultvalue, notnull, ckey from syscolumn col inner join systable tbl on tbl.segno=col.tsegno");
        // Print the name out
        Set<String> typeSet = new HashSet();

        while (rset.next()) {
            Object tname = rset.getObject("tname");
            Object ttype = rset.getObject("ttype");
            Object cname = rset.getObject("cname");

            // Gives the position of the field in the table (first position is 1).
            Object cpos = rset.getObject("cpos");

            Object ctype = rset.getObject("ctype");
            Object dfltval = rset.getObject("defaultvalue");
            Object isnotnull = rset.getObject("notnull");

            // Indicates whether the corresponding column of a table belongs to the primary key (value >= 1) or not (value = 0).
            Object ckey = rset.getObject("ckey");

            if (! ttype.equals("R")) {  // if system
                continue;
            }

            TableInfo table_info = tableMap.getOrDefault(tname, new TableInfo(tname.toString()));
            SyntaxItem si = typeParser.findSyntaxItem(ctype.toString());

            String translatedType = typeParser.parseEx(ctype.toString());
            if (translatedType != null) {
                table_info.appendColumn(
                        new ColumnInfo(
                                cname.toString(),
                                translatedType,
                                ctype.toString(),
                                si,
                                isnotnull.toString().equals("Y"),
                                dfltval.toString(),
                                (Integer) ckey,
                                (Integer) cpos
                        )
                );
            }

//            ResultMath r = null;
//            for (SyntaxItem i: typeParser.getListSyntaxItems())
//            {
//                r = i.match(ctype.toString());
//                if (r != null && r.getMatches()) {
//                    table_info.appendColumn(
//                            new ColumnInfo(
//                                    cname.toString(),
//                                    typeParser.parseEx(ctype.toString()),
//                                    ctype.toString(),
//                                    isnotnull.toString().equals("Y"),
//                                    dfltval.toString(),
//                                    (Integer) ckey,
//                                    (Integer) cpos
//                            )
//                    );
//                    break;
//                }
//            }

            tableMap.put(tname.toString(), table_info);
            typeSet.add(ctype.toString());

            List<ColumnInfo> l = tablesInfo.getOrDefault(tname, new ArrayList<ColumnInfo>());
            //System.out.println(cname);
        }

        Generator gen = new Generator(new Postgress(), tableMap);
        String gen_res = gen.take();
        return gen_res;


//        for(Map.Entry<String, TableInfo> i: tableMap.entrySet()) {
//            System.out.printf("Table: %s\n", i.getKey());
//            System.out.printf("%10s|%40s|%20s|%10s|%10s\n", "cpos",  "name", "translate", "iskey", "notnull");
//            System.out.printf("-------------------------------------------------------------------------------------------------\n");
//            List<String> sql_column_list = new ArrayList<>();
//            Map<Integer, String> primary_key_map = new HashMap<>();
//
//            for(ColumnInfo j: i.getValue().getColumns()) {
//                System.out.printf("%10d|%40s|%20s|%10d|%10s\n",
//                        j.getCPos(),
//                        j.getName(),
//                        j.getTransType(),
//                        j.getCKey(),
//                        j.isNotNull() ? "Y" : "N"
//                );
//                String col_repr = String.format("`%s` %s %s", j.getName(), j.getTransType(), j.isNotNull() ? "not null" : "");
//                sql_column_list.add(col_repr);
//                if (j.getCKey()>0) {
//                    primary_key_map.put(j.getCKey(), j.getName());
//                }
//                //System.out.printf("%s\n", col_repr);
//            }
//
//            Map<Integer, String>
//                    constraintSortedByKey =
//                        primary_key_map.entrySet().stream().sorted(Map.Entry.comparingByKey())
//                        .collect(
//                                Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new)
//                        );
//            List<String> constraintColumns =
//                    primary_key_map.entrySet().stream().sorted(Map.Entry.comparingByKey()).map(Map.Entry::getValue).collect(Collectors.toList());
//
//
//
//            String constraint_str;
//            if (i.getValue().getConstrainst().size() > 0) {
//                constraint_str = String.format("CONSTRAINT %s PRIMARY KEY (%s)",
//                        i.getValue().getConstrainst().get(0).getName(),
//                        String.join(",", constraintColumns)
//                );
//            }
//
//            String table_stmnt = String.format("create table `%s` (\n%s\n)", i.getKey(), String.join(",\n", sql_column_list));
//
//            System.out.printf("%s\n", table_stmnt);
//
//            //CONSTRAINT PK_Person PRIMARY KEY (ID,LastName)
//
//            for(Constraint c : i.getValue().getConstrainst()) {
//                //System.out.printf("- %s (%s)\n", c.getName(), c.getText());
//            }
//        }

    }
    public static void main (String args []) throws Exception {
        // Load Transbase driver, this might throw a ClassNotFoundException
        Class.forName("transbase.jdbc.Driver");
        String dburl = "jdbc:transbase://localhost:2024/etk_publ";
        String uname = "tbadmin";
        String pw = "altabe";

        Connection conn = DriverManager.getConnection(dburl, uname, pw);
        String tabdef = getTableDefinition(conn);
        System.out.println("==========");
        System.out.print(tabdef);


    }


//    public static void main(String[] args) throws SQLException {
//	// write your code here
//        //Class.forName("transbase.jdbc.Driver");
//        Connection c = DriverManager.getConnection("jdbc:transbase://localhost:2024/etk_publ", "tbadmin", "altabe");
//        Statement s = c.createStatement();
//        ResultSet r = s.executeQuery("select tname from systable");
//        while(r.next())
//            System.out.println(r.getString(1));
//        r.close();
//        s.close();
//        c.close();
//
//
//    }
}
