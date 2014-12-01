package com.onlinemobileshop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class WelcomeActivity extends Activity{
static SharedPreferences uniqueid;
static Editor editor;
public static String uid;
static int cartQuantity=0;
static String host="192.168.43.60";
static boolean loggedin=false,skipped=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome_activity);
		uniqueid=getSharedPreferences("UniquePrefersID",Context.MODE_PRIVATE); 
		/*editor=uniqueid.edit();
		editor.clear(); 
		editor.commit();*/
		if(uniqueid.contains("ID"))
		{	
			uid=uniqueid.getString("ID", null);
			cartQuantity=(int) uniqueid.getLong("Quant", 0);
			Thread t=new Thread(){
				@Override
				public void run() {
					// TODO Auto-generated method stu
					try{
					sleep(1000);
					}
					catch(InterruptedException e)
					{
						e.printStackTrace();
					}
					finally{
						Intent i=new Intent("com.onlinemobileshop.MAINACTIVITY");
						startActivity(i);
					}
		}
		};
		t.start();
		}
		else
		{
			Log.d("go","in");
			new getUniqueId().execute(); 
			Intent i=new Intent("com.onlinemobileshop.LOGINACTIVITY");
			startActivity(i);
		}
	
	}
	class getUniqueId extends AsyncTask<Void,String,Void>
	{

		@Override
	    protected Void doInBackground(Void... f_url) {
	    	HttpClient httpclient = new DefaultHttpClient();
	    	
		    HttpPost httppost = new HttpPost("http://"+host+":8080/OMSGenerateID/Generate");

		    try {
		        HttpResponse response = httpclient.execute(httppost);
		        uid=readJson(response);
		    } catch (ClientProtocolException e) {
		    	
		    } catch (IOException e) {
		    }
			return null;
	       
	    }

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			editor=uniqueid.edit();
			editor.putString("ID", uid);
			editor.commit();
			Toast.makeText(getBaseContext(), "Unique ID generated", Toast.LENGTH_SHORT).show();
		}
	}
	public String readJson(HttpResponse response)   throws IOException {
 		StringBuffer buffer;
        BufferedReader reader = null;  

        try {
            reader = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));
             buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);
        } finally {
            if (reader != null)
                reader.close();
        }
        return buffer.toString();        
}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		this.finish();
	}

}
