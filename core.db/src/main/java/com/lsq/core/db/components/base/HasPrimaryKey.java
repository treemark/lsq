package com.lsq.core.db.components.base;

/**
 * Defines a singular primary key object for DAO operations on a database
 * mapping object
 * 
 * @author treemark
 *
 * @param <T>
 */
public interface HasPrimaryKey<T> {
	public T getPrimaryKey();
}
