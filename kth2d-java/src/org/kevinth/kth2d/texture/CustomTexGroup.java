package org.kevinth.kth2d.texture;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:kevinth.fang@gmail.com">Kevinth</a>
 *
 */
public class CustomTexGroup extends TexGroup {
	private List<Texture> textures = new ArrayList<Texture>();

	/* (non-Javadoc)
	 * @see org.kevinth.kth2d.texture.TexAtlas#getTextureCount()
	 */
	@Override
	public int getTextureCount() {
		return textures.size();
	}

	/* (non-Javadoc)
	 * @see org.kevinth.kth2d.texture.TexAtlas#getTileDef(int)
	 */
	@Override
	public Texture getTexture(int index) {
		return this.textures.get(index);
	}

	public void add(Texture texture) {
		this.textures.add(texture);
	}

	public void remove(int index) {
		this.textures.remove(index);
	}

	public void remove(Texture texture) {
		this.textures.remove(texture);
	}

	public void clear() {
		this.textures.clear();
	}

}
