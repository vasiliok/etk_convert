package com.tbtrans.generators;

import com.tbtrans.TableInfo;

/**
 * Created by bacil on 13.07.2018.
 */
public class Mysql implements Meta {

    public Mysql() {
    }
    @Override
    public String getConstraint() {
        return "CONSTRAINT %s PRIMARY KEY (%s)";
    }

    @Override
    public String getCreateTableTemplate() {
        return "create table `%s` (\n%s\n)";
    }

    @Override
    public String  genTableDef(String table_name, TableInfo ti) {
        return "";
    }
}
