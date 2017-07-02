package com.pubmatic.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

import com.pubmatic.bean.StockQuote;
import com.pubmatic.bean.YahooApiResponse;
import com.pubmatic.service.StockInfoReader;

public class YahooReader implements StockInfoReader {

	private static final Logger logger = Logger.getLogger(YahooReader.class);

	private static final String SYMBOLS_TEMPLATE = "[%SYMBOLS%]";
	private static final String SEPARATOR = ",";
	private static final String QUOTE = "\"";
	private static final String USER_AGENT_VALUE = "ECLIPSE";
	private static final String USER_AGENT_KEY = "User-Agent";
	private static final String YQL_STRING = "select symbol,LastTradePriceOnly,OneyrTargetPrice,YearLow,YearHigh from yahoo.finance.quotes where symbol in (" + SYMBOLS_TEMPLATE + ")";
	private static final String API_URL = "https://query.yahooapis.com/v1/public/yql?q=";
	private static final String QUERY_PARAM = "&format=json&env=store://datatables.org/alltableswithkeys";

	public StockQuote readInfo(List<String> stockCodes) {
		if (stockCodes == null || stockCodes.size() <= 0) {
			return null;
		}
		try {
			final String url = buildURL(stockCodes);
			HttpClient client = HttpClientBuilder.create().build();
			HttpGet request = new HttpGet(new URI(url));
			// add request header
			request.addHeader(USER_AGENT_KEY, USER_AGENT_VALUE);
			HttpResponse response;

			response = client.execute(request);

			if (response.getStatusLine().getStatusCode() != 200) {
				logger.error("Yahoo Finance API returned error, status code - " + response.getStatusLine().getStatusCode());
				throw new RuntimeException();
			}
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			ObjectMapper mapper = new ObjectMapper().enable(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
			String jsonString = result.toString();
//			logger.debug("JSON Response - " + jsonString);
			return mapper.readValue(jsonString.getBytes(), YahooApiResponse.class).getQuery().getResults();
		} catch (Exception e) {
			logger.error("Error occured while creating Stock object from Yahoo API", e);
			throw new RuntimeException();
		}
	}

	protected String buildURL(List<String> stockCodes) throws UnsupportedEncodingException {
		String yqlString = YQL_STRING;
		String string = "";
		if (stockCodes != null && stockCodes.size() > 0) {
			StringBuilder stockCodesStr = new StringBuilder();
			for (String stockCode : stockCodes) {
				stockCodesStr.append(QUOTE);
				stockCodesStr.append(stockCode);
				stockCodesStr.append(QUOTE);
				stockCodesStr.append(SEPARATOR);
			}
			string = stockCodesStr.toString();
			string = string.substring(0, (string.length() - 1));
		}
		yqlString = yqlString.replace(SYMBOLS_TEMPLATE, string);
		final String apiUrl = API_URL + URLEncoder.encode(yqlString, "UTF-8") + QUERY_PARAM;
		return apiUrl;
	}
}
