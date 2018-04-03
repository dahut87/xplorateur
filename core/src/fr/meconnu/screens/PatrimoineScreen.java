package fr.meconnu.screens;

import java.lang.reflect.InvocationTargetException;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.reflect.Method;

import fr.meconnu.UI.Description;
import fr.meconnu.UI.Information;
import fr.meconnu.UI.Information.Informationtype;
import fr.meconnu.UI.LabeletClassement;
import fr.meconnu.UI.Notation;
import fr.meconnu.UI.Notation.Notationtype;
import fr.meconnu.UI.Particularite;
import fr.meconnu.UI.PatrimoListe;
import fr.meconnu.UI.Photo;
import fr.meconnu.UI.TabbedPane;
import fr.meconnu.UI.Titre;
import fr.meconnu.assets.AssetLoader;
import fr.meconnu.cache.Filler.Movetype;
import fr.meconnu.database.Base;
import fr.meconnu.cache.Patrimoine;
import fr.meconnu.cache.Patrimoine.FieldType;
import fr.meconnu.cache.Patrimoines;
import fr.meconnu.renderers.MenuRenderer;

public class PatrimoineScreen implements Screen {
	private Screen oldscreen;
	private Description description;
	private Photo photo;
	private Titre titre;
	private Label titre2_1,titre2_2,titre2_3,titre2_4,titre2_5;
	private Table main,background,informations,near,villages,types,keywords;
	private Information commune,coordonnees,type,motcle,auteur;
	private TabbedPane tab;
	private Table allnote;
	private Stage stage;
	private Stack stack;
	private float runTime;
	private Notation interet,marche,acces,time;
	private NinePatchDrawable lineeffect;
	private Table line1,line2;
	private Particularite particularite;
	private LabeletClassement labels;
	private ImageTextButton back;
	private PatrimoListe nearlist,villageslist,typeslist,keywordslist;
	private ScrollPane nearscroll,villagesscroll,typesscroll,keywordsscroll;
	private Array<Actor> actors;
	
	
	public PatrimoineScreen(Screen returnscreen, Patrimoine patrimoine) {
		Gdx.app.debug("xplorateur-PatrimoineScreen","Stockage du screen d'origine");
		this.oldscreen=returnscreen;
		Gdx.app.debug("xplorateur-PatrimoineScreen","Création des elements primordiaux du screen (stage, renderer, stack, table)");
		stack = new Stack();
		background = new Table();
		main = new Table();
		tab = new TabbedPane(AssetLoader.Skin_images,"Patrimoine");
		stage = new Stage(AssetLoader.viewport);
		stack.setSize(AssetLoader.width, AssetLoader.height);
		stack.add(background);
		//background.debug();
		background.add(main).size(1150, 1080);
		background.add(tab).size(770, 1080);	
		Gdx.app.debug("xplorateur-PatrimoineScreen","Création des notes");
		allnote = new Table();
		interet = new Notation(patrimoine, Notationtype.INTERET);
		interet.Showtext();
		marche = new Notation(patrimoine, Notationtype.MARCHE);		
		marche.Showtext();		
		acces = new Notation(patrimoine, Notationtype.ACCES);
		acces.Showtext();		
		time = new Notation(patrimoine, Notationtype.TIME);		
		time.Showtext();
		allnote.add(interet).padTop(15).size(275, 90).row();
		allnote.add(marche).padTop(40).size(275, 90).row();
		allnote.add(acces).padTop(40).size(275, 90).row();
		allnote.add(time).padTop(40).size(275, 90).row();
		actors=new Array<Actor>();
		Gdx.app.debug("xplorateur-PatrimoineScreen","Création de la fiche patrimoine: principale");
		titre = new Titre(patrimoine);
		actors.add(titre);
		main.add(titre).padLeft(25).padTop(25).top().left().expandX().colspan(2).row();
		photo = new Photo(patrimoine);
		actors.add(photo);
		main.add(photo).padLeft(25).padTop(30).top().left().size(750, 500);
		actors.add(interet);
		actors.add(marche);
		actors.add(acces);
		actors.add(time);
		main.add(allnote).padLeft(10).padTop(30).top().left().row();
		description = new Description(patrimoine);
		main.add(description).padLeft(25).padTop(30).top().left().size(1050,400).colspan(2).row();
		actors.add(description);
		Gdx.app.debug("xplorateur-PatrimoineScreen","Création de la fiche patrimoine: tabulaire");
		informations = new Table();
		near = new Table();
		villages = new Table();
		types = new Table();
		keywords = new Table();
		tab.addTab("Infos", informations);
		titre2_1 = new Label("Informations sur le patrimoine", AssetLoader.Skin_images, "Titre2");
		informations.add(titre2_1).top().center().expandX().row();
		commune=new Information(patrimoine,Informationtype.COMMUNE);
		informations.add(commune).padTop(40).top().left().size(400, 70).row();
		actors.add(commune);
		coordonnees=new Information(patrimoine,Informationtype.COORDINATES);
		informations.add(coordonnees).padTop(40).top().left().size(400, 70).row();
		actors.add(coordonnees);
		type=new Information(patrimoine,Informationtype.TYPE);
		informations.add(type).padTop(40).top().left().size(400, 70).row();
		actors.add(type);
		motcle=new Information(patrimoine,Informationtype.KEYWORDS);
		informations.add(motcle).padTop(40).top().left().size(400, 70).row();
		actors.add(motcle);
		auteur=new Information(patrimoine,Informationtype.AUTHORS);
		informations.add(auteur).padTop(40).top().left().size(400, 70).row();
		actors.add(auteur);
		lineeffect=new NinePatchDrawable(AssetLoader.Atlas_images.createPatch("line"));
		line1=new Table();
		line1.setBackground(lineeffect);
		informations.add(line1).padTop(40).top().left().size(700,3).row();
		particularite=new Particularite(patrimoine);
		informations.add(particularite).padTop(20).top().left().size(700,70).row();
		actors.add(particularite);
		line2=new Table();
		line2.setBackground(lineeffect);
		informations.add(line2).padTop(40).top().left().size(700,3).row();
		labels=new LabeletClassement(patrimoine);
		informations.add(labels).padTop(20).top().left().size(700,120).padBottom(60).row();
		actors.add(labels);
		
		Patrimoines patrimoines=Patrimoines.getNear(patrimoine);
		patrimoines.setUser(patrimoine);
		
		tab.addTab("Proximité", near);
		titre2_2 = new Label("Patrimoines à proximité", AssetLoader.Skin_images, "Titre2");
		near.add(titre2_2).top().center().expand().row();
		nearlist=new PatrimoListe(patrimoines,patrimoine);
		nearlist.addListener(new ChangeListener() {
		    @Override
		    public void changed(ChangeEvent event, Actor actor) {
		    	if (nearlist.getRenew()) {
		    		updateAllPatrimoine(nearlist.getSelected());
		    	}
		    }
		});
		nearscroll=new ScrollPane(nearlist, AssetLoader.Skin_images, "Scroll"); 
		near.add(nearscroll).top().center().size(710,900).expand().row();
		tab.addTab("Commune", villages);
		titre2_3 = new Label("Patrimoines de même commune", AssetLoader.Skin_images, "Titre2");
		villages.add(titre2_3).top().center().expand().row();
		villageslist=new PatrimoListe(patrimoines,patrimoine);
		villageslist.setFilter(FieldType.COMMUNE);
		villageslist.addListener(new ChangeListener() {
		    @Override
		    public void changed(ChangeEvent event, Actor actor) {
		    	if (villageslist.getRenew()) {
		    		updateAllPatrimoine(villageslist.getSelected());
		    	}
		    }
		});
		villagesscroll=new ScrollPane(villageslist, AssetLoader.Skin_images, "Scroll"); 
		villages.add(villagesscroll).top().center().size(710,900).expand().row();
		tab.addTab("Type", types);
		titre2_4 = new Label("Patrimoines de même catégorie", AssetLoader.Skin_images, "Titre2");
		types.add(titre2_4).top().center().expand().row();
		typeslist=new PatrimoListe(patrimoines,patrimoine);
		typeslist.setFilter(FieldType.TYPE);
		typeslist.addListener(new ChangeListener() {
		    @Override
		    public void changed(ChangeEvent event, Actor actor) {
		    	if (typeslist.getRenew()) {
		    		updateAllPatrimoine(typeslist.getSelected());
		    	}
		    }
		});
		typesscroll=new ScrollPane(typeslist, AssetLoader.Skin_images, "Scroll"); 
		types.add(typesscroll).top().center().size(710,900).expand().row();
		tab.addTab("Mot clé", keywords);
		titre2_5 = new Label("Patrimoines de même mot clé", AssetLoader.Skin_images, "Titre2");
		keywords.add(titre2_5).top().center().expand().row();
		keywordslist=new PatrimoListe(patrimoines,patrimoine);
		keywordslist.setFilter(FieldType.MOTCLE);
		keywordslist.addListener(new ChangeListener() {
		    @Override
		    public void changed(ChangeEvent event, Actor actor) {
		    	if (keywordslist.getRenew()) {
		    		updateAllPatrimoine(keywordslist.getSelected());
		    	}
		    }
		});
		keywordsscroll=new ScrollPane(keywordslist, AssetLoader.Skin_images, "Scroll"); 
		keywords.add(keywordsscroll).top().center().size(710,900).expand().row();
		back=new ImageTextButton("Menu",AssetLoader.Skin_images,"Back");
		back.setPosition(71f, 80f);
		back.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				close();
			}
		});
	}
	
	public void updateAllPatrimoine(Patrimoine patrimoine) {
		updatePatrimoine(patrimoine);
		Patrimoines patrimoines=Patrimoines.getNear(patrimoine);
		if (patrimoines!=null)
		{
			patrimoines.setUser(patrimoine);
			nearlist.setPatrimoines(patrimoines,patrimoine);
			villageslist.setPatrimoines(patrimoines,patrimoine);
			typeslist.setPatrimoines(patrimoines,patrimoine);
			keywordslist.setPatrimoines(patrimoines,patrimoine);
		}
	}
	
	public void updatePatrimoine(Patrimoine patrimoine) {
		if (patrimoine==null) return;
		Class[] cArg = new Class[1];
		cArg[0] = Patrimoine.class;
		for(Actor actor: actors) {
			java.lang.reflect.Method method;
			try {
				method = actor.getClass().getDeclaredMethod("setPatrimoine",cArg);
				if (method!=null)
					method.invoke(actor, patrimoine);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
					
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
		stage.addActor(stack);
		stage.addActor(back);
	}
	
	public void close() {
		((Game) Gdx.app.getApplicationListener()).setScreen(oldscreen);
		this.dispose();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(255, 255, 255, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		runTime += delta;
		stage.act(delta);
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
