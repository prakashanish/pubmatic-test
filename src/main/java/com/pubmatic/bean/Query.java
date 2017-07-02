package com.pubmatic.bean;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Query {

	private Integer count;
	private Date created;
	private StockQuote results;

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public StockQuote getResults() {
		return results;
	}

	public void setResults(StockQuote results) {
		this.results = results;
	}


}
