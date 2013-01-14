package com.ebay.skunk.sprite;

import org.kevinth.kth2d.Sprite;
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

public class LoadingSprite extends Sprite{
	
	private TexGroup RUN_TEXATLAS;
	private Animate runAnimate;
	
	public LoadingSprite(Context c)
	{
		this.getAnchor().set(Anchor.CENTER);
		this.setColor(0, 0, 0);
	}

	public void activate()
	{
		super.activate();
		stopAllActions();
		doAction(runAnimate);
	}
	
	public void setRunTexatlas(TexGroup tg)
	{
		RUN_TEXATLAS=tg;
	}
	public TexGroup getRunTexatlas()
	{
		return RUN_TEXATLAS;
	}
	
	public void setAnimate(Animate a)
	{
		this.runAnimate=a;
	}
	
	public Animate getAnimate()
	{
		return this.runAnimate;
	}
}
