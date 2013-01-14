package com.ebay.skunk.data;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

public class ImageLoaderHandler extends Handler {

    private Bitmap bitmap;

    public ImageLoaderHandler() {
    	bitmap=null;
    }

    @Override
    public void handleMessage(Message msg) {
        if (msg.what == ImageLoader.HANDLER_MESSAGE_ID) {
            Bundle data = msg.getData();
            bitmap = data.getParcelable(ImageLoader.BITMAP_EXTRA);
        }
    }

    Bitmap getBitmap() {
        return bitmap;
    }

}
