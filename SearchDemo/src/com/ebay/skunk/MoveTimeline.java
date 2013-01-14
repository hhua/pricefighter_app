package com.ebay.skunk;

import org.kevinth.kth2d.Director;
import org.kevinth.kth2d.Time;
import org.kevinth.kth2d.geometry.Point;
import org.kevinth.kth2d.geometry.Size;

public class MoveTimeline {
	public static final float ACC_MOVE = -80;

	private boolean clockwise = true;
	private float velocity = -1;
	private float angle = 0;
	private float intervalAngle = 0;
	private final Point pathOri = new Point();
	private final Size pathSize = new Size();
	private boolean isAcc = false;
	private long pastTime = 0;
	private boolean indexIncremental = true;

	public MoveTimeline() {
	}

	public Point getPathOri() {
		return pathOri;
	}

	public void setPathOri(Point pathOri) {
		this.pathOri.set(pathOri);
	}

	public Size getPathSize() {
		return pathSize;
	}

	public void setPathSize(Size pathSize) {
		this.pathSize.set(pathSize);
	}

	public boolean isClockwise() {
		return clockwise;
	}

	public void setClockwise(boolean clockwise) {
		this.clockwise = clockwise;
	}

	public float getVelocity() {
		return velocity;
	}

	public void setVelocity(float velocity) {
		this.velocity = velocity;
	}

	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

	public float getIntervalAngle() {
		return intervalAngle;
	}

	public void setIntervalAngle(float intervalAngle) {
		this.intervalAngle = intervalAngle;
	}

	public boolean isAcc() {
		return isAcc;
	}

	public boolean isIndexIncremental() {
		return indexIncremental;
	}

	public void setIndexIncremental(boolean indexIncremental) {
		this.indexIncremental = indexIncremental;
	}

	public float mappingYToPlaneInMath(float y) {
		float h = Director.getGameViewSize().getHeight();

		float bym = h - pathOri.getY() - pathSize.getHeight() / 2;
		float pym = h - y;
		return bym + (pym - bym) * pathSize.getWHRatio();
	}

	public float mappingYToSolidInDraw(float y) {
		float h = Director.getGameViewSize().getHeight();
		float bym = h - pathOri.getY() - pathSize.getHeight() / 2;
		float rym = bym + (y - bym) / pathSize.getWHRatio();
		return h - rym;
	}

	protected void setupPosition(Point position, float angle) {
		float oxm = pathOri.getX();
		float oym = mappingYToPlaneInMath(pathOri.getY());
		float r = pathSize.getWidth() / 2;
		float txm = oxm + r * (float) Math.cos(Math.toRadians(angle));
		float tym = oym + r * (float) Math.sin(Math.toRadians(angle));
		position.set(txm, mappingYToSolidInDraw(tym));
	}

	public void setupPosition(Point position) {
		setupPosition(position, angle);
	}

	public void setupPosition(Point position, int index) {
		if (indexIncremental)
			setupPosition(position, angle + intervalAngle * index);
		else
			setupPosition(position, angle - intervalAngle * index);
	}

	public float move(float dist, boolean clockwise) {
		float radius = pathSize.getWidth() / 2;

		//Log.d("m" , new Float(m).toString());
		float dDelta = (float) Math.toDegrees(Math.asin(dist / (2 * radius))) * 2;

		//Log.d("dDelta" , new Float(m).toString());
		if (clockwise)
			angle -= dDelta;
		else
			angle += dDelta;

		//Log.d("degree", new Float(degree).toString());
		return dDelta;
	}

	public void startAcc(float velocity, boolean clockwise) {
		pastTime = 0;
		this.velocity = velocity;
		this.clockwise = clockwise;
		this.isAcc = true;
	}

	public void stopAcc() {
		this.isAcc = false;
	}

	public void onUpdate(Time time) {
		if (this.isAcc) {
			long interval = time.getCurrentTicks() - time.getLastTicks();
			pastTime += interval;
			velocity += ACC_MOVE * pastTime / 1000;
			if (velocity <= 0) {
				this.isAcc = false;
			}

			float m = velocity * interval / 1000;
			move(m, clockwise);
		}
		//		if (getSprite() instanceof TimeLineSprite) {
		//			TimeLineSprite node = (TimeLineSprite) getSprite();
		//
		//			Float v = (Float) node.getAttribute(Move.ATTR_VELOCITY);
		//			if (v <= 0) {
		//				this.done();
		//				return;
		//			}
		//
		//			float m = this.calDistance(time);
		//
		//			float mDelta = 0;
		//			if (totalAngle > 0) {
		//				mDelta = totalAngle - pastAngle;
		//			}
		//
		//			pastAngle += node.moveOnTimeLine(m, clockwise, mDelta);
		//
		//			if (totalAngle > 0 && pastAngle > totalAngle) {
		//				this.done();
		//			}
		//		} else
		//			this.done();

	}
}
