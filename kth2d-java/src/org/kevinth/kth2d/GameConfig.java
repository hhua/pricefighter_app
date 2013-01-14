package org.kevinth.kth2d;

/**
 * @author <a href="mailto:kevinth.fang@gmail.com">Kevinth</a>
 *
 */
public class GameConfig {
	public static final int RENDERER_DEFAULT = 1;
	public static final int RENDERER_OPENGL = 2;

	private boolean fullScreen = true;
	private boolean landscape = true;
	private int rendererType = RENDERER_OPENGL;

	public boolean isFullScreen() {
		return fullScreen;
	}

	public void setFullScreen(boolean fullScreen) {
		this.fullScreen = fullScreen;
	}

	public boolean isLandscape() {
		return landscape;
	}

	public void setLandscape(boolean landscape) {
		this.landscape = landscape;
	}

	public int getRendererType() {
		return rendererType;
	}

	public void setRendererType(int rendererType) {
		this.rendererType = rendererType;
	}
}
