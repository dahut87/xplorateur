package fr.meconnu.screens;

import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
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

public class CompassScreen implements Screen {
	private float runTime;
	private Timer timer;
	private TimerTask MinisTask;
	private Stage stage;
	private Array<Miniature> minis;
	private ImageTextButton back,view;
	private Boussole boussole;
	private Titre titre;
	
	public CompassScreen() {
		Gdx.app.debug("xplorateur-CompassScreen","Création des elements primordiaux du screen (stage, renderer, stack, table)");
		stage = new Stage(AssetLoader.viewport);		
		Gdx.app.debug("xplorateur-CompassScreen","Ajout des élements");
		boussole=new Boussole();
		boussole.setPosition(1400, AssetLoader.height/2.0f-30);
		minis = new Array<Miniature>();
		titre=new Titre(null);
		titre.setPosition(10, 1020);
		for(int i=0;i<20;i++) {
			Miniature mini=new Miniature(boussole);
			minis.add(mini);
			mini.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					Miniature selectmini=((Miniature)event.getListenerActor());
					for(Miniature mini: minis)
						mini.unSelect();
					selectmini.Select();
					if (selectmini!=null && selectmini.getPatrimoine()!=null)
						titre.setPatrimoine(selectmini.getPatrimoine());
				}
			});	
		}
		back=new ImageTextButton("Menu",AssetLoader.Skin_images,"Back");
		back.setPosition(20f, 80f);
		back.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				((Game) Gdx.app.getApplicationListener()).setScreen(new MenuScreen());;
			}
		});
		view=new ImageTextButton("Voir fiche",AssetLoader.Skin_images,"View");
		view.setPosition(150f, 80f);
		view.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				for(Miniature mini: minis)
					if (mini.getSelect()!=null)
						((Game) Gdx.app.getApplicationListener()).setScreen(new PatrimoineScreen(((Game) Gdx.app.getApplicationListener()).getScreen(), mini.getSelect()));;
			}
		});
		Gdx.app.debug("xplorateur-CompassScreen", "Mise en place des timers.");
		timer = new Timer();
		MinisTask = new TimerTask() {
			@Override
			public void run() {
				updateMinis();
			}
		};
		timer.scheduleAtFixedRate(MinisTask, 0, 1000);
	}
	
	public void updateMinis() {
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
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
		stage.addActor(boussole);
		stage.addActor(titre);
		stage.addActor(back);
		stage.addActor(view);
		for(Miniature mini: minis)
			stage.addActor(mini);	
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(255, 255, 255, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		runTime += delta;
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
