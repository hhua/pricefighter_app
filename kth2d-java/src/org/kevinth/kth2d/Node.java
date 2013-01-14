package org.kevinth.kth2d;

import org.kevinth.kth2d.geometry.Anchor;
import org.kevinth.kth2d.geometry.Point;
import org.kevinth.kth2d.geometry.Size;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * @author <a href="mailto:kevinth.fang@gmail.com">Kevinth</a>
 *
 */
public abstract class Node implements IUpdatable, IDrawable {
	private static final int DEFAULT_POOL_SIZE = 10;

	private boolean activated = false;

	Scene scene = null;
	Layer layer = null;

	//in Cartesian coordinates, left top corner as 0, 0
	protected final Point position = new Point();

	protected final Size size = new Size();

	//actural degrees, could be -180 to 180
	protected float drawAngle = 0;

	//from 0 to 1, 0 as transparent
	protected float alpha = 1;

	//frome 0 to 1 , left top corner as 0
	protected final Anchor anchor = new Anchor(0f, 0f);

	//scale ratio, used for scaling
	protected float scaleRatio = 1;

	//true to be a left to right flip; 
	protected boolean flipLeftRight = false;

	//true to be a up to down flip;
	protected boolean flipUpDown = false;

	private static final ObjectPool<RectF> rectPool = new ObjectPool<RectF>(
			DEFAULT_POOL_SIZE);
	private static final ObjectPool<Paint> paintPool = new ObjectPool<Paint>(
			DEFAULT_POOL_SIZE);
	private static final ObjectPool<Matrix> matrixPool = new ObjectPool<Matrix>(
			DEFAULT_POOL_SIZE);
	private static final ObjectPool<Size> sizePool = new ObjectPool<Size>(
			DEFAULT_POOL_SIZE);
	private static final ObjectPool<Point> pointPool = new ObjectPool<Point>(
			DEFAULT_POOL_SIZE);

	protected void onAdd(Scene scene, Layer layer) {
	}

	protected void onRemove(Scene scene, Layer layer) {
	}

	protected void onActivate() {
	}

	protected void onInactivate() {
	}

	protected void onDestroy() {
	}

	public boolean isActivated() {
		return activated;
	}

	public void activate() {
		activated = true;
		this.onActivate();
	}

	public void inactivate() {
		this.onInactivate();
		activated = false;
	}

	public void destroy() {
		this.onDestroy();
	}

	public Point getPosition() {
		return position;
	}

	public void setPosition(float x, float y) {
		this.position.set(x, y);
	}

	public Size getSize() {
		return size;
	}

	public void setSize(float width, float height) {
		this.size.set(width, height);
	}

	public float getAlpha() {
		return alpha;
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}

	public Anchor getAnchor() {
		return anchor;
	}

	public void setAnchor(float x, float y) {
		this.anchor.set(x, y);
	}

	public float getScaleRatio() {
		return scaleRatio;
	}

	public void setScaleRatio(float scaleRatio) {
		this.scaleRatio = scaleRatio;
	}

	public boolean isFlipLeftRight() {
		return flipLeftRight;
	}

	public void setFlipLeftRight(boolean flipLeftRight) {
		this.flipLeftRight = flipLeftRight;
	}

	public boolean isFlipUpDown() {
		return flipUpDown;
	}

	public void setFlipUpDown(boolean flipUpDown) {
		this.flipUpDown = flipUpDown;
	}

	protected void processPaint(Paint paint, RectF drawArea) {
		if (alpha != 1)
			paint.setAlpha((int) (alpha * 255 + 0.5));
	}

	public void processDrawArea(RectF rect) {
		float apx = getPosition().getX();
		float apy = getPosition().getY();
		float w = getSize().getWidth() * scaleRatio;
		float h = getSize().getHeight() * scaleRatio;
		float dpx = apx - w * anchor.getX();
		float dpy = apy - h * anchor.getY();

		//coordinate conversion
		if (!Director.isFullResolution()) {
			dpx = Director.gameViewToDisplayX(dpx);
			dpy = Director.gameViewToDisplayY(dpy);
			w = Director.gameViewToDisplayX(w);
			h = Director.gameViewToDisplayY(h);
		}

		rect.set(dpx, dpy, (dpx + w), (dpy + h));
	}

	protected Size getSourceSize(RectF drawRect) {
		return null;
	}

	protected void processMatrixForFlip(Matrix matrix, RectF drawRect,
			Size sourceSize) {
		if (flipLeftRight && !flipUpDown) {
			matrix.postScale(-1, 1);
			matrix.postTranslate(sourceSize.getWidth(), 0);
		} else if (!flipLeftRight && flipUpDown) {
			matrix.postScale(1, -1);
			matrix.postTranslate(0, sourceSize.getHeight());
		} else if (flipLeftRight && flipUpDown) {
			matrix.postScale(-1, -1);
			matrix.postTranslate(sourceSize.getWidth(), sourceSize.getHeight());
		}
	}

	protected void processMatrixForPosAndScale(Matrix matrix, RectF drawRect,
			Size sourceSize, Point center) {
		float tw = sourceSize.getWidth();
		float th = sourceSize.getHeight();

		float sx = drawRect.width() / tw;
		float sy = drawRect.height() / th;

		float apx = center.getX();
		float apy = center.getY();

		matrix.postTranslate(apx, apy);
		matrix.postScale(sx, sy, apx, apy);

		matrix.postTranslate(drawRect.left - apx, drawRect.top - apy);
	}

	protected void processMatrixForAngle(Matrix matrix, RectF drawRect,
			Size sourceSize, Point center) {
		if (getDrawAngle() != 0) {
			matrix.postRotate(getDrawAngle(), center.getX(), center.getY());
		}
	}

	protected void processMatrix(Matrix matrix, RectF drawRect,
			Size sourceSize, Point center) {
		processMatrixForFlip(matrix, drawRect, sourceSize);
		processMatrixForPosAndScale(matrix, drawRect, sourceSize, center);
		processMatrixForAngle(matrix, drawRect, sourceSize, center);
	}

	public float getDrawAngle() {
		return drawAngle;
	}

	public void setDrawAngle(float drawAngle) {
		this.drawAngle = drawAngle;
	}

	public Scene getScene() {
		return scene;
	}

	public Layer getLayer() {
		return layer;
	}

	protected void onDrawNode(Canvas canvas, RectF rect, Matrix matrix,
			Point center, Paint paint) {
	}

	public void onDraw(Canvas canvas) {
		RectF rect = rectPool.obtain();
		if (rect == null)
			rect = new RectF();
		this.processDrawArea(rect);

		Paint paint = paintPool.obtain();
		if (paint == null)
			paint = new Paint();
		paint.reset();

		processPaint(paint, rect);

		Matrix matrix = matrixPool.obtain();
		if (matrix == null)
			matrix = new Matrix();
		matrix.reset();

		boolean useSize = false;
		Size sourceSize = getSourceSize(rect);
		if (sourceSize == null) {
			sourceSize = sizePool.obtain();
			if (sourceSize == null)
				sourceSize = new Size();
			sourceSize.reset();
			sourceSize.setWidth(rect.width());
			sourceSize.setHeight(rect.height());
			useSize = true;
		}

		float apx = getPosition().getX();
		float apy = getPosition().getY();

		//coordinate conversion
		if (!Director.isFullResolution()) {
			apx = Director.gameViewToDisplayX(apx);
			apy = Director.gameViewToDisplayY(apy);
		}

		Point center = pointPool.obtain();
		if (center == null)
			center = new Point();
		center.reset();
		center.set(apx, apy);

		processMatrix(matrix, rect, sourceSize, center);

		onDrawNode(canvas, rect, matrix, center, paint);
		matrixPool.recycle(matrix);
		rectPool.recycle(rect);
		paintPool.recycle(paint);
		pointPool.recycle(center);
		if (useSize) {
			sizePool.recycle(sourceSize);
		}
	}
}
