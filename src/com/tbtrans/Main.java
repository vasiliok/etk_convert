package com.tbtrans;

import java.sql.*;
import java.util.*;

public class Main {

    public static void main (String args [])
            throws SQLException, ClassNotFoundException {
        // Load Transbase driver, this might throw a ClassNotFoundException
        Class.forName("transbase.jdbc.Driver");
        String dburl = "jdbc:transbase://localhost:2024/etk_publ";
        String uname = "tbadmin";
        String pw = "altabe";

        TypeParser typeParser = new TypeParser();
        Connection conn = DriverManager.getConnection(dburl, uname, pw);
        // Query systable
        Statement stmt = conn.createStatement ();
        Map<String, List<ColumnInfo>> tablesInfo = new HashMap<String, List<ColumnInfo>>();
        ResultSet rset = stmt.executeQuery ("select * from syscolumn col inner join systable tbl on tbl.segno=col.tsegno");
        // Print the name out
        Set<String> typeSet = new HashSet();

        while (rset.next()) {
            Object cname = rset.getObject("cname");
            Object ctype = rset.getObject("ctype");
            Object tname = rset.getObject("tname");
            SyntaxItem si = typeParser.parse(ctype.toString());
            typeSet.add(ctype.toString());


            List<ColumnInfo> l = tablesInfo.getOrDefault(tname, new ArrayList<ColumnInfo>());
            System.out.println(cname);
        }

        System.out.println("==========");
        for(String i: typeSet) {
            System.out.println(i);
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
