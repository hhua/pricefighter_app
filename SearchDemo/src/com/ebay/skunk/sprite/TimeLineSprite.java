package com.ebay.skunk.sprite;

import org.kevinth.kth2d.Sprite;

import android.graphics.RectF;

import com.ebay.skunk.MoveTimeline;

public class TimeLineSprite extends Sprite {
	public static final float TOUCH_MOVE_VELOCITY = 300f;

	private boolean pressed;

	private boolean appearing = false;

	public boolean isPressed() {
		return pressed;
	}

	public void setPressed(boolean pressed) {
		this.pressed = pressed;
	}

	public boolean isAppearing() {
		return appearing;
	}

	public void setAppearing(boolean appearing) {
		this.appearing = appearing;
	}	

	public boolean isPointOn(float x, float y) {
		RectF rect = new RectF();
		super.processDrawArea(rect);
		return rect.contains(x, y);
	}

}
