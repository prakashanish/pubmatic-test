package com.pubmatic.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.pubmatic.bean.StockQuote;

public class YahooReaderTest {

	private YahooReader reader;
	
	@Before
	public void setup() {
		reader = new YahooReader();
	}
	
	@Test
	public void testReadInfoWithNullStockList() {
		StockQuote actualQuote = reader.readInfo(null);
		Assert.assertNull(actualQuote);
	}

	@Test
	public void testReadInfoWithEmptyStockList() {
		StockQuote actualQuote = reader.readInfo(null);
		Assert.assertNull(actualQuote);
	}

	@Test
	public void testReadInfoWithSingleStockList() {
		List<String> stockCodes = new ArrayList<String>();
		stockCodes.add("GOOG");
		StockQuote actualQuote = reader.readInfo(stockCodes);
		Assert.assertNotNull(actualQuote);
		Assert.assertEquals(1, actualQuote.getStocks().size());
	}

	@Test
	public void testReadInfoWithDoubleStockList() {
		List<String> stockCodes = new ArrayList<String>();
		stockCodes.add("GOOG");
		stockCodes.add("REL");
		StockQuote actualQuote = reader.readInfo(stockCodes);
		Assert.assertNotNull(actualQuote);
		Assert.assertEquals(2, actualQuote.getStocks().size());
	}

	@Test
	public void testReadInfoWithMultipleStockList() {
		List<String> stockCodes = new ArrayList<String>();
		stockCodes.add("GOOG");
		stockCodes.add("REL");
		stockCodes.add("APPL");
		StockQuote actualQuote = reader.readInfo(stockCodes);
		Assert.assertNotNull(actualQuote);
		Assert.assertEquals(3, actualQuote.getStocks().size());
	}

	@Test
	public void testBuildURLWithSingleStock() throws UnsupportedEncodingException {
		List<String> stockCodes = new ArrayList<String>();
		stockCodes.add("GOOG");
		final String expectedUrl = "https://query.yahooapis.com/v1/public/yql?q=select+symbol%2CLastTradePriceOnly%2COneyrTargetPrice%2CYearLow%2CYearHigh+from+yahoo.finance.quotes+where+symbol+in+%28%22GOOG%22%29&format=json&env=store://datatables.org/alltableswithkeys";
		final String actualUrl = reader.buildURL(stockCodes);
		Assert.assertNotNull(actualUrl);
		Assert.assertEquals(expectedUrl, actualUrl);
	}

	@Test
	public void testBuildURLWithTwoStock() throws UnsupportedEncodingException {
		List<String> stockCodes = new ArrayList<String>();
		stockCodes.add("GOOG");
		stockCodes.add("REL");
		final String expectedUrl = "https://query.yahooapis.com/v1/public/yql?q=select+symbol%2CLastTradePriceOnly%2COneyrTargetPrice%2CYearLow%2CYearHigh+from+yahoo.finance.quotes+where+symbol+in+%28%22GOOG%22%2C%22REL%22%29&format=json&env=store://datatables.org/alltableswithkeys";
		final String actualUrl = reader.buildURL(stockCodes);
		Assert.assertNotNull(actualUrl);
		Assert.assertEquals(expectedUrl, actualUrl);
	}

	@Test
	public void testBuildURLWithMultipleStock() throws UnsupportedEncodingException {
		List<String> stockCodes = new ArrayList<String>();
		stockCodes.add("GOOG");
		stockCodes.add("REL");
		stockCodes.add("APPL");
		final String expectedUrl = "https://query.yahooapis.com/v1/public/yql?q=select+symbol%2CLastTradePriceOnly%2COneyrTargetPrice%2CYearLow%2CYearHigh+from+yahoo.finance.quotes+where+symbol+in+%28%22GOOG%22%2C%22REL%22%2C%22APPL%22%29&format=json&env=store://datatables.org/alltableswithkeys";
		final String actualUrl = reader.buildURL(stockCodes);
		Assert.assertNotNull(actualUrl);
		Assert.assertEquals(expectedUrl, actualUrl);
	}

	@Test
	public void testBuildURLWithNullStock() throws UnsupportedEncodingException {
		final String expectedUrl = "https://query.yahooapis.com/v1/public/yql?q=select+symbol%2CLastTradePriceOnly%2COneyrTargetPrice%2CYearLow%2CYearHigh+from+yahoo.finance.quotes+where+symbol+in+%28%29&format=json&env=store://datatables.org/alltableswithkeys";
		final String actualUrl = reader.buildURL(null);
		Assert.assertNotNull(actualUrl);
		Assert.assertEquals(expectedUrl, actualUrl);
	}

	@Test
	public void testBuildURLWithEmptyStock() throws UnsupportedEncodingException {
		List<String> stockCodes = new ArrayList<String>();
		final String expectedUrl = "https://query.yahooapis.com/v1/public/yql?q=select+symbol%2CLastTradePriceOnly%2COneyrTargetPrice%2CYearLow%2CYearHigh+from+yahoo.finance.quotes+where+symbol+in+%28%29&format=json&env=store://datatables.org/alltableswithkeys";
		final String actualUrl = reader.buildURL(stockCodes);
		Assert.assertNotNull(actualUrl);
		Assert.assertEquals(expectedUrl, actualUrl);
	}
}
