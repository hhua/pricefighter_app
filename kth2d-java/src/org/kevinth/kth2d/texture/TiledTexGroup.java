package org.kevinth.kth2d.texture;

/**
 * @author <a href="mailto:kevinth.fang@gmail.com">Kevinth</a>
 *
 */
public class TiledTexGroup extends TexGroup {
	private boolean loaded;

	private Texture[] textures = null;
	private Object resIndex;
	private int rows;
	private int columns;

	public TiledTexGroup(Object resIndex) {
		this.resIndex = resIndex;
	}

	public TiledTexGroup(Object resIndex, int rows, int columns) {
		this.resIndex = resIndex;
		this.rows = rows;
		this.columns = columns;
	}

	/* (non-Javadoc)
	 * @see org.kevinth.kth2d.texture.TextureAtlas#getTextureCount()
	 */
	@Override
	public int getTextureCount() {
		return rows * columns;
	}

	private Texture[] getTextures() {
		if (textures == null) {
			int c = this.getTextureCount();
			textures = new Texture[c];
			for (int i = 0; i < c; i++) {
				//				TileDef tile = this.getTileDef(i);
				//				textures[i] = new Texture(this.resIndex, tile);
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

	//	private TileDef getTileDef(int index) {
	//		TileDef tile = new TileDef();
	//		int row = 0;
	//		int column = 0;
	//		if (type == GRID_TYPE_ROWS_FIRST) {
	//			row = index / columns;
	//			column = index % columns;
	//		} else if (type == GRID_TYPE_COLS_FIRST) {
	//			column = index / rows;
	//			row = index % rows;
	//		}
	//		tile.setMinX(column / columns);
	//		tile.setMinY(row / rows);
	//		tile.setMaxX((column + 1) / columns);
	//		tile.setMaxY((row + 1) / rows);
	//		return tile;
	//	}

	public boolean isLoaded() {
		return loaded;
	}

	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getColumns() {
		return columns;
	}

	public void setColumns(int columns) {
		this.columns = columns;
	}
}
