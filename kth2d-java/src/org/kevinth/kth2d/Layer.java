package org.kevinth.kth2d;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.kevinth.kth2d.event.IKeyEventListener;
import org.kevinth.kth2d.event.ITouchEventListener;

import android.graphics.Canvas;

/**
 * @author <a href="mailto:kevinth.fang@gmail.com">Kevinth</a>
 *
 */
public class Layer implements IUpdatable, IDrawable {
	private Scene scene = null;
	private final List<Node> nodes = new ArrayList<Node>();

	public Layer(Scene scene) {
		this.scene = scene;
	}

	public void destroy() {
		for (Node node : nodes) {
			node.destroy();
		}
	}

	public void onDraw(Canvas canvas) {
		for (int i = 0; i < nodes.size(); i++) {
			Node node = nodes.get(i);
			node.onDraw(canvas);
		}
	}

	public void onUpdate(Time time) {
		for (int i = 0; i < nodes.size(); i++) {
			Node node = nodes.get(i);
			if (node.isActivated())
				node.onUpdate(time);
		}
	}

	public Scene getScene() {
		return scene;
	}

	public Iterator<Node> iterateNodes() {
		return nodes.iterator();
	}

	public Node getNode(int index) {
		return nodes.get(index);
	}

	public int getNodeCount() {
		return nodes.size();
	}

	public void addNode(Node node) {
		nodes.add(node);
		node.scene = this.scene;
		node.layer = this;
		node.onAdd(this.scene, this);
		if (node instanceof ITouchEventListener)
			Director.getEventManager().addTouchListener(
					(ITouchEventListener) node);
		if (node instanceof IKeyEventListener)
			Director.getEventManager().addKeyListener((IKeyEventListener) node);
	}

	public void insert(int index, Node node) {
		nodes.add(index, node);
	}

	public void remove(Node node) {
		if (node instanceof ITouchEventListener)
			Director.getEventManager().removeTouchListener(
					(ITouchEventListener) node);
		if (node instanceof IKeyEventListener)
			Director.getEventManager().removeKeyListener(
					(IKeyEventListener) node);
		node.onRemove(this.scene, this);
		nodes.remove(node);
	}

	public Node remove(int index) {
		Node node = nodes.get(index);
		if (node instanceof ITouchEventListener)
			Director.getEventManager().removeTouchListener(
					(ITouchEventListener) node);
		if (node instanceof IKeyEventListener)
			Director.getEventManager().removeKeyListener(
					(IKeyEventListener) node);
		node.onRemove(this.scene, this);
		return nodes.remove(index);
	}

	public int indexOf(Node node) {
		return nodes.indexOf(node);
	}

	public void clear() {
		for (int i = 0; i < nodes.size(); i++)
			this.remove(i);
	}

}
