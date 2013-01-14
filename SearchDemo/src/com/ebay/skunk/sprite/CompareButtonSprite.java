package com.ebay.skunk.sprite;

import org.ebay.msif.core.ServiceClientException;
import org.kevinth.kth2d.Director;
import org.kevinth.kth2d.Time;
import org.kevinth.kth2d.geometry.Anchor;
import org.kevinth.kth2d.geometry.Point;
import org.kevinth.kth2d.geometry.Size;
import org.kevinth.kth2d.texture.TexManager;
import org.kevinth.kth2d.texture.Texture;

import com.ebay.msdk.security.SignInManager;
import com.ebay.skunk.CompareActivity;
import com.ebay.skunk.R;
import com.ebay.skunk.ResultListActivity;
import com.ebay.skunk.SearchDemoActivity;
import com.ebay.skunk.data.ChosenItemsManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class CompareButtonSprite extends TimeLineSprite{

	public static final float PRESSED_ALPHA = 0.3f;
	public static final float HOLD_ALPHA = 0.2f;
	private boolean hold;
	private static Context context;
	
	public CompareButtonSprite(Context c)
	{
		this.getAnchor().set(Anchor.CENTER);
//		this.setBorderColor(255, 255, 255);
//		this.setBorderWidth(3);
		this.setColor(0, 0, 0);
		this.context=c;

		TexManager texMgr = Director.getTexManager();
		Texture texture = new Texture(null);
		Bitmap bitmap = BitmapFactory.decodeResource(c.getResources(), R.drawable.compare);
		texture.setBitmap(bitmap);
		texture.setLefTop(new Point(0,0));
		texture.setSize(new Size(bitmap.getWidth(),bitmap.getHeight()));
		texture.onLoadTexture();
		this.setTexture(texture);
		this.setSize(bitmap.getWidth(),bitmap.getHeight());
		
//		SignInManager.setOAuthURI("wewe-wewe2d6d5-5eb9--vhkwktbd");
	}
	
	public void onUpdate(Time time) {
		if (this.isPressed()) {
			this.setAlpha(PRESSED_ALPHA);
		} else if (this.hold) {
			this.setAlpha(HOLD_ALPHA);
		} else {
			this.setAlpha(1f);
		}
		super.onUpdate(time);
	}
	
	public void setHold(boolean h)
	{
		this.hold=h;
	}
	
	public boolean getHold()
	{
		return this.hold;
	}
	
	public void onSelected(Toast toast)
	{
		this.setPressed(false);
		if(toast != null)
		{
			toast.show();
			return;
		}
		Intent intent = new Intent((ResultListActivity)context,CompareActivity.class);
		((ResultListActivity) context).startActivityForResult(intent,0);
		
//		onSignInOutClick();
		
	}
	
//	public void onSignInOutClick() {
//		try {
//			com.ebay.skunk.data.AppUtil.SignInOrOut((Activity) context);
//		} catch (ServiceClientException e) {
//			com.ebay.skunk.data.AppUtil.showMessageDialog("fail to sign in : " + e.getMessage(),
//					(Activity) context);
//		}
//	}
}
