package com.ebay.skunk.data;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

public class ImageLoader implements Runnable {

   private static ThreadPoolExecutor executor;

   private static final int DEFAULT_POOL_SIZE = 6;

   static final int HANDLER_MESSAGE_ID = 0;

   static final String BITMAP_EXTRA = "sdk:extra_bitmap";

   private static int numAttempts = 3;
   
   private int position=0;

   public static void setThreadPoolSize(int numThreads) {
       executor.setMaximumPoolSize(numThreads);
   }

   public static void setMaxDownloadAttempts(int numAttempts) {
       ImageLoader.numAttempts = numAttempts;
   }

   public static synchronized void initialize(Context context) {
       if (executor == null) {
           executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(DEFAULT_POOL_SIZE);
       }
   }

   private String imageUrl;

   private Handler handler;

   private ImageLoader(String imageUrl, ImageView imageView) {
       this.imageUrl = imageUrl;
       this.handler = new ImageLoaderHandler();
   }

   private ImageLoader(String imageUrl, ImageLoaderHandler handler) {
       this.imageUrl = imageUrl;
       this.handler = handler;
   }

   public static void start(String imageUrl, ImageView imageView) {
	   ImageLoader loader = new ImageLoader(imageUrl, imageView);
       executor.execute(loader);
   }

   public static void start(String imageUrl, ImageLoaderHandler handler) {
       ImageLoader loader = new ImageLoader(imageUrl, handler);
       if(imageUrl!=null && handler!=null)
    	   executor.execute(loader);
   }

   public void run() {
       Bitmap bitmap = null;
       int timesTried = 1;

       while (timesTried <= numAttempts) {
           try {
               URL url = new URL(imageUrl);
   			   InputStream is = url.openStream();
   			   BufferedInputStream bis = new BufferedInputStream(is);
   			   bitmap = BitmapFactory.decodeStream(bis);
               bis.close();
   			   is.close();
               break;
           } catch (Throwable e) {
               Log.w(ImageLoader.class.getSimpleName(), "download for " + imageUrl
                       + " failed (attempt " + timesTried + ")");
               try {
                   Thread.sleep(2000);
               } catch (InterruptedException e1) {
               }

               timesTried++;
           }
       }

       if (bitmap != null) {
           notifyImageLoaded(bitmap);
       }
   }

   private void notifyImageLoaded(Bitmap bitmap) {
       Message message = new Message();
       message.what = HANDLER_MESSAGE_ID;
       Bundle data = new Bundle();
       data.putParcelable(BITMAP_EXTRA, bitmap);
       BitmapGroupManager.getInstance().register(imageUrl, bitmap);
       message.setData(data);
       handler.sendMessage(message);
   }
}

