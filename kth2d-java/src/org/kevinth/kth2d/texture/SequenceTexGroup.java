package org.kevinth.kth2d.texture;

/**
 * @author <a href="mailto:kevinth.fang@gmail.com">Kevinth</a>
 *
 */
public class SequenceTexGroup extends TexGroup {
	private Texture[] textures = null;

	private int resFrom = 0;
	private int resTo = 0;

	public SequenceTexGroup(int resFrom, int resTo) {
		this.resFrom = resFrom;
		this.resTo = resTo;
	}

	@Override
	public int getTextureCount() {
		return this.resTo - this.resFrom + 1;
	}

	private Texture[] getTextures() {
		if (textures == null) {
			textures = new Texture[this.getTextureCount()];
			for (int i = resFrom; i <= resTo; i++) {
				textures[i - resFrom] = new Texture(i);
			}
		}
		return textures;
	}

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
		return "SequenceTexAtlas: from " + this.resFrom + " to " + this.resTo;
	}

}
