package com.tbtrans.generators;

import com.tbtrans.ColumnInfo;
import com.tbtrans.TableInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Created by bacil on 03.09.2018.
 */
public class Postgress implements Meta {


    @Override
    public String getConstraint() {
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

        for(ColumnInfo j: ti.getColumns()) {

            //String transType = j.getSyntaxItem().translate();
            System.out.printf("%10d|%40s|%20s|%10d|%10s\n",
                    j.getCPos(),
                    j.getName(),
                    j.getResultMatch().translate(),
                    j.getCKey(),
                    j.isNotNull() ? "Y" : "N");

            String col_repr = String.format("\"%s\" %s %s", j.getName(), j.getResultMatch().translate(), j.isNotNull() ? "not null" : "");

            sql_column_list.add(col_repr);

            if (j.getCKey() > 0) {
                primary_key_map.put(j.getCKey(), j.getName());
            }
        }

        List<String> constraintColumns =
                primary_key_map.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList());

        String constraint_str;
        if (!ti.getConstrainst().isEmpty()) {
            List<String> constraintNameList = ti.getConstrainst().stream().map(e->e.getName()).collect(Collectors.toList());
            constraint_str = String.format(getConstraint(),
                    ti.getConstrainst().get(0).getName(), String.join(",", constraintColumns)
            );
        }

        //CONSTRAINT PK_Person PRIMARY KEY (ID,LastName)

        String constraint_stmnt = "";
        if (!ti.getConstrainst().isEmpty()) {
            if (ti.getConstrainst().size() > 1) {
                System.out.printf("error\n");
                return "";
            }
            constraint_stmnt = String.format(
                    "CONSTRAINT %s PRIMARY KEY (%s)",
                    ti.getConstrainst().get(0).getName(),
                    constraintColumns.stream().collect(Collectors.joining(", "))
            );
//                for (Constraint c : i.getValue().getConstrainst()) {
//                    System.out.printf("- %s (%s)\n", c.getName(), c.getText());
//                }
        }
        String table_stmnt = String.format(getCreateTableTemplate(), table_name, String.join(",\n", sql_column_list) + ",\n" + constraint_stmnt);

        return table_stmnt;
    }

}
