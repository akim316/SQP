package com.cisco.stockquotepicker;


import java.net.URI;
import java.util.Arrays;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.cisco.ciscanner.R;

public class MainActivity extends Activity {
	
	public final static String STOCK_SYMBOL = "com.cisco.stockquotepicker.STOCK"; //store a unique message using package name; useful to passover when parsing XML or opening webpage
	
	private SharedPreferences stockSymbolsEntered; //Remembers all stock symbols entered
	
	private TableLayout stockTableScrollView; //ScrollView that holds buttons/layout
	
	private EditText stockSymbolEditText;
	
	Button enterStockSymbolButton;
	Button deleteStocksButton;
	Button enterQRCodeButton;
	Button testButton;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		stockSymbolsEntered = getSharedPreferences("stockList", MODE_PRIVATE); //only available to this program MODE_PRIVATE
		
		stockTableScrollView = (TableLayout)findViewById(R.id.stockTableScrollView);
		
		stockSymbolEditText = (EditText)findViewById(R.id.stockSymbolEditText);
		
		enterStockSymbolButton = (Button)findViewById(R.id.enterStockSymbolButton);
		deleteStocksButton = (Button)findViewById(R.id.deleteStocksButton);
		enterQRCodeButton = (Button)findViewById(R.id.enterQRCodeButton);

		
		enterStockSymbolButton.setOnClickListener(enterStockButtonListener);
		deleteStocksButton.setOnClickListener(deleteStocksButtonListener);
		enterQRCodeButton.setOnClickListener(enterQRCodeButtonListener);
		//testButton.setOnClickListener(testButtonListener);

		
		updateSavedStockList(null);
		
		
	}
	
	String macAddress;
	public void onActivityResult(int requestCode, int resultCode, Intent intent){
		IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		try{
		String[] url = scanResult.getContents().split("=");
		macAddress = url[1];
		if(scanResult != null){
			saveStockSymbol(macAddress);
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromInputMethod(stockSymbolEditText.getWindowToken(), 0);
		}
		}
		catch(NullPointerException e){
			finish();
		}
		

	}
	
	private void updateSavedStockList(String newStockSymbol){
		String[] stocks = stockSymbolsEntered.getAll().keySet().toArray(new String[0]);
		
		Arrays.sort(stocks, String.CASE_INSENSITIVE_ORDER);
		
		if(newStockSymbol != null){
			insertStockInScrollView(newStockSymbol, Arrays.binarySearch(stocks, newStockSymbol));
		}
		else{
			for(int i = 0; i < stocks.length; i++){
				insertStockInScrollView(stocks[i], i);
			}
		}
	}
	
	private void saveStockSymbol(String newStock){
		String isTheStockNew = stockSymbolsEntered.getString(newStock, null);
		
		SharedPreferences.Editor preferencesEditor = stockSymbolsEntered.edit();
		preferencesEditor.putString(newStock, newStock);
		preferencesEditor.apply();
		
		if(isTheStockNew == null){
			updateSavedStockList(newStock);
		}
	}
	View newStockRow;
	TextView newStockTextView;
	private void insertStockInScrollView(String stock, int arrayIndex){
		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE); //creates each new row and inserts in scroll view when new stock is entered
		
		newStockRow = inflater.inflate(R.layout.stock_quote_row, null);
		
		newStockTextView = (TextView)newStockRow.findViewById(R.id.stockSymbolTextView);
		
		newStockTextView.setText(stock);
		
		Button stockQuoteButton = (Button)newStockRow.findViewById(R.id.stockQuoteButton);
		stockQuoteButton.setOnClickListener(getStockActivityListener);
		Button quoteFromWebButton = (Button)newStockRow.findViewById(R.id.quoteFromWebButton);
		quoteFromWebButton.setOnClickListener(getStockFromWebsiteListener);
		Button deleteButton = (Button)newStockRow.findViewById(R.id.button1);
		deleteButton.setOnClickListener(deleteButtonListener);
		Button changeTextButton = (Button)newStockRow.findViewById(R.id.button2);
		changeTextButton.setOnClickListener(changeTextButtonListener);
		
		stockTableScrollView.addView(newStockRow, arrayIndex);
	}

	IntentIntegrator integrator;
	public OnClickListener enterQRCodeButtonListener = new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			integrator = new IntentIntegrator(MainActivity.this);
			integrator.initiateScan();
			
		}
		
	};
	
	
	public OnClickListener enterStockButtonListener = new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if(stockSymbolEditText.getText().length() > 0){
				saveStockSymbol(stockSymbolEditText.getText().toString());
				stockSymbolEditText.setText("");
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromInputMethod(stockSymbolEditText.getWindowToken(), 0);
			}
			else{
				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
				builder.setTitle(R.string.missing_stock_symbol);
				
				builder.setPositiveButton(R.string.ok, null);
				
				AlertDialog theAlertDialog = builder.create();
				
				theAlertDialog.show();
			}
		}
		
	};
	

	public OnClickListener changeTextButtonListener = new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			TableRow tableRow = (TableRow)arg0.getParent();
			final TextView theAddress = (TextView)tableRow.findViewById(R.id.stockSymbolTextView);
			
			AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

			alert.setTitle("Title");
			alert.setMessage("Change text for "+ theAddress.getText());

			// Set an EditText view to get user input 
			final EditText input = new EditText(MainActivity.this);
			alert.setView(input);

			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			  String value = input.getText().toString();
			  // Do something with value!
			  theAddress.setText(value);
			  }
			});

			alert.setNegativeButton("Cancel", null);

			alert.show();
			// see http://androidsnippets.com/prompt-user-input-with-an-alertdialog
			
			tableRow = null;
			//theAddress = null;
		}
		
	};
	
	
	public OnClickListener deleteStocksButtonListener = new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			deleteAllStocks();
			
			SharedPreferences.Editor preferencesEditor = stockSymbolsEntered.edit();
			preferencesEditor.clear();
			preferencesEditor.apply();
		}
		
	};
	
	public OnClickListener getStockActivityListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			TableRow tableRow = (TableRow)v.getParent();
			TextView stockTextView = (TextView)tableRow.findViewById(R.id.stockSymbolTextView);
			String stockSymbol = stockTextView.getText().toString();
			
			Intent intent = new Intent(MainActivity.this, StockInfoActivity.class);
			
			intent.putExtra(STOCK_SYMBOL, stockSymbol);
			
			startActivity(intent);
		}
		
	};
	
	public OnClickListener getStockFromWebsiteListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			TableRow tableRow = (TableRow)v.getParent();
			TextView stockTextView = (TextView)tableRow.findViewById(R.id.stockSymbolTextView);
			String stockSymbol = stockTextView.getText().toString();
			String stockURL = getString(R.string.svtunreal_site) + stockSymbol;
			
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(stockURL));
			
			startActivity(intent);
		}
		
	};
	
	
	private void deleteAllStocks(){
		stockTableScrollView.removeAllViews();
	}
	
	public OnClickListener deleteButtonListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			TableRow tableRow = (TableRow)v.getParent();
			stockTableScrollView.removeView(tableRow);
			
			if(stockTableScrollView.getChildCount() == 0){
				deleteAllStocks();
				SharedPreferences.Editor preferencesEditor = stockSymbolsEntered.edit();
				preferencesEditor.clear();
				preferencesEditor.apply();
			}
		}
		
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
