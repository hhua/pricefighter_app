package org.kevinth.kth2d;

import java.util.ArrayList;
import java.util.List;

import org.kevinth.kth2d.action.ActionListener;

/**
 * @author <a href="mailto:kevinth.fang@gmail.com">Kevinth</a>
 *
 */
public abstract class Action implements IRecyclable {
	private boolean loop = false;

	private boolean running = false;
	private Sprite sprite = null;
	private List<ActionListener> listeners = null;
	private Action next = null;

	public Action() {
	}

	public Sprite getSprite() {
		return sprite;
	}

	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}

	public boolean isRunning() {
		return running;
	}

	public boolean isLoop() {
		return loop;
	}

	public Action setLoop(boolean loop) {
		this.loop = loop;
		return this;
	}

	public Action getNext() {
		return next;
	}

	public void setNext(Action next) {
		this.next = next;
	}

	protected void start() {
		this.running = true;
		//Log.d("Act Start" , this.toString());
		if (listeners != null) {
			for (int i = 0; i < listeners.size(); i++)
				listeners.get(i).actionStart(this);
		}
		onStart();
	}

	protected void stop() {
		this.running = false;
		onStop();
		if (listeners != null) {
			for (int i = 0; i < listeners.size(); i++)
				listeners.get(i).actionStop(this);
		}
	}

	protected void done() {
		this.running = false;
		onDone();
		if (listeners != null) {
			for (int i = 0; i < listeners.size(); i++)
				listeners.get(i).actionDone(this);
		}
		if (loop)
			start();
		else if (next != null)
			sprite.doAction(next);
	}

	public void addActionListener(ActionListener listener) {
		if (listeners == null)
			listeners = new ArrayList<ActionListener>();
		listeners.add(listener);
	}

	public void removeActionListener(ActionListener listener) {
		if (listeners != null) {
			listeners.remove(listener);
		}
	}

	public boolean containsActionListener(ActionListener listener) {
		if (listeners != null) {
			return listeners.contains(listener);
		} else
			return false;
	}

	public void clearActionListener() {
		if (listeners != null) {
			listeners.clear();
		}
	}

	protected void onStart() {
	}

	protected void onStop() {
	}

	protected void onDone() {
	}

	protected void onUpdate(Time time) {
	}

	public void recycle() {
		this.running = false;
		this.sprite = null;
		this.loop = false;
		this.next = null;
		if (listeners != null)
			listeners.clear();
	}
}
