package fr.meconnu.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import fr.meconnu.UI.Notation.Notationtype;
import fr.meconnu.UI.NotationGroup;
import fr.meconnu.UI.ParticulariteGroup;
import fr.meconnu.UI.SearchList;
import fr.meconnu.UI.TypeGroup;
import fr.meconnu.assets.AssetLoader;
import fr.meconnu.renderers.MenuRenderer;

public class SearchScreen implements Screen {
	private Stage stage;
	private TextField searchfield;
	private NotationGroup interet,duree;
	private ParticulariteGroup particularite;
	private TypeGroup type;
	private ImageTextButton back;
	private SearchList searchlist,selectedlist;
	
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
		searchfield=new TextField("",AssetLoader.Skin_images,"Normal");
		searchfield.setPosition(10f, 1000f);
		searchfield.addListener(new ChangeListener() {
	        @Override
	        public void changed(ChangeEvent event, Actor actor) {
	        	searchlist.setSearch(searchfield.getText().toLowerCase());
	        }
	    });
		interet=new NotationGroup(Notationtype.INTERET);
		interet.setPosition(200f, 800f);
		duree=new NotationGroup(Notationtype.TIME);
		duree.setPosition(800f, 800f);
		type=new TypeGroup();
		type.setPosition(800f, 200f);
		type=new TypeGroup();
		type.setPosition(800f, 200f);
		particularite=new ParticulariteGroup();
		particularite.setPosition(1200f, 500f);
		searchlist=new SearchList();
		searchlist.setBounds(25f, 350f, 350f, 250f);
		selectedlist=new SearchList();
		selectedlist.setBounds(525f, 350f, 350f, 250f);
	}

	@Override
	public void show() {
		stage.addActor(back);
		stage.addActor(searchfield);
		stage.addActor(interet);
		stage.addActor(duree);
		stage.addActor(type);
		stage.addActor(particularite);
		stage.addActor(searchlist);
		stage.addActor(selectedlist);
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
