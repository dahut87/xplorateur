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
	private int boussolesize;
	private Vector2 flag=null;

	public Boussole() {
		minimaxi=false;
		//this.debug();
		boussolesize=1;
		shaperenderer=new ShapeRenderer();
		this.setWidth(1020f);
		this.setHeight(1020f);
		this.setOrigin(this.getWidth()/2.0f, this.getHeight()/2.0f);
		this.setPosition(AssetLoader.width/2.0f-this.getOriginX(), AssetLoader.height/2.0f-this.getOriginY());
		hit = new Array<Miniature>();
		maj = new Array<Miniature>();
		act = new Array<Miniature>();
		draw = new Array<Miniature>();
		for(int i=0;i<Patrimoines.maxpatrimoines;i++) {
			Miniature mini=new Miniature(this);
			hit.add(mini);
			maj.add(mini);
			act.add(mini);
			draw.add(mini);
			mini.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					Miniature selectmini=((Miniature)event.getListenerActor());
					if (selectmini.getPatrimoine()!=null) 
					{
						for(Miniature mini: hit)
							mini.unSelect();
						if (selected!=null && selectmini.getPatrimoine().getId().equals(selected.getId()))
						{
							selectedmini=null;
							selected=null;
						}
						else
						{
							selectmini.Select();
							selectedmini=selectmini;
							selected=selectmini.getPatrimoine();
						}
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
			public void tap (InputEvent event, float x, float y, int count, int button) {
				if (count>1)
					minimaxi=!minimaxi;
			   }
			@Override
			public void pinch(InputEvent event, Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
				Vector2 initial=new Vector2(initialPointer1.x,initialPointer2.y);
				Vector2 d1=initialPointer1.sub(pointer1);
				Vector2 d2=initialPointer2.sub(pointer2);
				Vector2 bigger=	d1.add(d2);
				if (Math.abs(bigger.y)>20 && (flag==null || (flag.x!=initial.x && flag.y!=initial.y)))
				{
					flag=initial.cpy();
					if (bigger.y>0 && boussolesize<2)
						boussolesize++;
					else if (bigger.y<0 && boussolesize>0)
						boussolesize--;
					else
						flag=null;
				}
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
		Patrimoines patrimoines=Patrimoines.getNearAutoFiltered();
		Iterator<Miniature> iterator = maj.iterator();
		for(Patrimoine patrimoine: patrimoines.getValues()) {
			if (iterator.hasNext())
				iterator.next().setPatrimoine(patrimoine);
		}
		while (iterator.hasNext())
			iterator.next().setPatrimoine(null);
		for(Miniature mini: maj)
			if (this.selected!=null && mini.getPatrimoine()!=null && mini.getPatrimoine().getId().equals(this.selected.getId()))
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
	
	public int getNeighborhoodSize() {
		return boussolesize;
	}
	
	public void setNeighborhoodSize(int size) {
		boussolesize=size%3;
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
		if (boussolesize==0)
			boussole2.setRegion(AssetLoader.Atlas_images.findRegion("boussole4"));
		else if (boussolesize==1)
			boussole2.setRegion(AssetLoader.Atlas_images.findRegion("boussole2"));
		else
			boussole2.setRegion(AssetLoader.Atlas_images.findRegion("boussole3"));
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
				shaperenderer.rectLine(this.getX()+this.getOriginX()-2, this.getY()+this.getOriginY()-4, selectedmini.getX()+selectedmini.getOriginX(), selectedmini.getY()+selectedmini.getOriginY(),2,red,red);
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