package com.ebay.skunk.sprite;

import org.ebay.msif.core.ServiceClientException;
import org.kevinth.kth2d.Action;
import org.kevinth.kth2d.Director;
import org.kevinth.kth2d.Layer;
import org.kevinth.kth2d.Scene;
import org.kevinth.kth2d.Sprite;
import org.kevinth.kth2d.Time;
import org.kevinth.kth2d.action.ActionListener;
import org.kevinth.kth2d.action.Fade;
import org.kevinth.kth2d.action.MoveTo;
import org.kevinth.kth2d.action.Rotate;
import org.kevinth.kth2d.action.Scale;
import org.kevinth.kth2d.geometry.Anchor;
import org.kevinth.kth2d.geometry.Point;
import org.kevinth.kth2d.geometry.Size;
import org.kevinth.kth2d.texture.TexManager;
import org.kevinth.kth2d.texture.TextTexture;
import org.kevinth.kth2d.texture.Texture;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ebay.msdk.SDKInitializer;
import com.ebay.msdk.affiliate.AffiliateTrackingManager;
import com.ebay.skunk.R;
import com.ebay.skunk.ResultListScene;
import com.ebay.skunk.data.BitmapGroupManager;
import com.ebay.skunk.data.ChosenItemsManager;
import com.ebay.skunk.data.SearchItem;
import com.ebay.skunk.data.Searcher;

public class ItemSprite extends TimeLineSprite {
	public static final float SHOW_SCALE_RATIO = 0.7f;
	public static final int FONT_SIZE_SMALL = 22;
	public static final int FONT_SIZE_BIG = 33;
	public static final String TITLE_TEXTURE_PREF = "Title_";
	public static final String PRICE_BID_TEXTURE_PREF = "Price_Bid_";
	public static final String SHOPPING_CART_PREF = "Shopping_Cart";
	public static final float PRESSED_ALPHA = 0.3f;

	public static final float APPEAR_ROTATE_MAX = 30f;
	public static final float APPEAR_SCALE_MAX = 3;
	public static final float APPEAR_SCALE_MIN = 1.5f;
	public static final float APPEAR_ALPHA = 0.1f;
	public static final long APPEAR_GROUP_TIME = 250l;
	public static final long APPEAR_SHOW_TIME = 250l;

	private SearchItem item = null;
	private ItemGroupSprite group = null;
	private boolean showInGroup = false;
	private final Size showShift = new Size();
	private float showAngle = 0;
	private boolean hideImage = false;
	private boolean showSingle = false;
	private Context context;

	private Scale appearScale = new Scale();
	private MoveTo appearMoveTo = new MoveTo();

	private Sprite titleSprite = new Sprite();
	private Sprite priceConditionSprite = new Sprite();
	private Sprite shoppingCartSprite = new Sprite();
	
	public ItemSprite(SearchItem item, ItemGroupSprite group, Context c) {
		this.getAnchor().set(Anchor.CENTER);
		this.setBorderColor(255, 255, 255);
		this.setBorderWidth(3);
		this.setColor(0, 0, 0);

		this.item = item;
		this.group = group;
		this.context=c;
		TexManager texMgr = Director.getTexManager();
		Texture texture = new Texture(null);
		texture.setBitmap(Searcher.defaultBitmap);
		texture.setLefTop(new Point());
		texture.setSize(new Size(Searcher.defaultBitmap.getWidth(),Searcher.defaultBitmap.getHeight()));
		texture.onLoadTexture();
		this.setTexture(texture);
		
		shoppingCartSprite.getAnchor().set(Anchor.BOTTOM_RIGHT);
		Texture t = new Texture(null);
		Bitmap bitmap = BitmapFactory.decodeResource(c.getResources(), R.drawable.flag);
		t.setBitmap(bitmap);
		texture.setLefTop(new Point());
		t.setSize(new Size(bitmap.getWidth() , bitmap.getHeight()));
		t.onLoadTexture();
		shoppingCartSprite.setTexture(t);
		if(item != null && item.getIsChosen())
			shoppingCartSprite.setAlpha(1);
		else
			shoppingCartSprite.setAlpha(0);
		shoppingCartSprite.setSize(bitmap.getWidth() , bitmap.getHeight());
		
		AffiliateTrackingManager affTrackManager;
		try {
			affTrackManager = SDKInitializer.getAffiliateTrackingManager(context);
			affTrackManager.setCampaignID("5336725725"); // default campaign id
			affTrackManager.setCustomID("ANDROIDREFAPP"); // default custom id
		} catch (ServiceClientException e1) {
			e1.printStackTrace();
		}
	}
	
	public void setContext(Context c)
	{
		this.context=c;
	}

	@Override
	protected void onAdd(Scene scene, Layer layer) {
		super.onAdd(scene, layer);
		Texture tex = createTitleTexture();
		titleSprite.setTexture(tex);
		titleSprite.getSize().set(tex.getSize());
		titleSprite.getAnchor().set(Anchor.LEFT_CENTER);
		tex = createPriceBidTexture();
		priceConditionSprite.setTexture(tex);
		priceConditionSprite.getSize().set(tex.getSize());
		priceConditionSprite.getAnchor().set(Anchor.LEFT_CENTER);
		shoppingCartSprite.setPosition(
				this.getPosition().getX() + this.getSize().getWidth()/2 , 
				this.getPosition().getY() + this.getSize().getHeight()/2);
	}

	@Override
	protected void onRemove(Scene scene, Layer layer) {
		layer.remove(titleSprite);
		layer.remove(priceConditionSprite);
		layer.remove(shoppingCartSprite);
		super.onRemove(scene, layer);
	}

	private Texture createTitleTexture() {
		String texKey = TITLE_TEXTURE_PREF + item.getTitle() + item.getPosition();
		Texture tex = Director.getTexManager().getTexture(texKey);
		if (tex == null) {
			TextTexture text = new TextTexture(texKey, item.getTitle());
			text.setFontColor(Color.YELLOW);
			text.setFontSize(FONT_SIZE_SMALL);
			text.onLoadTexture();
			Director.getTexManager().register(text);
			tex = text;
		}
		return tex;
	}

	private Texture createPriceBidTexture() {
		String texKey = PRICE_BID_TEXTURE_PREF + item.getPriceTimeleftTitle() + item.getPosition();
		Texture tex = Director.getTexManager().getTexture(texKey);
		if (tex == null) {
			TextTexture text = new TextTexture(texKey, item.getPriceTimeleftTitle());
			text.setFontColor(Color.WHITE);
			text.setFontSize(FONT_SIZE_SMALL);
			text.onLoadTexture();
			Director.getTexManager().register(text);
			tex = text;
		}
		return tex;
	}

	public SearchItem getItem() {
		return item;
	}

	public void setItem(SearchItem item) {
		this.item = item;
	}

	public ItemGroupSprite getGroup() {
		return group;
	}

	public void setGroup(ItemGroupSprite group) {
		this.group = group;
	}

	public void setupPositionByGroup(float x, float y) {
		if (isAppearing()) {
			this.appearMoveTo.setTarget(x, y);
		} else
			this.position.set(x, y);
	}

	public boolean isShowInGroup() {
		return showInGroup;
	}

	public void setShowInGroup(boolean show) {
		this.showInGroup = show;
	}

	public boolean isShowSingle() {
		return showSingle;
	}

	public void initShowSingle() {
		this.setDrawAngle(0f);
		this.setAlpha(1f);
		this.setScaleRatio(ItemSprite.SHOW_SCALE_RATIO);
		this.setHideImage(false);
		this.activate();
	}

	public void startShowSingle() {
		this.showSingle = true;

		getLayer().addNode(titleSprite);
		titleSprite.activate();
		getLayer().addNode(priceConditionSprite);
		priceConditionSprite.activate();
		getLayer().addNode(shoppingCartSprite);
		shoppingCartSprite.activate();
	}

	public void endShowSingle() {
		if (showSingle) {
			getLayer().remove(titleSprite);
			getLayer().remove(priceConditionSprite);
			getLayer().remove(shoppingCartSprite);
			this.showSingle = false;
		}
	}

	public float getShowAngle() {
		return showAngle;
	}

	public void setShowAngle(float showAngle) {
		this.showAngle = showAngle;
	}

	public Size getShowShift() {
		return showShift;
	}

	public boolean isHideImage() {
		return hideImage;
	}

	public void setHideImage(boolean hideImage) {
		this.hideImage = hideImage;
	}

	@Override
	protected void onDrawNode(Canvas canvas, RectF rect, Matrix matrix,
			Point center, Paint paint) {
		Matrix mOld = canvas.getMatrix();
		canvas.setMatrix(matrix);

		drawFill(canvas, rect, paint);

		//if (!hideImage) {
		matrix.reset();
		if(texture!=null)
			processMatrixImage(matrix, rect, texture.getSize(), center);
		canvas.setMatrix(matrix);
		drawTexture(canvas, rect, paint);
		//}		

		matrix.reset();
		if(texture!=null)
			processMatrix(matrix, rect, texture.getSize(), center);
		canvas.setMatrix(matrix);
		drawBorder(canvas, rect, paint);

		canvas.setMatrix(mOld);
	}

	public void endAppear() {
		this.stopAllActions();
		this.setAppearing(false);
	}

	public void appearGroup(Point target) {
		this.setAppearing(true);
		appearMoveTo.clearActionListener();
		appearMoveTo.setTime(APPEAR_GROUP_TIME);
		appearMoveTo.getTarget().set(target);
		appearMoveTo.addActionListener(new ActionListener() {
			@Override
			public void actionDone(Action action) {
				endAppear();
			}

		});
		doAction(appearMoveTo);
	}

	public void appearShow() {
		this.setAppearing(true);
		appearScale.clearActionListener();
		appearMoveTo.clearActionListener();
		appearScale.setTime(APPEAR_SHOW_TIME);
		appearScale.setStartRatio(group.getScaleRatio());
		appearScale.setEndRatio(this.getScaleRatio());

		appearMoveTo.setTime(APPEAR_SHOW_TIME);
		appearMoveTo.getTarget().set(this.getPosition());
		this.getPosition().set(group.getPosition());

		appearMoveTo.addActionListener(new ActionListener() {
			@Override
			public void actionDone(Action action) {
				((ResultListScene) action.getSprite().getScene())
						.endShowAppear();
			}

		});

		doAction(appearScale);
		doAction(appearMoveTo);
	}

	public void appearShowBack() {
		this.setAppearing(true);
		appearScale.clearActionListener();
		appearMoveTo.clearActionListener();

		appearScale.setTime(APPEAR_SHOW_TIME);
		appearScale.setStartRatio(this.getScaleRatio());
		appearScale.setEndRatio(group.getScaleRatio());

		appearMoveTo.setTime(APPEAR_SHOW_TIME);
		appearMoveTo.getTarget().set(group.getPosition());

		appearScale.addActionListener(new ActionListener() {
			@Override
			public void actionDone(Action action) {
				((ResultListScene) action.getSprite().getScene())
						.endShowBackAppear();
			}

		});

		doAction(appearScale);
		doAction(appearMoveTo);
	}

	protected void processMatrixImage(Matrix matrix, RectF drawRect,
			Size sourceSize, Point center) {
		processMatrixForFlip(matrix, drawRect, sourceSize);

		float tw = sourceSize.getWidth();
		float th = sourceSize.getHeight();
		float ratioT = tw / th;
		float ratioD = drawRect.width() / drawRect.height();
		float dx = drawRect.width();
		float dy = drawRect.height();
		float dex = 0;
		float dey = 0;
		if (ratioT > ratioD) {
			dy = dx / ratioT;
			dey = (drawRect.height() - dy) / 2;
		} else {
			dx = dy * ratioT;
			dex = (drawRect.width() - dx) / 2;
		}

		float sx = dx / tw;
		float sy = dy / th;

		float apx = center.getX();
		float apy = center.getY();
		matrix.postTranslate(apx, apy);
		matrix.postScale(sx, sy, apx, apy);

		matrix.postTranslate(drawRect.left - apx + dex, drawRect.top - apy
				+ dey);

		processMatrixForAngle(matrix, drawRect, sourceSize, center);
	}

	public void onUpdateTitle(Sprite sprite, int line) {
		float w = sprite.getTexture().getSize().getWidth();
		float h = sprite.getTexture().getSize().getHeight();
		float apx = getPosition().getX() + size.getWidth() / 2 * scaleRatio + 4;
		float apy = getPosition().getY() + (line - 1.5f) * (h + 6);

		if (!Director.isFullResolution()) {
			apx = Director.gameViewToDisplayX(apx);
			apy = Director.gameViewToDisplayY(apy);
			w = Director.gameViewToDisplayX(w);
			h = Director.gameViewToDisplayY(h);
		}

		//sprite.setDrawAngle(showAngle);
		sprite.setPosition(apx, apy);
		sprite.setAlpha(alpha);
	}

	@Override
	public void onUpdate(Time time) {
		
		//--------------------------------added--------------------------------------
		Bitmap bitmap = BitmapGroupManager.getInstance().get(item.getURL());
		if(bitmap!=null && texture.getBitmap()==Searcher.defaultBitmap)
		{
			texture.setBitmap(bitmap);
			texture.setLefTop(new Point());
			texture.setSize(new Size(bitmap.getWidth(),bitmap.getHeight()));
			texture.onLoadTexture();
		}
		//-----------------------------------------------------------------------------
		
		super.onUpdate(time);
		if (this.showSingle) {
			if (this.isPressed()) {
				this.setAlpha(PRESSED_ALPHA);
			} else {
				this.setAlpha(1f);
			}

			shoppingCartSprite.setPosition(
					this.getPosition().getX() + this.getSize().getWidth()/2 , 
					this.getPosition().getY() + this.getSize().getHeight()/2);
			onUpdateTitle(titleSprite, 1);
			onUpdateTitle(priceConditionSprite, 2);
		}
	}

	@Override
	protected void onActivate() {
		super.onActivate();
	}
	public boolean isPointOn(float x, float y) {
		RectF rect1 = new RectF();
		super.processDrawArea(rect1);
		if (rect1.contains(x, y))
			return true;

		RectF rect2 = new RectF();
		titleSprite.processDrawArea(rect1);
		priceConditionSprite.processDrawArea(rect2);
		rect1.right = Math.max(rect1.right, rect2.right);
		rect1.bottom = rect2.bottom;
		if (rect1.contains(x, y))
			return true;

		return false;
	}

	public void onSelected(Toast toast) {
		if(item.getIsChosen())
		{
			item.setIsChosen(false);
			ChosenItemsManager.getInstance().remove(item);
			Log.v("onSelected -> remove", item.getTitle());
			TextTexture ttex = (TextTexture) titleSprite.getTexture();
			ttex.setFontColor(Color.YELLOW);	
//			ttex.setFontSize(FONT_SIZE_SMALL);
			ttex.onLoadTexture();
			ttex = (TextTexture) priceConditionSprite.getTexture();
			ttex.setFontColor(Color.WHITE);	
//			ttex.setFontSize(FONT_SIZE_SMALL);	
			ttex.onLoadTexture();
			shoppingCartSprite.setAlpha(0);
			shoppingCartSprite.getTexture().onLoadTexture();
		}
		else
		{
			item.setIsChosen(true);
			ChosenItemsManager.getInstance().add(item);
			Log.v("onSelected -> add", item.getTitle());
			TextTexture ttex = (TextTexture) titleSprite.getTexture();
			ttex.setFontColor(Color.RED);
//			ttex.setFontSize(FONT_SIZE_BIG);
			ttex.onLoadTexture();
			ttex = (TextTexture) priceConditionSprite.getTexture();
			ttex.setFontColor(Color.RED);
//			ttex.setFontSize(FONT_SIZE_BIG);
			ttex.onLoadTexture();
			shoppingCartSprite.setAlpha(1);
			shoppingCartSprite.getTexture().onLoadTexture();
		}
		toast.show();
	}
	
	public void onLongPressed()
	{
		//--------------------launch the web browser-----------------------------
		String id=item.getId();
		try 
		{
			SDKInitializer.getAffiliateTrackingManager(context).launchViewItemPage(id);
		} 
		catch (ServiceClientException e) 
		{
			Log.e("ItemSprite", "cannot launch the web browser!");
		}
		//-----------------------------------------------------------------------
	}
}
