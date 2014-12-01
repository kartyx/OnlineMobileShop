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

public class CartAdapter extends BaseAdapter {
	 
    private static LayoutInflater mInflater;
 
    private List<CartItem> items = new ArrayList<CartItem>();
 
    public CartAdapter(Context context, List<CartItem> items) {
        mInflater = LayoutInflater.from(context);
        this.items = items;
    }
     public  CartAdapter(List<CartItem> items)
      {		
    	  this.items=items;
      }
    public int getCount() {
        return items.size();
    }
 
    public CartItem getItem(int position) {
        return items.get(position);
    }
 
    public long getItemId(int position) {
        return position;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        CartItem s = items.get(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.cart_items, null);
            holder = new ViewHolder();
            holder.Price=(TextView) convertView.findViewById(R.id.cartPrice);
            holder.name = (TextView) convertView.findViewById(R.id.cartName);
            holder.image = (ImageView) convertView.findViewById(R.id.cartPic);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(s.getName());
        holder.Price.setText(s.getPrice());
        if (s.getImage() != null) {
            holder.image.setImageBitmap(s.getImage());
        } else {
                // MY DEFAULT IMAGE
            holder.image.setImageResource(R.drawable.ic_launcher);
        }
        return convertView;
    }
 
    static class ViewHolder {
        TextView name,Price;
 
        ImageView image;
        
        
    }

	public void setContext(FragmentActivity activity) {
		// TODO Auto-generated method stub
        mInflater = LayoutInflater.from(activity);

	}

	}