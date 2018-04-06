package fr.meconnu.UI;

import com.badlogic.gdx.scenes.scene2d.ui.Image;

import fr.meconnu.assets.AssetLoader;
import fr.meconnu.cache.Patrimoine;

public class Photo extends Image{
	private Patrimoine patrimoine;
	
	public Photo(Patrimoine patrimoine) {
		super();
		this.setPatrimoine(patrimoine);
	}
	
	public void setPatrimoine(Patrimoine patrimoine) {
		this.patrimoine=patrimoine;
		update();
	}
	
	public void update() {
		this.setDrawable(AssetLoader.Skin_images, "nophoto");
	}
}
