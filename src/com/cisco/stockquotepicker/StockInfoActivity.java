package com.cisco.stockquotepicker;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.cisco.ciscanner.R;

public class StockInfoActivity extends Activity{
	
	TextView macAddressTextView;
	TextView rebootsTextView;
	TextView versionTextView;
	TextView modelTextView;
	TextView lastRebootTextView;
	
	TextView id1TextView;
	TextView id2TextView;
	TextView id3TextView;
	TextView id4TextView;
	TextView id5TextView;
	
	TextView time1TextView;
	TextView time2TextView;
	TextView time3TextView;
	TextView time4TextView;
	TextView time5TextView;
	TextView test;
	
	TextView headline1TextView;
	TextView headline2TextView;
	TextView headline3TextView;
	TextView headline4TextView;
	TextView headline5TextView;
	
	Button pwrButton;
	Button voldwnButton;
	Button chupButton;
	Button chdwnButton;
	Button volupButton;
	Button okButton;
	Button menuButton;
	Button guideButton;
	Button exitButton;
	
	
	String macAddress = "";
	String model = "";
	String lastReboot = "";
	String reboots = "";
	String version = "";
	String receivedMacAddress = "";
	String stbURL = "";
	
	String id1String = "";
	String id2String = "";
	String id3String = "";
	String id4String = "";
	String id5String = "";
	
	String time1String = "";
	String time2String = "";
	String time3String = "";
	String time4String = "";
	String time5String = "";
	
	String headline1String = "";
	String headline2String = "";
	String headline3String = "";
	String headline4String = "";
	String headline5String = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_stock_info);
		
		Intent intent = getIntent();
		receivedMacAddress = intent.getStringExtra(MainActivity.STOCK_SYMBOL);
		
		macAddressTextView = (TextView) findViewById(R.id.companyNameTextView);
		rebootsTextView = (TextView) findViewById(R.id.bidTextView);
		versionTextView = (TextView) findViewById(R.id.askTextView);
		modelTextView = (TextView) findViewById(R.id.prevCloseTextView);
		lastRebootTextView = (TextView) findViewById(R.id.openTextView);
		
		id1TextView = (TextView) findViewById(R.id.id1);
		id2TextView = (TextView)findViewById(R.id.id2);
		id3TextView = (TextView)findViewById(R.id.id3);
		id4TextView = (TextView)findViewById(R.id.id4);
		id5TextView = (TextView)findViewById(R.id.id5);

		
		time1TextView = (TextView)findViewById(R.id.time1);
		time2TextView = (TextView)findViewById(R.id.time2);
		time3TextView = (TextView)findViewById(R.id.time3);
		time4TextView = (TextView)findViewById(R.id.time4);
		time5TextView = (TextView)findViewById(R.id.time5);

		headline1TextView = (TextView)findViewById(R.id.headline1);
		headline2TextView = (TextView)findViewById(R.id.headline2);
		headline3TextView = (TextView)findViewById(R.id.headline3);
		headline4TextView = (TextView)findViewById(R.id.headline4);
		headline5TextView = (TextView)findViewById(R.id.headline5);
		
		test = (TextView)findViewById(R.id.textView4);
		
		pwrButton = (Button)findViewById(R.id.button9);
		voldwnButton = (Button)findViewById(R.id.button1);
		chupButton = (Button)findViewById(R.id.button2);
		chdwnButton = (Button)findViewById(R.id.button3);
		volupButton = (Button)findViewById(R.id.button4);
		okButton = (Button)findViewById(R.id.button5);
		menuButton = (Button)findViewById(R.id.button6);
		guideButton = (Button)findViewById(R.id.button7);
		exitButton = (Button)findViewById(R.id.button8);
		
	    voldwnButton.setOnClickListener(voldwnButtonOnClickListener);
		chdwnButton.setOnClickListener(chdwnButtonOnClickListener);
		chupButton.setOnClickListener(chupButtonOnClickListener);
		volupButton.setOnClickListener(volupButtonOnClickListener);
		okButton.setOnClickListener(okButtonOnClickListener);
		menuButton.setOnClickListener(menuButtonOnClickListener);
		guideButton.setOnClickListener(guideButtonOnClickListener);
		exitButton.setOnClickListener(exitButtonOnClickListener);
		pwrButton.setOnClickListener(pwrButtonOnClickListener);

//		stbURL = "http://svtunreal.cisco.com/tmp/info.php?mac=" + receivedMacAddress;
		stbURL = "http://192.168.0.100/info.php?mac=" + receivedMacAddress;

		new MyAsyncTask().execute();
		
		
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.stock_info, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.refresh:
	            finish();
	            startActivity(getIntent());
	            return true;
	        case R.id.action_settings:
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	public OnClickListener pwrButtonOnClickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			new Thread(new Runnable(){
				public void run(){
					HttpResponse response;
					HttpGet request;
					HttpClient httpclient;

					try{
						httpclient = new DefaultHttpClient();
						request = new HttpGet();
						URI website = new URI("http://svtunreal.cisco.com/tmp/cmd.php?mac=" + receivedMacAddress + "&cmd=pwr");
						request.setURI(website);
						response = httpclient.execute(request);
						Log.d("test", "response sent");

					} catch(Exception e){
						System.out.println("Something " + e.getMessage());
						e.printStackTrace();

					}
					finally{
						Log.d("test", "finally block reached");
					}
				}

			}).start();
		}
	};
	
	public OnClickListener voldwnButtonOnClickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			new Thread(new Runnable(){
				public void run(){
					HttpResponse response;
					HttpGet request;
					HttpClient httpclient;

					try{
						httpclient = new DefaultHttpClient();
						request = new HttpGet();
						URI website = new URI("http://svtunreal.cisco.com/tmp/cmd.php?mac="+ receivedMacAddress + "&cmd=vol&#45;");
						request.setURI(website);
						response = httpclient.execute(request);
						Log.d("test", "response sent");

					} catch(Exception e){
						System.out.println("Something " + e.getMessage());
						e.printStackTrace();

					}
					finally{
						Log.d("test", "finally block reached");
					}
				}

			}).start();
		}
	};
	
	public OnClickListener volupButtonOnClickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			new Thread(new Runnable(){
				public void run(){
					HttpResponse response;
					HttpGet request;
					HttpClient httpclient;

					try{
						httpclient = new DefaultHttpClient();
						request = new HttpGet();
						URI website = new URI("http://svtunreal.cisco.com/tmp/cmd.php?mac="+ receivedMacAddress + "&cmd=vol&#43;");
						request.setURI(website);
						response = httpclient.execute(request);
						Log.d("test", "response sent");

					} catch(Exception e){
						System.out.println("Something " + e.getMessage());
						e.printStackTrace();

					}
					finally{
						Log.d("test", "finally block reached");
					}
				}

			}).start();
		}
	};
	
	public OnClickListener okButtonOnClickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			new Thread(new Runnable(){
				public void run(){
					HttpResponse response;
					HttpGet request;
					HttpClient httpclient;

					try{
						httpclient = new DefaultHttpClient();
						request = new HttpGet();
						URI website = new URI("http://svtunreal.cisco.com/tmp/cmd.php?mac="+ receivedMacAddress + "&cmd=sel");
						request.setURI(website);
						response = httpclient.execute(request);
						Log.d("test", "response sent");

					} catch(Exception e){
						System.out.println("Something " + e.getMessage());
						e.printStackTrace();

					}
					finally{
						Log.d("test", "finally block reached");
					}
				}

			}).start();
		}
	};
	
	public OnClickListener guideButtonOnClickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			new Thread(new Runnable(){
				public void run(){
					HttpResponse response;
					HttpGet request;
					HttpClient httpclient;

					try{
						httpclient = new DefaultHttpClient();
						request = new HttpGet();
						URI website = new URI("http://svtunreal.cisco.com/tmp/cmd.php?mac="+ receivedMacAddress + "&cmd=g");
						request.setURI(website);
						response = httpclient.execute(request);
						Log.d("test", "response sent");

					} catch(Exception e){
						System.out.println("Something " + e.getMessage());
						e.printStackTrace();

					}
					finally{
						Log.d("test", "finally block reached");
					}
				}

			}).start();
		}
	};
	
	public OnClickListener exitButtonOnClickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			new Thread(new Runnable(){
				public void run(){
					HttpResponse response;
					HttpGet request;
					HttpClient httpclient;

					try{
						httpclient = new DefaultHttpClient();
						request = new HttpGet();
						URI website = new URI("http://svtunreal.cisco.com/tmp/cmd.php?mac="+ receivedMacAddress + "&cmd=e");
						request.setURI(website);
						response = httpclient.execute(request);
						Log.d("test", "response sent");

					} catch(Exception e){
						System.out.println("Something " + e.getMessage());
						e.printStackTrace();

					}
					finally{
						Log.d("test", "finally block reached");
					}
				}

			}).start();
		}
	};
	
	public OnClickListener menuButtonOnClickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			new Thread(new Runnable(){
				public void run(){
					HttpResponse response;
					HttpGet request;
					HttpClient httpclient;

					try{
						httpclient = new DefaultHttpClient();
						request = new HttpGet();
						URI website = new URI("http://svtunreal.cisco.com/tmp/cmd.php?mac="+ receivedMacAddress + "&cmd=menu");
						request.setURI(website);
						response = httpclient.execute(request);
						Log.d("test", "response sent");

					} catch(Exception e){
						System.out.println("Something " + e.getMessage());
						e.printStackTrace();

					}
					finally{
						Log.d("test", "finally block reached");
					}
				}

			}).start();
		}
	};
	
	public OnClickListener chupButtonOnClickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			new Thread(new Runnable(){
				public void run(){
					HttpResponse response;
					HttpGet request;
					HttpClient httpclient;

					try{
						httpclient = new DefaultHttpClient();
						request = new HttpGet();
						URI website = new URI("http://svtunreal.cisco.com/tmp/cmd.php?mac="+ receivedMacAddress + "&cmd=chup");
						request.setURI(website);
						response = httpclient.execute(request);
						Log.d("test", "response sent");

					} catch(Exception e){
						System.out.println("Something " + e.getMessage());
						e.printStackTrace();

					}
					finally{
						Log.d("test", "finally block reached");
					}
				}

			}).start();
		}
	};
	
	public OnClickListener chdwnButtonOnClickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			new Thread(new Runnable(){
				public void run(){
					HttpResponse response;
					HttpGet request;
					HttpClient httpclient;

					try{
						httpclient = new DefaultHttpClient();
						request = new HttpGet();
						URI website = new URI("http://svtunreal.cisco.com/tmp/cmd.php?mac=" + receivedMacAddress + "&cmd=chdown");
						request.setURI(website);
						response = httpclient.execute(request);
						Log.d("test", "response sent");

					} catch(Exception e){
						System.out.println("Something " + e.getMessage());
						e.printStackTrace();

					}
					finally{
						Log.d("test", "finally block reached");
					}
				}

			}).start();
		}
	};
	
	private class MyAsyncTask extends AsyncTask<String, String, String>{

		@Override
		protected String doInBackground(String... params) {
			
			DefaultHttpClient   httpclient = new DefaultHttpClient(new BasicHttpParams());
			HttpPost httppost = new HttpPost(stbURL);
			// Depends on your web service
			httppost.setHeader("Content-type", "application/json");

			InputStream inputStream = null;
			String result = null;
			try {
				HttpResponse response = httpclient.execute(httppost);           
				HttpEntity entity = response.getEntity();

				inputStream = entity.getContent();
				// json is UTF-8 by default
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
				StringBuilder sb = new StringBuilder();

				String line = null;
				while ((line = reader.readLine()) != null)
				{
					sb.append(line + "\n");
				}
				result = sb.toString();
			} catch (Exception e) { 
				// Oops
			}
			finally {
				try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
			}
			return result;
		}
		
		@Override
		protected void onPostExecute(String result){
			JSONObject jObject;
			JSONArray jArray;
			try {
				jObject = new JSONObject(result);
				macAddress = jObject.getString("mac");
				model = jObject.getString("model");
				lastReboot = jObject.getString("lastReboot");
				reboots = jObject.getString("reboots");
				version = jObject.getString("version");
				
				jArray = jObject.getJSONArray("last5");
				id1String = jArray.getJSONObject(0).getString("id");
				id2String = jArray.getJSONObject(1).getString("id");
				id3String = jArray.getJSONObject(2).getString("id");
				id4String = jArray.getJSONObject(3).getString("id");
				id5String = jArray.getJSONObject(4).getString("id");
				
				time1String = jArray.getJSONObject(0).getString("time");
				time2String = jArray.getJSONObject(1).getString("time");
				time3String = jArray.getJSONObject(2).getString("time");
				time4String = jArray.getJSONObject(3).getString("time");
				time5String = jArray.getJSONObject(4).getString("time");
				
				headline1String = jArray.getJSONObject(0).getString("headline");
				headline2String = jArray.getJSONObject(1).getString("headline");
				headline3String = jArray.getJSONObject(2).getString("headline");
				headline4String = jArray.getJSONObject(3).getString("headline");
				headline5String = jArray.getJSONObject(4).getString("headline");

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				AlertDialog.Builder alert = new AlertDialog.Builder(StockInfoActivity.this);

				alert.setMessage("Can't connect to svtunreal server!");

				alert.setPositiveButton("OK", null);

				alert.show();
			}

			macAddressTextView.setText("Mac Address: " + macAddress); 
			modelTextView.setText("Model: " + model);
			lastRebootTextView.setText("Last Reboot: " + lastReboot);
			rebootsTextView.setText("Reboots: " + reboots);
			versionTextView.setText("Version: " + version);
			
			//id1TextView.setText(id1String);

			id1TextView.setClickable(true);
			id1TextView.setMovementMethod(LinkMovementMethod.getInstance());
			String text = "<a href='http://svtunreal.cisco.com/cmd2k/showfindaddr.php?id=" + id1String + "'> " + id1String + " </a>";
			id1TextView.setText(Html.fromHtml(text));
			
			id2TextView.setClickable(true);
			id2TextView.setMovementMethod(LinkMovementMethod.getInstance());
			String text2 = "<a href='http://svtunreal.cisco.com/cmd2k/showfindaddr.php?id=" + id2String + "'> " + id2String + " </a>";
			id2TextView.setText(Html.fromHtml(text2));
			
			id3TextView.setClickable(true);
			id3TextView.setMovementMethod(LinkMovementMethod.getInstance());
			String text3 = "<a href='http://svtunreal.cisco.com/cmd2k/showfindaddr.php?id=" + id3String + "'> " + id3String + " </a>";
			id3TextView.setText(Html.fromHtml(text3));
			
			id4TextView.setClickable(true);
			id4TextView.setMovementMethod(LinkMovementMethod.getInstance());
			String text4 = "<a href='http://svtunreal.cisco.com/cmd2k/showfindaddr.php?id=" + id4String + "'> " + id4String + " </a>";
			id4TextView.setText(Html.fromHtml(text4));
			
			id5TextView.setClickable(true);
			id5TextView.setMovementMethod(LinkMovementMethod.getInstance());
			String text5 = "<a href='http://svtunreal.cisco.com/cmd2k/showfindaddr.php?id=" + id5String + "'> " + id5String + " </a>";
			id5TextView.setText(Html.fromHtml(text5));
			
			
			time1TextView.setText(time1String);
			time2TextView.setText(time2String);
			time3TextView.setText(time3String);
			time4TextView.setText(time4String);
			time5TextView.setText(time5String);
			
			headline1TextView.setText(headline1String);
			headline2TextView.setText(headline2String);
			headline3TextView.setText(headline3String);
			headline4TextView.setText(headline4String);
			headline5TextView.setText(headline5String);
			

		}
	}

	}
	


