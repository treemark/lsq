package com.lsq.invoice.mid.components.csv;

import java.io.StringReader;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.lsq.invoice.db.entities.Invoice;
import com.lsq.invoice.mid.components.csv.CsvImportDAO.ImportAction;

@Service
public class CsvImportService {

	Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	CsvImportDAO dao;

	@Autowired
	TransactionTemplate txTemp;

	public List<CsvRowResult> importCsv(String csv) {
		List<CsvRowResult> results = new ArrayList<CsvRowResult>();
		TransactionCallback<List<CsvRowResult>> importInv = (TransactionStatus txstat) -> {
			try {

				boolean failure = false;
				CSVParser parser = CSVFormat.DEFAULT.parse(new StringReader(csv));
				Set<String> duplicateCheck = new HashSet<String>();
				for (CSVRecord r : parser.getRecords()) {
					if (r.getRecordNumber() != 1) {// skip header
						Invoice inv = toEntity(r);
						if (duplicateCheck.add(inv.getInvoiceId() + "___" + inv.getSupplierId())) {
							ImportAction action = dao.updateOrInsert(inv);
							switch (action) {
							case INSERTED:
								logger.info(
										"Inserted new record for: " + inv.getSupplierId() + " " + inv.getInvoiceId());
								break;
							case UPDATED:
								logger.info("Updated existing record for: " + inv.getSupplierId() + " "
										+ inv.getInvoiceId());
								break;
							case ERROR:
								failure = true;
								break;
							default:
								break;
							}
							results.add(new CsvRowResult(inv.getSupplierId(), inv.getInvoiceId(), action));
						} else {
							results.add(
									new CsvRowResult(inv.getSupplierId(), inv.getInvoiceId(), ImportAction.DUPLICATE));
							failure = true;
						}
					}
				}
				if (failure) {
					throw new InvoiceCsvParsingException("Duplicate values found in csv", results);
				}
				return results;
			} catch (InvoiceCsvParsingException e) {
				throw e;
			} catch (Exception e) {
				throw new InvoiceCsvParsingException("unexpected error parsing input file", e);
			}
		};
		try {
			return txTemp.execute(importInv);
		} catch (InvoiceCsvParsingException e) {
			return e.getResults();
		}
	}

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	Invoice toEntity(CSVRecord r) throws ParseException {
		String supplyId = r.get(0);
		String invId = r.get(1);
		String invDate = r.get(2);
		String invAmount = r.get(3);
		String terms = r.get(4);
		String payDate = r.get(5);
		String payAmount = r.get(6);
		Invoice i = new Invoice();
		i.setSupplierId(supplyId);
		i.setInvoiceId(invId);
		i.setInvoiceDate(parseDate(invDate));
		i.setInvoiceAmount(parseBigDecimal(invAmount));
		i.setTerms(Integer.decode(terms));
		i.setPaymentDate(parseDate(payDate));
		i.setPaymentAmount(parseBigDecimal(payAmount));
		return i;
	}

	private BigDecimal parseBigDecimal(String amount) {
		if (amount == null || amount.trim().equals(""))
			return null;
		return new BigDecimal(amount);
	}

	private Date parseDate(String date) throws ParseException {
		if (date == null || date.trim().equals(""))
			return null;
		return sdf.parse(date);
	}
}
