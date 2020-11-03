package com.lsq.core.db.components.mysql.util;

import java.util.Date;

public class ShowTableStatusBean {
	String name;
	String engine;
	Integer version;
	String rowFormat;
	Integer aveRowLength;
	Integer dataLength;
	Integer indexLength;
	Integer dataFree;
	Integer autoIncrement;
	Date createTime;
	Date updateTime;
	Date checkTime;
	String collation;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEngine() {
		return engine;
	}

	public void setEngine(String engine) {
		this.engine = engine;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getRowFormat() {
		return rowFormat;
	}

	public void setRowFormat(String rowFormat) {
		this.rowFormat = rowFormat;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Date getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}

	public String getCollation() {
		return collation;
	}

	public void setCollation(String collation) {
		this.collation = collation;
	}

	public Integer getAveRowLength() {
		return aveRowLength;
	}

	public void setAveRowLength(Integer aveRowLength) {
		this.aveRowLength = aveRowLength;
	}

	public Integer getDataLength() {
		return dataLength;
	}

	public void setDataLength(Integer dataLength) {
		this.dataLength = dataLength;
	}

	public Integer getIndexLength() {
		return indexLength;
	}

	public void setIndexLength(Integer indexLength) {
		this.indexLength = indexLength;
	}

	public Integer getDataFree() {
		return dataFree;
	}

	public void setDataFree(Integer dataFree) {
		this.dataFree = dataFree;
	}

	public Integer getAutoIncrement() {
		return autoIncrement;
	}

	public void setAutoIncrement(Integer autoIncrement) {
		this.autoIncrement = autoIncrement;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "ShowTableStatusBean [name=" + name + ", engine=" + engine + ", version=" + version + ", rowFormat="
				+ rowFormat + ", aveRowLength=" + aveRowLength + ", dataLength=" + dataLength + ", indexLength="
				+ indexLength + ", dataFree=" + dataFree + ", autoIncrement=" + autoIncrement + ", createTime="
				+ createTime + ", updateTime=" + updateTime + ", checkTime=" + checkTime + ", collation=" + collation
				+ "]";
	}

}
