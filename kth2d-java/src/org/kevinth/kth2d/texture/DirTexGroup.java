package org.kevinth.kth2d.texture;

import java.io.IOException;

/**
 * @author <a href="mailto:kevinth.fang@gmail.com">Kevinth</a>
 *
 */
public class DirTexGroup extends TexGroup {
	private Texture[] textures = null;
	private String path = null;

	private Texture[] getTextures() {
		if (textures == null) {
			try {
				String[] assets = this.getContext().getAssets().list(path);
				textures = new Texture[assets.length];
				for (int i = 0; i < textures.length; i++) {
					textures[i] = new Texture(assets[i]);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return textures;
	}

	public DirTexGroup(String path) {
		this.path = path;
	}

	/* (non-Javadoc)
	 * @see org.kevinth.kth2d.texture.TextureAtlas#getTextureCount()
	 */
	@Override
	public int getTextureCount() {
		Texture[] texes = getTextures();
		if (texes != null)
			return texes.length;
		else
			return 0;
	}

	/* (non-Javadoc)
	 * @see org.kevinth.kth2d.texture.TextureAtlas#getTileDef(int)
	 */
	@Override
	public Texture getTexture(int index) {
		Texture[] texes = getTextures();
		if (texes != null)
			return texes[index];
		else
			return null;
	}

	@Override
	public String toString() {
		return "DirTexAtlas: path " + this.path;
	}
}
