package com.lsq.invoice.mid;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.StreamUtils;

import com.lsq.invoice.db.entities.Invoice;
import com.lsq.invoice.mid.components.csv.CsvImportDAO;
import com.lsq.invoice.mid.components.csv.CsvImportDAO.ImportAction;
import com.lsq.invoice.mid.components.csv.CsvImportService;
import com.lsq.invoice.mid.components.csv.CsvRowResult;

@TestMethodOrder(OrderAnnotation.class)
public class TestInvoiceImport extends AbstractInvoiceMidTest {

	Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	CsvImportService srv;

	@Autowired
	CsvImportDAO dao;

	@PersistenceContext
	EntityManager em;

	@Autowired
	TransactionTemplate txTemp;

	@Test
	@Order(1)
	public void testImportData1() throws IOException {
		File d = new File("..");
		logger.info(d.getCanonicalPath());
		File f = new File("../testdata/csv/invoice_data_1.csv");
		Assert.assertTrue(f.exists());
		FileInputStream in = new FileInputStream(f);
		String csv = StreamUtils.copyToString(in, Charset.defaultCharset());
		List<CsvRowResult> results = srv.importCsv(csv);

	}

	@Test
	@Order(2)
	public void testImportData2() throws IOException {
		TransactionCallback<Invoice> findExisting = (TransactionStatus txstat) -> {
			String updatedRecInvId = "8E74BED1-D7C0-4655-A528-0C9B67102C31";
			String updatedRecSupId = "supplier_7";
			Invoice existingRec = dao.findBy(updatedRecSupId, updatedRecInvId);
			return existingRec;
		};
		Invoice existing = txTemp.execute(findExisting);

		File d = new File("..");
		logger.info(d.getCanonicalPath());
		File f = new File("../testdata/csv/invoice_data_2.csv");
		Assert.assertTrue(f.exists());
		FileInputStream in = new FileInputStream(f);
		String csv = StreamUtils.copyToString(in, Charset.defaultCharset());
		List<CsvRowResult> results = srv.importCsv(csv);
		int updateCount = 0;
		for (CsvRowResult result : results) {
			if (result.getAction().equals(ImportAction.UPDATED))
				updateCount++;
		}
		logger.info("Update count:" + updateCount);

		Invoice afterUpdate = txTemp.execute(findExisting);

		Assert.assertNotEquals("The invoice was not updated " + existing, existing, afterUpdate);

	}

	@Test(expected = NoResultException.class)
	@Order(3)
	public void testImportData3() throws IOException {
		File d = new File("..");
		logger.info(d.getCanonicalPath());
		File f = new File("../testdata/csv/invoice_data_duplicates.csv");
		Assert.assertTrue(f.exists());
		FileInputStream in = new FileInputStream(f);
		String csv = StreamUtils.copyToString(in, Charset.defaultCharset());
		List<CsvRowResult> results = srv.importCsv(csv);
		int duplicateCount = 0;
		for (CsvRowResult result : results) {
			if (result.getAction().equals(ImportAction.DUPLICATE))
				duplicateCount++;
			if (result.getAction().equals(ImportAction.UPDATED))
				logger.info(result);
		}
		logger.info("Duplicate count:" + duplicateCount);
		// We expect this to throw a NoResultException, because the entire batch was
		// rolled back.
		Invoice afterUpdate = txTemp.execute((TransactionStatus txstat) -> {
			return dao.findBy("supplier_11", "57");
		});
	}
}
