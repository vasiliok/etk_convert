package com.tbtrans;

import java.sql.*;
import java.util.*;

public class Main {

    public static void main (String args [])
            throws Exception {
        // Load Transbase driver, this might throw a ClassNotFoundException
        Class.forName("transbase.jdbc.Driver");
        String dburl = "jdbc:transbase://localhost:2024/etk_publ";
        String uname = "tbadmin";
        String pw = "altabe";

        TypeParser typeParser = new TypeParser();
        Connection conn = DriverManager.getConnection(dburl, uname, pw);
        // Query systable
        Statement stmt = conn.createStatement();

        Map<String, TableInfo> tableMap = new HashMap<String, TableInfo>();
        ResultSet table_resuslt = stmt.executeQuery("select tname, ttype, tbl.segno, cnstrt.segno, constraintname, attributes, constrainttext from systable tbl " +
                        "left join sysconstraint cnstrt on cnstrt.segno=tbl.segno");
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
            Object cpos = rset.getObject("cpos");
            Object ctype = rset.getObject("ctype");
            Object dfltval = rset.getObject("defaultvalue");
            Object isnotnull = rset.getObject("notnull");
            Object ckey = rset.getObject("ckey");

            TableInfo table_info = tableMap.getOrDefault(tname, new TableInfo(tname.toString()));

            if (ttype.equals("R")) {
                ResultMath r = null;
                for (SyntaxItem i: typeParser.getListSyntaxItems())
                {
                    r = i.match(ctype.toString());
                    if (r != null && r.getMatches()) {
                        table_info.appendColumn(
                                new ColumnInfo(
                                        cname.toString(),
                                        typeParser.parseEx(ctype.toString()),
                                        ctype.toString(),
                                        isnotnull.toString().equals("Y"),
                                        dfltval.toString(),
                                        (Integer) ckey,
                                        (Integer) cpos
                                )
                        );
                        break;
                    }
                }
            }

            tableMap.put(tname.toString(), table_info);
            typeSet.add(ctype.toString());

            List<ColumnInfo> l = tablesInfo.getOrDefault(tname, new ArrayList<ColumnInfo>());
            //System.out.println(cname);
        }

        System.out.println("==========");
        for(Map.Entry<String, TableInfo> i: tableMap.entrySet()) {
            System.out.printf("Table: %s\n", i.getKey());
            System.out.printf("%10s|%40s|%20s|%10s|%10s\n", "cpos",  "name", "translate", "iskey", "notnull");
            System.out.printf("-------------------------------------------------------------------------------------------------\n");
            List<String> sql_column_list = new ArrayList<>();
            for(ColumnInfo j: i.getValue().getColumns()) {
                System.out.printf("%10d|%40s|%20s|%10d|%10s\n",
                        j.getCPos(),
                        j.getName(),
                        j.getTransType(),
                        j.isKey(),
                        j.isNotNull() ? "Y" : "N"
                );
                String col_repr = String.format("`%s` %s %s", j.getName(), j.getTransType(), j.isNotNull() ? "not null" : "");
                sql_column_list.add(col_repr);
                //System.out.printf("%s\n", col_repr);
            }
            String table_stmnt = String.format("create table `%s` (\n%s\n)", i.getKey(), String.join("\n", sql_column_list));
            System.out.printf("%s\n", table_stmnt);

            for (Constraint c : i.getValue().getConstrainst()) {
                //System.out.printf("- %s (%s)\n", c.getName(), c.getText());
            }
        }
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
