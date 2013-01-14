package com.ebay.skunk;

import java.util.ArrayList;

import org.ebay.msif.core.ServiceCallback;
import org.ebay.msif.core.ServiceClientException;
import org.ebay.msif.core.ServiceResponse;

import com.ebay.msdk.SDKInitializer;
import com.ebay.msdk.affiliate.AffiliateTrackingManager;
import com.ebay.msdk.error.eBaySDKErrors;
import com.ebay.services.trading.AddToWatchListRequestType;
import com.ebay.services.trading.AddToWatchListResponseType;
import com.ebay.services.trading.client.eBayAPIInterfaceServiceClient;
import com.ebay.skunk.data.BitmapGroupManager;
import com.ebay.skunk.data.ChosenItemsManager;
import com.ebay.skunk.data.SearchItem;
import com.ebay.types.common.trading.AckCodeType;
import com.ebay.types.common.trading.ErrorType;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Gallery.LayoutParams;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;


public class CompareActivity extends Activity {
	public GetNextItemManager indexManager;
	private GestureDetector mGestureDetector1 = null;
	private GestureDetector mGestureDetector2 = null;
	private GestureDetector mGestureDetector3 = null;
	private GestureDetector mGestureDetector4 = null;
	private ImageSwitcher  mySwitcher1;
	private ImageSwitcher  mySwitcher2;
	private TextSwitcher txtPrice1;
	private TextSwitcher txtPrice2;
	private TextSwitcher txtTime1;
	private TextSwitcher txtTime2;
	private TextSwitcher txtCondition1;
	private TextSwitcher txtCondition2;
	private TextSwitcher txtLocation1;
	private TextSwitcher txtLocation2;
	private ImageSwitcher btnAddToWatch1;
	private ImageSwitcher btnAddToWatch2;
 	public Bitmap[] bm;
 	public Drawable[] itemImages;
	public MyImageFactory mif = new MyImageFactory(this);
	public MyTextFactory mtf = new MyTextFactory(this);
	private Context context;
	private int[] index = new int[2];
	private eBayAPIInterfaceServiceClient m_tradingClient;
	private Toast showMsg;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                      WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.comparator);
        context = this;
        showMsg = Toast.makeText(this, null, Toast.LENGTH_SHORT);
        AffiliateTrackingManager affTrackManager;
		try {
			affTrackManager = SDKInitializer.getAffiliateTrackingManager(this);
			affTrackManager.setCampaignID("5336725725");
			affTrackManager.setCustomID("ANDROIDREFAPP");
		} catch (ServiceClientException e1) {
			e1.printStackTrace();
		}

       bm = new Bitmap[ChosenItemsManager.getInstance().getCount()];
       itemImages = new Drawable[ChosenItemsManager.getInstance().getCount()];
       for(int i=0;i<itemImages.length;i++)
       {
    	   Bitmap bitmap = BitmapGroupManager.getInstance().get(ChosenItemsManager.getInstance().getSearchItem(i).getURL());
    	   if(bitmap == null)
    		   bm[i]=BitmapFactory.decodeResource(this.getResources(), R.drawable.placeholder);
    	   else
    		   bm[i]=bitmap;
      	   itemImages[i]=new BitmapDrawable(bm[i]);
       }
       indexManager = new GetNextItemManager(itemImages.length);
       Animation in = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
       Animation out = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);
        
        txtPrice1 = (TextSwitcher) findViewById(R.id.txt_price1);
        txtPrice1.setFactory(mtf);
        txtPrice1.setInAnimation(in);
        txtPrice1.setOutAnimation(out);

        txtTime1 = (TextSwitcher) findViewById(R.id.txt_time1);
        txtTime1.setFactory(mtf);
        txtTime1.setInAnimation(in);
        txtTime1.setOutAnimation(out);
        
        txtLocation1 = (TextSwitcher) findViewById(R.id.txt_location1);
        txtLocation1.setFactory(mtf);
        txtLocation1.setInAnimation(in);
        txtLocation1.setOutAnimation(out);
        
        txtCondition1 = (TextSwitcher) findViewById(R.id.txt_condition1);
        txtCondition1.setFactory(mtf);
        txtCondition1.setInAnimation(in);
        txtCondition1.setOutAnimation(out);
        
        txtPrice2 = (TextSwitcher) findViewById(R.id.txt_price2);
        txtPrice2.setFactory(mtf);
        txtPrice2.setInAnimation(in);
        txtPrice2.setOutAnimation(out);
        
        txtTime2 = (TextSwitcher) findViewById(R.id.txt_time2);
        txtTime2.setFactory(mtf);
        txtTime2.setInAnimation(in);
        txtTime2.setOutAnimation(out);
        
        txtLocation2 = (TextSwitcher) findViewById(R.id.txt_location2);
        txtLocation2.setFactory(mtf);
        txtLocation2.setInAnimation(in);
        txtLocation2.setOutAnimation(out);
        
        txtCondition2 = (TextSwitcher) findViewById(R.id.txt_condition2);
        txtCondition2.setFactory(mtf);
        txtCondition2.setInAnimation(in);
        txtCondition2.setOutAnimation(out);

        btnAddToWatch1 = (ImageSwitcher) findViewById(R.id.addToWatch1);
        btnAddToWatch1.setFactory(mif);
        btnAddToWatch1.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
        btnAddToWatch1.setOutAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));
        btnAddToWatch1.setImageDrawable(new BitmapDrawable(BitmapFactory.decodeResource(this.getResources(), R.drawable.watch06)));
        mGestureDetector3 = new GestureDetector(new LearnGestureListener(3));
        btnAddToWatch1.setClickable(true);
        btnAddToWatch1.setOnTouchListener(new OnTouchListener(){
        	public boolean onTouch(View v,MotionEvent event){
        		return mGestureDetector3.onTouchEvent(event);
        	}
        });
        
        btnAddToWatch2 = (ImageSwitcher) findViewById(R.id.addToWatch2);
        btnAddToWatch2.setFactory(mif);
        btnAddToWatch2.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
        btnAddToWatch2.setOutAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));
        btnAddToWatch2.setImageDrawable(new BitmapDrawable(BitmapFactory.decodeResource(this.getResources(), R.drawable.watch07)));
        mGestureDetector4 = new GestureDetector(new LearnGestureListener(4));
        btnAddToWatch2.setClickable(true);
        btnAddToWatch2.setOnTouchListener(new OnTouchListener(){
        	public boolean onTouch(View v,MotionEvent event){
        		return mGestureDetector4.onTouchEvent(event);
        	}
        });
        
        mGestureDetector1 = new GestureDetector(new LearnGestureListener(1));
        mGestureDetector2 = new GestureDetector(new LearnGestureListener(2));
        mySwitcher1 = (ImageSwitcher)findViewById(R.id.switcher1);
        mySwitcher1.setFactory(mif);
        mySwitcher1.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
        mySwitcher1.setOutAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));
        mySwitcher1.setImageDrawable(itemImages[indexManager.getNext()]);
        updateUI(ChosenItemsManager.getInstance().getSearchItem(indexManager.getCurrent()),1);
        index[0] = indexManager.getCurrent();
        mySwitcher1.setOnTouchListener(new OnTouchListener(){
        	public boolean onTouch(View v,MotionEvent event){
        		return mGestureDetector1.onTouchEvent(event);
        	}
        });

        mySwitcher2 = (ImageSwitcher)findViewById(R.id.switcher2);
        mySwitcher2.setFactory(mif);
        mySwitcher2.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
        mySwitcher2.setOutAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));
        mySwitcher2.setImageDrawable(itemImages[indexManager.getNext()]);
		updateUI(ChosenItemsManager.getInstance().getSearchItem(indexManager.getCurrent()),2);
		index[1] = indexManager.getCurrent();
        mySwitcher2.setOnTouchListener(new OnTouchListener(){
        	public boolean onTouch(View v,MotionEvent event){
        		return mGestureDetector2.onTouchEvent(event);
        	}
        });
        m_tradingClient = new eBayAPIInterfaceServiceClient(this);
    }
    
    private void addToWatchList(final SearchItem item,final int comp)
			throws Exception {
		AddToWatchListRequestType req = new AddToWatchListRequestType();
		req.outputSelector = new ArrayList<String>();
		req.itemID = new ArrayList<String>();
		req.itemID.add(item.getId());
		Log.e("AddToWatchList", "" + item.getId());

		// SDK: make AddToWatchList API call
		m_tradingClient.AddToWatchList(req,
				new ServiceCallback<AddToWatchListResponseType>() {
					public void onResponse(
							ServiceResponse<AddToWatchListResponseType> response) {
						if (response.hasErrors()) {
							Log.e("addToWatchList",response.getErrors().get(0).message);
							if (response.getErrors().get(0).errorId != eBaySDKErrors.USER_ABORTED_SIGNIN.errorId)
							{
								try {
									new AlertDialog.Builder(context).setTitle(R.string.app_name)
											.setMessage( response.getResponseData().errors.get(0).shortMessage)
											.setPositiveButton("Close", null).show();
								} catch (Exception reallyBadTimes) {
									Log.e("AddToWatchList", "showErrorDialog", reallyBadTimes);
								}
							}
						} else if (response.getResponseData().ack == AckCodeType.Failure) {
							ErrorType error = response.getResponseData().errors
									.get(0);
							try {
								new AlertDialog.Builder(context).setTitle(R.string.app_name)
										.setMessage( error.shortMessage)
										.setPositiveButton("Close", null).show();
								item.setIsAddToWatch(true);
								updateUI(item, comp);
							} catch (Exception reallyBadTimes) {
								Log.e("AddToWatchList", "showErrorDialog", reallyBadTimes);
							}
						} else {
							showMsg.setText("Item added to watch list");
							showMsg.show();
							item.setIsAddToWatch(true);
							updateUI(item, comp);
//							btnAddToWatch1.setImageDrawable(new BitmapDrawable(BitmapFactory.decodeResource(context.getResources(), R.drawable.add)));
//							btnAddToWatch1.setEnabled(false);
						}
					}
				});
	}

    public class LearnGestureListener implements GestureDetector.OnGestureListener {
    	
    	private int id;
    	
    	public LearnGestureListener(int id)
    	{
    		super();
    		this.id=id;
    	}
    	
   	 @Override
        public boolean onSingleTapUp(MotionEvent ev) {
   		 	Log.v("onSingleTapUp", "onSingleTapUp");
   		 	if(id == 3)
   		 	{
   		 		if(ChosenItemsManager.getInstance().getSearchItem(index[0]).getIsAddToWatch())
   		 		{
   		 			Log.v("onSingleTapUp", "should already in the list");
//   		 			ChosenItemsManager.getInstance().getSearchItem(index[0]).setIsAddToWatch(false);
   		 			try {
		 				addToWatchList(ChosenItemsManager.getInstance().getSearchItem(index[0]), 3);
		 			} catch (Exception e) {
		 				e.printStackTrace();
		 			}
   		 			updateUI(ChosenItemsManager.getInstance().getSearchItem(index[0]), 3);
   		 		}
   		 		else
   		 		{
   		 			Log.v("onSingleTapUp", "should add to list");
   		 			try {
   		 				addToWatchList(ChosenItemsManager.getInstance().getSearchItem(index[0]), 3);
   		 			} catch (Exception e) {
   		 				e.printStackTrace();
   		 			}
//   		 			ChosenItemsManager.getInstance().getSearchItem(index[0]).setIsAddToWatch(true);
   		 			updateUI(ChosenItemsManager.getInstance().getSearchItem(index[0]), 3);
   		 		}
   		 	}
   		 	else if(id == 4)
		 	{
		 		if(ChosenItemsManager.getInstance().getSearchItem(index[1]).getIsAddToWatch())
		 		{
		 			Log.v("onSingleTapUp", "should already in the list");
//		 			ChosenItemsManager.getInstance().getSearchItem(index[0]).setIsAddToWatch(false);
		 			try {
		 				addToWatchList(ChosenItemsManager.getInstance().getSearchItem(index[1]), 4);
		 			} catch (Exception e) {
		 				e.printStackTrace();
		 			}
		 			updateUI(ChosenItemsManager.getInstance().getSearchItem(index[1]), 4);
		 		}
		 		else
		 		{
		 			Log.v("onSingleTapUp", "should add to list");
		 			try {
		 				addToWatchList(ChosenItemsManager.getInstance().getSearchItem(index[1]), 4);
		 			} catch (Exception e) {
		 				e.printStackTrace();
		 			}
//		 			ChosenItemsManager.getInstance().getSearchItem(index[0]).setIsAddToWatch(true);
		 			updateUI(ChosenItemsManager.getInstance().getSearchItem(index[1]), 4);
		 		}
		 	}
            return true;
        }

        @Override
        public void onShowPress(MotionEvent ev) {
        	Log.v("LearnGestureListener", "onShowPress");
        }

        @Override
        public void onLongPress(MotionEvent ev) {
        	try
        	{
				SDKInitializer.getAffiliateTrackingManager(context).launchViewItemPage(
						ChosenItemsManager.getInstance().getSearchItem(index[id-1]).getId());
			}
        	catch (ServiceClientException e)
        	{
				Log.e("Compare", "cannot launch the web browser!");
			}
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                float distanceX, float distanceY) {
            return true;
        }

        @Override
        public boolean onDown(MotionEvent ev) {
        	Log.v("LearnGestureListener", "onDown");
        	Log.v("LearnGestureListener", "" + ev.getRawX());
        	Log.v("LearnGestureListener", "" + ev.getRawY());
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                float velocityY) {
        	if(id == 1)
        	{
        		mySwitcher1.setImageDrawable(itemImages[indexManager.getNext()]);
              	updateUI(ChosenItemsManager.getInstance().getSearchItem(indexManager.getCurrent()),1);
              	index[0] = indexManager.getCurrent();
        	}
        	else
        	{
        		mySwitcher2.setImageDrawable(itemImages[indexManager.getNext()]);
              	updateUI(ChosenItemsManager.getInstance().getSearchItem(indexManager.getCurrent()),2);
              	index[1] = indexManager.getCurrent();
        	}
            return true;
        }
        public boolean onDoubleTap(MotionEvent event){
            return true;
        }

   }
    // Implements the ViewFactory interface, which object will be used to generate 
    public class MyTextFactory implements ViewFactory {
    	private Context myContext;
    	public MyTextFactory(Context context){
    		this.myContext = context;
    	}
    	@Override
        public View makeView() {
            TextView t = new TextView(myContext);
            t.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
            t.setTextSize(24);
            t.setMaxLines(1);
            t.setTextColor(Color.WHITE);
            return t;
        }
    }
    
    public class MyImageFactory implements ViewFactory {
    	private Context myContext;
    	public MyImageFactory(Context context){
    		this.myContext = context;
    	}
    	@Override
        public View makeView(){
        	ImageView i=new ImageView(myContext);
        	i.setBackgroundColor(0xFF000000);
        	i.setScaleType(ImageView.ScaleType.FIT_CENTER);
        	i.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
        	return i;
        }
    }

	public void updateUI(SearchItem item, int comparator){
		if (comparator == 1)
		{
			txtPrice1.setText("Price: " + String.valueOf(item.getPrice()));
			txtTime1.setText("Time Left: " + SearchItem.formatDuration(item.getTimeLeft()));
			txtCondition1.setText("Condition: " + item.getCondition());
			txtLocation1.setText("Location: " + item.getLocation());
			updateUI(item, 3);
		}
		else if(comparator == 2)
		{
			txtPrice2.setText("Price: " + String.valueOf(item.getPrice()));
			txtTime2.setText("Time Left: " + SearchItem.formatDuration(item.getTimeLeft()));
			txtCondition2.setText("Condition: " + item.getCondition());
			txtLocation2.setText("Location: " + item.getLocation());
			updateUI(item, 4);
//			if(item.getIsAddToWatch())
//				btnAddToWatch2.setText("Remove From Watch List");
//			else
//				btnAddToWatch2.setText("Add To Watch List");
//			String payments = "";
//			for (BuyerPaymentMethodCodeType payment : item.paymentMethods) {
//				payments += "," + payment;
//			}
//			payments = payments.replaceFirst(",", "");
//			txtPayment2.setText(payments);
//			String shippingCost = "NA";
//			if (item.shippingCostSummary != null
//					&& item.shippingCostSummary.shippingServiceCost != null) {
//				shippingCost = String.valueOf(item.shippingCostSummary.shippingServiceCost.value);
//			}
//			txtShipping2.setText(shippingCost);
		}
		else if(comparator == 3)
		{
			if(item.getIsAddToWatch())
			{
				btnAddToWatch1.setImageDrawable(new BitmapDrawable(BitmapFactory.decodeResource(context.getResources(), R.drawable.watch05)));
				btnAddToWatch1.setEnabled(false);
			}
			else
			{
				btnAddToWatch1.setImageDrawable(new BitmapDrawable(BitmapFactory.decodeResource(context.getResources(), R.drawable.watch07)));
				btnAddToWatch1.setEnabled(true);
			}
		}
		else if(comparator == 4)
		{
			if(item.getIsAddToWatch())
			{
				btnAddToWatch2.setImageDrawable(new BitmapDrawable(BitmapFactory.decodeResource(context.getResources(), R.drawable.watch04)));
				btnAddToWatch2.setEnabled(false);
			}
			else
			{
				btnAddToWatch2.setImageDrawable(new BitmapDrawable(BitmapFactory.decodeResource(context.getResources(), R.drawable.watch06)));
				btnAddToWatch2.setEnabled(true);
			}
		}
		
	}

	public class GetNextItemManager{
		int index;
		int max;
		GetNextItemManager(int m){
			index = 0;
			max = m;
		}
		public int getNext(){
			int current = index%max;
			index++;
			return current;
		}
		public int getCurrent(){
			int current = index%max;
			return current;
		}
	}
}
