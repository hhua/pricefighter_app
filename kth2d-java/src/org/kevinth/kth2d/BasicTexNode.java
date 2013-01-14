package org.kevinth.kth2d;

import org.kevinth.kth2d.geometry.Point;
import org.kevinth.kth2d.geometry.Size;
import org.kevinth.kth2d.texture.Texture;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;

/**
 * @author <a href="mailto:kevinth.fang@gmail.com">Kevinth</a>
 *
 */
public class BasicTexNode extends Node {
	public static final Matrix ORI_MATRIX = new Matrix();
	public static final short[] DEFAULT_INDICES = new short[] { 0, 2, 3, 0, 1,
			2 };

	//true to draw color
	protected boolean drawColor = false;

	//#AARRGGBB
	protected int color = Color.WHITE;

	//actural line width, should be 0, 1, 2...etc.
	protected float borderWidth = 0;

	//#AARRGGBB
	protected int borderColor = Color.WHITE;

	//node size determined by texture
	protected boolean sizeUpdatedByTex = false;
	protected final Size sizeRatioByTex = new Size();

	protected Texture texture = null;

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public void setColor(int red, int green, int blue) {
		this.color = Color.rgb(red, green, blue);
	}

	public boolean isDrawColor() {
		return drawColor;
	}

	public void setDrawColor(boolean drawColor) {
		this.drawColor = drawColor;
	}

	public float getBorderWidth() {
		return borderWidth;
	}

	public void setBorderWidth(float borderWidth) {
		this.borderWidth = borderWidth;
	}

	public int getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(int borderColor) {
		this.borderColor = borderColor;
	}

	public void setBorderColor(int red, int green, int blue) {
		this.borderColor = Color.rgb(red, green, blue);
	}

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
		if (sizeUpdatedByTex && !sizeRatioByTex.isEmpty()
				&& texture.getSize() != null) {
			//			Log.d("determine ratio", "w:" + sizeRatioByTex.getWidth() + ",y:"
			//					+ sizeRatioByTex.getHeight());
			getSize().setWidth(
					sizeRatioByTex.getWidth() * texture.getSize().getWidth());
			getSize().setHeight(
					sizeRatioByTex.getHeight() * texture.getSize().getHeight());
		}
	}

	@Override
	protected Size getSourceSize(RectF drawRect) {
		if (texture != null)
			return texture.getSize();
		else
			return null;
	}

	public boolean isSizeUpdatedByTex() {
		return sizeUpdatedByTex;
	}

	public void setSizeUpdatedByTex(boolean sizeUpdatedByTex) {
		this.sizeUpdatedByTex = sizeUpdatedByTex;
	}

	public void determineSizeRatioByTex() {
		if (texture == null || texture.getSize() == null
				|| texture.getSize().getWidth() == 0
				|| texture.getSize().getHeight() == 0)
			return;

		sizeRatioByTex.setWidth(getSize().getWidth()
				/ texture.getSize().getWidth());
		sizeRatioByTex.setHeight(getSize().getHeight()
				/ texture.getSize().getHeight());
	}

	@Override
	protected void onActivate() {
		super.onActivate();
		if (this.sizeUpdatedByTex && sizeRatioByTex.isEmpty())
			this.determineSizeRatioByTex();
	}

	protected void drawTexture(Canvas canvas, RectF rect, Paint paint) {
		paint.reset();
		processPaint(paint, rect);

		if(texture!=null)
		{
			paint.setShader(texture.getShader());
			canvas.drawVertices(Canvas.VertexMode.TRIANGLES, 8,
					texture.getCoords(), 0, texture.getCoords(), 0, null, 0,
					DEFAULT_INDICES, 0, 6, paint);
			//		Bitmap bitmap = texture.getBitmap();
			//		canvas.drawBitmap(bitmap, ORI_MATRIX, paint);
		}
	}

	protected void drawBorder(Canvas canvas, RectF rect, Paint paint) {
		paint.reset();
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(borderColor);
		paint.setStrokeWidth(borderWidth);
		float w = rect.width();
		float h = rect.height();
		Size size = this.getSourceSize(rect);
		if (size != null) {
			w = size.getWidth();
			h = size.getHeight();
		}
		processPaint(paint, rect);
		canvas.drawRect(0, 0, w, h, paint);
	}

	protected void drawFill(Canvas canvas, RectF rect, Paint paint) {
		paint.reset();
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(color);
		float w = rect.width();
		float h = rect.height();
		Size size = this.getSourceSize(rect);
		if (size != null) {
			w = size.getWidth();
			h = size.getHeight();
		}
		processPaint(paint, rect);
		canvas.drawRect(0, 0, w, h, paint);
	}

	@Override
	protected void onDrawNode(Canvas canvas, RectF rect, Matrix matrix,
			Point center, Paint paint) {
		Matrix mOld = canvas.getMatrix();
		canvas.setMatrix(matrix);

		if (drawColor) {
			drawFill(canvas, rect, paint);
		}

		if (texture != null) {
			drawTexture(canvas, rect, paint);
		}

		if (borderWidth > 0) {
			drawBorder(canvas, rect, paint);
		}

		canvas.setMatrix(mOld);
	}

	public void onDraw(Canvas canvas) {
		if (texture != null) {
			if (!texture.isLoaded()) {
				Director.getTexManager().loadTexture(texture);
			}
		}

		super.onDraw(canvas);
	}

	public void onUpdate(Time time) {
	}

}
