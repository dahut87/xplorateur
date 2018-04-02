package fr.meconnu.UI;

import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
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
	private Array<Miniature> hit,maj,act,draw;
	private Patrimoine selected;
	private Miniature selectedmini;
	private ShapeRenderer shaperenderer;

	public Boussole() {
		minimaxi=false;
		//this.debug();
		shaperenderer=new ShapeRenderer();
		this.setWidth(1020f);
		this.setHeight(1020f);
		this.setOrigin(this.getWidth()/2.0f, this.getHeight()/2.0f);
		this.setPosition(AssetLoader.width/2.0f-this.getOriginX(), AssetLoader.height/2.0f-this.getOriginY());
		hit = new Array<Miniature>();
		maj = new Array<Miniature>();
		act = new Array<Miniature>();
		draw = new Array<Miniature>();
		for(int i=0;i<20;i++) {
			Miniature mini=new Miniature(this);
			hit.add(mini);
			maj.add(mini);
			act.add(mini);
			draw.add(mini);
			mini.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					Miniature selectmini=((Miniature)event.getListenerActor());
					if (selectmini.getPatrimoine()!=null || selected!=null || !selectmini.getPatrimoine().getId().equals(selected.getId())) 
					{
						for(Miniature mini: hit)
							mini.unSelect();
						selectmini.Select();
						selectedmini=selectmini;
						selected=selectmini.getPatrimoine();
						ChangeEvent changed=new ChangeEvent();
						fire(changed);
					}
				}
			});	
		}
		boussole2 = new Sprite(AssetLoader.Atlas_images.createSprite("boussole2"));
		boussole = new Sprite(AssetLoader.Atlas_images.createSprite("boussole"));
		aiguille = new Sprite(AssetLoader.Atlas_images.createSprite("aiguille"));
		fleche=new Sprite(AssetLoader.Atlas_images.createSprite("fleche"));
		viseur=new Sprite(AssetLoader.Atlas_images.createSprite("viseur"));
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
		RefreshTask = new TimerTask() {
			@Override
			public void run() {
				update();
			}
		};
		timer.scheduleAtFixedRate(RefreshTask, 0, 1000);
		addListener(new ActorGestureListener() { 
			@Override
			public boolean longPress (Actor actor, float x, float y) {
					minimaxi=!minimaxi;
				return true;
			   }
		});
	}
	
	public Patrimoine getSelected() {
		return this.selected;
	}
	
	@Override
	public Actor hit(float x, float y, boolean touchable) {
		for(Miniature mini: hit)
		{
			Vector2 coords=this.localToStageCoordinates(new Vector2(x,y));
			if (coords.x>=mini.getX() && coords.x<=mini.getRight() && coords.y>=mini.getY() && coords.y<=mini.getTop())
				return mini;
		}
		return this;
	}
	
	public void update() {
		Vector2 position;
		if (Filler.isLocaliser())
			position=Filler.getLocaliser().get2DLocation();
		else
			position=new Vector2(45.038835f , 1.237758f);
		Patrimoines patrimoines=Patrimoines.getNear(position,20);
		Iterator<Miniature> iterator = maj.iterator();
		for(Patrimoine patrimoine: patrimoines.getValues()) {
			if (iterator.hasNext())
				iterator.next().setPatrimoine(patrimoine,position);
		}
		while (iterator.hasNext())
			iterator.next().setPatrimoine(null,null);
		for(Miniature mini: maj)
			if (this.selected!=null && mini.getPatrimoine().getId().equals(this.selected.getId()))
			{
				mini.Select();
				selectedmini=mini;
			}
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
		viseur.setPosition(boussole.getX()+boussole.getWidth()/2.0f-viseur.getWidth()/2.0f,boussole.getY()+boussole.getHeight()-viseur.getHeight());
		for(Miniature mini: act)
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
			if (selected!=null && selectedmini!=null)
			{
				batch.end();
				Gdx.gl.glEnable(GL20.GL_BLEND);
				shaperenderer.setProjectionMatrix(AssetLoader.Camera.combined);
				shaperenderer.begin(ShapeType.Line);
				Color red=new Color(1.0f, 0f, 0f, 1.0f);
				shaperenderer.rectLine(this.getX()+this.getOriginX()+4, this.getY()+this.getOriginY(), selectedmini.getX()+selectedmini.getOriginX(), selectedmini.getY()+selectedmini.getOriginY(),2,red,red);
				shaperenderer.end();
				Gdx.gl.glDisable(GL20.GL_BLEND);
				batch.begin();
			}
			for(Miniature mini: draw)
				mini.draw(batch, parentAlpha);
			viseur.draw(batch);
			fleche.draw(batch);
		}
	}
}