package com.ebay.skunk;

import java.util.ArrayList;

import org.ebay.msif.core.ServiceCallback;
import org.ebay.msif.core.ServiceClientException;
import org.ebay.msif.core.ServiceResponse;

import com.ebay.msdk.SDKInitializer;
import com.ebay.msdk.error.eBaySDKErrors;
import com.ebay.msdk.security.SignInCallback;
import com.ebay.msdk.security.SignInManager;
import com.ebay.msdk.security.SignInStatus;
import com.ebay.msdk.security.SignOutCallback;
import com.ebay.msdk.security.SignOutStatus;
import com.ebay.services.trading.AddToWatchListRequestType;
import com.ebay.services.trading.AddToWatchListResponseType;
import com.ebay.services.trading.client.eBayAPIInterfaceServiceClient;
import com.ebay.types.common.trading.AckCodeType;
import com.ebay.types.common.trading.ErrorType;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

public class SearchDemoActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                      WindowManager.LayoutParams. FLAG_FULLSCREEN);
		setContentView(R.layout.main);

		SignInManager.setOAuthURI("wewe-wewe2d6d5-5eb9--vhkwktbd");
		
		findViewById(R.id.MainSearch).setOnClickListener(new OnSearchClicked(this));
		
		//----------------add to watch----------------------
//		try
//		{
//			eBayAPIInterfaceServiceClient m_tradingClient = new eBayAPIInterfaceServiceClient(this);
//			
//			AddToWatchListRequestType req = new AddToWatchListRequestType();
//			req.outputSelector = new ArrayList<String>();
//			req.itemID = new ArrayList<String>();
//			req.itemID.add("130536661694");
//
//			// SDK: make AddToWatchList API call
//			m_tradingClient.AddToWatchList(req,
//					new ServiceCallback<AddToWatchListResponseType>() {
//						public void onResponse(
//								ServiceResponse<AddToWatchListResponseType> response) {
//							if (response.hasErrors()) {
//								Log.e("add to watch",
//										response.getErrors().get(0).message);
//								if (response.getErrors().get(0).errorId != eBaySDKErrors.USER_ABORTED_SIGNIN.errorId)
//								{
//									
//								}
//							} else if (response.getResponseData().ack == AckCodeType.Failure) {
//								ErrorType error = response.getResponseData().errors
//										.get(0);
//								if ("21003".equals(error.errorCode)) {
//								}
//							} else {
//							}
//
//						}
//					});
//		}catch(Exception e)
//		{
//			Log.e("Exception",e.getMessage());
//		}
		
		//--------------------------------------------------
	}

	private class OnSearchClicked implements View.OnClickListener {
		private Activity activity;
		
		public OnSearchClicked(Activity a)
		{
			super();
			activity = a;
		}
		
		public void onClick(View v) {
			String keywords = ((EditText) findViewById(R.id.edit_input)).getText().toString();	
			if(keywords == null || keywords.length() == 0)
			{
				AlertDialog.Builder alert = new AlertDialog.Builder(SearchDemoActivity.this);
				alert.setTitle("Alert");
				alert.setMessage("Please enter a keyword!");
				alert.setPositiveButton("Ok",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0,int arg1) {}
						});
				alert.show();
			}
			else
			{
				Bundle data = new Bundle();
				data.putString("Keywords", keywords);
				Intent intent = new Intent(SearchDemoActivity.this,ResultListActivity.class);
	    		intent.putExtra("newKW",data);
	    		startActivityForResult(intent,0);
			}
				
		}
		
	}

}