package com.onlinemobileshop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DetailedMobileActivity extends ActionBarActivity {
String Pid;
String query;
String jsonString;
String brand,model,primecam,secondcam,internal,external,size,processor,ram,battery,warranty,imageurl,color;
JSONArray jArray;
TextView brandnameText,modelnameText,screensizeText,internalText,externalText,primecamText,secondcamText,processorText,ramText,batteryText,warrantyText;
TextView TitleName,TitleColor,Offer;
ImageView mobileImage;
Bitmap bmp;
Button buy,add;
public String cartName;
private String price;
public String returnString;
public int SNo;
Editor editor;
SharedPreferences uniqueid;
public String OfferResponse;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detailed_mobile_activity);
		uniqueid=getSharedPreferences("UniquePrefersID",Context.MODE_PRIVATE); 
		Intent i=getIntent();
		Pid=i.getStringExtra("Pid");
		query="select * from specs where Pid="+Pid;
		buy=(Button)findViewById(R.id.buynow);
		add=(Button)findViewById(R.id.addtocart);
		mobileImage=(ImageView)findViewById(R.id.MobileImage);
		TitleName=(TextView)findViewById(R.id.TitleName);
		TitleColor=(TextView)findViewById(R.id.TitleColor);
		brandnameText=(TextView)findViewById(R.id.brandname);
		modelnameText=(TextView)findViewById(R.id.modelname);
		screensizeText=(TextView)findViewById(R.id.screensize);
		internalText=(TextView)findViewById(R.id.internalmem);
		externalText=(TextView)findViewById(R.id.externalmem);
		primecamText=(TextView)findViewById(R.id.primarycam);
		secondcamText=(TextView)findViewById(R.id.secondarycam);
		processorText=(TextView)findViewById(R.id.processorconf);
		ramText=(TextView)findViewById(R.id.ram);
		batteryText=(TextView)findViewById(R.id.battery);
		warrantyText=(TextView)findViewById(R.id.warranty);
		Offer=(TextView)findViewById(R.id.offer);
		new PopulateSpecs().execute();
		buy.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AddtoCart().execute();
				Intent cartIntent=new Intent("com.onlinemobileshop.CARTACTIVITY");
				cartIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				cartIntent.putExtra("Pid", Pid);
				startActivity(cartIntent);
			}
			
		});
		
		add.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AddtoCart().execute();
			}
			
		});
	}
	public class AddtoCart extends AsyncTask<String,String,String>
	{



		@Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        Toast.makeText(getBaseContext(), "Adding to Cart", Toast.LENGTH_SHORT).show();
	    }

	    @Override
	    protected String doInBackground(String... f_url) {
	    	HttpClient httpclient = new DefaultHttpClient();
	    	
		    HttpPost httppost = new HttpPost("http://"+WelcomeActivity.host+":8080/OMSAddToCart/AddToCart");

		    try {
		        // Add your data
		        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
		         SNo++;
		        nameValuePairs.add(new BasicNameValuePair("ProductId",Pid));
		        nameValuePairs.add(new BasicNameValuePair("Name",cartName));
		        nameValuePairs.add(new BasicNameValuePair("ImageURL",imageurl));
		        nameValuePairs.add(new BasicNameValuePair("Price",price));
		        Log.d("UID",WelcomeActivity.uid);
		        nameValuePairs.add(new BasicNameValuePair("uid",WelcomeActivity.uid));
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		        HttpResponse response = httpclient.execute(httppost);
		        returnString=readJson(response);
		        Log.d("cartReturn",returnString);
		        } catch (ClientProtocolException e) {
		    	e.printStackTrace();
		    } catch (IOException e) {
		    	e.printStackTrace();
		    }
			return null;
	       
	    }
	    

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			try {
				JSONObject returnObject=new JSONObject(returnString);
				if(returnObject.getString("added").equalsIgnoreCase("OK"))
						{
			WelcomeActivity.cartQuantity++;
			Log.d("cartQuant", String.valueOf(WelcomeActivity.cartQuantity));
			editor=uniqueid.edit();
			editor.putLong("Quant", WelcomeActivity.cartQuantity);
			editor.commit();
			Toast.makeText(getBaseContext(), "Added To Cart", Toast.LENGTH_SHORT).show();
						}
				else
				{
					Toast.makeText(getBaseContext(), "Already added", Toast.LENGTH_SHORT).show();
				}
				} catch (Exception e) {
				e.printStackTrace(); 
				Toast.makeText(getBaseContext(), "Already added", Toast.LENGTH_SHORT).show();
			}
		}

		
	}

public class PopulateSpecs extends AsyncTask<String,String,String>
{



	@Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected String doInBackground(String... f_url) {
    	HttpClient httpclient = new DefaultHttpClient();
    	
	    HttpPost httppost = new HttpPost("http://"+WelcomeActivity.host+":8080/OMSLoadSpecs/ProvideSpecs");

	    try {
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
	        nameValuePairs.add(new BasicNameValuePair("query",query));
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
		super.onPostExecute(result);
		try {
			parseData();
	        TitleName.setText(brand+" "+model);
	        TitleColor.setText(color);
	    	brandnameText.setText(brand);
	    	modelnameText.setText(model);
	    	processorText.setText(processor);
	    	primecamText.setText(primecam);
	    	secondcamText.setText(secondcam);
	    	internalText.setText(internal);
	    	externalText.setText(external);
	    	ramText.setText(ram);
	    	batteryText.setText(battery);
	    	warrantyText.setText(warranty);
	    	screensizeText.setText(size);
				 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
}
class getOffer extends AsyncTask<String,String,String>
{

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
HttpClient httpclient = new DefaultHttpClient();
    	
	    HttpPost httppost = new HttpPost("http://"+WelcomeActivity.host+":8080/OMSgetOffer/getOffer");

	    try {
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
	        nameValuePairs.add(new BasicNameValuePair("Pid",Pid));
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	        HttpResponse response = httpclient.execute(httppost);
			OfferResponse=readJson(response);
	    } catch (ClientProtocolException e) {
	    	e.printStackTrace();
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
       
       
        return null;
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		JSONObject returnObject;
		try {
			Log.d("post offer",OfferResponse);
			returnObject = new JSONObject(OfferResponse);
			String offer=returnObject.getString("Offer");
		if(!offer.equalsIgnoreCase("No"))
				{
			Offer.setVisibility(View.VISIBLE);
			Offer.setText("OFFER "+offer+"%");
				}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
public void parseData() throws JSONException {
JSONObject jObj=new JSONObject(jsonString);
	jArray=jObj.getJSONArray("Specs");
for(int i=0;i<jArray.length();i++)
{
	JSONObject c = jArray.getJSONObject(i);
brand=c.getString("Make").toString();
model=c.getString("Model").toString();
processor=c.getString("Speed").toString()+" "+c.getString("Core")+" Processor";
primecam=c.getString("Primecam").toString();
secondcam=c.getString("Secondcam").toString();
internal=c.getString("Internal").toString();
external=c.getString("External").toString();
size=c.getString("Size").toString();
ram=c.getString("Ram").toString();
battery=c.getString("Battery").toString();
warranty=c.getString("Warranty").toString();
imageurl=c.getString("imageurl").toString();
new loadImage().execute();
color=c.getString("Color").toString();
price=c.getString("Price").toString();
cartName=brand+" "+model+" ("+color+")";
}
}
class loadImage extends AsyncTask<String,String,String>
{

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		URL url;
		try {
			Log.d("url",imageurl);
	        bmp=ImageService.getBitmapFromURL(imageurl);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
       	}
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		try {
	    	mobileImage.setImageBitmap(bmp);
	    	new getOffer().execute();
		}
		catch(Exception e)
		{
			
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
@Override
public boolean onCreateOptionsMenu(Menu menu) {
	MenuInflater i=getMenuInflater();
	i.inflate(R.menu.menu, menu);
	return true;
}

@Override
public boolean onOptionsItemSelected(MenuItem item) {
	// TODO Auto-generated method stub
	switch(item.getItemId())
	{
	case R.id.cartmenu: 
		Intent cartIntent=new Intent(getApplicationContext(),CartActivity.class);
		cartIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		cartIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		startActivity(cartIntent);
		break;
	case R.id.Help:
		Intent helpIntent=new Intent("com.onlinemobileshop.HELPACTIVITY");
	      helpIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
	      startActivity(helpIntent);
	      break;
	case R.id.loginmenu:
		Intent loginIntent=new Intent("com.onlinemobileshop.LOGINACTIVITY");
		loginIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		startActivity(loginIntent);
	case R.id.logoutmenu:
		Intent logoutIntent=new Intent("com.onlinemobileshop.LOGINACTIVITY");
		logoutIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(logoutIntent);
	default:
	return super.onOptionsItemSelected(item);
	}
	return true;
}
@Override
public boolean onPrepareOptionsMenu(Menu menu) {
	// TODO Auto-generated method stub
	if(WelcomeActivity.loggedin==true)
	{
	menu.findItem(R.id.loginmenu).setVisible(false);
	menu.findItem(R.id.logoutmenu).setVisible(true);
	}
	else
	{
		menu.findItem(R.id.loginmenu).setVisible(true);
		menu.findItem(R.id.logoutmenu).setVisible(false);
		}
	return super.onPrepareOptionsMenu(menu);
}

}
