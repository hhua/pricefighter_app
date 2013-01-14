package org.kevinth.kth2d;

import org.kevinth.kth2d.event.TouchEvent;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * @author <a href="mailto:kevinth.fang@gmail.com">Kevinth</a>
 *
 */
public abstract class GameActivity extends Activity {

	protected abstract void onInit();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.onInit();
		Director.startEngine(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		Director.pause();
		Log.d("activity pause", "pause");
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d("activity resume", "resume");
		Director.resume();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Director.getEventManager().fireKeyEvent(event);
		//		return super.onKeyDown(keyCode, event);
		return true;
	}

	@Override
	public boolean onKeyLongPress(int keyCode, KeyEvent event) {
		Director.getEventManager().fireKeyEvent(event);
		//return super.onKeyLongPress(keyCode, event);
		return true;
	}

	@Override
	public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
		Director.getEventManager().fireKeyEvent(event);
		//return super.onKeyMultiple(keyCode, repeatCount, event);
		return true;
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		Director.getEventManager().fireKeyEvent(event);
		//return super.onKeyUp(keyCode, event);
		Log.d("onKeyUp" , "onKeyUp");
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		MotionEvent e = MotionEvent.obtain(event);
		//		Log.d("raw pointer ", "x:" + e.getRawX() + ", y:" + e.getRawY());
		//		for (int i = 0; i < e.getPointerCount(); i++) {
		//			Log.d("pointer " + i, "x:" + e.getX(i) + ", y:" + e.getY(i));
		//			Log.d("size " + i, ":" + e.getSize(i));
		//		}
		TouchEvent te = TouchEvent.obtain(e);
		Director.getEventManager().fireTouchEvent(te);
		return super.onTouchEvent(event);
	}
}
