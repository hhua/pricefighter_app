package org.kevinth.kth2d.action;

import java.util.Map;

import org.kevinth.kth2d.Sprite;
import org.kevinth.kth2d.Time;
import org.kevinth.kth2d.texture.TexGroup;
import org.kevinth.kth2d.texture.Texture;

import android.util.Log;

/**
 * @author <a href="mailto:kevinth.fang@gmail.com">Kevinth</a>
 *
 */
public class Animate extends BasicAction {
	public static final String ATTR_FRAME_INDEX = "frame-index";
	public static final String ATTR_FRAME_WAITED_TIME = "frame-waited-time";

	private long frameDuration = 40;
	private Map<Integer, Long> durationMap = null;
	private TexGroup texAtlas = null;

	public void recycle() {
		frameDuration = 40;
		durationMap = null;
		texAtlas = null;

		getSprite().removeAttribute(ATTR_FRAME_INDEX);
		getSprite().removeAttribute(ATTR_FRAME_WAITED_TIME);
		super.recycle();
	}

	public Animate() {
	}

	public Animate(TexGroup texAtlas) {
		this.texAtlas = texAtlas;
	}

	public Animate(TexGroup texAtlas, long frameDuration) {
		this.frameDuration = frameDuration;
		this.texAtlas = texAtlas;
	}

	public float getFrameDuration() {
		return frameDuration;
	}

	public Animate setFrameDuration(long frameDuration) {
		this.frameDuration = frameDuration;
		return this;
	}

	public TexGroup getTexAtlas() {
		return texAtlas;
	}

	public Animate setTexAtlas(TexGroup texAtlas) {
		this.texAtlas = texAtlas;
		return this;
	}

	public Map<Integer, Long> getDurationMap() {
		return durationMap;
	}

	public Animate setDurationMap(Map<Integer, Long> durationMap) {
		this.durationMap = durationMap;
		return this;
	}

	@Override
	public void onUpdate(Time time) {
		Sprite node = getSprite();
		int frameIndex = (Integer) node.getAttribute(ATTR_FRAME_INDEX);
		long waitedTime = (Long) node.getAttribute(ATTR_FRAME_WAITED_TIME);
		if (waitedTime == 0) {
			Texture tex = texAtlas.getTexture(frameIndex);
			node.setTexture(tex);
		}

		long dur = this.frameDuration;
		if (durationMap != null) {
			Long durObj = durationMap.get(frameIndex);
			if (durObj != null)
				dur = durObj;
		}

		waitedTime += time.getCurrentTicks() - time.getLastTicks();
		if (waitedTime > dur) {
			waitedTime = 0;
			frameIndex++;
			if (frameIndex >= texAtlas.getTextureCount()) {
				this.done();
				return;
			}
		}

		node.setAttribute(ATTR_FRAME_INDEX, frameIndex);
		node.setAttribute(ATTR_FRAME_WAITED_TIME, waitedTime);
	}

	@Override
	protected void onStart() {
		super.onStart();

		Integer frameIndex = 0;
		Long waitedTime = 0l;
		getSprite().setAttribute(ATTR_FRAME_INDEX, frameIndex);
		getSprite().setAttribute(ATTR_FRAME_WAITED_TIME, waitedTime);
	}

	@Override
	public String toString() {
		return this.getClass().getName() + " - frameDuration:" + frameDuration
				+ ", " + texAtlas.toString();
	}

}
