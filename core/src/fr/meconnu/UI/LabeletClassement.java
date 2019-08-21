package fr.meconnu.UI;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;

import fr.meconnu.assets.AssetLoader;
import fr.meconnu.cache.Patrimoine;

public class LabeletClassement extends Actor{
	private Patrimoine patrimoine;
	private TextureRegion difficile,risque,coeur,argent,chiens_oui,chiens_non,interdit;
	
	public LabeletClassement(Patrimoine patrimoine) {
		super();
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
		String labels[]= {"Monument historique","Site natura 2000","Patrimoine XXe siècle","Patrimoine mondial de l’Humanité (UNESCO)","Site classé","Tourisme et handicap","VMF-Patrimoine historique","Demeure historique","Qualité Tourisme"};		
		String textures[]= {"monument historique","natura 2000","patrimoine du xxe siecle","patrimoine mondial unesco","site classé","Tourisme et handicap","vmf patrimoine historique","demeure historique","qualité tourisme"};
	    float interx=10;
	    float x=this.getX();
	    int i=0;
	    for(String label: labels) {
	    	if (patrimoine.getLabels().contains(label)) {
	    		TextureRegion region=AssetLoader.Atlas_images.findRegion(textures[i]);
	    		batch.draw(region, x, this.getTop()-region.getRegionWidth(), region.getRegionWidth(), region.getRegionHeight());
	    		x=x+interx+region.getRegionWidth();
	    	}
	    	i++;
	    }
	    
	}

}
