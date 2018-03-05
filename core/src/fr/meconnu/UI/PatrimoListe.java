package fr.meconnu.UI;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ArraySelection;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.scenes.scene2d.utils.Cullable;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.UIUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;

import fr.meconnu.assets.AssetLoader;
import fr.meconnu.cache.Patrimoine;
import fr.meconnu.cache.Patrimoine.FieldType;
import fr.meconnu.cache.Patrimoines;

public class PatrimoListe extends Widget implements Cullable {
	private ListStyle style;
	private final Array<Patrimoine> items = new Array();
	private final ArraySelection<Patrimoine> selection = new ArraySelection(items);
	private Rectangle cullingArea;
	private float prefWidth, prefHeight;
	private float itemHeight;
	private final float Rowsize=80;
	private int alignment = Align.left;
	private int touchDown;
	private Patrimoines patrimoines,patrimoinegived;
	private Patrimoine patrimoine;
	private boolean renew;
	private FieldType field;
	

	public PatrimoListe (Skin skin) {
		this(skin.get(ListStyle.class));
	}
	
	public PatrimoListe (Patrimoines patrimoines,Patrimoine patrimoine) {
		this(AssetLoader.Skin_images.get("PatrimoListe", ListStyle.class));
		this.setPatrimoines(patrimoines,patrimoine);
	}
	
	public Patrimoines setPatrimoines(Patrimoines patrimoines,Patrimoine patrimoine) {
		if (patrimoines!=null) {
			this.patrimoinegived=patrimoines;
			this.patrimoine=patrimoine;
			init();
		}
		return patrimoines;
	}
	
	public Patrimoines getPatrimoines() {
		return patrimoinegived;
	}
	
	public void init() {
		this.patrimoines=Patrimoines.FilterPatrimoines(patrimoinegived, field, patrimoine);
		this.clearItems();
		this.renew=false;
		if (patrimoines!=null)
			this.setItems(patrimoines.getValues());
	}
	
	public boolean getRenew() {
		return renew;
	}

	public PatrimoListe (Skin skin, String styleName) {
		this(skin.get(styleName, ListStyle.class));
	}

	public PatrimoListe (ListStyle style) {
		selection.setActor(this);
		selection.setRequired(true);

		setStyle(style);
		setSize(getPrefWidth(), getPrefHeight());
		addListener(new ActorGestureListener() { 
			@Override
			public void tap(InputEvent event, float x, float y, int count, int button)  {
				renew=true;
				if (selection.isDisabled()) return;
				if (items.size == 0) return;
				float height = getHeight();
				Drawable background = PatrimoListe.this.style.background;
				if (background != null) {
					height -= background.getTopHeight() + background.getBottomHeight();
					y -= background.getBottomHeight();
				}
				int index = (int)((height - y) / itemHeight);
				if (index > items.size - 1) return;
				index = Math.max(0, index);
				selection.choose(items.get(index));
				touchDown = index;
			}
		});
	}

	public void setStyle (ListStyle style) {
		if (style == null) throw new IllegalArgumentException("style cannot be null.");
		this.style = style;
		invalidateHierarchy();
	}

	/** Returns the list's style. Modifying the returned style may not have an effect until {@link #setStyle(ListStyle)} is
	 * called. */
	public ListStyle getStyle () {
		return style;
	}

	public void layout () {
		BitmapFont font = style.font;
		Drawable selectedDrawable = style.selection;

		/*itemHeight = font.getCapHeight() - font.getDescent() * 2;
		itemHeight += selectedDrawable.getTopHeight() + selectedDrawable.getBottomHeight();*/
		itemHeight = Rowsize;

		prefWidth = 0;
		Pool<GlyphLayout> layoutPool = Pools.get(GlyphLayout.class);
		GlyphLayout layout = layoutPool.obtain();
		for (int i = 0; i < items.size; i++) {
			layout.setText(font, toString(items.get(i)));
			prefWidth = Math.max(layout.width, prefWidth);
		}
		layoutPool.free(layout);
		prefWidth += selectedDrawable.getLeftWidth() + selectedDrawable.getRightWidth();
		prefHeight = items.size * itemHeight;

		Drawable background = style.background;
		if (background != null) {
			prefWidth += background.getLeftWidth() + background.getRightWidth();
			prefHeight += background.getTopHeight() + background.getBottomHeight();
		}
	}

	@Override
	public void draw (Batch batch, float parentAlpha) {
		validate();

		BitmapFont font = style.font;
		Drawable selectedDrawable = style.selection;
		Color fontColorSelected = style.fontColorSelected;
		Color fontColorUnselected = style.fontColorUnselected;

		Color color = getColor();
		batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

		float x = getX(), y = getY(), width = getWidth(), height = getHeight();
		float itemY = height;

		Drawable background = style.background;
		if (background != null) {
			background.draw(batch, x, y, width, height);
			float leftWidth = background.getLeftWidth();
			x += leftWidth;
			itemY -= background.getTopHeight();
			width -= leftWidth + background.getRightWidth();
		}

		float textOffsetX = selectedDrawable.getLeftWidth(), textWidth = width - textOffsetX - selectedDrawable.getRightWidth();
		float textOffsetY = selectedDrawable.getTopHeight() - font.getDescent();

		font.setColor(fontColorUnselected.r, fontColorUnselected.g, fontColorUnselected.b, fontColorUnselected.a * parentAlpha);
		for (int i = 0; i < items.size; i++) {
			if (cullingArea == null || (itemY - itemHeight <= cullingArea.y + cullingArea.height && itemY >= cullingArea.y)) {
				Patrimoine item = items.get(i);
				boolean selected = selection.contains(item);
				if (selected) {
					Drawable drawable = selectedDrawable;
					if (touchDown == i && style.down != null) drawable = style.down;
					drawable.draw(batch, x, y + itemY - itemHeight, width, itemHeight);
					font.setColor(fontColorSelected.r, fontColorSelected.g, fontColorSelected.b, fontColorSelected.a * parentAlpha);
				}
				drawItem(batch, font, i, item, x + textOffsetX, y + itemY - textOffsetY, textWidth);
				if (selected) {
					font.setColor(fontColorUnselected.r, fontColorUnselected.g, fontColorUnselected.b,
						fontColorUnselected.a * parentAlpha);
				}
			} else if (itemY < cullingArea.y) {
				break;
			}
			itemY -= itemHeight;
		}
	}

	protected GlyphLayout drawItem (Batch batch, BitmapFont font, int index, Patrimoine item, float x, float y, float width) {
		String string = toString(item);
		TextureRegion icon=new TextureRegion(AssetLoader.Atlas_images.findRegion(item.getTypes().toString().replace(" ", "_").replace(",","")));
		batch.draw(icon, x, y-icon.getRegionHeight(), icon.getRegionWidth(), icon.getRegionHeight());
		if (item!=null) {
			String dist="";
			int distint = item.GetDistance().intValue();
			if (distint<1000)
				dist=String.valueOf(distint)+"m";
			else if (distint<10000)
				dist=String.valueOf(distint/1000)+"km";
			else
				dist=String.valueOf(Math.round(distint/1000))+"km";
			font.draw(batch, dist, x+icon.getRegionWidth()+550, y-Rowsize/2+15, 0, dist.length(), width, alignment, false, "...");
		}
		return font.draw(batch, string, x+icon.getRegionWidth(), y-Rowsize/2+15, 0, string.length(), width, alignment, false, "...");
	}

	public ArraySelection<Patrimoine> getSelection () {
		return selection;
	}

	/** Returns the first selected item, or null. */
	public Patrimoine getSelected () {
		return selection.first();
	}

	/** Sets the selection to only the passed item, if it is a possible choice.
	 * @param item May be null. */
	public void setSelected (Patrimoine item) {
		if (items.contains(item, false))
			selection.set(item);
		else if (selection.getRequired() && items.size > 0)
			selection.set(items.first());
		else
			selection.clear();
	}

	/** @return The index of the first selected item. The top item has an index of 0. Nothing selected has an index of -1. */
	public int getSelectedIndex () {
		ObjectSet<Patrimoine> selected = selection.items();
		return selected.size == 0 ? -1 : items.indexOf(selected.first(), false);
	}

	/** Sets the selection to only the selected index. */
	public void setSelectedIndex (int index) {
		if (index < -1 || index >= items.size)
			throw new IllegalArgumentException("index must be >= -1 and < " + items.size + ": " + index);
		if (index == -1) {
			selection.clear();
		} else {
			selection.set(items.get(index));
		}
	}

	public void setItems (Patrimoine... newItems) {
		if (newItems == null) throw new IllegalArgumentException("newItems cannot be null.");
		float oldPrefWidth = getPrefWidth(), oldPrefHeight = getPrefHeight();

		items.clear();
		items.addAll(newItems);
		selection.validate();

		invalidate();
		if (oldPrefWidth != getPrefWidth() || oldPrefHeight != getPrefHeight()) invalidateHierarchy();
	}

	/** Sets the items visible in the list, clearing the selection if it is no longer valid. If a selection is
	 * {@link ArraySelection#getRequired()}, the first item is selected. This can safely be called with a
	 * (modified) array returned from {@link #getItems()}. */
	public void setItems (Array newItems) {
		if (newItems == null) throw new IllegalArgumentException("newItems cannot be null.");
		float oldPrefWidth = getPrefWidth(), oldPrefHeight = getPrefHeight();

		if (newItems != items) {
			items.clear();
			items.addAll(newItems);
		}
		selection.validate();

		invalidate();
		if (oldPrefWidth != getPrefWidth() || oldPrefHeight != getPrefHeight()) invalidateHierarchy();
	}

	public void clearItems () {
		if (items.size == 0) return;
		items.clear();
		selection.clear();
		invalidateHierarchy();
	}

	/** Returns the internal items array. If modified, {@link #setItems(Array)} must be called to reflect the changes. */
	public Array<Patrimoine> getItems () {
		return items;
	}

	public float getItemHeight () {
		return itemHeight;
	}

	public float getPrefWidth () {
		validate();
		return prefWidth;
	}

	public float getPrefHeight () {
		validate();
		return prefHeight;
	}

	protected String toString (Patrimoine patrimoine) {
		String text=patrimoine.getTitre();
		if (text.length()>36)
			return text.substring(0, 36)+"...";
		else
			return text;
	}

	public void setCullingArea (Rectangle cullingArea) {
		this.cullingArea = cullingArea;
	}

	/** Sets the horizontal alignment of the list items.
	 * @param alignment See {@link Align}. */
	public void setAlignment (int alignment) {
		this.alignment = alignment;
	}

	public void setFilter(FieldType field) {
		this.field = field;
		init();
	}
}