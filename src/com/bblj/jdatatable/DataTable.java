package com.bblj.jdatatable;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.Types;

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
	
	public static DataTable fromResultSet(ResultSet rs) throws SQLException {
		DataTable dt = new DataTable();
		dt.populateColumns(rs);
		dt.fillRows(rs);
		return dt;
	}

	private void populateColumns(ResultSet rs) throws SQLException {
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
				Class<?> dataType = DataTable.getDataTypeFromSqlType(schema.getColumnType(i));
				col.setDatatype(dataType);
				this.columns.add(col);
			}
		}
		
	}

	private static Class<?> getDataTypeFromSqlType(int columnType) {
		
		Class<?> type = null;
		if(columnType == Types.NUMERIC) {
			type = java.math.BigDecimal.class;
		} else if(columnType == Types.BIT) {
			type = boolean.class;
		} else if(columnType == Types.TINYINT) {
			type = int.class;
		} else if(columnType == Types.SMALLINT) {
			type = int.class;
		} else if(columnType == Types.INTEGER) {
			type = int.class;
		} else if(columnType == Types.BIGINT) {
			type = long.class;
		} else if(columnType == Types.REAL) {
			type = float.class;
		} else if(columnType == Types.FLOAT) {
			type = double.class;
		} else if(columnType == Types.DOUBLE) {
			type = double.class;
		} else if(columnType == Types.BINARY) {
			type = byte[].class;
		} else if(columnType == Types.VARBINARY) {
			type = byte[].class;
		} else if(columnType == Types.LONGVARBINARY) {
			type = byte[].class;
		} else if(columnType == Types.DATE) {
			type = java.sql.Date.class;
		} else if(columnType == Types.TIME) {
			type = java.sql.Time.class;
		} else if(columnType == Types.TIMESTAMP) {
			type = java.sql.Timestamp.class;
		} else if(columnType == Types.CHAR) {
			type = String.class;
		} else if(columnType == Types.LONGNVARCHAR) {
			type = String.class;
		} else if(columnType == Types.LONGVARCHAR) {
			type = String.class;
		} else if(columnType == Types.NCHAR) {
			type = String.class;
		} else if(columnType == Types.VARCHAR) {
			type = String.class;
		} else {
			type = Object.class;
		}	
		return type;
	}
	
	private void fillRows(ResultSet rs) throws SQLException {
		while(rs.next()) {
			DataRow row = new DataRow();
			row.setTable(this);
			Object[] itemArray = new Object[this.columns.size()];
			row.setItemsOriginal(itemArray);
			row.setItemsCurrent(itemArray);
			for(DataColumn col : this.columns) {
				String colName = col.getColumnName();
				Object cellValue = rs.getObject(colName);
				row.setCellValue(colName, cellValue);
			}
			this.rows.add(row);
		}
		this.acceptChanged();
	}
	
	public List<Map<String, Object>> toItemArrays(){
		List<Map<String, Object>> arrayData = new ArrayList<>();
		for(int i = 0; i < this.rows.size(); i++) {
			DataRow row = this.rows.get(i);
			if(row.getRowState() == DataRowState.Deleted) {
				continue;
			} else if(row.getRowState() == DataRowState.Detached) {
				continue;
			} else {
				Map<String, Object> rowData = new HashMap<>();
				for(DataColumn col : this.getColumns()) {
					rowData.put(col.getColumnName(), row.getCellValue(col.getColumnName()));
				}
				arrayData.add(rowData);
			}
		}
		return arrayData;
	}
	
	public DataRow newRow() {
		DataRow row = DataRow.newRow();
		row.setTable(this);
		Object[] itemArray = new Object[this.columns.size()];
		row.setItemsOriginal(itemArray);
		row.setItemsCurrent(itemArray);
		return row;
	}
	
	public int getColumnIndex(String columnName) {
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

	public void acceptChanged() {
		for(DataRow row : rows) {
			row.acceptChanges();
		}
	}
	
	public void clear() {
		this.rows.clear();
	}
}
