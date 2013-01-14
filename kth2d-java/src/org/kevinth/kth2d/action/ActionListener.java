package org.kevinth.kth2d.action;

import org.kevinth.kth2d.Action;


/**
 * @author <a href="mailto:kevinth.fang@gmail.com">Kevinth</a>
 *
 */
public abstract class ActionListener {
	public void actionStart(Action action) {
	};

	public void actionStop(Action action) {
	};

	public void actionDone(Action action) {
	};
}
