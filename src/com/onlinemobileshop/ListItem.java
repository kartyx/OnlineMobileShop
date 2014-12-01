package com.onlinemobileshop;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

public class ListItem {
	 
    private String name;
 
    private String imgUrl,Color,Price,Features;
 
    private Bitmap image;
    
 
    private MobileAdapter sta;
 
    public ListItem(String name, String imgUrl,String Color,String Price,String Features) {
                this.name = name;
                this.imgUrl = imgUrl;
                this.Color= Color;
                this.Price=Price;
                this.Features=Features;
                // TO BE LOADED LATER - OR CAN SET TO A DEFAULT IMAGE
                this.image = null;
    }
 
    public String getColor() {
        return Color;
    }
 
    public void setColor(String Color) {
        this.Color = Color;
    }
    public String getPrice() {
        return Price;
    }
 
    public void setPrice(String Price) {
        this.Price = Price;
    }
    public String getFeatures() {
        return Features;
    }
 
    public void setFeatures(String Features) {
        this.Features = Features;
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
 
    public MobileAdapter getAdapter() {
        return sta;
    }
 
    public void setAdapter(MobileAdapter sta) {
        this.sta = sta;
    }
 
    public void loadImage(MobileAdapter sta) {
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