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

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ServicesActivity extends Fragment{
	View activityView;
	EditText customerName,billNumber,ContactNumber,customerAddress,complaint;
	Button submit;
	public Object jsonString;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		activityView = inflater.inflate(R.layout.services_activity, container,false);
		customerName=(EditText)activityView.findViewById(R.id.serviceName);
		billNumber=(EditText)activityView.findViewById(R.id.serviceBill);
		ContactNumber=(EditText)activityView.findViewById(R.id.serviceContact);
		customerAddress=(EditText)activityView.findViewById(R.id.serviceAddress);
		complaint=(EditText)activityView.findViewById(R.id.serviceComplaint);
		submit=(Button)activityView.findViewById(R.id.serviceSubmit);
		submit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(WelcomeActivity.loggedin)
				{
					new service().execute();
				}
				else
				{
					Toast.makeText(getActivity(),"Please Login",Toast.LENGTH_SHORT).show();
				}
			}
			
		});
		return activityView;
	}
class service extends AsyncTask<String,String,String>
{

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost("http://"+WelcomeActivity.host+":8080/OMSService/Service");
	    try {
	        // Add your data
 	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
	        nameValuePairs.add(new BasicNameValuePair("Name",customerName.getText().toString()));
	        nameValuePairs.add(new BasicNameValuePair("ProductId",billNumber.getText().toString()));
	        nameValuePairs.add(new BasicNameValuePair("Contact",ContactNumber.getText().toString()));
	        nameValuePairs.add(new BasicNameValuePair("Address",customerAddress.getText().toString()));
	        nameValuePairs.add(new BasicNameValuePair("Complaint",complaint.getText().toString()));
	        
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
		Toast.makeText(getActivity(), "Form Submitted", Toast.LENGTH_SHORT).show();
		super.onPostExecute(result);
		
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
