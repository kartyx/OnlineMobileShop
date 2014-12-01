package com.onlinemobileshop;

import grandcentral.security.TokenAuthority;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import javax.mail.PasswordAuthentication;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
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
import android.widget.Toast;


public class SignUpActivity extends Activity
{
    EditText editTextUserName,editTextPassword,editTextConfirmPassword,editTextName,editTextAddress,editTextContact,editTextAge;
    Button btnCreateAccount;
    String jsonString;
    
    static String userName,password,Address,confirmPassword,Name,Contact,Age;
 
 
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);

        editTextName=(EditText)findViewById(R.id.nameText);
        editTextUserName=(EditText)findViewById(R.id.emailText);
        editTextPassword=(EditText)findViewById(R.id.passwordText);
        editTextConfirmPassword=(EditText)findViewById(R.id.confirmPassText);
        editTextAddress=(EditText)findViewById(R.id.addressText);
        editTextContact=(EditText)findViewById(R.id.contactText);
        editTextAge=(EditText)findViewById(R.id.ageText);
        btnCreateAccount=(Button)findViewById(R.id.signUpBtn);
        btnCreateAccount.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d("clicked", "(y)");
				Name=editTextName.getText().toString();
		 		userName=editTextUserName.getText().toString();
			    password=editTextPassword.getText().toString();
			    confirmPassword=editTextConfirmPassword.getText().toString();
			    Contact=editTextContact.getText().toString();
			    Age=editTextAge.getText().toString();
			    Address=editTextAddress.getText().toString();
			            // check if any of the fields are vaccant
			            if(userName.equalsIgnoreCase(null)||password.equalsIgnoreCase(null)||confirmPassword.equalsIgnoreCase(null)||Name.equalsIgnoreCase(null)||Address.equalsIgnoreCase(null)||Age.equalsIgnoreCase(null)||Contact.equalsIgnoreCase(null))
			             {
			                    Toast.makeText(getApplicationContext(), "Field Vaccant", Toast.LENGTH_LONG).show();
			                    return;
			            }
			            // check if both password matches
			            if(!password.equals(confirmPassword))
			            {
			                Toast.makeText(getApplicationContext(), "Password does not match", Toast.LENGTH_LONG).show();
			                return;
			            }
			            else
			            {
			                // Save the Data in Database
			            	
			            	new signupOnline().execute();
			            }

			}
        });	
  
	}
    class signupOnline extends AsyncTask<Void,Void,Void>{
		protected void onPreExecute()
		{
		super.onPreExecute();	
		}
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			HttpClient httpclient = new DefaultHttpClient();
		
		    HttpPost httppost = new HttpPost("http://"+WelcomeActivity.host+":8080/MainSignUp/SignUp");

		    try {
		        // Add your data
		        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		        nameValuePairs.add(new BasicNameValuePair("Name", Name));
		        nameValuePairs.add(new BasicNameValuePair("UserName", userName));
		        nameValuePairs.add(new BasicNameValuePair("Password",password));
		        nameValuePairs.add(new BasicNameValuePair("Contact",Contact));
		        nameValuePairs.add(new BasicNameValuePair("Age",Age));
		        nameValuePairs.add(new BasicNameValuePair("Address",Address));
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		        // Execute HTTP Post Request
		        HttpResponse response = httpclient.execute(httppost);
		        jsonString=readJson(response);
		    } catch (ClientProtocolException e) {
		        // TODO Auto-generated catch block
		    } catch (IOException e) {
		        // TODO Auto-generated catch block
		    }
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			start();		
			}	
    }
    protected String readJson(HttpResponse resp)
	        throws IOException {
	 		StringBuffer buffer;
	        BufferedReader reader = null;  

	        try {
	            reader = new BufferedReader(new InputStreamReader(
	                    resp.getEntity().getContent()));
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
	public void start() {
		Log.d("jsonString",jsonString);
		// TODO Auto-generated method stub
		if(jsonString.equalsIgnoreCase("Yes"))
		{
            Toast.makeText(getApplicationContext(), "Account Successfully Created ", Toast.LENGTH_LONG).show();
            Intent i=new Intent("com.onlinemobileshop.LOGINACTIVITY");
            startActivity(i);
		}
		else
		{
			Toast.makeText(getApplicationContext(), "UserName Already Exists ", Toast.LENGTH_LONG).show();
            
		}
	}
	String getPassword()
	{
		TokenAuthority.setPassword(password);
	    String newpass=new TokenAuthority().generateToken().toString();
	    Log.d("new",newpass);
	    return newpass;
		}
}