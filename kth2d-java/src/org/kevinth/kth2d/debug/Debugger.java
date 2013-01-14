package org.kevinth.kth2d.debug;

import org.kevinth.kth2d.Time;

import android.util.Log;

/**
 * @author <a href="mailto:kevinth.fang@gmail.com">Kevinth</a>
 *
 */
public class Debugger {
	private boolean enabled = false;

	private boolean forceNoTex = false;
	private boolean forceDrawBorder = false;
	private boolean forceDrawColor = false;
	private boolean enableFPS = false;

	private final FPSCounter fpsCounter = new FPSCounter();

	public void enable() {
		enabled = true;
	}

	public void disable() {
		enabled = false;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public boolean isForceNoTex() {
		return forceNoTex;
	}

	public void setForceNoTex(boolean forceNoTex) {
		this.forceNoTex = forceNoTex;
	}

	public boolean isForceDrawBorder() {
		return forceDrawBorder;
	}

	public void setForceDrawBorder(boolean forceDrawBorder) {
		this.forceDrawBorder = forceDrawBorder;
	}

	public boolean isForceDrawColor() {
		return forceDrawColor;
	}

	public void setForceDrawColor(boolean forceDrawColor) {
		this.forceDrawColor = forceDrawColor;
	}

	public void fpsOnFrame(Time time) {
		this.fpsCounter.onFrame(time);
	}

	public int getFPS() {
		return this.fpsCounter.getFPS();
	}

	public boolean isEnableFPS() {
		return enableFPS;
	}

	public void setEnableFPS(boolean enableFPS) {
		this.enableFPS = enableFPS;
	}

	private class FPSCounter {

		private static final int FPS_AVG = 3;

		private int frameCount = 0;
		private long timeTotal = 0;
		private float lastFPS = 0;

		protected void onFrame(Time time) {
			timeTotal += time.getInterval();
			frameCount++;
			if (timeTotal >= 1000 * FPS_AVG) {
				lastFPS = (float) frameCount / (float) FPS_AVG;
				Log.d("FPS", "" + getFPS());
				frameCount = 0;
				timeTotal = 0;
			}
		}

		public int getFPS() {
			return (int) (lastFPS + 0.5);
		}
	}
}
