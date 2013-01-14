package org.kevinth.kth2d.event;

/**
 * @author <a href="mailto:kevinth.fang@gmail.com">Kevinth</a>
 *
 */
public interface IGameEventListener<T extends IGameEvent> {
	public void onEvent(IGameEvent event);
}
