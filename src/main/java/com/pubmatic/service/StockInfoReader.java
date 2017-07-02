package com.pubmatic.service;

import java.util.List;

import com.pubmatic.bean.StockQuote;

public interface StockInfoReader {

	StockQuote readInfo(List<String> stockCodes);
	
}
