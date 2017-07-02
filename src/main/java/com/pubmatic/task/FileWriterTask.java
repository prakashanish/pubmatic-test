package com.pubmatic.task;

import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;

import com.pubmatic.bean.Stock;
import com.pubmatic.launcher.GlobalResource;
import com.pubmatic.service.StockWriter;

public class FileWriterTask implements Runnable {
	
	private static final Logger logger = Logger.getLogger(FileWriterTask.class);

	private BlockingQueue<Stock> stockQueue;
	private StockWriter stockWriter;

	public FileWriterTask(BlockingQueue<Stock> stockQueue, StockWriter stockWriter) {
		this.stockQueue = stockQueue;
		this.stockWriter = stockWriter;
	}

	public void run() {
		logger.debug("Starting file writer thread [" + Thread.currentThread().getName() + "].");
		while (GlobalResource.appRunning.get()) {
			try {
				Stock stock = this.stockQueue.take();
				stockWriter.write(stock);
			} catch (InterruptedException e) {
				logger.debug("File writer thread [" + Thread.currentThread().getName() + "] interupted.");
			}
		}
		stockWriter.close();
		logger.debug("Stopping file writer thread [" + Thread.currentThread().getName() + "].");
	}

}
