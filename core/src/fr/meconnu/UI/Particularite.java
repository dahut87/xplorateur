package fr.meconnu.UI;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;

import fr.meconnu.assets.AssetLoader;
import fr.meconnu.cache.Patrimoine;

public class Particularite extends Actor{
	private Patrimoine patrimoine;
	private TextureRegion difficile,risque,coeur,argent,chiens_oui,chiens_non,interdit;
	
	public Particularite(Patrimoine patrimoine) {
		super();
		difficile=new TextureRegion(AssetLoader.Atlas_images.findRegion("difficile"));
		risque=new TextureRegion(AssetLoader.Atlas_images.findRegion("risque"));
		coeur=new TextureRegion(AssetLoader.Atlas_images.findRegion("coupdecoeur"));
		argent=new TextureRegion(AssetLoader.Atlas_images.findRegion("argent"));
		interdit=new TextureRegion(AssetLoader.Atlas_images.findRegion("interdit"));
		chiens_oui=new TextureRegion(AssetLoader.Atlas_images.findRegion("chiens_oui"));
		chiens_non=new TextureRegion(AssetLoader.Atlas_images.findRegion("chiens_non"));
		this.setPatrimoine(patrimoine);
	}
	
	public void setPatrimoine(Patrimoine patrimoine) {
		this.patrimoine=patrimoine;
		init();
	}
	
	public void init() {
		
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		float size=this.getHeight();
	    float interx=10;
	    float x=this.getX();
	    if (patrimoine.isDifficile()) {
	    	batch.draw(difficile, x, this.getTop()-size, size, size);
	    	x=x+interx+size;
	    }
	    if (patrimoine.isRisque()) {
	    	batch.draw(risque, x, this.getTop()-size, size, size);
	    	x=x+interx+size;
	    }
	    if (patrimoine.isCoeur()) {
	    	batch.draw(coeur, x, this.getTop()-size, size, size);
	    	x=x+interx+size;
	    }
	    if (patrimoine.isArgent()) {
	    	batch.draw(argent, x, this.getTop()-size, size, size);
	    	x=x+interx+size;
	    }
	    if (patrimoine.isInterdit()) {
	    	batch.draw(interdit, x, this.getTop()-size, size, size);
	    	x=x+interx+size;
	    }
	    if (patrimoine.getChien()=="oui") {
	    	batch.draw(chiens_oui, x, this.getTop()-size, size, size);
	    	x=x+interx+size;
	    }
	    if (patrimoine.getChien()=="non") {
	    	batch.draw(chiens_non, x, this.getTop()-size, size, size);
	    	x=x+interx+size;
	    } 
	}

}
