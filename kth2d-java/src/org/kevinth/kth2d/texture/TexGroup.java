package org.kevinth.kth2d.texture;

import android.content.Context;

/**
 * @author <a href="mailto:kevinth.fang@gmail.com">Kevinth</a>
 *
 */
public abstract class TexGroup {
	private Context context = null;

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public abstract int getTextureCount();

	public abstract Texture getTexture(int index);

}
