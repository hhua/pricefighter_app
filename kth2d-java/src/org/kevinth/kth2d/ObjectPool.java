package org.kevinth.kth2d;

/**
 * @author <a href="mailto:kevinth.fang@gmail.com">Kevinth</a>
 *
 */
public class ObjectPool<T> {
	public static final int DEFAULT_SIZE = 30;
	private int cursor = 0;
	private T[] array = null;

	public ObjectPool(int size) {
		array = (T[]) new Object[size];
	}

	public ObjectPool() {
		array = (T[]) new Object[DEFAULT_SIZE];
	}

	public T obtain() {
		int i = cursor - 1;
		if (i >= 0) {
			T ret = (T) array[i];
			array[i] = null;
			cursor = i;
			return ret;
		} else {
			return null;
		}
	}

	public void recycle(T obj) {
		int i = cursor + 1;
		if (i <= array.length) {
			array[cursor] = obj;
			cursor = i;
		}
	}

}
