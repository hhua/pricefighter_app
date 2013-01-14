package com.ebay.skunk.sprite;

import org.kevinth.kth2d.action.Animate;
import org.kevinth.kth2d.geometry.Point;
import org.kevinth.kth2d.geometry.Size;
import org.kevinth.kth2d.texture.SequenceTexGroup;
import org.kevinth.kth2d.texture.Texture;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.ebay.skunk.R;

public class LoadingTextSprite extends LoadingSprite{
	public LoadingTextSprite(Context c)
	{
		super(c);
		Texture texture = new Texture(null);
		Bitmap bitmap = BitmapFactory.decodeResource(c.getResources(), R.drawable.load01);
		texture.setBitmap(bitmap);
		texture.setLefTop(new Point(0,0));
		texture.setSize(new Size(bitmap.getWidth(),bitmap.getHeight()));
		texture.onLoadTexture();
		this.setTexture(texture);
		this.setSize(bitmap.getWidth(),bitmap.getHeight());
		this.setSizeUpdatedByTex(true);
		this.setRunTexatlas(new SequenceTexGroup(R.drawable.load01, R.drawable.load05));
		this.setAnimate((Animate) new Animate(this.getRunTexatlas(), 400l).setLoop(true));
	}
}
