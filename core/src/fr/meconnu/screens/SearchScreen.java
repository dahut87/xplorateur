package fr.meconnu.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import fr.meconnu.UI.Notation.Notationtype;
import fr.meconnu.UI.NotationGroup;
import fr.meconnu.assets.AssetLoader;
import fr.meconnu.renderers.MenuRenderer;

public class SearchScreen implements Screen {
	private Stage stage;
	private TextField searchfield;
	private NotationGroup notationgroup;
	private ImageTextButton back;
	
	public SearchScreen() {
		Gdx.app.debug("xplorateur-SearchScreenScreen","Création des elements primordiaux du screen (stage, renderer, stack, table)");
		stage = new Stage(AssetLoader.viewport);
		Gdx.app.debug("xplorateur-SearchScreenScreen","Ajout des élements");
		back=new ImageTextButton("Menu",AssetLoader.Skin_images,"Back");
		back.setPosition(71f, 80f);
		back.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				((Game) Gdx.app.getApplicationListener()).setScreen(new MenuScreen());;
			}
		});
		searchfield=new TextField("...",AssetLoader.Skin_images,"Normal");
		searchfield.setPosition(10f, 1000f);
		notationgroup=new NotationGroup(Notationtype.INTERET);
		notationgroup.setPosition(10f, 800f);
	}

	@Override
	public void show() {
		stage.addActor(back);
		stage.addActor(searchfield);
		stage.addActor(notationgroup);
		Gdx.input.setInputProcessor(stage);
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(255, 255, 255, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		stage.act(delta);
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
		
	}

}
