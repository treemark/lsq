package com.lsq.invoice.mid.components.csv;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.lsq.invoice.db.entities.Invoice;

@Service
public class CsvImportDAO {

	Logger logger = Logger.getLogger(this.getClass());

	@PersistenceContext
	EntityManager em;

	@Autowired
	TransactionTemplate txTemp;

	public static enum ImportAction {
		UPDATED, INSERTED, DUPLICATE, ERROR;
	}

	public CsvImportDAO() {
	}

	public Invoice findBy(String supplierId, String invoiceId) {
		TransactionCallback<Invoice> find = (TransactionStatus txstat) -> {
			Query q = this.em.createQuery("select i from Invoice i "//
					+ "where i.supplierId = :supplierId \n" + "and i.invoiceId = :invoiceId \n");
			q.setParameter("supplierId", supplierId);
			q.setParameter("invoiceId", invoiceId);
			return (Invoice) q.getSingleResult();
		};
		return txTemp.execute(find);
	}

	public ImportAction updateOrInsert(Invoice i) {
		// Using TransactionTemplate here instead of @Transactional. Ask me why
		// TransactionCallback<ImportAction> importInv = (TransactionStatus txstat) -> {
		try {
			String hql = "update Invoice i set \n"//
					+ "i.version=i.version+1, \n"//
					+ "i.terms = :terms ,\n"//
					+ "i.paymentDate = :paymentDate ,\n"//
					+ "i.invoiceDate = :invoiceDate ,\n"//
					+ "i.paymentAmount = :paymentAmount ,\n"//
					+ "i.invoiceAmount = :invoiceAmount ,\n" //
					+ " i.state = :state \n"//
					+ "where i.supplierId = :supplierId \n"//
					+ "and i.invoiceId = :invoiceId \n";
			Query q = em.createQuery(hql);
			q.setParameter("terms", i.getTerms());
			q.setParameter("paymentDate", i.getPaymentDate());
			q.setParameter("invoiceDate", i.getInvoiceDate());
			q.setParameter("paymentAmount", i.getPaymentAmount());
			q.setParameter("invoiceAmount", i.getInvoiceAmount());
			q.setParameter("supplierId", i.getSupplierId());
			q.setParameter("invoiceId", i.getInvoiceId());
			q.setParameter("state", i.getState());
			int rowsEffected = q.executeUpdate();
			if (rowsEffected == 0) {
				em.persist(i);
				return ImportAction.INSERTED;
			}
			return ImportAction.UPDATED;
		} catch (Exception e) {
			logger.error("Unexpected exception saving data", e);
			return ImportAction.ERROR;
		}
		// };

		// return txTemp.execute(importInv);

	}
}
