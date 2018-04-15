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
import com.badlogic.gdx.utils.Array;

import fr.meconnu.UI.Notation.Notationtype;
import fr.meconnu.UI.Description;
import fr.meconnu.UI.Information;
import fr.meconnu.UI.LabeletClassement;
import fr.meconnu.UI.Notation;
import fr.meconnu.UI.NotationGroup;
import fr.meconnu.UI.Order;
import fr.meconnu.UI.Particularite;
import fr.meconnu.UI.ParticulariteGroup;
import fr.meconnu.UI.PatrimoListe;
import fr.meconnu.UI.Photo;
import fr.meconnu.UI.SearchList;
import fr.meconnu.UI.Sizer;
import fr.meconnu.UI.TabbedPane;
import fr.meconnu.UI.Titre;
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
	private SearchList searchlist,selectlist;
	private Order orderlist;
	private Sizer sizer;
	private Label titre2_1,titre2_2,titre2_3,titre2_4,titre2_5,titre2_6,titre2_7;
	private Table main,background,search,notation,resultats;
	private TabbedPane tab;
	private Stack stack;
	private float runTime;
	private PatrimoListe resultlist;
	private ScrollPane resultscroll,searchscroll,selectscroll;
	
	public SearchScreen() {
		Gdx.app.debug("xplorateur-SearchScreenScreen","Création des elements primordiaux du screen (stage, renderer, stack, table)");
		stage = new Stage(AssetLoader.viewport);
		stack = new Stack();
		background = new Table();
		main = new Table();
		tab = new TabbedPane(AssetLoader.Skin_images,"Patrimoine");
		stage = new Stage(AssetLoader.viewport);
		stack.setSize(AssetLoader.width, AssetLoader.height);
		stack.add(background);
		background.add(main).size(1150, 1080);
		background.add(tab).size(770, 1080);		
		Gdx.app.debug("xplorateur-SearchScreenScreen","Ajout des élements");
		titre2_1=new Label("Rechercher par titre, mot clé, nom de commune ou code insee...",AssetLoader.Skin_images,"Little");
		main.add(titre2_1).padLeft(25).padTop(25).top().left();
		titre2_2=new Label("Type de patrimoine",AssetLoader.Skin_images,"Little");
		main.add(titre2_2).padLeft(25).padTop(25).top().left().row();
		search=new Table();
		searchfield=new TextField("",AssetLoader.Skin_images,"Normal");
		searchfield.addListener(new ChangeListener() {
	        @Override
	        public void changed(ChangeEvent event, Actor actor) {
	        	searchlist.setSearch(searchfield.getText().toLowerCase());
	        }
	    });
		search.add(searchfield).size(870f,40f).top().left().colspan(2).row();
		searchlist=new SearchList();
		searchlist.addListener(new ActorGestureListener() { 
			@Override
			public void tap (InputEvent event, float x, float y, int count, int button) {
				if (count>1)
				{
					selectlist.add(searchlist.getSelected());
					searchlist.removeSelected();
				}
			   }
		});
		searchscroll=new ScrollPane(searchlist, AssetLoader.Skin_images, "Scroll"); 
		search.add(searchscroll).top().left().size(430f,425f);
		selectlist=new SearchList();
		selectlist.addListener(new ActorGestureListener() { 
			@Override
			public void tap (InputEvent event, float x, float y, int count, int button) {
				if (count>1)
				{
					searchlist.add(selectlist.getSelected());
					selectlist.removeSelected();
				}
			   }
		});
		selectscroll=new ScrollPane(selectlist, AssetLoader.Skin_images, "Scroll"); 
		search.add(selectscroll).top().left().size(430f,425f).row();
		notation=new Table();
		titre2_3=new Label("Intérêt du patrimoine",AssetLoader.Skin_images,"Little");
		notation.add(titre2_3).padTop(25).top().left();
		titre2_4=new Label("Durée de visite",AssetLoader.Skin_images,"Little");
		notation.add(titre2_4).padTop(25).top().left().row();
		interet=new NotationGroup(Notationtype.INTERET);
		notation.add(interet).padTop(5).top().left().size(325f,325f);
		duree=new NotationGroup(Notationtype.TIME);
		notation.add(duree).padTop(5).padLeft(5f).top().left().size(325f,325f).row();
		search.add(notation).padLeft(225f).top().left().colspan(2).row();
		main.add(search).padLeft(5).padTop(5).top().left();
		type=new TypeGroup();
		main.add(type).padLeft(15).padTop(25).top().left().size(200f,600f).row();
		titre2_5=new Label("Particularités du patrimoine",AssetLoader.Skin_images,"Little");
		main.add(titre2_5).padLeft(225f).padTop(35).top().left().row();	
		particularite=new ParticulariteGroup();
		main.add(particularite).padLeft(250f).padTop(5).top().left().size(680f,95f);	
		resultats=new Table();
		titre2_6=new Label("Nombre de résultats",AssetLoader.Skin_images,"Little");
		resultats.add(titre2_6).top().left().row();
		sizer=new Sizer();
		resultats.add(sizer).top().left().row();
		titre2_7=new Label("Trier par",AssetLoader.Skin_images,"Little");
		resultats.add(titre2_7).top().left().row();
		orderlist=new Order();
		resultats.add(orderlist).top().left().row();
		main.add(resultats).padLeft(15f).padTop(0).top().left().row();	
		
		Gdx.app.debug("xplorateur-SearchScreenScreen","Ajout des boutons");
		back=new ImageTextButton("Menu",AssetLoader.Skin_images,"Back");
		back.setPosition(71f, 80f);
		back.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				((Game) Gdx.app.getApplicationListener()).setScreen(new MenuScreen());;
			}
		});
	}

	@Override
	public void show() {
		stage.addActor(back);
		stage.addActor(stack);
	/*	stage.addActor(searchfield);
		stage.addActor(interet);
		stage.addActor(duree);
		stage.addActor(type);
		stage.addActor(particularite);
		stage.addActor(searchlist);
		stage.addActor(selectedlist);
		stage.addActor(orderlist);
		stage.addActor(sizer);*/
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
