package com.pubmatic.task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;

import com.pubmatic.bean.Stock;
import com.pubmatic.bean.StockQuote;
import com.pubmatic.launcher.GlobalResource;
import com.pubmatic.service.StockInfoReader;

public class StockInfoReaderTask implements Runnable {

	private static final Logger logger = Logger.getLogger(StockInfoReaderTask.class);
	
	private BlockingQueue<String> stockCodesQueue;
	private BlockingQueue<Stock> stockQueue;

	private StockInfoReader stockInfoReader;

	public StockInfoReaderTask(BlockingQueue<String> stockCodes,
			BlockingQueue<Stock> stockQueue, StockInfoReader stockInfoReader) {
		this.stockCodesQueue = stockCodes;
		this.stockQueue = stockQueue;
		this.stockInfoReader = stockInfoReader;
	}

	public void run() {
		logger.debug("Starting data source reader thread [" + Thread.currentThread().getName() + "].");
		while (GlobalResource.appRunning.get()) {
			List<String> stockCodes = new ArrayList<String>();
			this.stockCodesQueue.drainTo(stockCodes, 20);
			StockQuote stockQuote = stockInfoReader.readInfo(stockCodes);
			if (stockQuote != null) {
				for (Stock stock : stockQuote.getStocks()) {
					try {
						this.stockQueue.put(stock);
					} catch (InterruptedException e) {
						logger.debug("Data source reader thread [" + Thread.currentThread().getName() + "] interupted.");
					}
				}
			}
		}
		logger.debug("Stopping data source reader thread [" + Thread.currentThread().getName() + "].");
	}
}
