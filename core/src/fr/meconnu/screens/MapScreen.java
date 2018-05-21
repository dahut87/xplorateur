package fr.meconnu.screens;


import static org.oscim.backend.GLAdapter.gl;
import org.oscim.renderer.GLState;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.oscim.backend.GL;
import org.oscim.gdx.GdxMap;
import org.oscim.layers.TileGridLayer;
import org.oscim.layers.tile.buildings.BuildingLayer;
import org.oscim.layers.tile.vector.VectorTileLayer;
import org.oscim.layers.tile.vector.labeling.LabelLayer;
import org.oscim.map.Layers;
import org.oscim.map.Map;
import org.oscim.renderer.MapRenderer;
import org.oscim.theme.VtmThemes;
import org.oscim.tiling.TileSource;
import org.oscim.utils.Parameters;

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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Array;

import fr.meconnu.UI.Notation.Notationtype;
import fr.meconnu.UI.Description;
import fr.meconnu.UI.Information;
import fr.meconnu.UI.LabeletClassement;
import fr.meconnu.UI.MyGdxMap;
import fr.meconnu.UI.Notation;
import fr.meconnu.UI.NotationGroup;
import fr.meconnu.UI.Order;
import fr.meconnu.UI.Particularite;
import fr.meconnu.UI.ParticulariteGroup;
import fr.meconnu.UI.PatrimoListe;
import fr.meconnu.UI.PhotoView;
import fr.meconnu.UI.SearchList;
import fr.meconnu.UI.Sizer;
import fr.meconnu.UI.TabbedPane;
import fr.meconnu.UI.Titre;
import fr.meconnu.UI.TypeGroup;
import fr.meconnu.assets.AssetLoader;
import fr.meconnu.assets.ScreenManager;
import fr.meconnu.assets.ScreenManager.Screentype;
import fr.meconnu.cache.Criteria;
import fr.meconnu.cache.Patrimoines;
import fr.meconnu.renderers.MenuRenderer;

public class MapScreen implements Screen {
	private Stage stage;
	private ImageTextButton back;
	private Stack stack;
	private float runTime;
	private Table background;
    protected MyGdxMap map;
	
	public MapScreen() {
		Gdx.app.debug("xplorateur-MapScreen","Création des elements primordiaux du screen (stage, renderer, stack, table)");
		stage = new Stage(AssetLoader.viewport);
		stack = new Stack();
		background = new Table();
		stage = new Stage(AssetLoader.viewport);
		stack.setSize(AssetLoader.width, AssetLoader.height);
		stack.add(background);
		Gdx.app.debug("xplorateur-MapScreen","Ajout des boutons");
		back=new ImageTextButton("Menu",AssetLoader.Skin_images,"Back");
		back.setPosition(71f, 80f);
		back.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				ChangeEvent changeevent=new ChangeEvent();
				ScreenManager.setScreen(Screentype.MENU);
			}
		});
		Gdx.graphics.setContinuousRendering(false);
		map = new MyGdxMap();
	}

	@Override
	public void show() {
		stage.addActor(back);
		stage.addActor(stack);	
		stage.addActor(map);	
		Gdx.input.setInputProcessor(stage);
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(255, 255, 255, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        GLState.enableVertexArrays(-1, -1);
        gl.viewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        gl.frontFace(GL.CW);
		map.render();
		GLState.bindVertexBuffer(0);
        GLState.bindElementBuffer(0);
        gl.flush();
        gl.viewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        gl.frontFace(GL.CCW);
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
		map.dispose();
	}

}
