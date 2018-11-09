package com.tbtrans.generators;

import com.tbtrans.TableInfo;

/**
 * Created by bacil on 13.07.2018.
 */
public interface Meta {
    public String getConstraint();
    public String getCreateTableTemplate();
    public String genTableDef(String table_name, TableInfo ti) throws Exception;
}
