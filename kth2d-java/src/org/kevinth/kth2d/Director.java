package org.kevinth.kth2d;

import org.kevinth.kth2d.debug.Debugger;
import org.kevinth.kth2d.event.EventManager;
import org.kevinth.kth2d.geometry.Size;
import org.kevinth.kth2d.render.CanvasView;
import org.kevinth.kth2d.render.GLSurfaceRenderer;
import org.kevinth.kth2d.texture.TexManager;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.SurfaceView;
import android.view.WindowManager;

/**
 * @author <a href="mailto:kevinth.fang@gmail.com">Kevinth</a>
 *
 */
public class Director {
	private static Context context = null;
	private static SurfaceView gameView = null;

	private static final GameConfig gameConfig = new GameConfig();
	private static boolean running = false;
	private static final Debugger debugger = new Debugger();
	private static final Time gameClock = new Time();
	private static TexManager texManager = null;
	private static TexManager globalTexManager = null;
	private static EventManager eventManager = null;
	private static final Size resolution = new Size();
	private static final Size gameViewSize = new Size();

	private static Scene currentScene;
	private static LoadingScene loadingScene;

	private static long lastFrameTime = 0l;
	private static long pauseTime = 0l;
	private static long resumeTime = 0l;

	private static void initActivity(Activity activity) {
		if (gameConfig.isFullScreen()) {
			activity.getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
			activity.getWindow().clearFlags(
					WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
			activity.getWindow().requestFeature(
					android.view.Window.FEATURE_NO_TITLE);
		}
		if (gameConfig.isLandscape()) {
			activity
					.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		} else {
			activity
					.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
	}

	private static void initMetrics(Activity activity) {
		int w = activity.getWindowManager().getDefaultDisplay().getWidth();
		int h = activity.getWindowManager().getDefaultDisplay().getHeight();
		if (gameConfig.isLandscape())
			resolution.set(h, w);
		else
			resolution.set(w, h);
	}

	public static void startEngine(Activity activity) {
		if (running)
			return;

		context = activity;

		initMetrics(activity);
		initActivity(activity);

		Log.d("startEngine", "start");
		texManager = new TexManager();
		globalTexManager = new TexManager();
		eventManager = new EventManager();
		if (gameConfig.getRendererType() == GameConfig.RENDERER_DEFAULT) {
			gameView = new CanvasView(activity);
		} else if (gameConfig.getRendererType() == GameConfig.RENDERER_OPENGL) {
			gameView = new GLSurfaceView(activity);
		}
		texManager.setContext(activity);
		globalTexManager.setContext(activity);
		activity.setContentView(gameView);

		running = true;
		lastFrameTime = System.currentTimeMillis();

		if (gameConfig.getRendererType() == GameConfig.RENDERER_DEFAULT) {
			((CanvasView) gameView).start();
		} else if (gameConfig.getRendererType() == GameConfig.RENDERER_OPENGL) {
			((GLSurfaceView) gameView).setRenderer(new GLSurfaceRenderer());
		}
	}

	public static void stopEngine() {
		endCurrentScene();
		if (gameConfig.getRendererType() == GameConfig.RENDERER_DEFAULT) {
			if (gameView != null)
				((CanvasView) gameView).stop();
		} else if (gameConfig.getRendererType() == GameConfig.RENDERER_OPENGL) {
			Canvas.freeGlCaches();
		}

		if (texManager != null)
			texManager.releaseAll();
		if (globalTexManager != null)
			globalTexManager.releaseAll();
		if (eventManager != null)
			eventManager.clearListeners();

	}

	public static Scene getCurrentScene() {
		return currentScene;
	}

	public static void endCurrentScene() {
		if (currentScene != null) {
			currentScene.destroy();
		}
		if (texManager != null)
			texManager.releaseAll();
		if (globalTexManager != null)
			globalTexManager.releaseAll();
		if (eventManager != null)
			eventManager.clearListeners();
	}

	public static void setCurrentScene(Scene scene) {
		endCurrentScene();
		currentScene = scene;
	}

	public static GameConfig getConfig() {
		return gameConfig;
	}

	public static Debugger getDebugger() {
		return debugger;
	}

	public static TexManager getTexManager() {
		return texManager;
	}

	public static TexManager getGlobalTexManager() {
		return globalTexManager;
	}

	public static boolean isRunning() {
		return running;
	}

	public static Time getGameClock() {
		return gameClock;
	}

	public static Context getContext() {
		return context;
	}

	public static Size getResolution() {
		return resolution;
	}

	public static void setResolution(float width, float height) {
		resolution.set(width, height);
	}

	public static Size getGameViewSize() {
		return gameViewSize;
	}

	public static void setGameViewSize(float width, float height) {
		gameViewSize.set(width, height);
	}

	public static boolean isFullResolution() {
		if (gameViewSize.getWidth() == resolution.getWidth()
				&& gameViewSize.getHeight() == resolution.getHeight())
			return true;
		else
			return false;
	}

	public static float gameViewToDisplayX(float x) {
		float rw = Director.getResolution().getWidth();
		float sw = gameViewSize.getWidth();
		return x * rw / sw;
	}

	public static float displayToGameViewX(float x) {
		float rw = Director.getResolution().getWidth();
		float sw = gameViewSize.getWidth();
		return x * sw / rw;
	}

	public static float gameViewToDisplayY(float y) {
		float rh = Director.getResolution().getHeight();
		float sh = gameViewSize.getHeight();
		return y * rh / sh;
	}

	public static float displayToGameViewY(float y) {
		float rh = Director.getResolution().getHeight();
		float sh = gameViewSize.getHeight();
		return y * sh / rh;
	}

	public static void pause() {
		running = false;
		long time = System.currentTimeMillis();
		pauseTime = time;
		if (gameConfig.getRendererType() == GameConfig.RENDERER_DEFAULT) {
			((CanvasView) gameView).stop();
		}
	}

	public static void resume() {
		running = true;
		long time = System.currentTimeMillis();
		resumeTime = time;
//		eventManager.initialize();//I'm here
		if (gameConfig.getRendererType() == GameConfig.RENDERER_DEFAULT) {
			((CanvasView) gameView).start();
		}
	}

	public static EventManager getEventManager() {
		return eventManager;
	}

	private static void updateTime() {
		long currentTime = System.currentTimeMillis();
		long inc = 0;

		if (pauseTime > 0 && resumeTime > 0) {
			inc = (currentTime - resumeTime) + (pauseTime - lastFrameTime);
			pauseTime = 0;
			resumeTime = 0;
		} else {
			inc = currentTime - lastFrameTime;
		}
		lastFrameTime = currentTime;

		if (inc <= 0)
			inc = 0;

		if (gameClock.getTimeFactor() != 1) {
			inc = (long) ((double) inc / gameClock.getTimeFactor());
		}

		long currentTick = gameClock.getCurrentTicks();
		gameClock.setLastTicks(currentTick);
		gameClock.setCurrentTicks(currentTick + inc);
	}

	public static void onFrame(Canvas canvas) {
		if (running) {
			updateTime();
		}

		if (currentScene != null) {
			if (debugger.isEnableFPS())
				debugger.fpsOnFrame(gameClock);
			if (currentScene.isLoaded()) {
				eventManager.handleInteractiveEvents();
				currentScene.onUpdate(gameClock);
				currentScene.onDraw(canvas);
			} else {
				currentScene.load(loadingScene);
				Size viewSize = Director.getGameViewSize();
				if (viewSize.getWidth() == 0 || viewSize.getHeight() == 0) {
					viewSize.set(Director.getResolution());
				}

				currentScene.onActivate();
			}
		}
	}
	
	public static void setBackground(Drawable d)
	{
		if(gameView!=null)
			gameView.setBackgroundDrawable(d);
	}
}
