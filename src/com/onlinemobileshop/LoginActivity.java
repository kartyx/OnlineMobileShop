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
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity{
EditText userName,Password;
TextView skip; 
Button login,signup;
public String jsonString;
String user,pass;
static String CustomerId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		skip=(TextView)findViewById(R.id.skipthis);
		userName=(EditText)findViewById(R.id.UserName);
		Password=(EditText)findViewById(R.id.passText);
		login=(Button)findViewById(R.id.Login);
		signup=(Button)findViewById(R.id.SignUp);
		login.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				user=userName.getText().toString();
				pass=Password.getText().toString();
				Log.d(user, pass);
				new loginClass().execute();
			}
			
		});
		
		signup.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i=new Intent("com.onlinemobileshop.SIGNUPACTIVITY");
				startActivity(i);
			}
			
		});
		
		skip.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				WelcomeActivity.skipped=true;
				Intent i=new Intent("com.onlinemobileshop.MAINACTIVITY");
				startActivity(i);
			}
		});
	}
class loginClass extends AsyncTask<String,String,String>
{

	@Override
	protected String doInBackground(String... params) {
		HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost("http://"+WelcomeActivity.host+":8080/OMSLogin/Login");
	    try {
	        // Add your data
 	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
	        nameValuePairs.add(new BasicNameValuePair("userName",user));
	        nameValuePairs.add(new BasicNameValuePair("Password",pass));
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	        HttpResponse response = httpclient.execute(httppost);
	        jsonString=readJson(response);
	    } catch (ClientProtocolException e) {
	    	
	    } catch (IOException e) {
	    }
        return null;
    
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		Log.d(user,jsonString);
		try{
		if(!jsonString.trim().equalsIgnoreCase("NO"))
		{	
			CustomerId=jsonString.trim();
			WelcomeActivity.loggedin=true;
			if(!WelcomeActivity.skipped==true)
			{
			Intent i=new Intent("com.onlinemobileshop.MAINACTIVITY");
			startActivity(i);
			}
			else
			{
				Intent i=new Intent("com.onlinemobileshop.ADDRESSACTIVITY");
				startActivity(i);
			}
			
		}
		else if(jsonString.trim().equalsIgnoreCase("NO"))
		{
			Toast.makeText(getBaseContext(), "UserName and PassWord Doesn't match", Toast.LENGTH_SHORT).show();
		}
		String updateQuery="update cart set CustomerId="+CustomerId+" where uniqueid="+WelcomeActivity.uid;
		new updateCustomer().execute(updateQuery);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
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
class updateCustomer extends AsyncTask<String,String,String>
{

	@Override
	protected String doInBackground(String... params) {
		HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost("http://"+WelcomeActivity.host+":8080/OMSupdateCart/update");
	    try {
	        // Add your data
 	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
	        nameValuePairs.add(new BasicNameValuePair("query",params[0]));
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	        HttpResponse response = httpclient.execute(httppost);
	        jsonString=readJson(response);
	    } catch (ClientProtocolException e) {
	    	e.printStackTrace();
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
        return null;
    }
	
}
}
