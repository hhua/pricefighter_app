package org.kevinth.kth2d;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.util.Log;

/**
 * @author <a href="mailto:kevinth.fang@gmail.com">Kevinth</a>
 *
 */
public class DefaultResLoader implements ResourceLoader {
	private Context context = null;

	public DefaultResLoader(Context context) {
		this.context = context;
	}

	/* (non-Javadoc)
	 * @see org.kevinth.kth2d.ResourceLoader#loadResource(java.lang.String, java.lang.String)
	 */
	public InputStream loadResource(String type, String path) {
		try {
			String s = type + "/" + path;

			InputStream is = context.getAssets().open(s);
			return is;
		} catch (IOException e) {
			Log.e("kth2d", e.getMessage(), e);
			return null;
		}
	}

}
