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
import org.json.*;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CartActivity extends ActionBarActivity{ 
TextView NoOfResults,NoProduct,totalPrice,PlaceOrder;
static String[] cartNames,cartPrices,cartURLs,cartPids;
protected List<CartItem> CartItems;
Button place;
ListView cartList;
public String jsonString;
private String query;
JSONArray jArray;
CartAdapter adapter;
public String result;
private int total=0;
JSONArray cartArray;
JSONObject cartObject;
static JSONObject sendObject;
public Editor editor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cart_activity);
		cartList=(ListView)findViewById(R.id.cartlist);
		NoOfResults=(TextView)findViewById(R.id.noOfItems);
		NoProduct=(TextView)findViewById(R.id.noproduct);
		PlaceOrder=(TextView)findViewById(R.id.placeorder);
		totalPrice=(TextView)findViewById(R.id.totalPrice);
		cartArray=new JSONArray();
		cartObject=new JSONObject();
		if(WelcomeActivity.cartQuantity>0)
		{ 
			Log.d("quantitiy",String.valueOf(WelcomeActivity.cartQuantity));
		NoProduct.setVisibility(View.INVISIBLE);
		cartList.setVisibility(View.VISIBLE);
		if(WelcomeActivity.loggedin)
			query="select * from cart where CustomerId="+LoginActivity.CustomerId;
		else
			query="select * from cart where uniqueid="+WelcomeActivity.uid;
		new getCartData().execute(query);
		registerForContextMenu(cartList);
		}
PlaceOrder.setOnClickListener(new OnClickListener(){

	@Override
	public void onClick(View v){
		// TODO Auto-generated method stub
		if(!WelcomeActivity.loggedin)
		{ 	WelcomeActivity.skipped=false;
			Intent loginIntent=new Intent("com.onlinemobileshop.LOGINACTIVITY");
			loginIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			startActivity(loginIntent);
		}
		else
		{
		try
		{
			Toast.makeText(getBaseContext(),"Selling", Toast.LENGTH_SHORT).show();
		for(int i=0;i<WelcomeActivity.cartQuantity;i++)
		{
		 cartObject.put("query",cartPids[i]);
			cartArray.put(cartObject);
			cartObject=new JSONObject();
		}
		sendObject=new JSONObject();
		sendObject.put("queries", cartArray);
		Log.d("cartArray", sendObject.toString());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			Intent i =new Intent("com.onlinemobileshop.ADDRESSACTIVITY");
			startActivity(i);
		}
		}
		}
});
	}

	@Override   
	 public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)  
	 {  
	         super.onCreateContextMenu(menu, v, menuInfo);      
	         menu.add(0, v.getId(), 0, "Remove from cart");//groupId, itemId, order, title    
	 }   
	   
	 @Override    
	 public boolean onContextItemSelected(MenuItem item){    
		 AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
		  int menuItemIndex = item.getItemId();
	         if(item.getTitle()=="Remove from cart"){  
	             Toast.makeText(getApplicationContext(),"Removing",Toast.LENGTH_LONG).show();
	             String Pid=cartPids[info.position];
	             String newquery="Delete from cart where uniqueid="+WelcomeActivity.uid+" and ProductId="+Pid;
	             new deleteFromCart().execute(newquery);
	             CartItems.remove(info.position);
	         }    
	         else{  
	            return false;  
	         }    
	       return true;    
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

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		WelcomeActivity.cartQuantity--;
		changeTotal();
		adapter.notifyDataSetChanged();
		adapter.notifyDataSetInvalidated();
	}
	
} 
class getCartData extends AsyncTask<String, String, String> {
		


		@Override
	    protected void onPreExecute() {
	        super.onPreExecute();

	    }

	    @Override
	    protected String doInBackground(String... f_url) {
	    	HttpClient httpclient = new DefaultHttpClient();
	    	
		    HttpPost httppost = new HttpPost("http://"+WelcomeActivity.host+":8080/OMSLoadCart/LoadCart");

		    try {
		        // Add your data
		        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		        nameValuePairs.add(new BasicNameValuePair("query",f_url[0]));
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
	    

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			try {
		          parseData();
		    	  CartItems = new ArrayList<CartItem>();
				for (int i = 0; i <jArray.length() ; i++) {
					   CartItem item = new CartItem(cartNames[i],cartURLs[i],cartPrices[i]);
					   CartItems.add(item);
					 }
						adapter = new CartAdapter(getBaseContext(), CartItems);	
				
				      cartList.setAdapter(adapter);
					  adapter.notifyDataSetChanged();
					  for(CartItem item: CartItems)
					  {
						  item.loadImage(adapter);
					  }					
					 
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
class deleteFromCart extends AsyncTask<String, String, String> {
	


	@Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected String doInBackground(String... f_url) {
    	HttpClient httpclient = new DefaultHttpClient();
    	
	    HttpPost httppost = new HttpPost("http://"+WelcomeActivity.host+":8080/OMSDeleteFromCart/DeleteFromCart");

	    try {
	        // Add your data
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
	        Log.d("delete",f_url[0]);
	        nameValuePairs.add(new BasicNameValuePair("query",f_url[0]));
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	        HttpResponse response = httpclient.execute(httppost);
	        result=readJson(response);
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
			adapter.notifyDataSetChanged();
			WelcomeActivity.cartQuantity--;
			editor=WelcomeActivity.uniqueid.edit();
			editor.putLong("Quant", WelcomeActivity.cartQuantity);
			editor.commit();
			changeTotal();
			Toast.makeText(getBaseContext(),"Item Sold",Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
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
void changeTotal()
{	total=0;
	NoOfResults.setText("My Cart("+String.valueOf(WelcomeActivity.cartQuantity)+")");
	try{
		if(WelcomeActivity.cartQuantity==0)
			totalPrice.setText("0");
		else
		{
	for(int i=0;i<WelcomeActivity.cartQuantity;i++)
	{
		total+=Integer.parseInt(cartPrices[i]);
	}
	totalPrice.setText("Rs."+String.valueOf(total));
		}
	}
	catch(Exception e)
	{
		e.printStackTrace();
		
	}
}
public void parseData() throws JSONException {
	try{
JSONObject jObj=new JSONObject(jsonString);
	jArray=jObj.getJSONArray("cart");
	NoOfResults.setText("My Cart("+jArray.length()+")");
	cartNames=new String[jArray.length()];
	cartPrices=new String[jArray.length()];
	cartURLs=new String[jArray.length()];
	cartPids=new String[jArray.length()];
for(int i=0;i<jArray.length();i++)
{   JSONObject c = jArray.getJSONObject(i);
	cartNames[i]=c.getString("Name").toString();
	cartPrices[i]=c.getString("Price").toString();
	cartURLs[i]=c.getString("ImageUrl").toString();
	cartPids[i]=c.getString("Pid").toString();
}
total=0;
for(int i=0;i<jArray.length();i++)
{
	total+=Integer.parseInt(cartPrices[i]);
}
totalPrice.setText("Rs."+String.valueOf(total));
	}
	catch(JSONException e)
	{	 
		e.printStackTrace();
    	Toast.makeText(this, "No Result Found", Toast.LENGTH_SHORT).show();
	}
}

}
