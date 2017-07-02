package com.pubmatic.task;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;

import com.pubmatic.service.StockReader;

public class FileReaderTask implements Runnable {

	private static final Logger logger = Logger.getLogger(FileReaderTask.class);
	
	private StockReader reader;
	private BlockingQueue<String> stockCodesQueue;

	public FileReaderTask(StockReader reader, BlockingQueue<String> stockCodesQueue) {
		this.reader = reader;
		this.stockCodesQueue = stockCodesQueue;
	}

	public void run() {
		logger.debug("Starting file reader thread [" + Thread.currentThread().getName() + "].");
		this.reader.read();
		do {
			List<String> stockCodes = this.reader.getStockCodes();
			logger.debug("Read " + stockCodes.size() + " stocks from file.");
			for (String stockCode : stockCodes) {
				try {
					this.stockCodesQueue.put(stockCode);
				} catch (InterruptedException e) {
					logger.debug("File reader thread [" + Thread.currentThread().getName() + "] interupted.");
				}
			}
		} while (!this.reader.readCompleted());
		logger.debug("Stopping file reader thread [" + Thread.currentThread().getName() + "].");
	}
}
