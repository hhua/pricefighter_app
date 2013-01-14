package com.ebay.skunk.data;

import java.util.ArrayList;

public class ChosenItemsManager {

	private static ChosenItemsManager m_instance=null;
	private static final ArrayList<SearchItem> chosenItems = new ArrayList<SearchItem>();
	
	public ChosenItemsManager(){}
	
	public static ChosenItemsManager getInstance()
	{
		if(m_instance==null)
		{
			return new ChosenItemsManager();
		}
		return m_instance;
	}
	
	public void clear()
	{
		chosenItems.clear();
	}
	
	public void add(SearchItem searchItem)
	{
		if(!isInList(searchItem))
		{
			chosenItems.add(searchItem);
		}
	}
	
	private boolean isInList(SearchItem searchItem)
	{
		return chosenItems.contains(searchItem);
	}
	
	public void remove(SearchItem searchItem)
	{
		chosenItems.remove(searchItem);
	}
	
	public int getCount()
	{
		return chosenItems.size();
	}
	
	public SearchItem getSearchItem(int i)
	{
		return chosenItems.get(i);
	}
	
}
