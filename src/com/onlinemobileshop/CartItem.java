package com.onlinemobileshop;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

public class CartItem {
	 
    private String name;
 
    private String imgUrl,Price;
 
    private Bitmap image;
    
 
    private CartAdapter sta;
 
    public CartItem(String name, String imgUrl,String Price) {
                this.name = name;
                this.imgUrl = imgUrl;
                this.Price=Price;
                // TO BE LOADED LATER - OR CAN SET TO A DEFAULT IMAGE
                this.image = null;
    }
 
    
 
    
    public String getPrice() {
        return Price;
    }
 
    public void setPrice(String Price) {
        this.Price = Price;
    }
    public String getName() {
        return name;
    }
 
    public void setName(String name) {
        this.name = name;
    }
    public String getImgUrl() {
        return imgUrl;
    }
 
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
 
        public Bitmap getImage() {
        return image;
    }
 
    public CartAdapter getAdapter() {
        return sta;
    }
 
    public void setAdapter(CartAdapter sta) {
        this.sta = sta;
    }
 
    public void loadImage(CartAdapter sta) {
        // HOLD A REFERENCE TO THE ADAPTER
        this.sta = sta;
        if (imgUrl != null && !imgUrl.equals("")) {
            new ImageLoadTask().execute(imgUrl);
        }
    }
 
    // ASYNC TASK TO AVOID CHOKING UP UI THREAD
    private class ImageLoadTask extends AsyncTask<String, String, Bitmap> {
 
        @Override
        protected void onPreExecute() {
            Log.i("ImageLoadTask", "Loading image...");
        }
 
        // PARAM[0] IS IMG URL
        protected Bitmap doInBackground(String... param) {
            Log.i("ImageLoadTask", "Attempting to load image URL: " + param[0]);
            try {
                Bitmap b = ImageService.getBitmapFromURL(param[0]);
                return b;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
 
        protected void onProgressUpdate(String... progress) {
            // NO OP
        }
 
        protected void onPostExecute(Bitmap ret) {
            if (ret != null) {
                Log.i("ImageLoadTask", "Successfully loaded " + name + " image");
                image = ret;
                if (sta != null) {
                    // WHEN IMAGE IS LOADED NOTIFY THE ADAPTER
                    sta.notifyDataSetChanged();
                }
            } else {
                Log.e("ImageLoadTask", "Failed to load " + name + " image");
            }
        }
    }
 
}