package com.onlinemobileshop;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
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

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MobileActivity extends Fragment implements OnItemClickListener{
String[] brands,models,colors,prices,features,imageurls,ids;
String jsonString;
Dialog listDialog;
FragmentActivity fa;
protected List<ListItem> MobileItems;
ListView mobilelistview;
JSONArray jArray;
TextView results;
View activityView;
static String query;
Button Sort,Filter;
ExpandableListAdapter listAdapter;
ExpandableListView expListView;
List<String> listDataHeader;
HashMap<String, List<String>> listDataChild;
String[] sortArray={"Price: High-Low","Price: Low-High"};
public MobileAdapter adapter;

@Override
public void onResume() {
	// TODO Auto-generated method stub

	super.onResume();
}

@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
	activityView = inflater.inflate(R.layout.mobile_activity, container,false);
    query="select * from available";
    fa=(FragmentActivity) getActivity();
    	Sort=(Button)activityView.findViewById(R.id.sort);
    	Filter=(Button)activityView.findViewById(R.id.Filter);
		  mobilelistview = (ListView) activityView.findViewById(R.id.Mobilelist);
	    results=(TextView)activityView.findViewById(R.id.noOfResults);
    new getResource().execute(query);
    
        Sort.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				   listDialog = new Dialog(fa);
			        listDialog.setTitle("Sort Item");
			         LayoutInflater li = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			         View view = li.inflate(R.layout.sort_activity, null, false);
			         listDialog.setContentView(view);
			         listDialog.setCancelable(true);
			         listDialog.show();
			         ListView list1 = (ListView) listDialog.findViewById(R.id.Sortlist);
			         list1.setAdapter(new ArrayAdapter<String>(fa,android.R.layout.simple_list_item_1, sortArray));
			         list1.setOnItemClickListener(new OnItemClickListener()
			         {

						private String newquery;

						@Override
						public void onItemClick(AdapterView<?> parent, View view,
								int position, long id) {
							if(position==0)
							{
								newquery=query+ " order by price DESC";
								Log.d("sortquery",query);
								new getResource().execute(newquery);
								listDialog.dismiss();
							}
							else if(position==1)
							{
								newquery=query +" order by price ASC";
								Log.d("sortquery",query);
								new getResource().execute(newquery);
								listDialog.dismiss();
							}
						}
			        	 
			         });
			  }
			});
        	

    	Filter.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				 listDialog = new Dialog(fa);
			        listDialog.setTitle("Filter");
			         LayoutInflater li = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			         View view = li.inflate(R.layout.filter_activity, null, false);
			         listDialog.setContentView(view);
			         listDialog.setCancelable(true);
			         listDialog.show();
			         
			         expListView = (ExpandableListView)listDialog.findViewById(R.id.Filterlist); // you can use (ExpandableListView) findViewById(R.id.list)
			     	 

				      expListView.setDividerHeight(2);
				      expListView.setGroupIndicator(null);
				      expListView.setClickable(true);
				      prepareListData();
				 	 
				        listAdapter = new ExpandableListAdapter(fa, listDataHeader, listDataChild);
				 
				        // setting list adapter
				        expListView.setAdapter(listAdapter);
				    	expListView.setOnGroupExpandListener(new OnGroupExpandListener() { 
				    		 
				    	    @Override 
				    	    public void onGroupExpand(int groupPosition) {
				    	            for(int groups=0;groups<5;groups++)
				    	            {
				    	            	if(groups!=groupPosition)
				    	            	{
				    	            		expListView.collapseGroup(groups);
				    	            	}
				    	            }
				    	    }
				    	});
				        expListView.setOnGroupClickListener(new OnGroupClickListener() {
				       	 
				            @Override
				            public boolean onGroupClick(ExpandableListView parent, View v,
				                    int groupPosition, long id) {
				                return false;
				            }
				        });
				 
				 
				        // Listview on child click listener
				        expListView.setOnChildClickListener(new OnChildClickListener() {
				 
				            @Override
				            public boolean onChildClick(ExpandableListView parent, View v,
				                    int groupPosition, int childPosition, long id) {
				                // TODO Auto-generated method stub
				            	
				                Toast.makeText(
				                        fa,
				                        listDataHeader.get(groupPosition)
				                                + " : "
				                                + listDataChild.get(
				                                        listDataHeader.get(groupPosition)).get(
				                                        childPosition), Toast.LENGTH_SHORT)
				                        .show();
				                if(groupPosition==0)
				                {	
				                	query="select * from available where MobileBrand='"+listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition)+"'";
				                			new getResource().execute(query);
				                }
				                else if(groupPosition==1)
				                {
				                	switch(childPosition)
				                	{
				                	case 0:query="select * from available where Price < 10000 ";
				                			new getResource().execute(query);
				                			break;
				                	case 1:query="select * from available where Price >10000 and Price <=20000";
				                			new getResource().execute(query);
				                			break;
				                	case 2:query="select * from available where Price >20000 and Price <=30000";
		                					new getResource().execute(query);
		                					break;
				                	case 3:query="select * from available where Price >30000 and Price <=40000";
		                					new getResource().execute(query);
		                					break;
				                	case 4:query="select * from available where Price >40000";
		                					new getResource().execute(query);
		                					break;
				                	}
				                }
				                else if(groupPosition==2)
				                {
				                	query="select * from available where OS='"+listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition)+"'";
		                			new getResource().execute(query);
				                }
				                else if(groupPosition==3)
				                {
				                	switch(childPosition)
				                	{
				                	case 0:query="select * from available where size<=3 ";
				                			new getResource().execute(query);
				                			break;
				                	case 1:query="select * from available where size>3 and size<=4";
				                			new getResource().execute(query);
				                			break;
				                	case 2:query="select * from available where size>4 and size<=5";
		                					new getResource().execute(query);
		                					break;
				                	case 3:query="select * from available where size>5";
		                					new getResource().execute(query);
		                					break;
				                	}	
				                }
				                
				                listDialog.dismiss();
				                return false;
				            }
				        });
			}
    		
    	});
     
    return activityView;
    	}

class getResource extends AsyncTask<String, String, String> {
		


		@Override
	    protected void onPreExecute() {
	        super.onPreExecute();

	    }

	    @Override
	    protected String doInBackground(String... f_url) {
	    	HttpClient httpclient = new DefaultHttpClient();
		    HttpPost httppost = new HttpPost("http://"+WelcomeActivity.host+":8080/OMSLoadData/ProvideResources");

		    try {
		        // Add your data
		        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		        nameValuePairs.add(new BasicNameValuePair("query",f_url[0]));
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
		    	  MobileItems = new ArrayList<ListItem>();
				for (int i = 0; i <jArray.length() ; i++) {
					   ListItem item = new ListItem(brands[i]+" "+models[i],imageurls[i],colors[i],"Rs."+prices[i],features[i]);
					   MobileItems.add(item);
					 }
						adapter = new MobileAdapter(fa, MobileItems);	
				
				      mobilelistview.setAdapter(adapter);
					  adapter.notifyDataSetChanged();
					  for(ListItem item: MobileItems)
					  {
						  item.loadImage(adapter);
					  }	
					  mobilelistview.setOnItemClickListener(com.onlinemobileshop.MobileActivity.this);
				
					 
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

public void parseData() throws JSONException {
	try{
JSONObject jObj=new JSONObject(jsonString);
	jArray=jObj.getJSONArray("Resources");
	results.setText("Mobile("+jArray.length()+" Results)");
	models=new String[jArray.length()];
	colors=new String[jArray.length()];
	prices=new String[jArray.length()];
	features=new String[jArray.length()];
	imageurls=new String[jArray.length()];
	ids=new String[jArray.length()];
	brands=new String[jArray.length()];
for(int i=0;i<jArray.length();i++)
{ 
	JSONObject c = jArray.getJSONObject(i);
	models[i]=c.getString("Model").toString();
	prices[i]=c.getString("Price").toString();
	features[i]=c.getString("Features").toString();
	imageurls[i]=c.getString("ImageURL").toString();
	brands[i]=c.getString("Brand").toString();
	colors[i]=c.getString("Color").toString();
	ids[i]=c.getString("Id").toString();
	

}
	}
	catch(JSONException e)
	{
    	Toast.makeText(fa, "No Result Found", Toast.LENGTH_SHORT).show();
	}
}

@Override
public void onItemClick(AdapterView<?> parent, View view, int pos,
  long id) {

 Intent i=new Intent(getActivity(),com.onlinemobileshop.DetailedMobileActivity.class);
 i.putExtra("Pid",ids[pos]);
 startActivity(i);
	
	 	}
private void prepareListData() {
    listDataHeader = new ArrayList<String>();
    listDataChild = new HashMap<String, List<String>>();

    
    // Adding child data
    listDataHeader.add("Brand");
    listDataHeader.add("Price");
    listDataHeader.add("OS");
    listDataHeader.add("Screen Size");

    // Adding child data
    List<String> Brand = new ArrayList<String>();
    Brand.add("Samsung");
    Brand.add("Nokia");
    Brand.add("LG");
    Brand.add("Micromax");
    Brand.add("HTC");
    Brand.add("Xolo");
    Brand.add("Intex");

    List<String> Price = new ArrayList<String>();
    Price.add("Below 10000");
    Price.add("10000-20000");
    Price.add("20000-30000");
    Price.add("30000-40000");
    Price.add("Above 40000");

    List<String> OS = new ArrayList<String>();
    OS.add("Android");
    OS.add("Windows");
    OS.add("iOS");
    OS.add("Symbian");
    OS.add("Asha");

    List<String> ScreenSize = new ArrayList<String>();
    ScreenSize.add("Less than 3 inches");
    ScreenSize.add("3 to 4 inches");
    ScreenSize.add("4 to 5 inches");
    ScreenSize.add("More than 5 inches");
    
    listDataChild.put(listDataHeader.get(0), Brand); // Header, Child data
    listDataChild.put(listDataHeader.get(1), Price);
    listDataChild.put(listDataHeader.get(2), OS);
    listDataChild.put(listDataHeader.get(3), ScreenSize);
}


}
