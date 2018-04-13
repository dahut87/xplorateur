package fr.meconnu.UI;

import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;

import fr.meconnu.assets.AssetLoader;
import fr.meconnu.cache.Patrimoine.FieldType;

public class Order extends SelectBox {
	FieldType[] fields = new FieldType[] {FieldType.TITRE, FieldType.PROXIMITE, FieldType.INTERET, FieldType.DUREE, FieldType.TYPE};
	String[] items = new String[]{"Titre", "Proximité", "Intérêt", "Durée de visite","Type"};

	public Order() {
		super(AssetLoader.Skin_images);
		// TODO Auto-generated constructor stub
		this.setItems(items);
		this.setSize(250f, 50f);
	}
	
	public FieldType getField() {
		return fields[this.getSelectedIndex()];
	}

}
