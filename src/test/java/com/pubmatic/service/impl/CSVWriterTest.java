package com.pubmatic.service.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.pubmatic.bean.Stock;

public class CSVWriterTest {

	private static final String CSV_SEPARATOR = ",";
	private static final String END_LINE = "\n";
	private CSVWriter csvWriter;
	private OutputStream csvOutputStream;
	
	@Before
	public void setup() {
		csvOutputStream = new MockOutputStream();
		csvWriter = new CSVWriter(csvOutputStream);
	}

	@Test
	public void testWriteHeader() throws IOException {
		String expectedHeader =  "Symbol,Current Price,Year Target Price,Year High,Year Low\n";
		csvWriter.writeHeader();
		String actualString = getActualString(((MockOutputStream)csvOutputStream).getWritenBytes());
		Assert.assertEquals(expectedHeader, actualString);
	}

	@Test
	public void testWriteWithNull() throws IOException {		
		Stock stock = new Stock();
		stock.setSymbol("GOOG");
		stock.setLastTradePriceOnly(100.0);
		stock.setOneyrTargetPrice(100.0);
		stock.setYearHigh(100.0);
		stock.setYearLow(100.0);
		
		String expectedHeader = "";/* "Symbol,Current Price,Year Target Price,Year High,Year Low\n"
				+ stock.getSymbol() + CSV_SEPARATOR
				+ stock.getLastTradePriceOnly() + CSV_SEPARATOR
				+ stock.getOneyrTargetPrice() + CSV_SEPARATOR
				+ stock.getYearHigh() + CSV_SEPARATOR
				+ stock.getYearLow() + END_LINE;*/

		csvWriter.write(null);
		String actualString = getActualString(((MockOutputStream)csvOutputStream).getWritenBytes());
		Assert.assertEquals(expectedHeader, actualString);
	}

	
	@Test
	public void testWriteOnce() throws IOException {		
		Stock stock = new Stock();
		stock.setSymbol("GOOG");
		stock.setLastTradePriceOnly(100.0);
		stock.setOneyrTargetPrice(100.0);
		stock.setYearHigh(100.0);
		stock.setYearLow(100.0);
		
		String expectedHeader =  "Symbol,Current Price,Year Target Price,Year High,Year Low\n"
				+ stock.getSymbol() + CSV_SEPARATOR
				+ stock.getLastTradePriceOnly() + CSV_SEPARATOR
				+ stock.getOneyrTargetPrice() + CSV_SEPARATOR
				+ stock.getYearHigh() + CSV_SEPARATOR
				+ stock.getYearLow() + END_LINE;

		csvWriter.write(stock);
		String actualString = getActualString(((MockOutputStream)csvOutputStream).getWritenBytes());
		Assert.assertEquals(expectedHeader, actualString);
	}

	@Test
	public void testWriteTwice() throws IOException {		
		Stock stock1 = new Stock();
		stock1.setSymbol("GOOG");
		stock1.setLastTradePriceOnly(100.0);
		stock1.setOneyrTargetPrice(100.0);
		stock1.setYearHigh(100.0);
		stock1.setYearLow(100.0);

		Stock stock2 = new Stock();
		stock2.setSymbol("REL");
		stock2.setLastTradePriceOnly(10.0);
		stock2.setOneyrTargetPrice(10.0);
		stock2.setYearHigh(10.0);
		stock2.setYearLow(10.0);

		String expectedHeader =  "Symbol,Current Price,Year Target Price,Year High,Year Low\n"
				+ stock1.getSymbol() + CSV_SEPARATOR
				+ stock1.getLastTradePriceOnly() + CSV_SEPARATOR
				+ stock1.getOneyrTargetPrice() + CSV_SEPARATOR
				+ stock1.getYearHigh() + CSV_SEPARATOR
				+ stock1.getYearLow() + END_LINE
				+ stock2.getSymbol() + CSV_SEPARATOR
				+ stock2.getLastTradePriceOnly() + CSV_SEPARATOR
				+ stock2.getOneyrTargetPrice() + CSV_SEPARATOR
				+ stock2.getYearHigh() + CSV_SEPARATOR
				+ stock2.getYearLow() + END_LINE;

		csvWriter.write(stock1);
		csvWriter.write(stock2);
		String actualString = getActualString(((MockOutputStream)csvOutputStream).getWritenBytes());
		Assert.assertEquals(expectedHeader, actualString);
	}

	private String getActualString(List<Object> writenBytes) {
		String actualString = "";
		for (Object bytes : writenBytes) {
			actualString += new String((byte[])bytes);
		}
		return actualString;
	}
}
