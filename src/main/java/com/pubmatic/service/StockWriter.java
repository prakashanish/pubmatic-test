package com.pubmatic.service;

import com.pubmatic.bean.Stock;

public interface StockWriter {
	void write(Stock stock);
	void close();
}
