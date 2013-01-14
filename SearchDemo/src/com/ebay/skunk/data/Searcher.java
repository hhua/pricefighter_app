package com.ebay.skunk.data;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.ebay.msif.core.ServiceCallback;
import org.ebay.msif.core.ServiceClientException;
import org.ebay.msif.core.ServiceResponse;
import org.kevinth.kth2d.Director;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import com.ebay.services.finding.FindItemsByKeywordsRequest;
import com.ebay.services.finding.FindItemsByKeywordsResponse;
import com.ebay.services.finding.PaginationInput;
import com.ebay.services.finding.client.FindingServiceClient;
import com.ebay.types.common.soa.AckValue;

public class Searcher {
	public static final int GRADE_PRICE = 0;
	public static final int GRADE_BIDS = 1;
	public static final int GRADE_TIMELEFT = 2;
	public static final int GRADE_FEEDBACK = 3;

	public static final int GRADE_COUNT = 10;

	public static final int ONE_TIME_SEARCH_COUNT = 50;
	public static final int TOTAL_SEARCH_TIMES = 1;

	public static double PRICE_MIN = 0;
	public static double PRICE_MAX = 1000;

	public static final int BIDS_MIN = 20;
	public static final int BIDS_MAX = 220;

	public static final int TIMELEFT_MIN = 200;
	public static final int TIMELEFT_MAX = 2200;

	public static final int FEEDBACK_MIN = 20;
	public static final int FEEDBACK_MAX = 220;

	private int gradeType = GRADE_PRICE;
	private IDataHandler handler = null;
	private int pageNumber = 1;
	private String keyword = "";
	public static Bitmap defaultBitmap;
	private Context context;
	
	private FindingServiceClient m_findingClient;
	SearchItem[] items = new SearchItem[ONE_TIME_SEARCH_COUNT];

	public void setDefaultBitmap(Bitmap b)
	{
		this.defaultBitmap=b;
	}
	
	public void setContext(Context c)
	{
		this.context=c;
	}
	
	public static float getRandom(float min, float max) {
		return (float) (min + (max - min) * Math.random());
	}

	public static int getRandom(int min, int max) {
		return (int) (min + (max - min) * Math.random());
	}

	public static String getGradeValueString(int gradeType, String gradeValueLow, String gradeValueHigh) {
		if (Searcher.GRADE_PRICE == gradeType) {
			if(gradeValueLow.equals("0"))
				return "Less than $"+gradeValueHigh;
			if(gradeValueHigh.equals("0"))
				return "More than $"+gradeValueLow;
			return "$" + gradeValueLow + " ~ $" + gradeValueHigh;
		} else {
			return new Float(gradeValueLow).toString();
		}
	}

	public int getGradeType() {
		return gradeType;
	}

	public void setGradeType(int gradeType) {
		this.gradeType = gradeType;
	}

	public IDataHandler getHandler() {
		return handler;
	}

	public void setHandler(IDataHandler handler) {
		this.handler = handler;
	}

	public void onResponse(String keyword,Context context) {
		m_findingClient = new FindingServiceClient(context);	 
		this.keyword = keyword;
	    try {
			findItemsByKeywords(true);
		} catch (Exception e) {
			Log.e("ViewAndChoose", "FindItemsByKeywords Error", e);
		}
	}

	public List<List<SearchItem>> group(SearchItem[] items) {
		List<List<SearchItem>> ret = new ArrayList<List<SearchItem>>();
		for (int i = 0; i < GRADE_COUNT; i++)
			ret.add(new ArrayList<SearchItem>());
		for (SearchItem item : items) {
			ret.get(getGrade(item)).add(item);
		}

		return ret;
	}

	public double getGradeValue(int grade) {
		double max = 0;
		double min = 0;

		if (GRADE_PRICE == gradeType) {
			max = PRICE_MAX;
			min = PRICE_MIN;
		} else if (GRADE_BIDS == gradeType) {
			max = BIDS_MAX;
			min = BIDS_MIN;
		} else if (GRADE_TIMELEFT == gradeType) {
			max = TIMELEFT_MAX;
			min = TIMELEFT_MIN;
		} else {
			max = FEEDBACK_MAX;
			min = FEEDBACK_MIN;
		}

		return min + (max - min) / GRADE_COUNT * grade;
	}

	public int getGrade(SearchItem item) {
		double max = 0;
		double min = 0;
		double value = 0;

		if (GRADE_PRICE == gradeType) {
			max = PRICE_MAX;
			min = PRICE_MIN;
			value = item.getPrice();
		} else if (GRADE_BIDS == gradeType) {
			max = BIDS_MAX;
			min = BIDS_MIN;
//			value = item.getBids();
		} else if (GRADE_TIMELEFT == gradeType) {
			max = TIMELEFT_MAX;
			min = TIMELEFT_MIN;
//			value = item.getTimeLeft();
		} else {
			max = FEEDBACK_MAX;
			min = FEEDBACK_MIN;
//			value = item.getFeedbackScore();
		}

		double gradeInterval = (max - min) / GRADE_COUNT;
		int ret = (int) ((value - min) / gradeInterval);
		return (ret >= GRADE_COUNT) ? GRADE_COUNT - 1 : ret;
	}
	
	public int getGradeCount()
	{
		return GRADE_COUNT;
	}
	
	 public void findItemsByKeywords(final boolean isNewSearch)
	   {
		   FindItemsByKeywordsRequest request = new FindItemsByKeywordsRequest();
		   request.keywords = this.keyword;
		   PaginationInput pi = new PaginationInput();
	       pi.pageNumber = pageNumber;
	       pi.entriesPerPage = ONE_TIME_SEARCH_COUNT;
	       request.paginationInput = pi;
	       ImageLoader.initialize(context);
			try {
				m_findingClient.findItemsByKeywords(request,new ServiceCallback<FindItemsByKeywordsResponse>() 
				{
						public void onResponse(
								ServiceResponse<FindItemsByKeywordsResponse> response) 
						{
							if (response.hasErrors()) 
							{
								Log.d("findItemsByKeywords", "response.hasErrors()");
							} 
							else if (response.getResponseData().ack == AckValue.Failure) 
							{
								Log.d("findItemsByKeywords", "response.getResponseData().ack == AckValue.Failure");
							}
							else
							{
								if(response.getResponseData().searchResult.item!=null)
								{ 	
						        	int total=response.getResponseData().searchResult.item.size();
						        	if(total>ONE_TIME_SEARCH_COUNT)
						        		total=ONE_TIME_SEARCH_COUNT;
						        	int i=0;
						            double minPrice=0;
						            double maxPrice=0;
						            double secondPrice=0;
						            if(isNewSearch)
						            {
						            	BitmapGroupManager.getInstance().clear();
						            	ChosenItemsManager.getInstance().clear();
						            }
						            int totalNum=response.getResponseData().searchResult.item.size();
						            if(totalNum>items.length)
						            	totalNum=items.length;
						        	for (i = 0; i<totalNum;i++){
						        		SearchItem item = new SearchItem();
						        		com.ebay.services.finding.SearchItem result = response.getResponseData().searchResult.item.get(i);
						    			item.setTitle(result.title);
						    			
						    			double price=result.sellingStatus.convertedCurrentPrice.value;
						    			item.setPrice(price);
						    			if(isNewSearch)
						    			{
						    				if(price>maxPrice)
						    				{
						    					secondPrice=maxPrice;
						    					maxPrice=price;
						    				}else if(price<minPrice)
						    				{
						    					minPrice=price;
						    				}else if(price > secondPrice)
						    				{
						    					secondPrice = price;
						    				}
						    			}
						    			
						    			String conditionDisplayName = "NA";
						    			if (result.condition != null) {
						    				conditionDisplayName = result.condition.conditionDisplayName;
						    			}
						    			item.setCondition(conditionDisplayName);
						    			item.setId(result.itemId);
						    			item.setLocation(result.location);
						    			item.setTimeLeft(result.sellingStatus.timeLeft);
						    			item.setURL(result.galleryURL);
										item.setPosition((pageNumber-1)*ONE_TIME_SEARCH_COUNT+i);
						    			
										ImageLoader.start(item.getURL(), new ImageLoaderHandler());
										
						    			items[i] = item;
						        	}
						        	if(isNewSearch)
						        	{
						        		PRICE_MAX=secondPrice;
						        		PRICE_MIN=minPrice;
						        		
						        	}
						        	pageNumber ++;
						        	if(pageNumber <= TOTAL_SEARCH_TIMES)
						        		findItemsByKeywords (false);
					        	    handler.onDataComplete(items);
								}
							}
						}
				});
			} catch(ServiceClientException e) {
				Log.e("" ,"" ,e);
			}
	   }
}
