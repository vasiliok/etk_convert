package com.tbtrans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bacil on 18.11.2017.
 */
public class TableInfo {
    String _tableName;
    List<ColumnInfo> _listColumns = new ArrayList<ColumnInfo>();
    List<Constraint> _listConstraint = new ArrayList<Constraint>();

    TableInfo(String tableName) {
        _tableName = tableName;
    }

    public void appendColumn(ColumnInfo col) {
        _listColumns.add(col);
    }

    public void appendConstraint(Constraint cnstrt) {
        _listConstraint.add(cnstrt);
    }

    public List<ColumnInfo> getColumns() {
        return _listColumns;
    }
    public List<Constraint> getConstrainst() {
        return _listConstraint;
    }
}
