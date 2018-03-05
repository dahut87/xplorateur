package fr.meconnu.UI;

import com.badlogic.gdx.scenes.scene2d.ui.TextArea;

import fr.meconnu.assets.AssetLoader;
import fr.meconnu.cache.Patrimoine;

public class Description extends TextArea{
	private Patrimoine patrimoine;
	
	public Description(Patrimoine patrimoine) {
		super("", AssetLoader.Skin_images,"Description");
		this.setPatrimoine(patrimoine);
	}
	
	public void setPatrimoine(Patrimoine patrimoine) {
		this.patrimoine=patrimoine;
		init();
	}
	
	public void init() {
		this.setDisabled(true);
		this.setText(patrimoine.getTexte());
	}
	

}
