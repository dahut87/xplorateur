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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Array.ArrayIterator;

import fr.meconnu.UI.Boussole;
import fr.meconnu.UI.Miniature;
import fr.meconnu.UI.TabbedPane;
import fr.meconnu.UI.Titre;
import fr.meconnu.assets.AssetLoader;
import fr.meconnu.cache.Filler;
import fr.meconnu.cache.Patrimoine;
import fr.meconnu.cache.Patrimoines;
import fr.meconnu.renderers.CompassRenderer;
import fr.meconnu.renderers.MenuRenderer;

public class CompassScreen implements Screen {
	private float runTime;
	private Stage stage;
	private ImageTextButton back,view;
	private Boussole boussole;
	private Titre titre;
	private Label vitesse,direction,accelX,accelY,accelZ,X,Y,Z;
	private TimerTask RefreshTask;
	private Timer timer;
	private CompassRenderer Renderer;
	
	public CompassScreen() {
		Gdx.app.debug("xplorateur-CompassScreen","Création des elements primordiaux du screen (stage, renderer, stack, table)");
		stage = new Stage(AssetLoader.viewport);
		Renderer = new CompassRenderer(this);
		Gdx.app.debug("xplorateur-CompassScreen","Ajout des élements");
		boussole=new Boussole();
		boussole.setPosition(900, 0);
		boussole.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				if (boussole.getSelected()!=null)
					titre.setText(boussole.getSelected().getTitre());
		    }
		});
		titre=new Titre(null,"Informations");
		titre.setPosition(280, 342);
		back=new ImageTextButton("Menu",AssetLoader.Skin_images,"Back");
		back.setPosition(71f, 80f);
		back.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				((Game) Gdx.app.getApplicationListener()).setScreen(new MenuScreen());;
			}
		});
		view=new ImageTextButton("Voir fiche",AssetLoader.Skin_images,"View");
		view.setPosition(71f, 250f);
		view.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
					if (boussole.getSelected()!=null)
						((Game) Gdx.app.getApplicationListener()).setScreen(new PatrimoineScreen(((Game) Gdx.app.getApplicationListener()).getScreen(), boussole.getSelected()));;
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
		timer = new Timer();
		RefreshTask = new TimerTask() {
			@Override
			public void run() {
				if (AssetLoader.Compass)
				{
					float angle;
					angle=Gdx.input.getAzimuth()+90;
					if (angle<0)
						angle=360-angle;
					direction.setText(String.valueOf(angle)+"°");
				}
				else
					direction.setText("-");
				if (Filler.isRunning())
				{
					float realspeed=Filler.getSpeed();
					if (realspeed>=0)
					{
						String speed=String.valueOf(Filler.getSpeed()*3.6);
						if (speed.length()>5)
							speed=speed.substring(0, 5);
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
				if (Filler.isLocaliser() && Filler.getLocaliser().isLocalisable())
				{
					String coord;
					coord=String.valueOf(Filler.getLocaliser().getLocation().x);
					X.setText(coord);
					coord=String.valueOf(Filler.getLocaliser().getLocation().y);
					Y.setText(coord);
					coord=String.valueOf(Filler.getLocaliser().getLocation().z);
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
		timer.scheduleAtFixedRate(RefreshTask, 0, 250);
	}

	@Override
	public void show() {
		stage.addActor(boussole);
		stage.addActor(titre);
		stage.addActor(back);
		stage.addActor(view);
		stage.addActor(vitesse);
		stage.addActor(direction);
		stage.addActor(accelX);
		stage.addActor(accelY);
		stage.addActor(accelZ);
		stage.addActor(X);
		stage.addActor(Y);
		stage.addActor(Z);
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
		// TODO Auto-generated method stub
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
		
	}
}
