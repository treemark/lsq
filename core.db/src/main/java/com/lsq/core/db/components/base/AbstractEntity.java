package com.lsq.core.db.components.base;

public abstract class AbstractEntity<K> implements HasPrimaryKey<K> {
	K key;

	public K getPrimaryKey() {
		return key;
	}
}
