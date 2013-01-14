package org.kevinth.kth2d.texture;

import org.kevinth.kth2d.geometry.Size;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * @author <a href="mailto:kevinth.fang@gmail.com">Kevinth</a>
 *
 */
public class TextTexture extends Texture {
	public String text = null;

	public int fontColor = Color.WHITE;

	public float fontSize = 20;

	public TextTexture(Object resIndex, String text) {
		super(resIndex);
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getFontColor() {
		return fontColor;
	}

	public void setFontColor(int fontColor) {
		this.fontColor = fontColor;
	}

	public float getFontSize() {
		return fontSize;
	}

	public void setFontSize(float fontSize) {
		this.fontSize = fontSize;
	}

	public void onLoadTexture() {
		Paint paint = new Paint();

		paint.setColor(fontColor);
		paint.setStyle(Paint.Style.FILL);
		paint.setTextSize(fontSize);
		String str = this.text;
		Rect rectFont = new Rect();
		paint.getTextBounds(str, 0, str.length(), rectFont);
		this.setSize(new Size(rectFont.right - rectFont.left, rectFont.bottom
				- rectFont.top));
		Bitmap bitmap = Bitmap.createBitmap((int) getSize().getWidth(),
				(int) getSize().getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		canvas.drawText(str, 0, this.getSize().getHeight(), paint);
		this.setBitmap(bitmap);
		super.onLoadTexture();
	}

}
