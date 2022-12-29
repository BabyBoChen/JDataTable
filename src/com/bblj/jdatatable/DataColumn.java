package com.bblj.jdatatable;

public class DataColumn {
	private boolean allowDbNull;
	private boolean autoIncrement;
	private String caption;
	
	private String columnName;
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	
	private Class<?> datatype;
	public Class<?> getDatatype() {
		return datatype;
	}
	public void setDatatype(Class<?> datatype) {
		this.datatype = datatype;
	}

	private Object defaultValue;
	private boolean readOnly;
	
	private DataTable table;
	public DataTable getTable() {
		return table;
	}
	public void setTable(DataTable table) {
		this.table = table;
	}
}
