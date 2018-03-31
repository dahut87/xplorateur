package fr.meconnu.UI;

import com.badlogic.gdx.scenes.scene2d.ui.Label;

import fr.meconnu.assets.AssetLoader;
import fr.meconnu.cache.Patrimoine;

public class Titre extends Label {
	private Patrimoine patrimoine;
	
	public Titre(Patrimoine patrimoine) {
		super("", AssetLoader.Skin_images, "Titre");
		this.setPatrimoine(patrimoine);
	}
	
	public Titre(Patrimoine patrimoine, String police) {
		super("", AssetLoader.Skin_images, police);
		this.setPatrimoine(patrimoine);
	}
	
	public void setPatrimoine(Patrimoine patrimoine) {
		this.patrimoine=patrimoine;
		init();
	}

	public void init() {
		String text;
		if (patrimoine!=null) {
			text=patrimoine.getTitre();
			if (text.length()>36)
				text=text.substring(0, 36)+"...";
			this.setText(text);
		}
	}
	
}
