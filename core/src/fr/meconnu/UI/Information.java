package fr.meconnu.UI;

import java.util.function.Function;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

import fr.meconnu.assets.AssetLoader;
import fr.meconnu.cache.Filler.Movetype;
import fr.meconnu.cache.Patrimoine;

public class Information extends Actor{
	private Informationtype information;
	private Patrimoine patrimoine;
	private TextureRegion icon;
	private Label label;
	private String text;
	private LabelStyle style;
	
	public enum Informationtype {
		COMMUNE("Commune"), COORDINATES("Coordonnées"), TYPE("Catégorie"), KEYWORDS("Mots clés"), AUTHORS("Auteurs");
		private String text;
		
		private Informationtype(final String text) {
			this.text = text;
		}
		
		@Override
		public String toString() {
			return text;
		}
	}
	
	public Information(Patrimoine patrimoine,Informationtype information) {
		this.information=information;
		label=new Label("",AssetLoader.Skin_images,"Informations");
		style=label.getStyle();
		this.setPatrimoine(patrimoine);
	}
	
	public void setPatrimoine(Patrimoine patrimoine) {
		this.patrimoine=patrimoine;
		init();
	}
	
	public void init() {
		style.font=AssetLoader.Skin_images.getFont("DejaVuSans-26");
		switch (this.information) {
		case COMMUNE:
			icon=new TextureRegion(AssetLoader.Atlas_images.findRegion("Commune"));
			text=patrimoine.getVille_nom_reel()+" - "+patrimoine.getInsee_str();
			break;
		case COORDINATES:
			icon=new TextureRegion(AssetLoader.Atlas_images.findRegion("earth"));
			text=patrimoine.getCoordx_str()+" ,"+patrimoine.getCoordy_str()+" - lat/lon WGS84";
			break;
		case TYPE:
			icon=new TextureRegion(AssetLoader.Atlas_images.findRegion(patrimoine.getTypes_str().replace(",", "").replace(" ", "_")));
			text=patrimoine.getTypes().toString();
			break;
		case KEYWORDS:
			icon=new TextureRegion(AssetLoader.Atlas_images.findRegion("keywords"));
			text=patrimoine.getMots();
			if (text!=null && text.equals(""))
			{
				text="Aucun";
				style.font=AssetLoader.Skin_images.getFont("DejaVuSans-26-red");
			}
			break;
		case AUTHORS:
			icon=new TextureRegion(AssetLoader.Atlas_images.findRegion("person"));
			text="Rédigé par "+patrimoine.getNom()+"\nMis à jour le "+patrimoine.getMaj_formated()+"\nCache datant de "+patrimoine.getLocalmaj_formated();
			style.font=AssetLoader.Skin_images.getFont("DejaVuSans-18-grey");
			break;
		}
		this.label.setStyle(style);
		this.label.setText(text);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
	    batch.draw(icon, this.getX(), this.getTop()-this.getHeight(), this.getHeight(), this.getHeight());
	    this.label.setPosition(this.getX()+this.getHeight()+8, this.getY()+(this.getHeight()-this.label.getHeight())/2.0f);
	    this.label.draw(batch, parentAlpha);
	}

}
