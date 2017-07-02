package com.pubmatic.service.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import com.pubmatic.service.StockReader;

/**
 * Read Stock information from a text file.
 */
public class TxtFileReader implements StockReader {

	private static final Logger logger = Logger.getLogger(TxtFileReader.class);
	
	private BlockingQueue<String> stockCodes;
	private boolean readComplete;
	private String filePath;

	public TxtFileReader(final String filePath) {
		this.filePath = filePath;
		this.stockCodes = new LinkedBlockingQueue<String>();
	}

	public void read() {
		logger.info("Input file reading started.");
		BufferedReader bufferedReader = null;
		FileReader reader = null;
		try {
			reader = new FileReader(filePath);
			bufferedReader = new BufferedReader(reader);
			String currentLine;
			bufferedReader = new BufferedReader(reader);
			while ((currentLine = bufferedReader.readLine()) != null) {
				if (currentLine != null || currentLine.trim().length() > 0) {
					String stockCode = currentLine.trim();
					this.stockCodes.add(stockCode);
				}
			}
			this.readComplete = true;
			logger.info("Input file reading completed");
		} catch (IOException e) {
			logger.error("Error occured while reading the input file - ", e);
		} finally {
			try {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
				if (reader != null) {
					reader.close();
				}
			} catch (IOException ex) {
				logger.error("Error occured while closing reader resource, snoozing - " + ex.getMessage());
			}
		}
	}

	public boolean readCompleted() {
		return readComplete;
	}

	public List<String> getStockCodes() {
		List<String> stockCodes = new ArrayList<String>();
		this.stockCodes.drainTo(stockCodes);
		return stockCodes;
	}
}
