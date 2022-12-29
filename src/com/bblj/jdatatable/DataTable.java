package com.bblj.jdatatable;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataTable {
	
	private List<DataColumn> columns = new ArrayList<>();
	public List<DataColumn> getColumns() {
		return columns;
	}

	private List<DataColumn> primaryKeys = new ArrayList<>();
	public List<DataColumn> getPrimaryKeys() {
		return primaryKeys;
	}
	public void setPrimaryKeys(List<DataColumn> primaryKeys) {
		this.primaryKeys = primaryKeys;
	}
	
	private List<DataRow> rows = new ArrayList<>();
	public List<DataRow> getRows() {
		return rows;
	}

	private String tableName;
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	public DataTable() {}
	
	public static DataTable FromResultSet(ResultSet rs) throws SQLException {
		DataTable dt = new DataTable();
		dt.PopulateColumns(rs);
		dt.FillRows(rs);
		return dt;
	}

	private void PopulateColumns(ResultSet rs) throws SQLException {
		ResultSetMetaData schema = rs.getMetaData();
		int colCnt = schema.getColumnCount();
		for(int i = 1; i <= colCnt; i++) {
			String colName = schema.getColumnName(i);
			boolean colExisting = false;
			for(DataColumn existingCol : this.columns) {
				if(existingCol.getColumnName().equals(colName)) {
					colExisting = true;
					break;
				}
			}
			if(colExisting == false) {
				DataColumn col = new DataColumn();
				col.setTable(this);
				col.setColumnName(colName);
				Class<?> dataType = DataTable.GetDataTypeFromSqlType(schema.getColumnType(i));
				col.setDatatype(dataType);
				this.columns.add(col);
			}
		}
		
	}

	private static Class<?> GetDataTypeFromSqlType(int columnType) {
		Class<?> type = null;
		if(columnType == 2) {
			type = java.math.BigDecimal.class;
		} else if(columnType == 2) {
			type = java.math.BigDecimal.class;
		} else if(columnType == -7) {
			type = boolean.class;
		} else if(columnType == -6) {
			type = int.class;
		} else if(columnType == 5) {
			type = int.class;
		} else if(columnType == 4) {
			type = int.class;
		} else if(columnType == -5) {
			type = long.class;
		} else if(columnType == 7) {
			type = float.class;
		} else if(columnType == 6) {
			type = double.class;
		} else if(columnType == 8) {
			type = double.class;
		} else if(columnType == -2) {
			type = byte[].class;
		} else if(columnType == -3) {
			type = byte[].class;
		} else if(columnType == -4) {
			type = byte[].class;
		} else if(columnType == 91) {
			type = java.sql.Date.class;
		} else if(columnType == 92) {
			type = java.sql.Time.class;
		} else if(columnType == 93) {
			type = java.sql.Timestamp.class;
		} else if(columnType == 1) {
			type = String.class;
		} else if(columnType == -16) {
			type = String.class;
		} else if(columnType == -1) {
			type = String.class;
		} else if(columnType == -15) {
			type = String.class;
		} else if(columnType == 12) {
			type = String.class;
		} else {
			type = Object.class;
		}	
		return type;
	}
	
	private void FillRows(ResultSet rs) throws SQLException {
		while(rs.next()) {
			DataRow row = new DataRow();
			row.setTable(this);
			Object[] itemArray = new Object[this.columns.size()];
			row.setItemsOriginal(itemArray);
			row.setItemsCurrent(itemArray);
			for(DataColumn col : this.columns) {
				String colName = col.getColumnName();
				Object cellValue = rs.getObject(colName);
				row.SetCellValue(colName, cellValue);
			}
			this.rows.add(row);
		}
		this.AcceptChanged();
	}
	
	public List<Object[]> ToItemArrays(){
		List<Object[]> arrayData = new ArrayList<>();
		for(int i = 0; i < this.rows.size(); i++) {
			DataRow row = this.rows.get(i);
			if(row.getRowState() == DataRowState.Deleted) {
				continue;
			} else if(row.getRowState() == DataRowState.Detached) {
				continue;
			} else {
				arrayData.add(row.getItemsCurrent());
			}
		}
		return arrayData;
	}
	
	public DataRow NewRow() {
		DataRow row = DataRow.New();
		row.setTable(this);
		Object[] itemArray = new Object[this.columns.size()];
		row.setItemsOriginal(itemArray);
		row.setItemsCurrent(itemArray);
		return row;
	}
	
	public int GetColumnIndex(String columnName) {
		int colIndex = -1;
		for(int i = 0; i < columns.size(); i++) {
			DataColumn col = columns.get(i);
			if(columnName.equals(col.getColumnName())) {
				colIndex = i;
				break;
			}
		}
		return colIndex;
	}

	public void AcceptChanged() {
		for(DataRow row : rows) {
			row.AcceptChanges();
		}
	}
	
	public void Clear() {
		this.rows.clear();
	}
}
