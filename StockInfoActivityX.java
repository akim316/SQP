package com.cisco.stockquotepicker;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

public class StockInfoActivity extends Activity{
	
	TextView companyNameTextView;
	TextView bidTextView;
	TextView askTextView;
	TextView prevCloseTextView;
	TextView openTextView;
	TextView volumeTextView;
	TextView marketCapTextView;
	TextView daysRangeTextView;
	
//	String name = "";
//	String yearLow = "";
//	String yearHigh = "";
//	String daysLow = "";
//	String daysHigh = "";
//	String lastTradePriceOnly = "";
//	String change = "";
//	String daysRange = "";
	String stockSymbol = "";
	String yqlURL = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_stock_info);
		
		Intent intent = getIntent();
		stockSymbol = intent.getStringExtra(MainActivity.STOCK_SYMBOL);
		
		companyNameTextView = (TextView) findViewById(R.id.companyNameTextView);
		bidTextView = (TextView) findViewById(R.id.bidTextView);
		askTextView = (TextView) findViewById(R.id.askTextView);
		prevCloseTextView = (TextView) findViewById(R.id.prevCloseTextView);
		openTextView = (TextView) findViewById(R.id.openTextView);
		volumeTextView = (TextView) findViewById(R.id.volumeTextView);
		marketCapTextView = (TextView) findViewById(R.id.marketCapTextView);
		daysRangeTextView = (TextView) findViewById(R.id.daysRangeTextView);
		
		yqlURL = "http://finance.yahoo.com/q?s=" + stockSymbol;
		new MyAsyncTask().execute();
		
		
	}
	private class MyAsyncTask extends AsyncTask<Void, ArrayList<String>, ArrayList<String>>{

		@Override
		protected ArrayList<String> doInBackground(Void... params) {
			ArrayList<String> importantValues = new ArrayList<String>();
			try{
			Document doc = Jsoup.connect(yqlURL).get();
			Element title = doc.select(".title h2").get(0);
			Element table1 = doc.select("table#table1").get(0);
			Element table2 = doc.select("table#table2").get(0);
			Elements td1 = table1.select("td");
			Elements td2 = table2.select("td");
			
			importantValues.add(title.text());
			importantValues.add(td1.get(0).text());
			importantValues.add(td1.get(1).text());
			importantValues.add(td1.get(2).text());
			importantValues.add(td1.get(3).text());
			importantValues.add(td2.get(0).text());
			importantValues.add(td2.get(2).text());
			importantValues.add(td2.get(4).text());
			
			}
			catch(IOException e){
				e.printStackTrace();
			}
			return importantValues;
		}
		
		@Override
		protected void onPostExecute(ArrayList<String> result){
			companyNameTextView.setText(result.get(0));
			prevCloseTextView.setText("Previous Close: " + result.get(1));
			openTextView.setText("Open: " + result.get(2));
			bidTextView.setText("Bid: " + result.get(3));
			askTextView.setText("Ask: " + result.get(4));
			daysRangeTextView.setText("Day's Range: " + result.get(5));
			volumeTextView.setText("Volume: " + result.get(6));
			marketCapTextView.setText("Market Cap: " + result.get(7));

		}
	}

	}
	


