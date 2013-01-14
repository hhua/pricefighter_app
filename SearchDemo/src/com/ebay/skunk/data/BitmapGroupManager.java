package com.ebay.skunk.data;

import java.util.HashMap;
import java.util.Map;
import android.graphics.Bitmap;

public class BitmapGroupManager{

	private static BitmapGroupManager m_instance=null;

	private static final Map<String, Bitmap> m_bitmaps = new HashMap<String, Bitmap>();
	
	public BitmapGroupManager(){}
	
	public static BitmapGroupManager getInstance()
	{
		if(m_instance==null)
		{
			return new BitmapGroupManager();
		}
		return m_instance;
	}
	
	public synchronized void register(String url, Bitmap b)
	{
		m_bitmaps.put(url, b);
	}
	
	public Bitmap get(String url)
	{
		return m_bitmaps.get(url);
	}
	
	public void clear()
	{
		m_bitmaps.clear();
	}
}
