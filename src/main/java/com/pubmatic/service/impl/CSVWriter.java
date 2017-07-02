package com.pubmatic.service.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.log4j.Logger;

import com.pubmatic.bean.Stock;
import com.pubmatic.service.StockWriter;

/**
 * Write the Stock object to a CSV file.
 * 
 * @author
 */
public class CSVWriter implements StockWriter {

	private static final Logger logger = Logger.getLogger(CSVWriter.class);

	private static final String CSV_SEPARATOR = ",";
	private static final String END_LINE = "\n";

	private boolean headerWritten;
	private OutputStream csvOutputStream;

	public CSVWriter(String filePath) {
		try {
			csvOutputStream = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
		} catch (FileNotFoundException e) {
			logger.error("Invalid CSV file / path provided, exiting.");
		}
	}

	// for test
	protected CSVWriter(OutputStream csvOutputStream) {
		this.csvOutputStream = csvOutputStream;
	}

	public void write(Stock stock) {
		if (stock == null) {
			return;
		}
		if (!headerWritten) {
			writeHeader();
			headerWritten = true;
			logger.debug("CSV header written to file.");
		}
		try {
			csvOutputStream.write(convertTOCsv(stock).getBytes());
			logger.debug("1 row added to CSV file.");
		} catch (IOException e) {
			 logger.error("Error occured while writing stock info to CSV file - " + e.getMessage());
		}
	}

	public void close() {
		try {
			this.csvOutputStream.flush();
			this.csvOutputStream.close();
		} catch (IOException e) {
			logger.error("Error occured while flushing data to file", e);
		}
	}
	
	protected void writeHeader() {
		try {
			csvOutputStream.write("Symbol,Current Price,Year Target Price,Year High,Year Low\n".getBytes());
		} catch (IOException e) {
			logger.error("Error occured while writing header column to CSV file - " + e.getMessage());
		}
	}

	private String convertTOCsv(Stock stock) {
		return stock.getSymbol() + CSV_SEPARATOR
				+ defaultIfNull(stock.getLastTradePriceOnly(), -1) + CSV_SEPARATOR
				+ defaultIfNull(stock.getOneyrTargetPrice(), -1) + CSV_SEPARATOR
				+ defaultIfNull(stock.getYearHigh(), -1) + CSV_SEPARATOR
				+ defaultIfNull(stock.getYearLow(), -1) + END_LINE;
	}

	private String defaultIfNull(Double lastTradePriceOnly, int defaultValue) {
		return String.valueOf(lastTradePriceOnly != null ? lastTradePriceOnly : defaultValue);
	}
}
