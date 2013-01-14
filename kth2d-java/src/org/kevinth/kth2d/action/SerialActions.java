package org.kevinth.kth2d.action;

import org.kevinth.kth2d.Action;
import org.kevinth.kth2d.Sprite;

/**
 * @author <a href="mailto:kevinth.fang@gmail.com">Kevinth</a>
 *
 */
public class SerialActions extends Action {
	private Action[] actions = null;
	private int current = 0;

	private ActionListener serialStopListener = new ActionListener() {
		public void actionStop(Action action) {
			current++;
			if (current < actions.length) {
				Sprite sprite = getSprite();
				sprite.doAction(actions[current]);
			} else {
				done();
			}
		}

		public void actionStart(Action action) {
		}

	};

	public SerialActions() {
	}

	public SerialActions(Action[] actions) {
		this.actions = actions;
	}

	public Action[] getActions() {
		return actions;
	}

	public void setActions(Action[] actions) {
		this.actions = actions;
		for (int i = 0; i < actions.length; i++) {
			if (!actions[i].containsActionListener(serialStopListener))
				actions[i].addActionListener(serialStopListener);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		current = 0;
		if (actions != null && actions.length > 0) {
			Sprite sprite = getSprite();
			sprite.doAction(actions[0]);
		}
	}

	@Override
	protected void onStop() {
		if (actions != null && actions.length > current
				&& actions[current] != null && actions[current].isRunning())
			getSprite().stopAction(actions[current]);
		super.onStop();
	}
}
