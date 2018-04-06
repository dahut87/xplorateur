package fr.meconnu.UI;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import fr.meconnu.assets.AssetLoader;
import fr.meconnu.cache.Filler.Movetype;
import fr.meconnu.cache.Patrimoine;

public class Notation extends Actor{
	private Notationtype notation;
	private boolean showtext;
	private Patrimoine patrimoine;
	private TextureRegion highlighted,nohighlight;
	private byte note;
	private Label label;
	private boolean writable;
	
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
	
	public Notation()
	{
		CreateNotation(null,Notationtype.INTERET);
	}
	
	public Notation(Notationtype notation)
	{
		CreateNotation(null,notation);
	}
	
	public Notation(Patrimoine patrimoine,Notationtype notation)
	{
		CreateNotation(patrimoine,notation);
	}
	
	public void CreateNotation(Patrimoine patrimoine,Notationtype notation) {
		this.notation=notation;
		this.writable=false;
		this.debug();
		this.setWidth(200f);
		this.setHeight(50);
		highlighted=new TextureRegion(AssetLoader.Atlas_images.findRegion(notation.toTextureName()));
		nohighlight=new TextureRegion(AssetLoader.Atlas_images.findRegion(notation.toTextureName()+"2"));
		label=new Label(this.notation.toString(),AssetLoader.Skin_images,"Notation");
		this.setPatrimoine(patrimoine);
		this.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				{
					Notation me=(Notation)event.getListenerActor();
					if (writable)
					{
						me.note=(byte) (Math.floor(4f*x/me.getWidth())+1);
						if (note<0) note=0;
						if (note>3) note=4;
						if (me.patrimoine!=null)
						{
							switch (me.notation) {
							case INTERET:
								me.patrimoine.setInteret(note);
								break;
							case MARCHE:
								me.patrimoine.setMarche(note);
								break;
							case TIME:
								me.patrimoine.setTime(note);
								break;
							case ACCES:
								me.patrimoine.setAcces(note);
								break;
							}
						}
					}
				}
			}
		});
	}
	
	public void setWritable(boolean writable) {
		this.writable=writable;
	}
	
	public void setPatrimoine(Patrimoine patrimoine) {
		this.patrimoine=patrimoine;
	}
	
	public void setNote(byte note) {
		this.note=note;
	}
	
	public byte getNote() {
		return this.note;
	}
	
	public Notationtype getNotationType()
	{
		return this.notation;
	}
	
	@Override
	public void act(float delta) {
		if (patrimoine!=null)
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
	}
	
	public void hidetext() {
		this.showtext=false;
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
