package com.lsq.core.db.components.fk;

public class ForeignKey<K, V> {

	K fk;
	V value;
	boolean initialized = false;
	String tableName;
	String idColumn;

	public ForeignKey(String tableName, String idColumn) {
		this(null, tableName, idColumn);
	}

	public ForeignKey(K fk, String tableName, String idColumn) {
		this.fk = fk;
		this.tableName = tableName;
		this.idColumn = idColumn;
	}

	public K getFK() {
		return fk;
	}

	public V getValue() {
		return value;
	}

	public K getFk() {
		return fk;
	}

	public void setFk(K fk) {
		this.fk = fk;
	}

	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	public void setValue(V value) {
		this.value = value;
	}

}
