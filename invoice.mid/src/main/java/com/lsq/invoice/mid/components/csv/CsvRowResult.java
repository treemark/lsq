package com.lsq.invoice.mid.components.csv;

import com.lsq.invoice.mid.components.csv.CsvImportDAO.ImportAction;

public class CsvRowResult {
	public CsvRowResult(String supplierId, String invoiceId, ImportAction action) {
		this.invoiceId = invoiceId;
		this.supplierId = supplierId;
		this.action = action;
	}

	String invoiceId;
	String supplierId;
	ImportAction action;

	public String getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}

	public String getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
	}

	public ImportAction getAction() {
		return action;
	}

	public void setAction(ImportAction action) {
		this.action = action;
	}

	@Override
	public String toString() {
		return "CsvRowResult [invoiceId=" + invoiceId + ", supplierId=" + supplierId + ", action=" + action + "]";
	}

}