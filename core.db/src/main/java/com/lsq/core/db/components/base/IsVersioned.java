package com.lsq.core.db.components.base;

/**
 * Provides optimistic locking compatibility to database mapping objects
 * 
 * @author treemark
 *
 */
public interface IsVersioned {
	public int getVersion();

	public void setVersion(int version);
}
