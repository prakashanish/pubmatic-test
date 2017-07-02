package com.pubmatic.bean;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StockQuote {

	private List<Stock> stocks;

	@JsonProperty("quote")
	public List<Stock> getStocks() {
		return stocks;
	}

	public void setStocks(List<Stock> quote) {
		this.stocks = quote;
	}
}

