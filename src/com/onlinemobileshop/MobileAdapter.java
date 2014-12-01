package com.onlinemobileshop;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MobileAdapter extends BaseAdapter {
	 
    private static LayoutInflater mInflater;
 
    private List<ListItem> items = new ArrayList<ListItem>();
 
    public MobileAdapter(Context context, List<ListItem> items) {
        mInflater = LayoutInflater.from(context);
        this.items = items;
    }
     public  MobileAdapter(List<ListItem> items)
      {		
    	  this.items=items;
      }
    public int getCount() {
        return items.size();
    }
 
    public ListItem getItem(int position) {
        return items.get(position);
    }
 
    public long getItemId(int position) {
        return position;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        ListItem s = items.get(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list, null);
            holder = new ViewHolder();
            holder.Color=(TextView) convertView.findViewById(R.id.Color);
            holder.Price=(TextView) convertView.findViewById(R.id.Price);
            holder.Features=(TextView)convertView.findViewById(R.id.Features);
            holder.name = (TextView) convertView.findViewById(R.id.MobileName);
            holder.image = (ImageView) convertView.findViewById(R.id.pic);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(s.getName());
        holder.Color.setText(s.getColor());
        holder.Price.setText(s.getPrice());
        holder.Features.setText(s.getFeatures());
        if (s.getImage() != null) {
            holder.image.setImageBitmap(s.getImage());
        } else {
                // MY DEFAULT IMAGE
            holder.image.setImageResource(R.drawable.ic_launcher);
        }
        return convertView;
    }
 
    static class ViewHolder {
        TextView name,Color,Price,Features;
 
        ImageView image;
        
        
    }

	public void setContext(FragmentActivity activity) {
		// TODO Auto-generated method stub
        mInflater = LayoutInflater.from(activity);

	}

	}