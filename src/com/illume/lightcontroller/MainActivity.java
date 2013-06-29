package com.illume.lightcontroller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.*;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

class LightControllerTask extends AsyncTask<Integer, Void, Void> {

	private OnTaskCompleted listener;
	
	public LightControllerTask(OnTaskCompleted listener) {
		this.listener = listener;
	}
	
	@Override
	protected Void doInBackground(Integer... params) {
		// Create a new HttpClient and Post Header
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost("http://192.168.1.210/form");

	    try {
	        // Add your data
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
	        nameValuePairs.add(new BasicNameValuePair("l0", Integer.toString(params[0])));
	        nameValuePairs.add(new BasicNameValuePair("l1", Integer.toString(params[0])));
	        nameValuePairs.add(new BasicNameValuePair("l2", Integer.toString(params[0])));
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	        System.out.println(httppost.toString());
	        // Execute HTTP Post Request
	        httpclient.execute(httppost);
	        
	    } catch (ClientProtocolException e) {
	        // TODO Auto-generated catch block
	    	System.out.println(e.toString());
	    } catch (IOException e) {
	    	System.out.println(e.toString());
	        // TODO Auto-generated catch block
	    }
		return null;
	}
	
	protected void onPostExecute(Void result) {
		listener.onTaskCompleted();
	}
}

class LightSeekBarChangeListener implements OnSeekBarChangeListener, OnTaskCompleted {
	
	LightControllerTask lightControllerTask;
	
	Integer target;
	
	Integer currentValue;
	
	public LightSeekBarChangeListener()
	{
		lightControllerTask = new LightControllerTask(this);
				
		target = currentValue = 0;
	}
	
	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		System.out.println("onProgressChanged : " + target.toString());
		System.out.println("target : " + target.toString());
		System.out.println("cValue : " + currentValue.toString());
		target = progress;
		if (lightControllerTask.getStatus() != AsyncTask.Status.RUNNING) {
			currentValue = target;
			lightControllerTask = new LightControllerTask(this);
			lightControllerTask.execute(target);
		}				
	}
	
	@Override
	public void onTaskCompleted() {
		System.out.println("onTaskCompleted");
		System.out.println("target : " + target.toString());
		System.out.println("cValue : " + currentValue.toString());
		// Check if target is different than currentValue
		if (target != currentValue) {
			currentValue = target;
			lightControllerTask = new LightControllerTask(this);
			lightControllerTask.execute(target);
		}
	}
}

public class MainActivity extends Activity {

	SeekBar seekBar;
	
	LightSeekBarChangeListener listener;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		seekBar = (SeekBar) findViewById(R.id.seekBar1);
		
		listener = new LightSeekBarChangeListener();
				
		seekBar.setOnSeekBarChangeListener( listener );
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
