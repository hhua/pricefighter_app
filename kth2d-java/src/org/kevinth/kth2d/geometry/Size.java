package org.kevinth.kth2d.geometry;

/**
 * @author <a href="mailto:kevinth.fang@gmail.com">Kevinth</a>
 *
 */
public class Size {
	private float width;

	private float height;

	public Size() {
	}

	public Size(float width, float height) {
		this.width = width;
		this.height = height;
	}

	public Size(Size size) {
		this.width = size.width;
		this.height = size.height;
	}

	public float getWidth() {
		return width;
	}

	public Size setWidth(float width) {
		this.width = width;
		return this;
	}

	public float getHeight() {
		return height;
	}

	public Size setSize(Size size) {
		this.width = size.width;
		this.height = size.height;
		return this;
	}

	public Size setHeight(float height) {
		this.height = height;
		return this;
	}

	public Size set(float width, float height) {
		this.width = width;
		this.height = height;
		return this;
	}

	public Size scale(float ratio) {
		this.width *= ratio;
		this.height *= ratio;
		return this;
	}

	public float getWHRatio() {
		if (this.height == 0)
			return -1;
		else
			return this.width / this.height;
	}

	public Size set(Size size) {
		this.width = size.width;
		this.height = size.height;
		return this;
	}

	public boolean isEmpty() {
		if (this.width == 0 && this.height == 0)
			return true;
		return false;
	}

	public void reset() {
		this.width = 0;
		this.height = 0;
	}

	@Override
	public String toString() {
		return new StringBuffer().append("width:").append(width).append(
				", height:").append(height).toString();
	}

}
