package com.ebay.skunk.data;

import java.net.URL;

import android.graphics.Bitmap;

import com.ebay.jxb.type.Duration;
import com.ebay.skunk.R;

public class SearchItem {
	
	private String url;
	
	private String title;

	private double price;

	private Duration timeLeft;

	private String condition;
	
	private String id;
	
	private String location;

	private boolean shown = false;
	
	private int position;
	
	private boolean isChosen=false;
	
	private boolean isAddToWatch = false;
	
	public void setURL(String u)
	{
		this.url=u;
	}
	
	public String getURL()
	{
		return url;
	}
	
	public void setIsAddToWatch(boolean b)
	{
		isAddToWatch = b;
	}
	
	public boolean getIsAddToWatch()
	{
		return isAddToWatch;
	}
	
	public boolean isShown() {
		return shown;
	}

	public void setShown(boolean shown) {
		this.shown = shown;
	}

	public String getPriceTimeleftTitle() {
		return "Price: $" + price + "   TimeLeft: " + formatDuration(timeLeft);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double value) {
		this.price = value;
	}

	public Duration getTimeLeft() {
		return timeLeft;
	}

	public void setTimeLeft(Duration timeLeft) {
		this.timeLeft = timeLeft;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	public void setPosition(int p)
	{
		this.position=p;
	}
	
	public int getPosition()
	{
		return this.position;
	}
	
	public void setIsChosen(boolean b)
	{
		this.isChosen=b;
	}
	
	public boolean getIsChosen()
	{
		return this.isChosen;
	}

	public static String formatDuration(Duration duration) {
		StringBuffer formattedText = new StringBuffer();
		boolean leading = false;
		if (duration.isNegative()) {
			formattedText.append("- ");
		}
		if (duration.getYears() > 0) {
			leading = true;
			formattedText.append(duration.getYears() + "y ");
		}
		if (leading || duration.getMonths() > 0) {
			leading = true;
			formattedText.append(duration.getMonths() + "M ");
		}
		if (leading || duration.getDays() > 0) {
			leading = true;
			formattedText.append(duration.getDays() + "d ");
		}
		if (leading || duration.getHours() > 0) {
			leading = true;
			formattedText.append(duration.getHours() + "H ");
		}
		if (leading || duration.getMinutes() > 0) {
			leading = true;
			formattedText.append(duration.getMinutes() + "m ");
		}
		if (leading || duration.getSeconds() > 0) {
			formattedText.append((int) duration.getSeconds() + "s");
		}

		if (formattedText.length() == 0) {
			return "Ended";
		}

		return formattedText.toString();
	}
}
