package com.ebay.skunk.sprite;

import org.kevinth.kth2d.action.Animate;
import org.kevinth.kth2d.geometry.Anchor;
import org.kevinth.kth2d.geometry.Point;
import org.kevinth.kth2d.geometry.Size;
import org.kevinth.kth2d.texture.SequenceTexGroup;
import org.kevinth.kth2d.texture.TexGroup;
import org.kevinth.kth2d.texture.Texture;

import com.ebay.skunk.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class LoadingPicSprite extends LoadingSprite{	
	public LoadingPicSprite(Context c)
	{
		super(c);
		Texture texture = new Texture(null);
		Bitmap bitmap = BitmapFactory.decodeResource(c.getResources(), R.drawable.loading0001);
		texture.setBitmap(bitmap);
		texture.setLefTop(new Point(0,0));
		texture.setSize(new Size(bitmap.getWidth(),bitmap.getHeight()));
		texture.onLoadTexture();
		this.setTexture(texture);
		this.setSize(bitmap.getWidth(),bitmap.getHeight());
		this.setSizeUpdatedByTex(true);
		this.setRunTexatlas(new SequenceTexGroup(R.drawable.loading0001, R.drawable.loading0027));
		this.setAnimate((Animate) new Animate(this.getRunTexatlas(), 50l).setLoop(true));
	}
}
