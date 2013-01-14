package org.kevinth.kth2d.texture;

import org.kevinth.kth2d.geometry.Point;
import org.kevinth.kth2d.geometry.Size;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Shader;

/**
 * @author <a href="mailto:kevinth.fang@gmail.com">Kevinth</a>
 *
 */
public class Texture {
	private Object resIndex;
	private Point lefTop; //new一个
	private Size size; //bitmap 的大小
	private boolean loaded;  
	private Bitmap bitmap = null; //设，射完了调onLoadTexture
	private BitmapShader shader = null;
	private float[] coords = null;

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public Texture(Object resIndex) {
		this.resIndex = resIndex;
	}

	public Object getResIndex() {
		return resIndex;
	}

	public void setResIndex(Object resIndex) {
		this.resIndex = resIndex;
	}

	public Point getLefTop() {
		return lefTop;
	}

	public void setLefTop(Point lefTop) {
		this.lefTop = lefTop;
	}

	public Size getSize() {
		return size;
	}

	public void setSize(Size size) {
		this.size = size;
	}

	public boolean isLoaded() {
		return loaded;
	}

	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}

	public BitmapShader getShader() {
		return shader;
	}

	public float[] getCoords() {
		return coords;
	}

	public void onLoadTexture() {
		if (bitmap != null) {
			shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP,
					Shader.TileMode.CLAMP);
			coords = new float[] { 0, 0, size.getWidth(), 0, size.getWidth(),
					size.getHeight(), 0, size.getHeight() };
		}
		setLoaded(true);
	}

	public void onRecycle() {
		if (getBitmap() != null && !getBitmap().isRecycled())
			getBitmap().recycle();
		setLoaded(false);
	}

	@Override
	public String toString() {
		return new StringBuffer().append("Texture: ").append(resIndex)
				.toString();
	}
}