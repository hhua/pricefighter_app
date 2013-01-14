package org.kevinth.kth2d.geometry;

/**
 * @author <a href="mailto:kevinth.fang@gmail.com">Kevinth</a>
 *
 */
public class Anchor extends Point {
	public static final Anchor TOP_LEFT = new Anchor(0, 0);
	public static final Anchor BOTTOM_LEFT = new Anchor(0, 1);
	public static final Anchor TOP_RIGHT = new Anchor(1, 0);
	public static final Anchor BOTTOM_RIGHT = new Anchor(1, 1);
	public static final Anchor CENTER = new Anchor(0.5f, 0.5f);
	public static final Anchor TOP_CENTER = new Anchor(0.5f, 0);
	public static final Anchor BOTTOM_CENTER = new Anchor(0.5f, 1);
	public static final Anchor LEFT_CENTER = new Anchor(0, 0.5f);
	public static final Anchor RIGHT_CENTER = new Anchor(1, 0.5f);

	public Anchor(float x, float y) {
		super(x, y);
	}
}
