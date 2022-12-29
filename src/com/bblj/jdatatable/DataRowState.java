package com.bblj.jdatatable;

public enum DataRowState {
	Added(4),Deleted(8),Detached(1),Modified(16),Unchanged(2);

	private int value;
	DataRowState(int value) {
		this.value = value;
	}
	
	public int GetValue() {
		return this.value;
	}
}
