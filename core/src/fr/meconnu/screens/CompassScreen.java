package fr.meconnu.screens;

import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton.ImageTextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Array.ArrayIterator;

import fr.meconnu.UI.Boussole;
import fr.meconnu.UI.Miniature;
import fr.meconnu.UI.TabbedPane;
import fr.meconnu.UI.Titre;
import fr.meconnu.assets.AssetLoader;
import fr.meconnu.assets.ScreenManager;
import fr.meconnu.assets.ScreenManager.Screentype;
import fr.meconnu.cache.Criteria;
import fr.meconnu.cache.Filler;
import fr.meconnu.cache.Patrimoine;
import fr.meconnu.cache.Patrimoine.FieldSizeType;
import fr.meconnu.cache.Patrimoines;
import fr.meconnu.calc.Geo;
import fr.meconnu.dialogs.WarningDialog;
import fr.meconnu.renderers.CompassRenderer;
import fr.meconnu.renderers.MenuRenderer;

public class CompassScreen implements Screen {
	private float runTime;
	private Stage stage;
	private ImageTextButton back,view,filtres,filtreprefs,sizes,zoom;
	private Boussole boussole;
	private Titre titre;
	private Label vitesse,direction,accelX,accelY,accelZ,X,Y,Z,distance,directiondest,consigne;
	private Image consignevisuel,logo2;
	private Sprite visuel;
	private TimerTask RefreshTask;
	private Timer timer;
	private CompassRenderer Renderer;
	private WarningDialog dialog;
	
	public CompassScreen() {
		Gdx.app.debug("xplorateur-CompassScreen","Création des elements primordiaux du screen (stage, renderer, stack, table)");
		Gdx.graphics.setContinuousRendering(true);
		dialog = new WarningDialog();
		stage = new Stage(AssetLoader.viewport);
		Renderer = new CompassRenderer(this);
		Gdx.app.debug("xplorateur-CompassScreen","Ajout des élements");
		boussole=new Boussole();
		boussole.setSelected(AssetLoader.cible);
		boussole.setPosition(900, 0);
		boussole.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				if (boussole.getSelected()!=null)
					AssetLoader.cible=boussole.getSelected();
					RefreshTask.run();
		    }
		});
		titre=new Titre(null,"Informations");
		titre.setPosition(280, 342);
		titre.setWidth(400f);
		back=new ImageTextButton("Menu",AssetLoader.Skin_images,"Back");
		back.setPosition(71f, 80f);
		back.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				ScreenManager.setScreen(Screentype.MENU);
			}
		});
		view=new ImageTextButton("Voir fiche",AssetLoader.Skin_images,"View");
		view.setPosition(71f, 250f);
		view.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
					if (boussole.getSelected()!=null)
					{
						ScreenManager.setArgument(boussole.getSelected());
						ScreenManager.callScreen(Screentype.PATRIMOINE);
					}
					else
					{
						dialog.show("Selectionnez d'abord un patrimoine !", stage);
					}
			}
		});
		SpriteDrawable sprite=new SpriteDrawable(AssetLoader.Atlas_images.createSprite("filtre"+String.valueOf(Patrimoines.getFilter())));
		ImageTextButtonStyle style=new ImageTextButton.ImageTextButtonStyle();
		style.up=sprite;
		style.font=AssetLoader.Skin_images.getFont("DejaVuSans-18");
		style.unpressedOffsetY=-52;
		style.pressedOffsetY=-56;	
		style.checkedOffsetY=-52;
		filtres=new ImageTextButton("Filtrage",style);
		filtres.setPosition(71f, 420f);
		filtres.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				int value=Patrimoines.getFilter()+1;
				value=value%3;
				Patrimoines.setFilter(value);
				SpriteDrawable sprite=new SpriteDrawable(AssetLoader.Atlas_images.createSprite("filtre"+String.valueOf(value)));
				ImageTextButtonStyle style=filtres.getStyle();
				style.up=sprite;
				filtres.setStyle(style);
				boussole.update();
			}
		});
		filtreprefs=new ImageTextButton("Options",AssetLoader.Skin_images,"filtreprefs");
		filtreprefs.setPosition(71f, 590f);
		filtreprefs.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (Patrimoines.getFilter()>0)
				{
					ScreenManager.setArgument(Patrimoines.getFilter());
					ScreenManager.callScreen(Screentype.SEARCH);
				}
				else
				{
					dialog.show("Selectionnez le filtre 1 ou 2 avant de choisir une option !", stage);
				}
			}
		});
		sprite=new SpriteDrawable(AssetLoader.Atlas_images.createSprite("moins"));
		style=new ImageTextButton.ImageTextButtonStyle();
		style.up=sprite;
		style.font=AssetLoader.Skin_images.getFont("DejaVuSans-18");
		style.unpressedOffsetY=-52;
		style.pressedOffsetY=-56;	
		style.checkedOffsetY=-52;
		zoom=new ImageTextButton("Taille boussole",style);
		zoom.setPosition(71f, 760);
		zoom.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				int value=boussole.getNeighborhoodSize()+1;
				value=value%3;
				SpriteDrawable sprite;
				if (value==2)
					sprite=new SpriteDrawable(AssetLoader.Atlas_images.createSprite("plus"));
				else
					sprite=new SpriteDrawable(AssetLoader.Atlas_images.createSprite("moins"));
				ImageTextButtonStyle style=zoom.getStyle();
				style.up=sprite;
				zoom.setStyle(style);
				boussole.setNeighborhoodSize(value);
			}
		});
		FieldSizeType choicesize=Criteria.getResultSize(Patrimoines.getselectedFilter());
		int value=choicesize.toSize();
		if (choicesize==FieldSizeType.MAXIMALE) 
			sprite=new SpriteDrawable(AssetLoader.Atlas_images.createSprite("less"));
		else
			sprite=new SpriteDrawable(AssetLoader.Atlas_images.createSprite("more"));
		style=new ImageTextButton.ImageTextButtonStyle();
		style.up=sprite;
		style.font=AssetLoader.Skin_images.getFont("DejaVuSans-18");
		style.unpressedOffsetY=-52;
		style.pressedOffsetY=-56;	
		style.checkedOffsetY=-52;
		sizes=new ImageTextButton(value+" résultats",style);
		sizes.setPosition(71f, 930f);
		sizes.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				FieldSizeType choicesize=Criteria.getResultSize(Patrimoines.getselectedFilter()).getPrevious();
				int value=choicesize.toSize();
				SpriteDrawable sprite;
				if (choicesize==FieldSizeType.MAXIMALE) 
					sprite=new SpriteDrawable(AssetLoader.Atlas_images.createSprite("less"));
				else
					sprite=new SpriteDrawable(AssetLoader.Atlas_images.createSprite("more"));
				ImageTextButtonStyle style=sizes.getStyle();
				style.up=sprite;
				sizes.setStyle(style);
				sizes.setText(value+" résultats");
				Criteria.setResultSize(Patrimoines.getselectedFilter(),choicesize);
			}
		});
		
		vitesse = new Label("-", AssetLoader.Skin_images,"Transparent");
		vitesse.setWidth(300f);
		vitesse.setPosition(534.0f, 793.0f);
		direction = new Label("-", AssetLoader.Skin_images,"Transparent");
		direction.setWidth(300f);
		direction.setPosition(580.0f, 700.0f);
		accelX = new Label("-", AssetLoader.Skin_images,"Transparent");
		accelX.setWidth(300f);
		accelX.setPosition(475.0f, 530.0f);
		accelY = new Label("-", AssetLoader.Skin_images,"Transparent");
		accelY.setWidth(300f);
		accelY.setPosition(475.0f, 468.0f);
		accelZ = new Label("-", AssetLoader.Skin_images,"Transparent");
		accelZ.setWidth(300f);
		accelZ.setPosition(475.0f, 406.0f);
		X = new Label("-", AssetLoader.Skin_images,"Transparent");
		X.setWidth(300f);
		X.setPosition(335.0f, 1030.0f);
		Y = new Label("-", AssetLoader.Skin_images,"Transparent");
		Y.setWidth(300f);
		Y.setPosition(640.0f, 1030.0f);
		Z = new Label("-", AssetLoader.Skin_images,"Transparent");
		Z.setWidth(300f);
		Z.setPosition(961.0f, 1030.0f);
		distance = new Label("-", AssetLoader.Skin_images,"Transparent");
		distance.setWidth(300f);
		distance.setPosition(580.0f, 230.0f);
		directiondest = new Label("-", AssetLoader.Skin_images,"Transparent");
		directiondest.setWidth(300f);
		directiondest.setPosition(585.0f, 143.0f);
		consigne = new Label("-", AssetLoader.Skin_images,"Little");
		consigne.setWidth(300f);
		consigne.setPosition(625.0f, 50.0f);
		consignevisuel=new Image(AssetLoader.Skin_images.getDrawable("white"));
		consignevisuel.setPosition(850f, 38f);
		logo2=new Image(AssetLoader.Skin_images.getDrawable("logo2"));
		logo2.setPosition(270f, 41f);
		Gdx.app.debug("xplorateur-CompassScreen","Création du timer");
		timer = new Timer();
		RefreshTask = new TimerTask() {
			@Override
			public void run() {
				if (boussole.getSelected()==null) {
					titre.setText("AUCUNE DESTINATION");
					logo2.setVisible(true);
					consignevisuel.setVisible(false);
				}
				else
				{
					titre.setPatrimoine(boussole.getSelected());
					logo2.setVisible(false);	
					consignevisuel.setVisible(true);
				}
				if (AssetLoader.Compass)
				{
					float angle;
					angle=Gdx.input.getAzimuth()+90;
					if (angle<0.0f)
					angle=360.0f+angle;
					direction.setText(String.valueOf(Math.round(angle))+"°");
					if (boussole.getSelected()!=null && AssetLoader.wrapper.isLocalisable())
					{
						Vector2 position=AssetLoader.wrapper.get2DLocation();
						Patrimoine patrimoine=boussole.getSelected();
						float Distance=patrimoine.GetDistance();
						if (Distance>5000)
							distance.setText(String.valueOf(Math.round(Distance/1000))+" km");
						else if (Distance>1000)
							distance.setText(String.valueOf(Math.round(Distance/100)/10.0f)+" km");
						else
							distance.setText(String.valueOf(Math.round(Distance))+" m");
						float anglepatri;
						int anglefinal;
						anglepatri=Geo.Angle(patrimoine.getPosition(), position);
						anglefinal=Math.round(anglepatri-angle+180f)%360;
						if (anglefinal>180)
							anglefinal=anglefinal-360;
						else if (anglefinal<-180)
							anglefinal=anglefinal+360;
						if (anglefinal<0)
							directiondest.setText(String.valueOf(anglefinal)+"°");
						else
							directiondest.setText("+"+String.valueOf(anglefinal)+"°");
						String laconsigne="Tout droit sur %dist%";
						String dir="dir1";
						if (Math.abs(anglefinal)>120)
						{
							laconsigne="Faire demi-tour";
							dir="dir4";
						}
						else if (Math.abs(anglefinal)>80)
						{
							laconsigne="Tourner de %angle%°";
							dir="dir3";
						}
						else if (Math.abs(anglefinal)>10)
						{
							laconsigne="Obliquer de %angle%°";
							dir="dir2";
						}
						laconsigne=laconsigne.replace("%angle%", String.valueOf(anglefinal)).replace("%dist%", String.valueOf(Math.round(Distance))+"m");					
						consigne.setText(laconsigne);
						visuel=AssetLoader.Atlas_images.createSprite(dir);
						if (anglefinal<0)
							visuel.flip(true, false);
						consignevisuel.setDrawable(new SpriteDrawable(visuel));
						consignevisuel.setSize(AssetLoader.Skin_images.getDrawable(dir).getMinWidth(), AssetLoader.Skin_images.getDrawable(dir).getMinHeight());

					}
				}
				else 
				{
					direction.setText("-");
					distance.setText("-");
					directiondest.setText("-");
					consigne.setText("-");	
				}
				if (AssetLoader.wrapper.isLocalisable())
				{
					float realspeed=AssetLoader.wrapper.getSpeed()*3.6f;
					if (realspeed>=10)
					{
						String speed=String.valueOf(Math.round(realspeed));
						vitesse.setText(speed+" km/h");
					}
					else if (realspeed>=0)
					{
						String speed=String.valueOf(realspeed);
						if (speed.length()>3)
							speed=speed.substring(0, 3);
						vitesse.setText(speed+" km/h");
					}
					else
						vitesse.setText("-");
				}
				if (AssetLoader.Accelerometer)
				{
					String accel;
					accel=String.valueOf(Gdx.input.getAccelerometerX());
					if (accel.length()>7)
						accel=accel.substring(0, 7);
					accelX.setText(accel);
					accel=String.valueOf(Gdx.input.getAccelerometerY());
					if (accel.length()>7)
						accel=accel.substring(0, 7);
					accelY.setText(accel);
					accel=String.valueOf(Gdx.input.getAccelerometerZ());
					if (accel.length()>7)
						accel=accel.substring(0, 7);
					accelZ.setText(accel);				
				}
				else
				{
					accelX.setText("-");
					accelY.setText("-");
					accelZ.setText("-");					
				}
				if (AssetLoader.wrapper.isLocalisable())
				{
					String coord;
					Vector3 coords=AssetLoader.wrapper.getLocation();
					coord=String.valueOf(coords.x);
					X.setText(coord);
					coord=String.valueOf(coords.y);
					Y.setText(coord);
					coord=String.valueOf(Math.round(coords.z));
					Z.setText(coord);
				}
				else
				{
					X.setText("-");
					Y.setText("-");
					Z.setText("-");	
				}
			}
		};
		Gdx.app.debug("xplorateur-CompassScreen","Lancement des tâches");
		timer.scheduleAtFixedRate(RefreshTask, 0, 250);
	}

	@Override
	public void show() {
		stage.addActor(boussole);
		stage.addActor(titre);
		stage.addActor(back);
		stage.addActor(view);		
		stage.addActor(filtres);
		stage.addActor(filtreprefs);
		stage.addActor(sizes);
		stage.addActor(zoom);
		stage.addActor(vitesse);
		stage.addActor(direction);
		stage.addActor(accelX);
		stage.addActor(accelY);
		stage.addActor(accelZ);
		stage.addActor(X);
		stage.addActor(Y);
		stage.addActor(Z);
		stage.addActor(distance);
		stage.addActor(directiondest);
		stage.addActor(consigne);
		stage.addActor(consignevisuel);
		stage.addActor(logo2);
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render(float delta) {
		runTime += delta;
		Renderer.render(delta, runTime);
		stage.act();
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		AssetLoader.viewport.update(width, height);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		timer.cancel();
		RefreshTask.cancel();
		stage.dispose();
	}
}
