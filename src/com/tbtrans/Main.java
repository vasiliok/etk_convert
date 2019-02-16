package com.tbtrans;

import com.tbtrans.generators.Meta;
import com.tbtrans.generators.Mysql;
import com.tbtrans.generators.Postgress;

import javax.swing.table.TableModel;
import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

/*
MODIFICS:
Cleared code
Add check that field do not being default null and primary key together


TODO:
    // for pg specific
    // if field occurs in primary key and if smae field marked as have null value than that field must be exclude from list of primary keys
    // Bug incorrect detrmining modification of column defined
    // in detail:
    //   table w_btzeilen_cp, field "btzeilenc_datum",
    //   must be default null
    //   actual value


 */
public class Main {
    private static class Options {
        public String outFileName = "pg_etk_dump_sql.txt";
        public String tableName;
        public Boolean takeTableList = false;
        public Boolean skipTableDef = false;
    }
    public static String getTableDefinition(Connection conn, Options opts) throws Exception {
        // Query systable
        TypeParser typeParser = new TypeParser();

        Meta dbutil = new Postgress();

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
            Object cnstrnt_name_obj = table_resuslt.getObject("constraintname");
            String constrnt_name_str = "";
            if (cnstrnt_name_obj != null) {
                constrnt_name_str = dbutil.convertConstraintName(table_resuslt.getObject("constraintname").toString());
            }

            Object cnstrnt_text = table_resuslt.getObject("constrainttext");
            if (ttype.equals("R") && ! constrnt_name_str.isEmpty()) {
                TableInfo table_info = tableMap.getOrDefault(tname.toString(), new TableInfo(tname.toString()));
                table_info.appendConstraint(new Constraint(constrnt_name_str, cnstrnt_text.toString()));
                tableMap.put(tname.toString(), table_info);
            }
        }

        ResultSet rset = stmt.executeQuery (
                "select tname, ttype, cname, cpos, ctype, defaultvalue, notnull, ckey from syscolumn col inner join systable tbl on tbl.segno=col.tsegno");

        // Print the name out


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
            ResultMath rm = typeParser.findSyntaxItem(ctype.toString());

            if (rm.isMatch()) {
                table_info.appendColumn(
                        new ColumnInfo(
                                cname.toString(),
                                ctype.toString(),
                                rm,
                                isnotnull.toString().equals("Y"),
                                dfltval.toString(),
                                (Integer) ckey,
                                (Integer) cpos
                        )
                );
            }
            else {
                throw new Exception(String.format("Can not parse item '%s'", ctype.toString()));
            }

            tableMap.put(tname.toString(), table_info);

        }

        if (opts.takeTableList) {
            for (String t: tableMap.keySet()) {
                System.out.printf("%s\n", t);
            }
            return "";
        }
        Generator gen = new Generator(new Postgress(), tableMap);
        Map<String, String> gen_res = gen.take(opts.tableName);

        BufferedWriter writer = new BufferedWriter(new FileWriter(opts.outFileName));


        for (Map.Entry<String, String> e: gen_res.entrySet()) {
            if (e.getKey().equalsIgnoreCase("w_marketingprodukt_text")) {
                int a=1;
            }
            System.out.printf("Table: %s\n", e.getKey());
            //System.out.printf("%s\n", e.getValue());
            if (!opts.skipTableDef) {
                writer.write(String.format("%s;\n", e.getValue()));
            }
            getTableContent(tableMap.get(e.getKey()), conn, writer);
        }
        writer.close();

        return "";


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






    public static void getTableContent(TableInfo ti, Connection conn, BufferedWriter writer) throws SQLException, IOException {
        //every table
        String tqr_s = String.format("select %s from %s",
                ti.getColumns().stream().map(ent -> ent.getName()).collect(Collectors.joining(", ")),
                ti.getTableName());
        Statement stmt = conn.createStatement();
        ResultSet rset = stmt.executeQuery(tqr_s);

        class columnsign {
            public columnsign(String name, String val) {
                this.name = name;
                this.val = val;
            }

            public String name;
            public String val;
        }
        int ci=0;
        //while(rset.next() && ci < 100) {
        while(rset.next()) {
            // every row
            List <columnsign> collist = new ArrayList<>();
            Map<ColumnInfo, Object> colvals = new HashMap<>();
            for (ColumnInfo col : ti.getColumns()) {
                // every col
                if (col.getName().equalsIgnoreCase("btzeilenc_datum")) {
                    int a=1;
                }
                DBType dbtype = col.getResultMatch().getSyntaxItem().getDBType();
                if (dbtype.equals(DBType.BYTEA)) {
                    InputStream is = rset.getBinaryStream(col.getName());
                    ByteArrayOutputStream buffer = new ByteArrayOutputStream();

                    int nRead;
                    byte[] data = new byte[16384];

                    while ((nRead = is.read(data, 0, data.length)) != -1) {
                        buffer.write(data, 0, nRead);
                    }
                    String hex_str = javax.xml.bind.DatatypeConverter.printHexBinary(buffer.toByteArray());
                    collist.add(new columnsign(col.getName(), "E'\\\\x" + hex_str + "'"));
                }
                else if (dbtype.equals(DBType.CHAR) || dbtype.equals(DBType.VARCHAR)) {
                    String s = rset.getString(col.getName());
                    if (s != null ) {
                        s = s.replaceAll("'", "''");
                        String wrap = col.getResultMatch().getSyntaxItem().getWrap();
                        collist.add(
                                new columnsign(col.getName(), wrap + s + wrap)
                        );
                    }
                }
                else {
                    Object col_obj = rset.getObject(col.getName());
                    if (col_obj != null) {
                        String str_val = col.getResultMatch().getSyntaxItem().takeData(col_obj);
                        String wrap = col.getResultMatch().getSyntaxItem().getWrap();
                        collist.add(
                                new columnsign(col.getName(), wrap + str_val + wrap)
                        );
                    } else {
                        System.out.printf("Column %s is null\n", col.getName());
                    }
                    //colvals.put(col, rset.getObject(col.getName()));
                }
            }
            String insert_Str = String.format("insert into %s (%s) values (%s)",
                    ti.getTableName(),
                    collist.stream().map(i -> i.name).collect(Collectors.joining(", ")),
                    collist.stream().map(i -> i.val).collect(Collectors.joining(", "))
            );
            writer.write(String.format("%s;\n", insert_Str));
            //colvals.entrySet().stream().map(i->i.getKey().getResultMatch().getSyntaxItem().getDBType())
            ci++;
        }
    }

    public static void main (String args []) throws Exception {
        // Load Transbase driver, this might throw a ClassNotFoundException
        Class.forName("transbase.jdbc.Driver");
        String dburl = "jdbc:transbase://localhost:2024/etk_publ";
        String uname = "tbadmin";
        String pw = "altabe";

        Options options = new Options();
        Vector<String> argsvec = new Vector<String>();

        for (String i: args ) {
            argsvec.add(i);
        }
        argsvec.copyInto(args);

        while (argsvec.size() > 0) {
            switch (argsvec.firstElement()) {
                case "--out-file":
                    argsvec.remove(0);
                    options.outFileName = argsvec.firstElement();
                    argsvec.remove(0);
                    break;
                case "--table":
                    argsvec.remove(0);
                    options.tableName = argsvec.firstElement();
                    argsvec.remove(0);
                    break;
                case "--get-table-list":
                    argsvec.remove(0);
                    options.takeTableList = true;
                case "--skip-tabel-def":
                    argsvec.remove(0);
                    options.skipTableDef = true;

            }

        }

        Connection conn = DriverManager.getConnection(dburl, uname, pw);
        String tabdef = getTableDefinition(conn, options);
//        System.out.println("==========");
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
