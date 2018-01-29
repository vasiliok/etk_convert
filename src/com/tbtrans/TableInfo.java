package com.tbtrans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bacil on 18.11.2017.
 */
public class TableInfo {
    String _tableName;
    List<ColumnInfo> _listColumns = new ArrayList<ColumnInfo>();
    TableInfo(String tableName) {
        _tableName = tableName;
    }
    public void appendColumn(ColumnInfo col) {
        _listColumns.add(col);
    }
}
