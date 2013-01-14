package org.kevinth.kth2d;

import java.io.InputStream;

/**
 * @author <a href="mailto:kevinth.fang@gmail.com">Kevinth</a>
 *
 */
public interface ResourceLoader {
	public InputStream loadResource(String type , String path);
}
