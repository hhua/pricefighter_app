package org.kevinth.kth2d.render;

import org.kevinth.kth2d.Director;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * @author <a href="mailto:kevinth.fang@gmail.com">Kevinth</a>
 *
 */
public class CanvasView extends SurfaceView implements SurfaceHolder.Callback {
	public final static long GAME_LOOP_SLEEP = 10;
	public final static String CANVAS_THREAD_NAME = "kth2d-canvas";

	private CanvasRunnable runnable = null;
	private Thread thread = null;
	private boolean created = false;
	private boolean changed = false;

	public CanvasView(Context context) {
		super(context);
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);

		runnable = new CanvasRunnable(holder);
		setFocusable(true);
	}

	public CanvasView(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Log.d("canvas size", "w :" + width + ", h:" + height);
		Director.getResolution().set(width, height);
		changed = true;
	}

	public synchronized void start() {
		runnable.setRunning(true);
		thread = new Thread(runnable);
		thread.start();
	}

	public synchronized void stop() {
		runnable.setRunning(false);
	}

	public void surfaceCreated(SurfaceHolder holder) {
		created = true;
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		created = false;
		changed = false;
	}

	class CanvasRunnable implements Runnable {
		private boolean running = false;
		private SurfaceHolder holder = null;

		public CanvasRunnable(SurfaceHolder holder) {
			this.holder = holder;
		}

		public boolean isRunning() {
			return running;
		}

		public void setRunning(boolean running) {
			this.running = running;
		}

		public void run() {
			while (true) {
				synchronized (CanvasView.this) {
					if (running) {
						if (created && changed) {
							Canvas c = null;
							try {
								c = holder.lockCanvas(null);
								Director.onFrame(c);
							} finally {
								if (c != null) {
									holder.unlockCanvasAndPost(c);
								}
							}
						}
					} else {
						break;
					}
				}

				try {
					Thread.sleep(GAME_LOOP_SLEEP);
				} catch (InterruptedException e) {
				}
			}

		}

	}

}
