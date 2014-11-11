package com.cisco.stockquotepicker;

public class StockInfo{
	
	private String companyName = "";
	private String stockExchange = "";
	private String daysLow = "";
	private String daysHigh = "";
	private String yearLow = "";
	private String yearHigh = "";
	private String lastTradePriceOnly = "";
	private String change = "";
	private String daysRange = "";
	

	public StockInfo(String name, String stockExchange, String daysLow,
			String daysHigh, String yearLow, String yearHigh,
			String lastTradePriceOnly, String change, String daysRange) {
		super();
		this.companyName = name;
		this.stockExchange = stockExchange;
		this.daysLow = daysLow;
		this.daysHigh = daysHigh;
		this.yearLow = yearLow;
		this.yearHigh = yearHigh;
		this.lastTradePriceOnly = lastTradePriceOnly;
		this.change = change;
		this.daysRange = daysRange;
	}

	public String getName() {
		return companyName;
	}

	public void setName(String name) {
		this.companyName = name;
	}

	public String getStockExchange() {
		return stockExchange;
	}

	public void setStockExchange(String stockExchange) {
		this.stockExchange = stockExchange;
	}

	public String getDaysLow() {
		return daysLow;
	}

	public void setDaysLow(String daysLow) {
		this.daysLow = daysLow;
	}

	public String getDaysHigh() {
		return daysHigh;
	}

	public void setDaysHigh(String daysHigh) {
		this.daysHigh = daysHigh;
	}

	public String getYearLow() {
		return yearLow;
	}

	public void setYearLow(String yearLow) {
		this.yearLow = yearLow;
	}

	public String getYearHigh() {
		return yearHigh;
	}

	public void setYearHigh(String yearHigh) {
		this.yearHigh = yearHigh;
	}

	public String getLastTradePriceOnly() {
		return lastTradePriceOnly;
	}

	public void setLastTradePriceOnly(String lastTradePriceOnly) {
		this.lastTradePriceOnly = lastTradePriceOnly;
	}

	public String getChange() {
		return change;
	}

	public void setChange(String change) {
		this.change = change;
	}

	public String getDaysRange() {
		return daysRange;
	}

	public void setDaysRange(String daysRange) {
		this.daysRange = daysRange;
	}

}
