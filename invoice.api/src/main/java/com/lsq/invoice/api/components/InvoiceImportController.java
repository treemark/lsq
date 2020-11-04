package com.lsq.invoice.api.components;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lsq.invoice.mid.components.csv.CsvImportService;
import com.lsq.invoice.mid.components.csv.CsvRowResult;

import io.swagger.annotations.Api;

@Service
@RestController
@Api(tags = { "Invoice" })
@RequestMapping(value = "/invoice", produces = "application/json; charset=UTF-8")
public class InvoiceImportController {
	@Autowired
	CsvImportService importSrv;

	@PostMapping("/import")
	public List<CsvRowResult> importData(@RequestParam String csvData) {
		List<CsvRowResult> results = importSrv.importCsv(csvData);
		return results;
	}
}
