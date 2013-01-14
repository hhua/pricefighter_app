package com.ebay.skunk;

import java.io.IOException;

import org.kevinth.kth2d.Director;
import org.kevinth.kth2d.GameActivity;
import org.kevinth.kth2d.GameConfig;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ebay.skunk.data.BitmapGroupManager;
import com.ebay.skunk.data.Searcher;

public class ResultListActivity extends GameActivity {

	public Searcher searcher = null;
	public ResultListScene scene = null;
	private String keyword = null;
	private Toast toast;

	@Override
	protected void onInit() {
		Director.getDebugger().enable();
		Director.getDebugger().setEnableFPS(true);

		Director.getConfig().setRendererType(GameConfig.RENDERER_DEFAULT);
		
		searcher = new Searcher();
		scene = new ResultListScene();
		searcher.setHandler(scene);
		scene.setSearcher(searcher);
		
		Intent intent = getIntent();
		Bundle b = intent.getBundleExtra("newKW");
		keyword = b.getString("Keywords");
		
		searcher.setDefaultBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.placeholder));
		searcher.setContext(this);
		initToast();
		
		Director.setCurrentScene(scene);
		searcher.onResponse(keyword, this);
		scene.setContext(this);
	}
	
	private void initToast()
	{
		toast = Toast.makeText(getApplicationContext(), "add", Toast.LENGTH_SHORT);
		scene.registerToast("add", toast);
		toast = Toast.makeText(getApplicationContext(), "remove", Toast.LENGTH_SHORT);
		scene.registerToast("remove", toast);
		toast = Toast.makeText(getApplicationContext(), "Please choose items", Toast.LENGTH_SHORT);
		scene.registerToast("none", toast);
		toast = Toast.makeText(getApplicationContext(), "Please choose at least 2 items", Toast.LENGTH_SHORT);
		scene.registerToast("one", toast);
		toast = Toast.makeText(getApplicationContext(), "Loading...Please wait", Toast.LENGTH_SHORT);
		scene.registerToast("loading", toast);
		
//		toast = Toast.makeText(this,"Added to Compare Trolley", Toast.LENGTH_SHORT);
//		toast.setGravity(Gravity.BOTTOM, 0, 0);
//		LinearLayout toastView = (LinearLayout) toast.getView();
//		ImageView imageCodeProject = new ImageView(this);
//		imageCodeProject.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.add));
//		toastView.addView(imageCodeProject, 0);
//		scene.setAddToast(toast);
//		
//		toast = Toast.makeText(this,"Removed from Compare Trolley", Toast.LENGTH_SHORT);
//		toast.setGravity(Gravity.BOTTOM, 0, 0);
//		toastView = (LinearLayout) toast.getView();
//		imageCodeProject = new ImageView(this);
//		imageCodeProject.setImageBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.add));
//		toastView.addView(imageCodeProject, 0);
//		scene.setRemoveToast(toast);
	}

}
