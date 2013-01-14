package com.ebay.skunk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.kevinth.kth2d.BasicTexNode;
import org.kevinth.kth2d.Director;
import org.kevinth.kth2d.Loading;
import org.kevinth.kth2d.Scene;
import org.kevinth.kth2d.Sprite;
import org.kevinth.kth2d.Time;
import org.kevinth.kth2d.event.IKeyEventListener;
import org.kevinth.kth2d.event.ITouchEventListener;
import org.kevinth.kth2d.event.TouchEvent;
import org.kevinth.kth2d.geometry.Anchor;
import org.kevinth.kth2d.geometry.Point;
import org.kevinth.kth2d.geometry.Size;
import org.kevinth.kth2d.texture.TexManager;
import org.kevinth.kth2d.texture.Texture;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Toast;

import com.ebay.skunk.data.ChosenItemsManager;
import com.ebay.skunk.data.IDataHandler;
import com.ebay.skunk.data.SearchItem;
import com.ebay.skunk.data.Searcher;
import com.ebay.skunk.sprite.CompareButtonSprite;
import com.ebay.skunk.sprite.ItemGroupSprite;
import com.ebay.skunk.sprite.ItemSprite;
import com.ebay.skunk.sprite.LoadingPicSprite;
import com.ebay.skunk.sprite.LoadingSprite;
import com.ebay.skunk.sprite.LoadingTextSprite;
import com.ebay.skunk.sprite.TimeLineSprite;

public class ResultListScene extends Scene implements IDataHandler {
	public static final Size ITEM_IMAGE_SIZE = new Size(110, 100);
	public static final float GROUP_INTERVAL_DEGREES = 26;
	public static final float GROUP_START_DEGREES = 225;
	public static final float ITEM_INTERVAL_DEGREES = 11.5f;
	public static final float ITEM_START_DEGREES = 203;
	public static final int ITEM_START_COUNT = 8;
	public static final int MIN_TIMELINE_REMAIN = 2;
	public static final Point GROUP_PATH_ORI = new Point(520, 120);
	public static final Size GROUP_PATH_SIZE = new Size(840, 520);
	public static final Point SHOW_PATH_ORI = new Point(700, 240);
	public static final Size SHOW_PATH_SIZE = new Size(1000, 1000);
	public static final float GROUP_VISIBLE_START_ANGLE = 135;
	public static final float GROUP_VISIBLE_END_ANGLE = 359;
	public static final float SHOW_VISIBLE_START_ANGLE = 135;
	public static final float SHOW_VISIBLE_END_ANGLE = 225;

	private MoveTimeline groupTimeline = new MoveTimeline();
	private MoveTimeline itemTimeline = new MoveTimeline();

	private BasicTexNode background = null;
	private LinkedList<ItemGroupSprite> groupItems = new LinkedList<ItemGroupSprite>();
	private LinkedList<ItemSprite> showItems = new LinkedList<ItemSprite>();
	private CompareButtonSprite compareButton;
	private LoadingPicSprite loadingPicSprite;
	private LoadingTextSprite loadingTextSprite;
	
	private final List<List<SearchItem>> groupedItemData = new ArrayList<List<SearchItem>>();
	private final List<SearchItem> incrementalData = Collections
			.synchronizedList(new LinkedList<SearchItem>());
	private Map<String, Toast> toasts = new HashMap<String, Toast>();
	private boolean rotateClockwise = true;

	private boolean appearing = false;

	public static final int STATUS_GROUP_LIST = 1;
	public static final int STATUS_GROUP_SHOW = 2;
	private int sceneStatus = STATUS_GROUP_LIST;
	private ItemGroupSprite selectedGroup = null;
	private ItemSprite selectedItem = null;
	private boolean clickButton=false;
	private final static int SHOW_ITEM_LAYER = 10;
	private Searcher searcher = null;
	private Context context;
	private boolean isLoading = true;
	
	public Searcher getSearcher() {
		return searcher;
	}
	
	public void registerToast(String name, Toast t)
	{
		toasts.put(name, t);
	}

	public void setSearcher(Searcher searcher) {
		this.searcher = searcher;
	}

	public int getSceneStatus() {
		return sceneStatus;
	}
	
	public void setContext(Context c)
	{
		this.context=c;
	}
	
	public Context getContext()
	{
		return context;
	}

	public void setSceneStatus(int sceneStatus) {
		this.sceneStatus = sceneStatus;
	}

	protected void onLoad(Loading loading) {
		super.onLoad(loading);
		
		Director.setGameViewSize(800, 480);

		TexManager texMgr = Director.getTexManager();
		Texture texBg = new Texture(R.drawable.bg2);
		texMgr.register(texBg);

		background = new BasicTexNode();
		background.getSize().set(Director.getGameViewSize());
		background.setColor(0, 0, 0);
		background.setTexture(texBg);
		this.addNode(0, background);
		
		compareButton = new CompareButtonSprite(context);
		this.addNode(10,compareButton);
		compareButton.setPosition(650, 100);
		compareButton.activate();
		
		loadingPicSprite = new LoadingPicSprite(context);
		texMgr.register(loadingPicSprite.getRunTexatlas());
		this.addNode(11, loadingPicSprite);
		loadingPicSprite.setPosition(400, 220);
		loadingPicSprite.activate();
		
		loadingTextSprite = new LoadingTextSprite(context);
		texMgr.register(loadingTextSprite.getRunTexatlas());
		this.addNode(11, loadingTextSprite);
		loadingTextSprite.setPosition(400, 260);
		loadingTextSprite.activate();

		this.setupLayerCount(SHOW_ITEM_LAYER);

		groupTimeline.setPathOri(GROUP_PATH_ORI);
		groupTimeline.setPathSize(GROUP_PATH_SIZE);
		groupTimeline.setIntervalAngle(GROUP_INTERVAL_DEGREES);
		groupTimeline.setIndexIncremental(true);
		groupTimeline.setAngle(GROUP_START_DEGREES);
		itemTimeline.setPathOri(SHOW_PATH_ORI);
		itemTimeline.setPathSize(SHOW_PATH_SIZE);
		itemTimeline.setIntervalAngle(ITEM_INTERVAL_DEGREES);
		itemTimeline.setIndexIncremental(false);
		itemTimeline.setAngle(ITEM_START_DEGREES);
	}

	private void removeGroupItem(ItemGroupSprite item) {
		item.stopAllActions();
		item.getLayer().remove(item);
		groupItems.remove(item);
	}
	
	private void removeLoadingSprite()
	{
		loadingPicSprite.stopAllActions();
		loadingPicSprite.getLayer().remove(loadingPicSprite);
		loadingTextSprite.stopAllActions();
		loadingTextSprite.getLayer().remove(loadingTextSprite);
	}

	private void removeShowItem(ItemSprite item) {
		item.stopAllActions();
		item.getLayer().remove(item);
		showItems.remove(item);
	}

	public void onUpdateGroupSprites() {
		float angle = groupTimeline.getAngle();
		int sIdx = 0;
		float a = angle;
		while (a <= GROUP_VISIBLE_START_ANGLE) {
			a += GROUP_INTERVAL_DEGREES;
			sIdx++;
		}
		int eIdx = sIdx;
		while (a < GROUP_VISIBLE_END_ANGLE) {
			a += GROUP_INTERVAL_DEGREES;
			eIdx++;
		}

		if (groupItems.size() > 0) {
			int sIdxA = groupedItemData
					.indexOf(groupItems.getFirst().getData());
			int eIdxA = groupedItemData.indexOf(groupItems.getLast().getData());
			for (int i = sIdxA; i < sIdx; i++) {
				int idxR = groupedItemData.indexOf(groupItems.getFirst()
						.getData());
				if (idxR == i)
					removeGroupItem(groupItems.getFirst());
			}
			for (int i = eIdxA; i > eIdx; i--) {
				int idxR = groupedItemData.indexOf(groupItems.getLast()
						.getData());
				if (idxR == i)
					removeGroupItem(groupItems.getLast());
			}
		}
		int c = Math.min(eIdx, groupedItemData.size());
		for (int i = sIdx; i < c; i++) {
			checkAndCreateGroupSprite(groupedItemData.get(i));
		}
	}

	public void checkAndCreateGroupSprite(List<SearchItem> items) {
		if (items.size() == 0)
			return;

		removeLoadingSprite();
		isLoading = false;
		
		int grade = searcher.getGrade(items.get(0));
		int i = 0;
		for (; i < groupItems.size(); i++) {
			int g = groupItems.get(i).getGrade();
			if (g == grade)
				return;
			else if (g > grade) {
				break;
			}
		}
		this.groupItems.add(i, createGroupSprite(items));
	}

	public void checkAndCreateItemSprite(SearchItem item) {
		for (ItemSprite sprite : showItems) {
			if (sprite.getItem() == item)
				return;
		}
		ItemSprite sprite = new ItemSprite(item, selectedGroup,context);
		sprite.getSize().set(selectedGroup.getSize());
		this.addNode(SHOW_ITEM_LAYER, sprite);
		sprite.initShowSingle();
		if(!this.appearing)
			sprite.startShowSingle();
		sprite.activate();
		this.showItems.addLast(sprite);
	}

	public void onUpdateShowSprites() {
		List<SearchItem> list = selectedGroup.getData();
		float angle = itemTimeline.getAngle();
		int sIdx = 0;
		float a = angle;
		while (a >= SHOW_VISIBLE_END_ANGLE) {
			a -= ITEM_INTERVAL_DEGREES;
			sIdx++;
		}
		int eIdx = sIdx;
		while (a > SHOW_VISIBLE_START_ANGLE) {
			a -= ITEM_INTERVAL_DEGREES;
			eIdx++;
		}

		if (showItems.size() > 0) {
			int sIdxA = list.indexOf(showItems.getFirst().getItem());
			int eIdxA = list.indexOf(showItems.getLast().getItem());
			for (int i = sIdxA; i < sIdx; i++) {
				int idxR = list.indexOf(showItems.getFirst().getItem());
				if (idxR == i)
					removeShowItem(showItems.getFirst());
			}
			for (int i = eIdxA; i > eIdx; i--) {
				int idxR = list.indexOf(showItems.getLast().getItem());
				if (idxR == i)
					removeShowItem(showItems.getLast());
			}
		}
		int c = Math.min(eIdx, list.size());
		for (int i = sIdx; i < c; i++) {
			checkAndCreateItemSprite(list.get(i));
		}
	}

	public void onUpdateData() {
		while (this.incrementalData.size() > 0) {
			SearchItem si = this.incrementalData.remove(0);
			if (si != null) {
				int grade = searcher.getGrade(si);
				int i = 0;
				for (; i < groupedItemData.size(); i++) {
					List<SearchItem> list = groupedItemData.get(i);
					SearchItem sample = list.get(0);
					int g = searcher.getGrade(sample);
					if (grade == g) {
						groupedItemData.get(i).add(list.size(), si);
						return;
					} else if (grade < g) {
						break;
					}
				}
				List<SearchItem> newList = new ArrayList<SearchItem>();
				groupedItemData.add(i, newList);
				newList.add(si);
				if (i == 0) {
					groupTimeline.setAngle(groupTimeline.getAngle()
							- GROUP_INTERVAL_DEGREES);
				}

			}
		}
	}

	public void endShowAppear() {
		for (ItemSprite item : this.showItems) {
			if (item.isAppearing())
				item.endAppear();
		}
		if (this.appearing == true) {
			for (ItemSprite item : this.showItems) {
				item.startShowSingle();
			}
			this.appearing = false;
		}
	}

	public void endShowBackAppear() {
		for (ItemSprite item : this.showItems) {
			if (item.isAppearing())
				item.endAppear();
		}
		if (this.appearing == true) {
			for (ItemSprite item : this.showItems) {
				item.stopAllActions();
				this.getLayer(SHOW_ITEM_LAYER).remove(item);
			}
			showItems.clear();

			if (selectedGroup != null) {
				selectedGroup.startGroupList();
				selectedGroup.setPressed(false);
				selectedGroup = null;
			}
			
			if(clickButton)
				compareButton.setPressed(false);

			for (TimeLineSprite item : groupItems) {
				((ItemGroupSprite) item).setHold(false);
			}
			compareButton.setHold(false);
			this.setSceneStatus(STATUS_GROUP_LIST);
			this.appearing = false;
		}
	}

	public void showSingleGroupItem() {
		//		int c = Math.min(selectedGroup.getItems().size(), ITEM_START_COUNT);
		//		if (c == 0)
		//			return;
		//		float startDe = Math.min(180 + (float) (c - 1) * ITEM_INTERVAL_DEGREES
		//				/ 2, ITEM_START_DEGREES);
		//		this.appearing = true;
		//		for (int i = 0; i < c; i++) {
		//			ItemSprite item = selectedGroup.getItems().get(i);
		//			this.addNode(SHOW_ITEM_LAYER, item);
		//			showItems.add(item);
		//			item.initShowSingle(startDe - i * ITEM_INTERVAL_DEGREES);
		//			item.appearShow();
		//		}
		int c = Math.min(selectedGroup.getData().size(), ITEM_START_COUNT);
		if (c == 0)
			return;
		float startDe = Math.min(180 + (float) (c - 1) * ITEM_INTERVAL_DEGREES
				/ 2, ITEM_START_DEGREES);
		itemTimeline.setAngle(startDe);
		this.appearing = true;
		onUpdateShowSprites();
		List<SearchItem> list = selectedGroup.getData();
		for (ItemSprite sprite : showItems) {
			int index = list.indexOf(sprite.getItem());
			itemTimeline.setupPosition(sprite.getPosition(), index);
			sprite.appearShow();
		}
	}

	public void clearSignleGroupItem() {
		this.appearing = true;
		for (ItemSprite item : showItems) {
			item.endShowSingle();
			item.appearShowBack();
		}
	}

	public ItemGroupSprite createGroupSprite(List<SearchItem> data) {
		if (data.size() == 0)
			return null;
		int grade = searcher.getGrade(data.get(0));
		double value = searcher.getGradeValue(grade);
		double valueNext = searcher.getGradeValue(grade+1);
		if(grade>=searcher.getGradeCount()-1)
			valueNext=0;
		ItemGroupSprite sprite = new ItemGroupSprite(context);
		sprite.setGrade(grade);
		sprite.setGradeType(searcher.getGradeType());
		sprite.setGradeValue(value);
		sprite.setGradeValueNext(valueNext);

		sprite.setScaleRatio(sprite.getCurrentScaleRatio());
		sprite.getSize().set(ITEM_IMAGE_SIZE);
		sprite.setData(data);

		if (STATUS_GROUP_SHOW == this.sceneStatus) {
			sprite.setHold(true);
			compareButton.setHold(true);
		}
		this.addNode(2, sprite);
		sprite.activate();

		return sprite;
	}

	@Override
	public void onUpdate(Time time) {
		onUpdateData();
		if (!appearing) {
			if (STATUS_GROUP_LIST == this.sceneStatus) {
				onUpdateGroupSprites();
				groupTimeline.onUpdate(time);
				for (ItemGroupSprite sprite : this.groupItems) {
					int index = groupedItemData.indexOf(sprite.getData());
					groupTimeline.setupPosition(sprite.getPosition(), index);
				}
			} else if (STATUS_GROUP_SHOW == this.sceneStatus) {
				onUpdateShowSprites();
				itemTimeline.onUpdate(time);
				List<SearchItem> list = selectedGroup.getData();
				for (ItemSprite sprite : this.showItems) {
					int index = list.indexOf(sprite.getItem());
					itemTimeline.setupPosition(sprite.getPosition(), index);
				}
			}
		}

		super.onUpdate(time);		
	}

	public TimeLineSprite pickUpItem(float x, float y,
			List<? extends TimeLineSprite> items) {
		for (TimeLineSprite item : items) {
			if (item.isPointOn(x, y))
				return (TimeLineSprite) item;
		}
		return null;
	}
	
	public boolean isOnButton(float x, float y)
	{
		return compareButton.isPointOn(x, y);
	}

	public void switchSceneStatus(int sceneStatus) {
		groupTimeline.stopAcc();
		itemTimeline.stopAcc();
		for (Sprite item : groupItems) {
			item.stopAllActions();
		}
		for (Sprite item : showItems) {
			item.stopAllActions();
		}
		compareButton.stopAllActions();

		if (STATUS_GROUP_LIST == sceneStatus) {
			if (selectedGroup != null) {
				clearSignleGroupItem();
			}
		} else if (STATUS_GROUP_SHOW == sceneStatus) {
			if (selectedGroup != null) {
				selectedGroup.startGroupShow();
				showSingleGroupItem();
			}

			for (TimeLineSprite item : groupItems) {
				((ItemGroupSprite) item).setHold(true);
			}
			compareButton.setHold(true);
			this.setSceneStatus(STATUS_GROUP_SHOW);
		}
	}

	@Override
	protected void onActivate() {
		Director.getEventManager().addTouchListener(new TouchListener());
		Director.getEventManager().addKeyListener(new KeyListener());
	}

	public void onDataComplete(SearchItem[] newItems) {
		for (SearchItem item : newItems) {
			incrementalData.add(item);
		}
	}

	class KeyListener implements IKeyEventListener {

		public void onEvent(KeyEvent event) {
			if (KeyEvent.ACTION_DOWN == event.getAction()
					&& KeyEvent.KEYCODE_BACK == event.getKeyCode()) {
				if (STATUS_GROUP_SHOW == sceneStatus) {
					if (!appearing)
					{
						switchSceneStatus(STATUS_GROUP_LIST);
					}
				} else {
					((Activity) Director.getContext()).finish();
				}
			}
		}

	}

	class TouchListener implements ITouchEventListener {
		public static final int STATUS_NONE = 0;
		public static final int STATUS_DOWN = 1;
		public static final int STATUS_MOVE = 2;
		public static final int MAX_HISTORICAL_COUNT = 5;

		private float lastX = -1;
		private float lastY = -1;
		private float lastD = 0;
		private boolean clockwise = false;
		private LinkedList<Float> hisDQueue = new LinkedList<Float>();

		private int status = STATUS_NONE;

		public float getTotalHisD() {
			float ret = 0;
			for (Float d : hisDQueue)
				ret += d;
			return ret;
		}

		public void valueReset() {
			lastX = 0;
			lastY = 0;
			lastD = 0;
			hisDQueue.clear();
		}

		public void processEvent(TouchEvent event,
				List<? extends TimeLineSprite> items, int sceneStatus) {
			if (appearing)
				return;
			
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				for (TimeLineSprite item : items)
					item.stopAllActions();
				groupTimeline.stopAcc();
				itemTimeline.stopAcc();
				valueReset();
				TimeLineSprite sprite = pickUpItem(event.getX(), event.getY(),items);
				if (sprite != null) {
					sprite.setPressed(true);
				}
				if (STATUS_GROUP_LIST == sceneStatus)
					selectedGroup = (ItemGroupSprite) sprite;
				else if (STATUS_GROUP_SHOW == sceneStatus)
					selectedItem = (ItemSprite) sprite;
				if(sprite ==null)
				{
					if(isOnButton(event.getX(), event.getY()))
					{
						clickButton=true;
						compareButton.setPressed(true);
					}	
				}
				status = STATUS_DOWN;
			} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
				if (lastD > 0) {
					rotateClockwise = clockwise;
					for (TimeLineSprite item : items) {
						item.setPressed(false);
					}
					if (STATUS_GROUP_LIST == sceneStatus) {
						groupTimeline.move(lastD, clockwise);
						selectedGroup = null;
					} else if (STATUS_GROUP_SHOW == sceneStatus) {
						itemTimeline.move(lastD, clockwise);
						selectedItem = null;
					}
					clickButton=false;
				}
				status = STATUS_MOVE;
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				if (status == STATUS_MOVE) {
					float hisD = getTotalHisD();
					if (STATUS_GROUP_LIST == sceneStatus) {
						if (hisD > 0) {
							groupTimeline.startAcc(hisD * 4, clockwise);
							
						}
						if (selectedGroup != null) {
							selectedGroup.setPressed(false);
							selectedGroup = null;
						}
						if(clickButton)
						{
							compareButton.setPressed(false);
							clickButton=false;
						}
					} else if (STATUS_GROUP_SHOW == sceneStatus) {
						if (hisD > 0) {
							itemTimeline.startAcc(hisD * 4, clockwise);
						}
						if (selectedItem != null) {
							selectedItem.setPressed(false);
							selectedItem = null;
						}
					}
				} 
				else if (status == STATUS_DOWN) 
				{
					if (STATUS_GROUP_LIST == sceneStatus && selectedGroup != null)
						switchSceneStatus(STATUS_GROUP_SHOW);
					else if (STATUS_GROUP_SHOW == sceneStatus && selectedItem != null) 
					{
//						if(event.getEventTime() - event.getDownTime() >= 500)//for long press-----time > 500ms, then launch web browser
//						{
//							selectedItem.setPressed(false);
//							selectedItem.onLongPressed();
//						}
//						else//for click ------ add/remove to/from list
//						{
							selectedItem.setPressed(false);
							if(selectedItem.getItem().getIsChosen())
								selectedItem.onSelected(toasts.get("remove"));
							else
								selectedItem.onSelected(toasts.get("add"));
//						}
					}
					else if(STATUS_GROUP_LIST == sceneStatus && clickButton)
					{
						if(isLoading)
							compareButton.onSelected(toasts.get("loading"));
						else if(ChosenItemsManager.getInstance().getCount() == 0)
							compareButton.onSelected(toasts.get("none"));
						else if(ChosenItemsManager.getInstance().getCount() == 1)
							compareButton.onSelected(toasts.get("one"));
						else
							compareButton.onSelected(null);
					}
				}
				status = STATUS_NONE;
				clickButton = false;
			}
		}

		public void onEvent(TouchEvent event) {
			if (STATUS_GROUP_LIST == sceneStatus)
				processEvent(event, groupItems, sceneStatus);
			else if (STATUS_GROUP_SHOW == sceneStatus)
				processEvent(event, showItems, sceneStatus);

			if (lastX > 0 && lastY > 0) {
				float d1 = event.getX() - lastX;
				float d2 = event.getY() - lastY;

				if (STATUS_GROUP_LIST == sceneStatus)
					clockwise = event.getX() < lastX;
				else if (STATUS_GROUP_SHOW == sceneStatus)
					clockwise = event.getY() < lastY;

				lastD = (float) Math.sqrt(d1 * d1 + d2 * d2);
				hisDQueue.addLast(lastD);
				if (hisDQueue.size() > MAX_HISTORICAL_COUNT)
					hisDQueue.removeFirst();
			}
			lastX = event.getX();
			lastY = event.getY();
		}

	}

}
