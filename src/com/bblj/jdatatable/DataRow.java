package com.bblj.jdatatable;

import java.util.List;
import java.util.Map;

public class DataRow {
	
	private Object[] itemsOriginal;
	public Object[] getItemsOriginal() {
		return itemsOriginal;
	}
	public void setItemsOriginal(Object[] itemsOriginal) {
		this.itemsOriginal = itemsOriginal;
	}
	
	private Object[] itemsCurrent;
	public Object[] getItemsCurrent() {
		return itemsCurrent;
	}
	public void setItemsCurrent(Object[] itemsCurrent) {
		this.itemsCurrent = itemsCurrent;
	}

	private DataTable table;
	public DataTable getTable() {
		return table;
	}
	public void setTable(DataTable table) {
		this.table = table;
	}
	
	private DataRowState rowState = DataRowState.Detached;
	public DataRowState getRowState() {
		return rowState;
	}
	
	public DataRow() {}
	
	public static DataRow New() {
		DataRow row = new DataRow();
		row.rowState = DataRowState.Added;
		return row;
	}
	
	public Object GetCellValue(String columnName) {
		int colIndex = this.table.GetColumnIndex(columnName);
		return itemsCurrent[colIndex];
	}
	
	public Object GetCellValue(String columnName, DataRowVersion version) {
		int colIndex = this.table.GetColumnIndex(columnName);
		if(version == DataRowVersion.Current) {
			return itemsCurrent[colIndex];
		} else if(version == DataRowVersion.Original) {
			return itemsOriginal[colIndex];
		} else {
			return null;
		}
	}
	
	public void SetCellValue(String columnName, Object value) {
		int colIndex = this.table.GetColumnIndex(columnName);
		itemsCurrent[colIndex] = value;
		if(this.rowState == DataRowState.Unchanged) {
			this.rowState = DataRowState.Modified;
		}
	}
	
	public void Delete() {
		if(this.rowState == DataRowState.Added) {
			this.rowState = DataRowState.Detached;
			List<DataRow> rows = this.table.getRows();
			rows.remove(this);
		} else if(this.rowState == DataRowState.Modified) {
			this.rowState = DataRowState.Deleted;
		} else if(this.rowState == DataRowState.Unchanged) {
			this.rowState = DataRowState.Deleted;
		}
	}
	
	public void AcceptChanges() {
		this.itemsOriginal = this.itemsCurrent;
		this.rowState = DataRowState.Unchanged;
	}
	
	
}
