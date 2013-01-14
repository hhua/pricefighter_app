package com.ebay.skunk.sprite;

import java.util.ArrayList;
import java.util.List;

import org.kevinth.kth2d.Director;
import org.kevinth.kth2d.Layer;
import org.kevinth.kth2d.Scene;
import org.kevinth.kth2d.Time;
import org.kevinth.kth2d.geometry.Anchor;
import org.kevinth.kth2d.geometry.Size;
import org.kevinth.kth2d.geometry.Vector;
import org.kevinth.kth2d.texture.TextTexture;
import org.kevinth.kth2d.texture.Texture;

import android.content.Context;
import android.graphics.Color;
import android.graphics.RectF;

import com.ebay.skunk.ResultListScene;
import com.ebay.skunk.data.SearchItem;
import com.ebay.skunk.data.Searcher;

public class ItemGroupSprite extends TimeLineSprite {
	public static final float MIN_SCALE_RATIO = 0.3f;
	public static final int MAX_SHOW_ITEM = 4;
	public static final int SHOW_START_LAYER = 4;
	public static final int FONT_SIZE = 25;
	public static final float PRESSED_ALPHA = 0.3f;
	public static final float HOLD_ALPHA = 0.2f;

	public static final String GRADE_TEXTURE_PREF = "GradeValue_";

	public static final Size MAX_SHOW_SHIFT = new Size(15, 15);
	public static final Size MIN_SHOW_SHIFT = new Size(10, 10);
	public static final float MAX_SHOW_ANGLE = 25;
	public static final float MIN_SHOW_ANGLE = 10;

	private final List<ItemSprite> items = new ArrayList<ItemSprite>(
			MAX_SHOW_ITEM);
	private List<SearchItem> data = null;

	private int grade;
	private int gradeType;
	private double gradeValue;
	private double gradeValueNext;

	private boolean hold;
	private Context context;

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public double getGradeValue() {
		return gradeValue;
	}
	
	public double getGradeValueNext()
	{
		return this.gradeValueNext;
	}
	
	public void setGradeValueNext(double d)
	{
		this.gradeValueNext=d;
	}

	public void setGradeValue(double gradeValue) {
		this.gradeValue = gradeValue;
	}

	public int getGradeType() {
		return gradeType;
	}

	public void setGradeType(int gradeType) {
		this.gradeType = gradeType;
	}

	public List<SearchItem> getData() {
		return data;
	}

	public void setData(List<SearchItem> data) {
		this.data = data;
	}

	public ItemGroupSprite(Context c) {
		this.getAnchor().set(Anchor.CENTER);
		this.setScaleRatio(MIN_SCALE_RATIO);
		this.context=c;
	}

	public boolean isHold() {
		return hold;
	}

	public void setHold(boolean hold) {
		this.hold = hold;
	}

	@Override
	public void processDrawArea(RectF rect) {
		float apx = getPosition().getX();
		float apy = getPosition().getY() + size.getHeight() / 2 * scaleRatio;
		float w = texture.getSize().getWidth() * scaleRatio;
		float h = texture.getSize().getHeight() * scaleRatio;
		float dpx = apx - w * anchor.getX();
		float dpy = apy + 6;

		//coordinate conversion
		if (!Director.isFullResolution()) {
			dpx = Director.gameViewToDisplayX(dpx);
			dpy = Director.gameViewToDisplayY(dpy);
			w = Director.gameViewToDisplayX(w);
			h = Director.gameViewToDisplayY(h);
		}

		rect.set(dpx, dpy, (dpx + w), (dpy + h));
	}

	public float getCurrentScaleRatio() {
		//calculate scale;
		float by = ResultListScene.GROUP_PATH_ORI.getY()
				+ ResultListScene.GROUP_PATH_SIZE.getHeight() / 2;
		if (by > ResultListScene.GROUP_PATH_SIZE.getHeight())
			by = ResultListScene.GROUP_PATH_SIZE.getHeight();
		float dy = Math.abs(position.getY() - by);

		float ratio = 1 - (1 - MIN_SCALE_RATIO) * dy / by;
		return ratio;
	}

	public void onUpdateItems() {
		if (items.size() < MAX_SHOW_ITEM) {
			int s = items.size();
			int t = Math.min(MAX_SHOW_ITEM, data.size());
			for (int i = s; i < t; i++) {
				ItemSprite sprite = createItemSprite(data.get(i), i,context);
				items.add(sprite);
				getScene()
						.addNode(SHOW_START_LAYER + MAX_SHOW_ITEM - i, sprite);
				sprite.activate();
				if (!sprite.getItem().isShown()) {
					Vector randomV = new Vector((float) Math.random() - 0.5f,
							(float) Math.random() - 0.5f);
					randomV.move(this.getPosition(), Director.getGameViewSize()
							.getWidth(), sprite.getPosition());
					sprite.appearGroup(this.getPosition());
					sprite.getItem().setShown(true);
				} else {
					
				}
			}
		}
	}

	@Override
	public void onUpdate(Time time) {
		//calculate scale;
		float ratio = this.getCurrentScaleRatio();
		//Log.d("ratio", new Float(dd / dm).toString());
		setScaleRatio(ratio);

		if (this.isPressed()) {
			this.setAlpha(PRESSED_ALPHA);
		} else if (this.hold) {
			this.setAlpha(HOLD_ALPHA);
		} else {
			this.setAlpha(1f);
		}

		onUpdateItems();

		for (ItemSprite item : items) {
			onUpdateShowItem(item);
		}

		super.onUpdate(time);
	}

	public void onUpdateShowItem(ItemSprite item) {
		item.setScaleRatio(this.getScaleRatio());
		item.setDrawAngle(item.getShowAngle());
		item.setupPositionByGroup(position.getX()
				+ item.getShowShift().getWidth(), position.getY()
				+ item.getShowShift().getHeight());
		if (item.isShowInGroup())
			item.setAlpha(this.getAlpha());
		else
			item.setAlpha(0f);
		item.setHideImage(this.hold);
	}

	public ItemSprite createItemSprite(SearchItem data, int index, Context context) {
		ItemSprite item = new ItemSprite(data, this,context);
		item.getSize().set(getSize());

		//Log.d("index", new Integer(index).toString());
		if (index > 0) {
			float sx = (float) (MIN_SHOW_SHIFT.getWidth() + (MAX_SHOW_SHIFT
					.getWidth() - MIN_SHOW_SHIFT.getWidth())
					* Math.random());
			float sy = (float) (MIN_SHOW_SHIFT.getHeight() + (MAX_SHOW_SHIFT
					.getHeight() - MIN_SHOW_SHIFT.getHeight())
					* Math.random());
			item.getShowShift().set((Math.random() > 0.5) ? sx : -sx, -sy);
			float angle = (float) (MIN_SHOW_ANGLE + (MAX_SHOW_ANGLE - MIN_SHOW_ANGLE)
					* Math.random());
			item.setShowAngle((Math.random() > 0.5) ? -angle : angle);
		}
		item.setShowInGroup(true);
		item.activate();
		return item;
	}

	public void startGroupShow() {
		for (ItemSprite item : items) {
			item.setShowInGroup(false);
		}
		//c
	}

	public void startGroupList() {
		//this.setupShowItems();
		for (ItemSprite item : items) {
			item.setShowInGroup(true);
		}
	}

	@Override
	protected void onActivate() {
		super.onActivate();

		startGroupList();
	}

	//	public void appear() {
	//		for (ItemSprite item : items) {
	//			if (item.isShowInGroup()) {
	//				//this.setupItemAttrs(item);
	//				item.appearGroup();
	//			}
	//		}
	//	}
	//
	//	public void endAppear() {
	//		for (ItemSprite item : items) {
	//			if (item.isShowInGroup()) {
	//				item.endAppear();
	//			}
	//		}
	//	}

	private Texture createGradeValueTexture() {
		String texKey = GRADE_TEXTURE_PREF + this.gradeValue;
		Texture tex = Director.getTexManager().getTexture(texKey);
		if (tex == null) {
			TextTexture text = new TextTexture(texKey, Searcher
					.getGradeValueString(gradeType, ""+(int)gradeValue, ""+(int)gradeValueNext));
			text.setFontColor(Color.WHITE);
			text.setFontSize(FONT_SIZE);
			text.onLoadTexture();
			Director.getTexManager().register(text);
			tex = text;
		}
		return tex;
	}

	@Override
	protected void onAdd(Scene scene, Layer layer) {
		// TODO Auto-generated method stub
		super.onAdd(scene, layer);

		this.setTexture(createGradeValueTexture());
	}

	@Override
	protected void onRemove(Scene scene, Layer layer) {
		for (ItemSprite item : items) {
			if (item.isShowInGroup()) {
				item.getLayer().remove(item);
			}
		}
		items.clear();
	}

}
