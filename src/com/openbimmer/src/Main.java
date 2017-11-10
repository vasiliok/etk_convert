package com.openbimmer.src;

import java.sql.*;

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
        ResultSet rset = stmt.executeQuery ("select * from syscolumn col inner join systable tbl on tbl.segno=col.tsegno");
        // Print the name out
        while (rset.next()) {
            Object cname = rset.getObject("cname");
            Object ctype = rset.getObject("ctype");
            typeParser.parse(ctype.toString());
            System.out.println (cname);
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
