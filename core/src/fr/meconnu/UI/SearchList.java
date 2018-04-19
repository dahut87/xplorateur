package fr.meconnu.UI;

import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
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
import fr.meconnu.cache.Criteria;
import fr.meconnu.cache.Patrimoine;
import fr.meconnu.cache.Patrimoine.FieldType;
import fr.meconnu.cache.Patrimoines;

public class SearchList extends Widget implements Cullable {
	private ListStyle style;
	private final Array<Criteria> items = new Array();
	private final ArraySelection<Criteria> selection = new ArraySelection(items);
	private Rectangle cullingArea;
	private float prefWidth, prefHeight;
	private float itemHeight;
	private final float Rowsize=80;
	private int alignment = Align.left;
	private int touchDown;
	private boolean renew;
	private String text;
	

	public SearchList (Skin skin) {
		this(skin.get(ListStyle.class));
	}
	
	public SearchList () {
		this(AssetLoader.Skin_images.get("PatrimoListe", ListStyle.class));
		this.setSearch("");
	}
	
	public void setSearch(String text) {
		if (text!=null) {
			this.text=text;
			update();
		}
	}
	
	public String getSearch() {
		return text;
	}
	
	public void add(Criteria criteria)
	{
		this.renew=false;
		items.add(criteria);
		selection.clear();
		selection.add(criteria);
	}
	
	public void removeSelected() 
	{
		this.renew=false;
		for(Criteria criteria: selection)
		{
			if (items.contains(criteria, true))
				items.removeValue(criteria,true);
		}
		selection.clear();
		if (items.size>0)
			selection.add(items.get(0));
	}
	
	public static boolean isNumeric(String str)  
	{  
	  try  
	  {  
	    double d = Double.parseDouble(str);  
	  }  
	  catch(NumberFormatException nfe)  
	  {  
	    return false;  
	  }  
	  return true;  
	}
	
	public void update() {
		this.clearItems();
		this.renew=false;
		items.addAll(AssetLoader.Datahandler.cache().readType(text));
		items.addAll(AssetLoader.Datahandler.cache().readMotcle(text));
		items.addAll(AssetLoader.Datahandler.cache().readTitre(text));
		items.addAll(AssetLoader.Datahandler.cache().readText(text));
		if (isNumeric(text))
		{
			items.addAll(AssetLoader.Datahandler.cache().readInsee(text));
		}
		items.addAll(AssetLoader.Datahandler.cache().readCommune(text));
		if (items.size>0)
			selection.add(items.get(0));
	}
	
	public boolean getRenew() {
		return renew;
	}

	public SearchList (Skin skin, String styleName) {
		this(skin.get(styleName, ListStyle.class));
	}

	public SearchList (ListStyle style) {
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
				Drawable background = SearchList.this.style.background;
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
		addListener(new DragListener() {
		    public void drag(InputEvent event, float x, float y, int pointer) {
		    	Actor actor=event.getStage().hit(getX()+x, getY()+y, true);
		    	if (actor!=null)
		    	if (actor!=null && actor.getClass() == SearchList.class && actor!=event.getListenerActor() && getSelected()!=null)
		    	{
		    		((SearchList)actor).add(getSelected());
		    		removeSelected();
		    		this.cancel();
		    	}
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
			layout.setText(font, items.get(i).getValues().toString());
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
				Criteria item = items.get(i);
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

	protected GlyphLayout drawItem (Batch batch, BitmapFont font, int index, Criteria item, float x, float y, float width) {
		String string = "";
		TextureRegion icon=null;
		if (item!=null && item.getTypes()!=null)
		{
	    switch (item.getTypes())
	    {
	    case TYPE:
	    	string=(String)item.getValues();
	    	icon=new TextureRegion(AssetLoader.Atlas_images.findRegion(item.getValues().toString().replace(" ", "_").replace(",","")));
	    break;
	    case TITRE:
	    	string=(String)item.getValues()+"(Titre seul)";
	    	icon=new TextureRegion(AssetLoader.Atlas_images.findRegion("Text"));
	    break;
		case TEXTE:
	    	string=(String)item.getValues();
		    icon=new TextureRegion(AssetLoader.Atlas_images.findRegion("Text"));
		break;
	    case MOTCLE:
	    	string=(String)item.getValues();
	    	icon=new TextureRegion(AssetLoader.Atlas_images.findRegion("keywords"));
	    break;
	    case COMMUNE:
	    	string=(String)item.getValues();
	    	icon=new TextureRegion(AssetLoader.Atlas_images.findRegion("Commune"));
	    break;
	    }
		batch.draw(icon, x, y-icon.getRegionHeight(), icon.getRegionWidth(), icon.getRegionHeight());
		return font.draw(batch, string, x+icon.getRegionWidth(), y-Rowsize/2+15, 0, string.length(), width-icon.getRegionWidth(), alignment, false, "...");
		}
		else
			return null;
	}

	public ArraySelection<Criteria> getSelection () {
		return selection;
	}
	
	public Array<Criteria> getCriterias() {
		return items;
	}
	
	public void setCriterias(Array<Criteria> criterias) {
		items.clear();
		if (criterias!=null)
		for(Criteria criteria:criterias)
		{
			if (criteria.getTypes()==FieldType.TITRE || criteria.getTypes()==FieldType.TEXTE || criteria.getTypes()==FieldType.MOTCLE || criteria.getTypes()==FieldType.COMMUNE)
				items.add(criteria);
		}
	}

	/** Returns the first selected item, or null. */
	public Criteria getSelected () {
		return selection.first();
	}

	/** Sets the selection to only the passed item, if it is a possible choice.
	 * @param item May be null. */
	public void setSelected (Criteria item) {
		if (items.contains(item, false))
			selection.set(item);
		else if (selection.getRequired() && items.size > 0)
			selection.set(items.first());
		else
			selection.clear();
	}

	/** @return The index of the first selected item. The top item has an index of 0. Nothing selected has an index of -1. */
	public int getSelectedIndex () {
		ObjectSet<Criteria> selected = selection.items();
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

	public void setItems (Criteria... newItems) {
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
	public Array<Criteria> getItems () {
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

	public void setCullingArea (Rectangle cullingArea) {
		this.cullingArea = cullingArea;
	}

	/** Sets the horizontal alignment of the list items.
	 * @param alignment See {@link Align}. */
	public void setAlignment (int alignment) {
		this.alignment = alignment;
	}
}