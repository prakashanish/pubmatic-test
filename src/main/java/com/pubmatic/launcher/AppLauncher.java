package com.pubmatic.launcher;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import com.pubmatic.bean.Stock;
import com.pubmatic.service.StockInfoReader;
import com.pubmatic.service.StockReader;
import com.pubmatic.service.StockWriter;
import com.pubmatic.service.impl.CSVWriter;
import com.pubmatic.service.impl.TxtFileReader;
import com.pubmatic.service.impl.YahooReader;
import com.pubmatic.task.FileReaderTask;
import com.pubmatic.task.FileWriterTask;
import com.pubmatic.task.StockInfoReaderTask;

public class AppLauncher {

	private static final Logger logger = Logger.getLogger(AppLauncher.class);

	private static final String OUTPUT_FILE_PATH = "output/stock-info.csv";
	private static final String INPUT_FILE_PATH = "input/Stocks.txt";
	private final static String CURRENT_PATH = AppLauncher.class.getResource(".").getPath();
	private final static String RELATIVE_PATH_TO_HOME_DIR = "../../../../";
	private static final String CONFIG_DIR = "../config/";
	private static final String LOG4J_XML = "log4j.xml";

	public static void main(String[] args) {	
		String log4jConfigFile = CURRENT_PATH + CONFIG_DIR + File.separator + LOG4J_XML;
        DOMConfigurator.configure(log4jConfigFile);
		
		Date startTime = Calendar.getInstance().getTime();
		logger.info("Starting the application @ - " + startTime);
		
		// Resources
		BlockingQueue<String> stockCodesQueue = new ArrayBlockingQueue<String>(50);
		BlockingQueue<Stock> stockQueue = new ArrayBlockingQueue<Stock>(50);

		// Reader / Writer
		StockReader stockCodeReader = new TxtFileReader(CURRENT_PATH + RELATIVE_PATH_TO_HOME_DIR + INPUT_FILE_PATH);
		StockWriter stockWriter = new CSVWriter(CURRENT_PATH + RELATIVE_PATH_TO_HOME_DIR + OUTPUT_FILE_PATH);
		StockInfoReader stockInfoReader = new YahooReader();
		
		// Tasks 
		FileReaderTask fileReaderTask = new FileReaderTask(stockCodeReader, stockCodesQueue);
		StockInfoReaderTask stockInfoReaderTask = new StockInfoReaderTask(stockCodesQueue, stockQueue, stockInfoReader);
		FileWriterTask fileWriterTask = new FileWriterTask(stockQueue, stockWriter);

		logger.info("Starting threads to read StockInfo from external source.");
		ExecutorService stockInfoReaderTaskExecutor = Executors.newFixedThreadPool(3); // scale as required
		stockInfoReaderTaskExecutor.execute(stockInfoReaderTask);
		stockInfoReaderTaskExecutor.execute(stockInfoReaderTask);
		stockInfoReaderTaskExecutor.execute(stockInfoReaderTask);

		logger.info("Starting thread to write StockInfo objects.");
		ExecutorService writerTaskExecutor = Executors.newFixedThreadPool(1);
		writerTaskExecutor.execute(fileWriterTask);		

		logger.info("Starting thread to read StockCode from file.");
		ExecutorService readerTaskExecutor = Executors.newFixedThreadPool(1);
		readerTaskExecutor.execute(fileReaderTask);

		logger.info("Waiting for 30 secs before Stopping the application.");
		// wait for 30 secs
		try {
			Thread.sleep(30 * 1000);
		} catch (InterruptedException e) {
			logger.error("Error occured while speeping for 30 secs, ignoring");
		}

		logger.info("Stopping the application.");
		// Stop the application
		GlobalResource.appRunning.set(false);
		writerTaskExecutor.shutdownNow();
		readerTaskExecutor.shutdownNow();
		stockInfoReaderTaskExecutor.shutdownNow();
		logger.info("Applicaiton stopped (Bye Bye)!");
	}
}
