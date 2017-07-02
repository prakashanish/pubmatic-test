package com.pubmatic.bean;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;


/**
 * Represents a Stock object.
 * 
 * @author
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Stock {

	private String symbol; // symbol
	private Double lastTradePriceOnly; // LastTradePriceOnly
	private Double oneyrTargetPrice; // OneyrTargetPrice
	private Double yearLow; // YearLow
	private Double yearHigh; // YearHigh

	@JsonProperty("symbol")
	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	@JsonProperty("LastTradePriceOnly")
	public Double getLastTradePriceOnly() {
		return this.lastTradePriceOnly;
	}

	public void setLastTradePriceOnly(Double lastTradePriceOnly) {
		this.lastTradePriceOnly = lastTradePriceOnly;
	}

	@JsonProperty("OneyrTargetPrice")
	public Double getOneyrTargetPrice() {
		return this.oneyrTargetPrice;
	}

	public void setOneyrTargetPrice(Double oneyrTargetPrice) {
		this.oneyrTargetPrice = oneyrTargetPrice;
	}

	@JsonProperty("YearLow")
	public Double getYearLow() {
		return this.yearLow;
	}

	public void setYearLow(Double yearLow) {
		this.yearLow = yearLow;
	}

	@JsonProperty("YearHigh")
	public Double getYearHigh() {
		return this.yearHigh;
	}

	public void setYearHigh(Double yearHigh) {
		this.yearHigh = yearHigh;
	}

}
