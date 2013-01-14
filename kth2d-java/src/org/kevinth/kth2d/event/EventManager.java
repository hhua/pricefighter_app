package org.kevinth.kth2d.event;

import java.util.ArrayList;
import java.util.List;

import org.kevinth.kth2d.Director;

import android.app.Activity;
import android.util.Log;
import android.view.KeyEvent;

/**
 * @author <a href="mailto:kevinth.fang@gmail.com">Kevinth</a>
 *
 */
public class EventManager {
	private final static int KEY_BUFFER_SIZE = 5;
	private final static int TOUCH_BUFFER_SIZE = 10;

	private List<TouchEvent> touchEventQueue = new ArrayList<TouchEvent>(
			TOUCH_BUFFER_SIZE);
	private List<KeyEvent> keyEventQueue = new ArrayList<KeyEvent>(
			KEY_BUFFER_SIZE);
	private DefaultKeyListener defaultKeyListener = new DefaultKeyListener();

	private List<ITouchEventListener> touchListeners = new ArrayList<ITouchEventListener>();
	private List<IKeyEventListener> keyListeners = new ArrayList<IKeyEventListener>();

	public synchronized void fireTouchEvent(TouchEvent event) {
		if (touchEventQueue.size() < TOUCH_BUFFER_SIZE)
			touchEventQueue.add(event);
	}

	public synchronized void fireKeyEvent(KeyEvent event) {
		if (keyEventQueue.size() < KEY_BUFFER_SIZE)
			keyEventQueue.add(event);
	}

	public synchronized void addTouchListener(ITouchEventListener listener) {
		touchListeners.add(listener);
	}

	public synchronized void addKeyListener(IKeyEventListener listener) {
		keyListeners.add(listener);
	}

	public synchronized void removeTouchListener(ITouchEventListener listener) {
		touchListeners.remove(listener);
	}

	public synchronized void removeKeyListener(IKeyEventListener listener) {
		keyListeners.remove(listener);
	}

	public synchronized void clearListeners() {
		touchListeners.clear();
		keyListeners.clear();
	}

	public synchronized void handleInteractiveEvents() {
		for (int i = 0; i < touchListeners.size(); i++) {
			ITouchEventListener listener = touchListeners.get(i);
			for (int j = 0; j < touchEventQueue.size(); j++) {
				listener.onEvent(touchEventQueue.get(j));
			}
		}
		for (int j = 0; j < touchEventQueue.size(); j++) {
			touchEventQueue.get(j).recycle();
		}
		touchEventQueue.clear();

		if (keyListeners.size() == 0) {
			for (int j = 0; j < keyEventQueue.size(); j++) {
				if(defaultKeyListener!=null)
					defaultKeyListener.onEvent(keyEventQueue.get(j));
			}
		} else {
			for (int i = 0; i < keyListeners.size(); i++) {
				IKeyEventListener listener = keyListeners.get(i);
				for (int j = 0; j < keyEventQueue.size(); j++) {
					listener.onEvent(keyEventQueue.get(j));
				}
			}
		}
		keyEventQueue.clear();
	}

	class DefaultKeyListener implements IKeyEventListener {
		public void onEvent(KeyEvent event) {
			if (KeyEvent.ACTION_UP == event.getAction()
					&& KeyEvent.KEYCODE_BACK == event.getKeyCode()) {
				((Activity) Director.getContext()).finish();
			}
		}

	}
}
