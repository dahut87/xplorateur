package fr.meconnu.UI;

import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

import fr.meconnu.assets.AssetLoader;
import fr.meconnu.cache.Filler;
import fr.meconnu.cache.Patrimoine;
import fr.meconnu.cache.Patrimoines;
import fr.meconnu.screens.PatrimoineScreen;

public class Boussole extends Actor {
	private Sprite boussole,boussole2,aiguille,fleche,viseur;
	private Timer timer;
	private TimerTask CompassTask,RefreshTask;
	private boolean minimaxi;
	private Array<Miniature> minis;
	private Patrimoine selected;

	public Boussole() {
		minimaxi=false;
		//this.debug();
		this.setWidth(1020f);
		this.setHeight(1020f);
		this.setOrigin(this.getWidth()/2.0f, this.getHeight()/2.0f);
		this.setPosition(AssetLoader.width/2.0f-this.getOriginX(), AssetLoader.height/2.0f-this.getOriginY());
		minis = new Array<Miniature>();
		for(int i=0;i<20;i++) {
			Miniature mini=new Miniature(this);
			minis.add(mini);
			mini.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					Miniature selectmini=((Miniature)event.getListenerActor());
					for(Miniature mini: minis)
						mini.unSelect();
					selectmini.Select();
					selected=selectmini.getPatrimoine();
				}
			});	
		}
		viseur=new Sprite(AssetLoader.Atlas_images.createSprite("viseur"));
		fleche=new Sprite(AssetLoader.Atlas_images.createSprite("fleche"));
		boussole2 = new Sprite(AssetLoader.Atlas_images.createSprite("boussole2"));
		boussole = new Sprite(AssetLoader.Atlas_images.createSprite("boussole"));
		aiguille = new Sprite(AssetLoader.Atlas_images.createSprite("aiguille"));
		timer = new Timer();
		CompassTask = new TimerTask() {
			@Override
			public void run() {
				/*if (minimaxi)
					aiguille.setRotation(Gdx.input.getAzimuth()+90);
				else
					boussole2.setRotation(Gdx.input.getAzimuth()+90);*/
			}
		};
		timer.scheduleAtFixedRate(CompassTask, 0, 250);
		RefreshTask = new TimerTask() {
			@Override
			public void run() {
				update();
			}
		};
		timer.scheduleAtFixedRate(RefreshTask, 0, 1000);
		addListener(new ActorGestureListener() { 
			@Override
			public void tap(InputEvent event, float x, float y, int count, int button)  {
				minimaxi=!minimaxi;
			}
		});
	}
	
	public Patrimoine getSelected() {
		return this.selected;
	}
	
	public Actor hit(float x, float y, boolean touchable) {
		for(Miniature mini: minis)
		{
			Actor hitter=mini.hit(x, y, touchable);
			if (hitter!=null) 
				return hitter;
		}
		return null;
	}
	
	public void update() {
		Vector2 position;
		if (Filler.isLocaliser())
			position=Filler.getLocaliser().getLocation();
		else
			position=new Vector2(45.038835f , 1.237758f);
		Patrimoines patrimoines=Patrimoines.getNear(position,20);
		Iterator<Miniature> iterator = minis.iterator();
		for(Patrimoine patrimoine: patrimoines.getValues()) {
				iterator.next().setPatrimoine(patrimoine,position);
		}
		while (iterator.hasNext())
			iterator.next().setPatrimoine(null,null);
		for(Miniature mini: minis)
			if (mini.getPatrimoine()==this.selected)
				mini.Select();
			else
				mini.unSelect();
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
		boussole2.setPosition(this.getX(),this.getY());
		boussole.setScale(ratio);
		boussole.setOrigin(boussole.getWidth()/2.0f, boussole.getHeight()/2.0f);
		boussole.setPosition(this.getX(), this.getY());
		aiguille.setScale(ratio);
		aiguille.setOrigin(aiguille.getWidth()/2.0f, aiguille.getHeight()/2.0f);
		aiguille.setPosition(this.getX()+boussole2.getWidth()/2.0f-aiguille.getWidth()/2.0f, this.getY()+boussole2.getHeight()/2.0f-aiguille.getHeight()/2.0f);
		fleche.setScale(ratio);
		fleche.setPosition(boussole.getX()+boussole.getWidth()/2.0f-fleche.getWidth()/2.0f,boussole.getY()+boussole.getHeight()-15);
		viseur.setPosition(boussole.getX()+boussole.getWidth()/2.0f-viseur.getWidth()/2.0f,boussole.getY()+boussole.getHeight()-viseur.getHeight()-3);
		for(Miniature mini: minis)
			mini.act(delta);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		if (minimaxi) {
			boussole.draw(batch);
			aiguille.draw(batch);
		}
		else
		{
			boussole2.draw(batch);
			viseur.draw(batch);
		}
		fleche.draw(batch);
		if (!this.getMaxi())
			for(Miniature mini: minis)
				mini.draw(batch, parentAlpha);
	}
}