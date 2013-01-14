package org.kevinth.kth2d;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author <a href="mailto:kevinth.fang@gmail.com">Kevinth</a>
 *
 */
public class Sprite extends BasicTexNode {
	private ActionNode actionHeader = null;
	private Map<String, Object> attributes = new HashMap<String, Object>();

	public void doAction(Action action) {
		ActionNode node = getExisting(action);
		if (node == null) {
			node = ActionNode.obtain();
			node.action = action;
			if (actionHeader == null)
				actionHeader = node;
			else {
				node.next = actionHeader;
				actionHeader = node;
			}
			action.setSprite(this);
		}
		action.start();
	}

	private ActionNode getExisting(Action action) {
		ActionNode node = actionHeader;
		while (node != null) {
			if (node.action == action)
				return node;
			node = node.next;
		}
		return null;
	}

	public <T extends Action> T getRunningAction(Class<T> actionClass) {
		ActionNode node = actionHeader;
		while (node != null) {
			if (node.action.getClass().isAssignableFrom(actionClass)
					&& node.action.isRunning())
				return (T) node.action;
			node = node.next;
		}
		return null;
	}

	public <T extends Action> int getRunningActionCount(Class<T> actionClass) {
		int ret = 0;
		ActionNode node = actionHeader;
		while (node != null) {
			if (node.action.getClass().isAssignableFrom(actionClass)
					&& node.action.isRunning())
				ret++;
			node = node.next;
		}
		return ret;
	}

	public <T extends Action> T getRunningAction(Class<T> actionClass, int index) {
		int i = 0;
		ActionNode node = actionHeader;
		while (node != null) {
			if (node.action.getClass().isAssignableFrom(actionClass)
					&& node.action.isRunning()) {
				if (i == index)
					return (T) node.action;
				i++;
			}

			node = node.next;
		}

		return null;
	}

	@Override
	public void onUpdate(Time time) {
		super.onUpdate(time);
		ActionNode node = this.actionHeader;
		ActionNode pre = null;
		while (node != null) {
			if (node.action.isRunning()) {
				node.action.onUpdate(time);
				pre = node;
				node = node.next;
			} else {
				//Log.d("del Act", node.action.toString());
				ActionNode next = node.next;
				if (pre != null)
					pre.next = next;
				else
					this.actionHeader = next;

				node.recycle();
				node = next;
			}
		}
	}

	public void stopActions(Class<?> baseClass) {
		ActionNode node = this.actionHeader;
		while (node != null) {
			if (node.action.getClass().isAssignableFrom(baseClass)) {
				node.action.stop();
			}
			node = node.next;
		}
	}

	public void stopAction(Action action) {
		action.stop();
	}

	public void stopAllActions() {
		ActionNode node = this.actionHeader;
		while (node != null) {
			node.action.stop();
			node = node.next;
		}
	}

	public void setAttribute(String key, Object value) {
		attributes.put(key, value);
	}

	public Object getAttribute(String key) {
		return attributes.get(key);
	}

	public void removeAttribute(String key) {
		attributes.remove(key);
	}

	public void clearAttributes() {
		attributes.clear();
	}

	public static class ActionNode implements IRecyclable {
		Action action;
		ActionNode next;
		private static ObjectPool<ActionNode> pool = new ObjectPool<ActionNode>(
				30);

		public static ActionNode obtain() {
			ActionNode node = pool.obtain();
			if (node == null)
				node = new ActionNode();
			return node;
		}

		public void recycle() {
			action = null;
			next = null;
			pool.recycle(this);
		}
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Sprite " + this.getClass().getName() + ":");
		Iterator<Entry<String, Object>> it = this.attributes.entrySet()
				.iterator();
		while (it.hasNext()) {
			Entry<String, Object> entry = it.next();
			sb.append(entry.getKey() + "-" + entry.getValue().toString());
		}

		return sb.toString();
	}
}
