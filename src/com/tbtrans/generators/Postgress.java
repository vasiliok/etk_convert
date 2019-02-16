package com.tbtrans.generators;

import com.tbtrans.ColumnInfo;
import com.tbtrans.TableInfo;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by bacil on 03.09.2018.
 */
public class Postgress implements Meta {

    Pattern _constraint_pat = Pattern.compile("^@@(sys\\d+)\\.(\\d+)$");

    @Override
    public String getFormatConstraint() {
        return "CONSTRAINT %s PRIMARY KEY (%s)";
    }

    @Override
    public String getCreateTableTemplate() {
        return "create table \"%s\" (\n%s\n)";
    }

    @Override
    public String genTableDef(String table_name, TableInfo ti) throws Exception {
        List<String> sql_column_list = new ArrayList<>();
        Map<Integer, String> primary_key_map = new TreeMap<>();

        Set<String> dflt_nul_cols = ti.getColumns().stream()
                .filter(a -> a.getDefaultValue().equalsIgnoreCase("null"))
                .map(a -> a.getName())
                .collect(Collectors.toSet());

        for(ColumnInfo j: ti.getColumns()) {

            //String transType = j.getSyntaxItem().translate();
//            System.out.printf("%10d|%40s|%20s|%10d|%10s\n",
//                    j.getCPos(),
//                    j.getName(),
//                    j.getResultMatch().translate(),
//                    j.getCKey(),
//                    j.isNotNull() ? "Y" : "N");

            String col_repr = String.format("\"%s\" %s %s",
                    j.getName(),
                    j.getResultMatch().translate(),
                    j.isNotNull() ? "not null" : "");

            sql_column_list.add(col_repr);

            if (j.getCKey() > 0  && ! dflt_nul_cols.contains(j.getName())) {
                primary_key_map.put(j.getCKey(), j.getName());
            }
            else if (j.getCKey()>0 && dflt_nul_cols.contains(j.getName())) {
                System.out.printf("Found the contradiction in column \"%s\"", j.getName());
            }
        }

        List<String> constraintColumns =
                primary_key_map.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList());

        String constraint_str = "";
        if (!constraintColumns.isEmpty()) {
            List<String> constraintNameList = ti.getConstrainst().stream().map(e -> e.getName()).collect(Collectors.toList());
            constraint_str = String.format(getFormatConstraint(),
                    ti.getConstrainst().get(0).getName(), String.join(",", constraintColumns)
            );
        }

        //CONSTRAINT PK_Person PRIMARY KEY (ID,LastName)

        String constraint_stmnt = "";
        if (!constraintColumns.isEmpty()) {
            if (ti.getConstrainst().size() > 1) {
                System.out.printf("Error, table '%s' have more that one constraints\n", ti.getTableName());
                return null;
            }
            constraint_stmnt = String.format(
                    "CONSTRAINT %s PRIMARY KEY (%s)",
                    ti.getConstrainst().get(0).getName(),
                    constraintColumns.stream().collect(Collectors.joining(", "))
            );
        }
        String table_stmnt = String.format("DROP TABLE IF EXISTS %s;\n", table_name) +
                String.format(getCreateTableTemplate(), table_name, String.join(",\n", sql_column_list));

        if (!constraint_stmnt.isEmpty()) {
            table_stmnt = table_stmnt + ",\n" + constraint_stmnt;
        };

        return table_stmnt;
    }

    @Override
    public String convertConstraintName(String cn) {
        Matcher m = _constraint_pat.matcher(cn);
        if (m.matches()) {
            return m.group(1)+"_"+m.group(2);
        }
        return cn;
    }

}
