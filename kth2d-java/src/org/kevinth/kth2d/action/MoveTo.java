package org.kevinth.kth2d.action;

import org.kevinth.kth2d.Sprite;
import org.kevinth.kth2d.Time;
import org.kevinth.kth2d.geometry.Point;
import org.kevinth.kth2d.geometry.Vector;

/**
 * @author <a href="mailto:kevinth.fang@gmail.com">Kevinth</a>
 *
 */
public class MoveTo extends Move {
	public static final float DEFAULT_VELOCITY = 100;

	private final Point target = new Point();
	private float velocity = -1;
	private long time = -1;

	private long pastTime = -1;
	private final Point runTarget = new Point();

	public void recycle() {
		target.reset();
		velocity = -1;
		time = -1;

		super.recycle();
	}

	public MoveTo() {
	}

	public MoveTo(Point target, float velocity) {
		this.target.set(target);
		this.velocity = velocity;
	}

	public MoveTo(Point target, long time) {
		this.target.set(target);
		this.time = time;
	}

	public Point getTarget() {
		return target;
	}

	public void setTarget(float x, float y) {
		target.set(x, y);
	}

	public float getVelocity() {
		return velocity;
	}

	public MoveTo setVelocity(float velocity) {
		this.velocity = velocity;
		return this;
	}

	public long getTime() {
		return time;
	}

	public MoveTo setTime(long time) {
		this.time = time;
		return this;
	}

	@Override
	public void onStart() {
		super.onStart();

		pastTime = 0;
		runTarget.set(target);
		calAttrs();
	}

	private void calAttrs() {
		float velocityRun = 0;
		if (time > 0)
			velocityRun = runTarget.distance(getSprite().getPosition())
					/ (time - pastTime) * 1000;
		else if (velocity > 0)
			velocityRun = velocity;
		Sprite node = getSprite();
		node.setAttribute(Move.ATTR_VELOCITY, velocityRun);

		float dX = this.runTarget.getX() - node.getPosition().getX();
		float dY = this.runTarget.getY() - node.getPosition().getY();
		Vector v = (Vector) node.getAttribute(Move.ATTR_DIRECTION);
		if (v == null) {
			v = new Vector();
			node.setAttribute(Move.ATTR_DIRECTION, v);
		}
		v.set(dX, dY);
	}

	@Override
	public void onUpdate(Time time) {
		Sprite node = getSprite();
		if (this.time > 0) {
			long interval = time.getCurrentTicks() - time.getLastTicks();
			pastTime += interval;
			if (pastTime > this.time) {
				this.done();
				node.getPosition().set(runTarget);
				return;
			}
		}
		if (!this.target.equals(this.runTarget)) {
			runTarget.set(target);
			calAttrs();
		}

		float dX = this.runTarget.getX() - node.getPosition().getX();
		float dY = this.runTarget.getY() - node.getPosition().getY();
		float d = (float) Math.sqrt(dX * dX + dY * dY);
		float m = this.calDistance(time);
		if (m >= d) {
			node.getPosition().set(runTarget);
			this.done();
		} else {
			this.moveDistance(m);
		}
	}

}
