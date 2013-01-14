package org.kevinth.kth2d.event;

import org.kevinth.kth2d.Director;
import org.kevinth.kth2d.IRecyclable;
import org.kevinth.kth2d.ObjectPool;

import android.view.MotionEvent;

/**
 * @author <a href="mailto:kevinth.fang@gmail.com">Kevinth</a>
 *
 */
public class TouchEvent implements IRecyclable {
	private static final ObjectPool<TouchEvent> eventPool = new ObjectPool<TouchEvent>(
			10);

	private MotionEvent motionEvent;

	public static TouchEvent obtain(MotionEvent event) {
		TouchEvent ret = eventPool.obtain();
		if (ret == null)
			ret = new TouchEvent(event);
		else
			ret.setMotionEvent(event);
		return ret;
	}

	public void recycle() {
		if (motionEvent != null)
			motionEvent.recycle();
		motionEvent = null;
		eventPool.recycle(this);
	}

	public MotionEvent getMotionEvent() {
		return motionEvent;
	}

	public void setMotionEvent(MotionEvent motionEvent) {
		this.motionEvent = motionEvent;
	}

	private TouchEvent(MotionEvent event) {
		this.motionEvent = event;
	}

	public int getAction() {
		return motionEvent.getAction();
	}

	public long getDownTime() {
		return motionEvent.getDownTime();
	}

	public long getEventTime() {
		return motionEvent.getEventTime();
	}

	public float getX() {
		return Director.isFullResolution() ? motionEvent.getX() : Director
				.displayToGameViewX(motionEvent.getX());
	}

	public float getY() {
		return Director.isFullResolution() ? motionEvent.getY() : Director
				.displayToGameViewY(motionEvent.getY());
	}

	public float getPressure() {
		return motionEvent.getPressure();
	}

	public float getSize() {
		return motionEvent.getSize();
	}

	public int getPointerCount() {
		return motionEvent.getPointerCount();
	}

	public int getPointerId(int pointerIndex) {
		return motionEvent.getPointerId(pointerIndex);
	}

	public int findPointerIndex(int pointerId) {
		return motionEvent.findPointerIndex(pointerId);
	}

	public float getX(int pointerIndex) {
		return Director.isFullResolution() ? motionEvent.getX(pointerIndex)
				: Director.displayToGameViewX(motionEvent.getX(pointerIndex));
	}

	public float getY(int pointerIndex) {
		return Director.isFullResolution() ? motionEvent.getY(pointerIndex)
				: Director.displayToGameViewY(motionEvent.getY(pointerIndex));

	}

	public float getPressure(int pointerIndex) {
		return motionEvent.getPressure(pointerIndex);
	}

	public float getSize(int pointerIndex) {
		return motionEvent.getSize(pointerIndex);
	}

	public int getMetaState() {
		return motionEvent.getMetaState();
	}

	public float getRawX() {
		return Director.isFullResolution() ? motionEvent.getRawX() : Director
				.displayToGameViewX(motionEvent.getRawX());
	}

	public float getRawY() {
		return Director.isFullResolution() ? motionEvent.getRawY() : Director
				.displayToGameViewY(motionEvent.getRawY());
	}

	public float getXPrecision() {
		return motionEvent.getXPrecision();
	}

	public float getYPrecision() {
		return motionEvent.getYPrecision();
	}

	public int getHistorySize() {
		return motionEvent.getHistorySize();
	}

	public long getHistoricalEventTime(int pos) {
		return motionEvent.getHistoricalEventTime(pos);
	}

	public float getHistoricalX(int pos) {
		return Director.isFullResolution() ? motionEvent.getHistoricalX(pos)
				: Director.displayToGameViewX(motionEvent.getHistoricalX(pos));
	}

	public float getHistoricalY(int pos) {
		return Director.isFullResolution() ? motionEvent.getHistoricalY(pos)
				: Director.displayToGameViewY(motionEvent.getHistoricalY(pos));
	}

	public float getHistoricalPressure(int pos) {
		return motionEvent.getHistoricalPressure(pos);
	}

	public float getHistoricalSize(int pos) {
		return motionEvent.getHistoricalSize(pos);
	}

	public float getHistoricalX(int pointerIndex, int pos) {
		return Director.isFullResolution() ? motionEvent.getHistoricalX(
				pointerIndex, pos) : Director.displayToGameViewX(motionEvent
				.getHistoricalX(pointerIndex, pos));
	}

	public float getHistoricalY(int pointerIndex, int pos) {
		return Director.isFullResolution() ? motionEvent.getHistoricalY(
				pointerIndex, pos) : Director.displayToGameViewY(motionEvent
				.getHistoricalY(pointerIndex, pos));
	}

	public float getHistoricalPressure(int pointerIndex, int pos) {
		return motionEvent.getHistoricalPressure(pointerIndex, pos);
	}

	public float getHistoricalSize(int pointerIndex, int pos) {
		return motionEvent.getHistoricalSize(pointerIndex, pos);
	}

	public int getDeviceId() {
		return motionEvent.getDeviceId();
	}

	public int getEdgeFlags() {
		return motionEvent.getEdgeFlags();
	}
}
