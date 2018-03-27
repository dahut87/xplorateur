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
	private Stage stage;
	private ImageTextButton back,view;
	private Boussole boussole;
	private Titre titre;
	
	public CompassScreen() {
		Gdx.app.debug("xplorateur-CompassScreen","Création des elements primordiaux du screen (stage, renderer, stack, table)");
		stage = new Stage(AssetLoader.viewport);		
		Gdx.app.debug("xplorateur-CompassScreen","Ajout des élements");
		boussole=new Boussole();
		boussole.setPosition(900, 0);
		titre=new Titre(null);
		titre.setPosition(10, 1020);
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
					if (boussole.getSelected()!=null)
						((Game) Gdx.app.getApplicationListener()).setScreen(new PatrimoineScreen(((Game) Gdx.app.getApplicationListener()).getScreen(), boussole.getSelected()));;
			}
		});
	}

	@Override
	public void show() {
		stage.addActor(boussole);
		stage.addActor(titre);
		stage.addActor(back);
		stage.addActor(view);
		Gdx.input.setInputProcessor(stage);
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
