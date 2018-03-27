package fr.meconnu.UI;

import java.util.Timer;
import java.util.TimerTask;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import fr.meconnu.assets.AssetLoader;
import fr.meconnu.screens.PatrimoineScreen;

public class Boussole extends Actor {
	private Sprite boussole,boussole2,aiguille,fleche,viseur;
	private Timer timer;
	private TimerTask CompassTask;
	private boolean minimaxi;

	public Boussole() {
		minimaxi=false;
		this.setWidth(1020f);
		this.setHeight(1020f);
		this.setOrigin(this.getWidth()/2.0f, this.getHeight()/2.0f);
		this.setPosition(AssetLoader.width/2.0f-this.getOriginX(), AssetLoader.height/2.0f-this.getOriginY());
		viseur=new Sprite(AssetLoader.Atlas_images.createSprite("viseur"));
		fleche=new Sprite(AssetLoader.Atlas_images.createSprite("fleche"));
		boussole2 = new Sprite(AssetLoader.Atlas_images.createSprite("boussole2"));
		boussole = new Sprite(AssetLoader.Atlas_images.createSprite("boussole"));
		aiguille = new Sprite(AssetLoader.Atlas_images.createSprite("aiguille"));
		timer = new Timer();
		CompassTask = new TimerTask() {
			@Override
			public void run() {
				if (minimaxi)
					aiguille.setRotation(Gdx.input.getAzimuth()+90);
				else
					boussole2.setRotation(Gdx.input.getAzimuth()+90);
			}
		};
		timer.scheduleAtFixedRate(CompassTask, 0, 250);
		addListener(new ActorGestureListener() { 
			@Override
			public void tap(InputEvent event, float x, float y, int count, int button)  {
				minimaxi=!minimaxi;
			}
		});
	}
	
	public float getAzimuth() {
		if (minimaxi)
			return aiguille.getRotation();
		else
			return boussole2.getRotation();
	}
	
	public boolean getMaxi() {
		return minimaxi;
	}
	
	@Override
	public void act(float delta) {
		float ratio=this.getHeight()/1020f;
		boussole2.setScale(ratio);
		boussole2.setOrigin(boussole2.getWidth()/2.0f, boussole2.getHeight()/2.0f);
		boussole2.setPosition(this.getX()-boussole2.getOriginX(),this.getY()-boussole2.getOriginY());
		boussole.setScale(ratio);
		boussole.setOrigin(boussole.getWidth()/2.0f, boussole.getHeight()/2.0f);
		boussole.setPosition(this.getX()-boussole.getOriginX(), this.getY()-boussole.getOriginY());
		aiguille.setScale(ratio);
		aiguille.setOrigin(aiguille.getWidth()/2.0f, aiguille.getHeight()/2.0f);
		aiguille.setPosition(this.getX()-aiguille.getOriginX(), this.getY()-aiguille.getOriginY());
		fleche.setScale(ratio);
		fleche.setPosition(boussole.getX()+boussole.getWidth()/2.0f-fleche.getRegionWidth()/2.0f,boussole.getY()+boussole.getHeight()-15);
		viseur.setPosition(boussole.getX()+boussole.getWidth()/2.0f-viseur.getRegionWidth()/2.0f,boussole.getY()+boussole.getHeight()-viseur.getHeight()-3);
	} 
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		fleche.draw(batch);
		if (minimaxi) {
			boussole.draw(batch);
			aiguille.draw(batch);
		}
		else
		{
			boussole2.draw(batch);
			viseur.draw(batch);
		}
	}
}