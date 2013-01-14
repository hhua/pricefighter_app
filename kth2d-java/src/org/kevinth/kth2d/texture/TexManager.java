package org.kevinth.kth2d.texture;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.kevinth.kth2d.DefaultResLoader;
import org.kevinth.kth2d.Loading;
import org.kevinth.kth2d.ResourceLoader;
import org.kevinth.kth2d.geometry.Size;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.util.Log;

/**
 * @author <a href="mailto:kevinth.fang@gmail.com">Kevinth</a>
 *
 */
public class TexManager {
	public static final String TEXTURE_RES_TYPE = "texture";

	private Context context = null;
	private ResourceLoader resourceLoader = null;

	private final Map<Object, Texture> textures = new HashMap<Object, Texture>();

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public void register(Texture texture) {
		Object resIndex = texture.getResIndex();
		textures.put(resIndex, texture);
	}

	public void register(TexGroup atlas) {
		atlas.setContext(this.context);
		int c = atlas.getTextureCount();

		for (int i = 0; i < c; i++) {
			Texture texture = atlas.getTexture(i);
			if (texture != null) {
				this.register(texture);
			}
		}
	}

	public Bitmap loadBitmap(Texture texture) {
		Bitmap bitmap = null;
		Object index = texture.getResIndex();

			try {
				Options opts = new Options();
				opts.inPurgeable = false;
				if (index instanceof Integer) {
					bitmap = BitmapFactory.decodeResource(context.getResources(),
							(Integer) index, opts);
				} else if (index instanceof String) {
					InputStream is = getResourceLoader().loadResource(
							TEXTURE_RES_TYPE, (String) index);
					bitmap = BitmapFactory.decodeStream(is);
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		return bitmap;
	}

	public Texture getTexture(Object index) {
		Texture ret = this.textures.get(index);
		return ret;
	}

	public void loadAll(Loading loading) {
		for (Texture tex : textures.values()) {
			if (!tex.isLoaded())
				this.loadTexture(tex);
		}
	}

	public void releaseAll() {
		for (Texture tex : textures.values()) {
			if (tex.isLoaded())
				this.releaseTexture(tex);
		}
		textures.clear();
	}

	public ResourceLoader getResourceLoader() {
		if (resourceLoader == null) {
			resourceLoader = new DefaultResLoader(context);
		}
		return resourceLoader;
	}

	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	public void clearAll() {
		textures.clear();
	}

	public void loadTexture(Texture texture) {
		if (texture.isLoaded())
			return;

		if (texture.getBitmap() == null || texture.getBitmap().isRecycled()) {
			Bitmap bitmap = this.loadBitmap(texture);
			texture.setBitmap(bitmap);
			if (texture.getSize() == null) {
				int width = bitmap.getWidth();
				int height = bitmap.getHeight();
				texture.setSize(new Size(width, height));
			}
			
		}
		texture.onLoadTexture();
	}

	public void releaseTexture(Texture texture) {
		if (!texture.isLoaded())
			return;

		texture.onRecycle();
	}
}
