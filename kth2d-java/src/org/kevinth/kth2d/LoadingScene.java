package org.kevinth.kth2d;

/**
 * @author <a href="mailto:kevinth.fang@gmail.com">Kevinth</a>
 *
 */
public abstract class LoadingScene extends Scene implements Loading {
	private int percentage;

	public int getCurrent() {
		return percentage;
	}

	public void step(int percentage) {
		this.percentage = percentage;
		this.onStep(percentage);
	}

	protected abstract void onStep(int percentage);
}
