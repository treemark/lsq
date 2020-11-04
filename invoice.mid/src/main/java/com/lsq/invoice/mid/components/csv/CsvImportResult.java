package com.lsq.invoice.mid.components.csv;

import java.util.ArrayList;
import java.util.List;

import com.lsq.invoice.mid.components.csv.CsvImportDAO.ImportAction;

public class CsvImportResult {

	public CsvImportResult() {
	}

	List<RowResult> results = new ArrayList<RowResult>();

	public class RowResult {
		String invoiceId;
		String supplierId;
		ImportAction action;
	}
}
