package com.bblj.jdatatable;

public enum DataRowVersion {
	Current(512),Default(1536),Original(256),Proposed(1024);

	private int value;
	DataRowVersion(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return this.value;
	}
}
