package fr.meconnu.UI;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import fr.meconnu.assets.AssetLoader;
import fr.meconnu.cache.Patrimoine;
import fr.meconnu.calc.Geo;

public class Miniature extends Actor{
	private Patrimoine patrimoine;
	private TextureRegion icon;
	private Vector2 position;
	private float transparence,angle,distance;
	private boolean selected;
	private Boussole boussole;
	
	public Miniature(Boussole boussole) {
		super();
		this.setTouchable(Touchable.enabled);
		this.setVisible(true);
		this.setPatrimoine(null,null);
		this.boussole=boussole;
		this.debug();
	}
	
	public void setPatrimoine(Patrimoine patrimoine, Vector2 position) {
		this.patrimoine=patrimoine;
		this.position=position;
	}
	
	public Patrimoine getPatrimoine()
	{
		return this.patrimoine;
	}
	
	public void Select() {
		this.selected=true;
	}
	
	public void unSelect() {
		this.selected=false;
	}
	
	@Override
	public void act(float delta) {
		if (patrimoine!=null && position!=null)
		{
			icon=AssetLoader.Atlas_images.findRegion(patrimoine.getTypes().toString().replace(" ", "_").replace(",",""));
			distance=Geo.Distance2D(patrimoine.getPosition(), position);
			angle=Geo.Angle(patrimoine.getPosition(), position);
			Float dist=0f,size=0f,mindistance=0f;
			switch (boussole.getSize())
			{
			case 1:
				mindistance=310f;
				if (distance<10)
				{
					transparence=1f;
					size=128f;
					dist=-mindistance;
				}
				else if (distance<2000)
				{
					transparence=1f;
					size=90f;
					dist=23f;
				}
				else if (distance<5000)
				{
					transparence=0.8f;
					size=50f;
					dist=90f;
				}
				else if (distance<10000)
				{
					transparence=0.6f;
					size=47f;
					dist=193f;
				}
				else
				{
					transparence=0.45f;
					size=20f;
					dist=276f;
				}
				break;
			case 2:
				mindistance=210f;
				if (distance<10)
				{
					transparence=1f;
					size=128f;
					dist=-mindistance;
				}
				else if (distance<200)
				{
					transparence=1f;
					size=80f;
					dist=43f;
				}
				else if (distance<500)
				{
					transparence=0.8f;
					size=65f;
					dist=128f;
				}
				else if (distance<2000)
				{
					transparence=0.6f;
					size=47f;
					dist=193f;
				}
				else if (distance<4000)
				{
					transparence=0.45f;
					size=30f;
					dist=241f;
				}
				else
				{
					transparence=0.3f;
					size=20f;
					dist=276f;
				}
				break;
			case 3:
				mindistance=110f;
				if (distance<10)
				{
					transparence=1f;
					size=128f;
					dist=-mindistance;
				}
				else if (distance<500)
				{
					transparence=1f;
					size=96f;
					dist=42f;
				}
				else if (distance<4000)
				{
					transparence=0.8f;
					size=68f;
					dist=152f;
				}
				else if (distance<10000)
				{
					transparence=0.6f;
					size=55f;
					dist=235f;
				}
				else if (distance<20000)
				{
					transparence=0.45f;
					size=46f;
					dist=310f;
				}
				else
				{
					transparence=0.4f;
					size=32f;
					dist=356f;
				}
				break;
			}
			this.setWidth(size);
			this.setHeight(size);
			this.setOrigin(this.getWidth()/2.0f, this.getHeight()/2.0f);
			//this.debug();
			Vector2 longueur=new Vector2(mindistance+dist,0);
			longueur.setAngle(270-angle+boussole.getAzimuth());
			longueur.add(new Vector2(1410,AssetLoader.height/2-30));
			this.setPosition(longueur.x-size/2.0f, longueur.y-size/2.0f);
		}
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		Color tempcolor=batch.getColor();
		if (selected)
			batch.setColor(1.0f, 0.0f, 0.0f, 1.0f);
		else
			batch.setColor(1.0f, 1.0f, 1.0f, transparence);
		if (icon!=null)
			if (distance<10)
				batch.draw(icon, this.getX(), this.getY(), this.getOriginX(), this.getOriginY(),this.getWidth(), this.getHeight(), 1f, 1f,0);
			else
				batch.draw(icon, this.getX(), this.getY(), this.getOriginX(), this.getOriginY(),this.getWidth(), this.getHeight(), 1f, 1f, 180-angle+boussole.getAzimuth());
		batch.setColor(tempcolor);
	}

}
