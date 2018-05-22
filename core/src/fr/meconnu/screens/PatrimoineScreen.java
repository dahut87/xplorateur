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
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton.ImageTextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
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
import fr.meconnu.UI.PhotoView;
import fr.meconnu.UI.TabbedPane;
import fr.meconnu.UI.Titre;
import fr.meconnu.assets.AssetLoader;
import fr.meconnu.assets.ScreenManager;
import fr.meconnu.assets.ScreenManager.Screentype;
import fr.meconnu.cache.Filler.Movetype;
import fr.meconnu.database.Base;
import fr.meconnu.cache.Patrimoine;
import fr.meconnu.cache.Patrimoine.FieldType;
import fr.meconnu.cache.Patrimoines;
import fr.meconnu.renderers.MenuRenderer;

public class PatrimoineScreen implements Screen {
	private Description description;
	private PhotoView photo;
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
	private ImageTextButton back,cible;
	private PatrimoListe nearlist,villageslist,typeslist,keywordslist;
	private ScrollPane nearscroll,villagesscroll,typesscroll,keywordsscroll,textscroll,fullscroll,dummyscroll;
	private Array<Actor> actors;
	
	
	public PatrimoineScreen(Object argument) {
		Patrimoine patrimoine=(Patrimoine)argument;
		Gdx.app.debug("xplorateur-PatrimoineScreen","Stockage du screen d'origine");
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
		photo = new PhotoView(patrimoine);
		photo.addListener(new ActorGestureListener() {
			@Override
			public void fling (InputEvent event, float VelocityX, float VelocityY, int button) {
				if (Math.abs(VelocityX)>Math.abs(VelocityY)) {
					if (VelocityX<0) {
						photo.next();
						photo.setOverright();
					}

					else {
						photo.previous();
						photo.setOverleft();
					}
				}
			}

			@Override
			public void tap (InputEvent event, float x, float y, int count, int button) {
				{
					if (count==1) {
						if (photo.getOverleft()) photo.previous();
						if (photo.getOverright()) photo.next();
						if (y < 50 && x > 0 && x < photo.getWidth())
							photo.goTo(Math.round((x - 20) / 28));
					}
					else
					{
						if (photo.getWidth()==AssetLoader.width) {
							photo.remove();
							dummyscroll.setActor(photo);
							background.setVisible(true);
							photo.setBounds(0, 0, 750, 500);
						}
						else {
							dummyscroll.removeActor(photo);
							stage.addActor(photo);
							background.setVisible(false);
							photo.setBounds(0, 0, AssetLoader.width, AssetLoader.height);
						}

					}
				}
			}
		});
		actors.add(photo);
		dummyscroll=new ScrollPane(photo, AssetLoader.Skin_images, "Scroll"); 
		dummyscroll.setScrollingDisabled(true, true);
		main.add(dummyscroll).padLeft(25).padTop(30).top().left().size(750, 500);
		actors.add(interet);
		actors.add(marche);
		actors.add(acces);
		actors.add(time);
		main.add(allnote).padLeft(10).padTop(30).top().left().row();
		description = new Description(patrimoine);
		description.addListener(new ActorGestureListener() { 
			@Override
			public void tap (InputEvent event, float x, float y, int count, int button) {
				if (count==2) {
					if (!fullscroll.isVisible())
					{
						description.remove();
						fullscroll.setActor(description);
						fullscroll.setVisible(true);
						background.setVisible(false);
						TextFieldStyle style=description.getStyle();
						style.font=AssetLoader.Skin_images.getFont("DejaVuSans-34");
						description.setStyle(style);
					}
					else
					{
						description.remove();
						textscroll.setActor(description);
						fullscroll.setVisible(false);
						background.setVisible(true);
						TextFieldStyle style=description.getStyle();
						style.font=AssetLoader.Skin_images.getFont("DejaVuSans-26");
						description.setStyle(style);
					}
				}
			}
		});
		fullscroll=new ScrollPane(null, AssetLoader.Skin_images, "Scroll"); 
		fullscroll.setVisible(false);
		fullscroll.setBounds(30f, 30f, AssetLoader.width-60f, AssetLoader.height-60f);
		textscroll=new ScrollPane(description, AssetLoader.Skin_images, "Scroll"); 
		main.add(textscroll).padLeft(200).padTop(30).top().left().size(920,348).colspan(2).row();
		actors.add(description);
		Gdx.app.debug("xplorateur-PatrimoineScreen","Création de la fiche patrimoine: tabulaire");
		tab.addListener(new ActorGestureListener() { 
			@Override
			public void fling (InputEvent event, float VelocityX, float VelocityY, int button) {
				if (Math.abs(VelocityX)>Math.abs(VelocityY)) {
					int index=tab.getSelectedIndex();
					if (VelocityX<0)
						index++;
					else
						index--;
					if (index>=0 & index<=4)
					tab.setSelectedIndex(index);
				}
			 }
		});
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
		
		Patrimoines patrimoines=Patrimoines.getNearToPatrimoine(patrimoine);
		
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
		cible=new ImageTextButton("Aller vers",AssetLoader.Skin_images,"cible");
		cible.setPosition(71f, 250f);
		cible.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
					AssetLoader.cible=nearlist.getSelected();
					ScreenManager.setScreen(Screentype.COMPASS);
			}
		});
	}
	
	public void updateAllPatrimoine(Patrimoine patrimoine) {
		updatePatrimoine(patrimoine);
		Patrimoines patrimoines=Patrimoines.getNearToPatrimoine(patrimoine);
		if (patrimoines!=null)
		{
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
		stage.addActor(cible);
		stage.addActor(fullscroll);
	}
	
	public void close() {
		ScreenManager.returnScreen();
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
