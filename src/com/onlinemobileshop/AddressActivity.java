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

import com.onlinemobileshop.CartActivity.sell;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

public class AddressActivity extends Activity{
RadioGroup payment;
EditText shipAddress;
Button placeOrder;
public String jsonString;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.address_activity);
		payment=(RadioGroup)findViewById(R.id.Payment);
		shipAddress=(EditText)findViewById(R.id.shipAddress);
		placeOrder=(Button)findViewById(R.id.shipPlaceOrder);
		placeOrder.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new sell().execute(CartActivity.sendObject.toString());
			}
			
		});
	}
	class sell extends AsyncTask<String,String,String>
	{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			HttpClient httpclient = new DefaultHttpClient();
	    	
		    HttpPost httppost = new HttpPost("http://"+WelcomeActivity.host+":8080/OMSsell/SellProduct");

		    try {
		        // Add your data
		        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		        nameValuePairs.add(new BasicNameValuePair("ids",params[0]));
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

}