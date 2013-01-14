package org.kevinth.kth2d;

import java.util.ArrayList;
import java.util.List;

import org.kevinth.kth2d.texture.TexGroup;

import android.graphics.Canvas;
import android.util.Log;

/**
 * @author <a href="mailto:kevinth.fang@gmail.com">Kevinth</a>
 *
 */
public abstract class Scene implements IUpdatable, IDrawable {
	private final List<Layer> layers = new ArrayList<Layer>();

	private boolean loaded = false;

	protected void onLoad(Loading loading) {
	}

	protected void onActivate() {
	}

	protected void onInactivate() {
	}

	protected void onDestroy() {
	}

	protected TexGroup onGatherTextures() {
		return null;
	}

	public void load(Loading loading) {
		Director.getTexManager().releaseAll();
		this.onLoad(loading);

		loaded = true;
		TexGroup atlas = onGatherTextures();
		if (atlas != null)
			Director.getTexManager().register(atlas);
		Director.getTexManager().loadAll(loading);
	}

	public void destroy() {
		this.clear();
		this.onDestroy();
		for (Layer layer : layers) {
			layer.destroy();
		}
		loaded = false;
	}

	public boolean isLoaded() {
		return loaded;
	}

	public void onUpdate(Time time) {
		if (loaded) {
			for (Layer layer : layers) {
				layer.onUpdate(time);
			}
		}
	}

	public void onDraw(Canvas canvas) {
		if (loaded) {
			for (Layer layer : layers) {
				layer.onDraw(canvas);
			}
		}
	}

	public int layerCount() {
		return layers.size();
	}

	public void setupLayerCount(int count) {
		if (layers.size() < count) {
			for (int i = layers.size(); i < count + 1; i++) {
				layers.add(new Layer(this));
			}
		}
	}

	public int nodeCount() {
		int c = this.layerCount();
		int t = 0;
		for (int i = 0; i < c; i++)
			t += this.getLayer(i).getNodeCount();
		return t;
	}

	public Layer getLayer(int index) {
		setupLayerCount(index + 1);
		return layers.get(index);
	}

	public void clearLayer(int i) {
		getLayer(i).clear();
	}

	public void clear() {
		for (int i = 0; i < layers.size(); i++)
			clearLayer(i);
	}

	public void addNode(Node node) {
		addNode(0, node);
	}

	public void addNode(int layerIndex, Node node) {
		getLayer(layerIndex).addNode(node);
	}

	public void getNode(int layerIndex, int nodeIndex) {
		getLayer(layerIndex).getNode(nodeIndex);
	}
}
