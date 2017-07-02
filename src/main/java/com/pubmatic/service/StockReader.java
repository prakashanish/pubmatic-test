package com.pubmatic.service;

import java.util.List;

public interface StockReader {
	void read();
	boolean readCompleted();
	List<String> getStockCodes();
}
