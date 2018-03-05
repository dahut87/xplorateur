package fr.meconnu.UI;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import fr.meconnu.assets.AssetLoader;
import fr.meconnu.cache.Filler.Movetype;
import fr.meconnu.cache.Patrimoine;

public class Notation extends Actor{
	private Notationtype notation;
	private boolean showtext;
	private Patrimoine patrimoine;
	private TextureRegion highlighted,nohighlight;
	private int note;
	private Label label;
	
	public enum Notationtype {
		INTERET("Intérêt","goldstar"), MARCHE("Marche d'approche","pied"), TIME("Durée de la visite","time"), ACCES("Difficulté d'accès","cle");
		private final String text;
		private final String texture;
		
		private Notationtype(final String text,String texture) {
			this.text = text;
			this.texture = texture;
		}
		
		@Override
		public String toString() {
			return text;
		}
		
		public String toTextureName() {
			return texture;
		}
	}
	
	public Notation(Patrimoine patrimoine,Notationtype notation) {
		this.notation=notation;
		highlighted=new TextureRegion(AssetLoader.Atlas_images.findRegion(notation.toTextureName()));
		nohighlight=new TextureRegion(AssetLoader.Atlas_images.findRegion(notation.toTextureName()+"2"));
		label=new Label(this.notation.toString(),AssetLoader.Skin_images,"Notation");
		this.setPatrimoine(patrimoine);
	}
	
	public void setPatrimoine(Patrimoine patrimoine) {
		this.patrimoine=patrimoine;
		init();
	}
	
	public void init() {
		switch (this.notation) {
		case INTERET:
			this.note=this.patrimoine.getInteret();
			break;
		case MARCHE:
			this.note=this.patrimoine.getMarche();		
			break;
		case TIME:
			this.note=this.patrimoine.getTime();	
			break;
		case ACCES:
			this.note=this.patrimoine.getAcces();	
			break;
		}
		this.label.setText(this.notation.toString());
		this.label.setColor(0.55f, 0.55f, 0.55f, 1.0f);
	}
	
	public void Showtext() {
		this.showtext=true;
		init();
	}
	
	public void hidetext() {
		this.showtext=false;
		init();
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		float sizey=this.getHeight();
		float decy=0;
		final float sizetext=25;
		if (this.showtext) {
			decy=sizetext;
			sizey=sizey-sizetext;
		}
		float sizex=this.getWidth()/5;
	    float interx=sizex/4;
	    for(int i=0;i<this.note;i++)
	    	batch.draw(highlighted, this.getX()+i*(sizex+interx), this.getTop()-decy-sizey, sizey, sizey);
	    for(int i=note;i<4;i++)
	    	batch.draw(nohighlight, this.getX()+i*(sizex+interx), this.getTop()-decy-sizey, sizey, sizey);
		if (this.showtext) {
			this.label.setPosition(this.getX(),this.getY()+sizey);
			this.label.draw(batch, parentAlpha);
		}
	}

}
