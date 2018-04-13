package fr.meconnu.UI;

import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.utils.Array;

import fr.meconnu.assets.AssetLoader;
import fr.meconnu.cache.Patrimoine.FieldSizeType;
import fr.meconnu.cache.Patrimoine.FieldType;

public class Sizer extends SelectBox {
	FieldSizeType[] fields = new FieldSizeType[] {FieldSizeType.PETITE, FieldSizeType.MOYENNE, FieldSizeType.GRANDE, FieldSizeType.MAXIMALE};
	Array<String> items;

	public Sizer() {
		super(AssetLoader.Skin_images);
		// TODO Auto-generated constructor stub
		items=new Array<String>();
		items.clear();
		for(FieldSizeType type:FieldSizeType.values())
			items.add(type.toString());		
		this.setItems(items);
		this.setSize(250f, 50f);
	}
	
	public FieldSizeType getField() {
		return fields[this.getSelectedIndex()];
	}

}
