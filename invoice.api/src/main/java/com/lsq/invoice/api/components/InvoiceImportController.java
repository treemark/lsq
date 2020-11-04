package com.lsq.invoice.api.components;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@Service
@RestController
@Api(tags = { "Invoice" })
@RequestMapping(value = "/invoice", produces = "application/json; charset=UTF-8")
public class InvoiceImportController {
}
