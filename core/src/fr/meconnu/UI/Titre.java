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
		update();
	}

	public void update() {
		if (patrimoine!=null) {
			String text;
			int maxsize=Math.round(this.getWidth()/this.getStyle().font.getRegion().getRegionWidth()-3);
			if (maxsize<0 || this.hasParent()) maxsize=35;
			text=patrimoine.getTitre();
			if (text.length()>maxsize)
				text=text.substring(0, maxsize)+"...";
			this.setText(text);
		}
	}
	
}
