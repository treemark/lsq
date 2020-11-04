package com.lsq.invoice.mid.components.csv;

import java.util.List;

public class InvoiceCsvParsingException extends RuntimeException {

	private List<CsvRowResult> results;

	public InvoiceCsvParsingException() {
	}

	public InvoiceCsvParsingException(String message, List<CsvRowResult> results) {
		super(message);
		this.results = results;
	}

	public InvoiceCsvParsingException(Throwable cause) {
		super(cause);
	}

	public InvoiceCsvParsingException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvoiceCsvParsingException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public List<CsvRowResult> getResults() {
		return results;
	}

}
