package fr.meconnu.screens;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
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

public class SearchScreen implements Screen {
	private Stage stage;
	private TextField searchfield;
	private NotationGroup interet,duree;
	private ParticulariteGroup particularite;
	private TypeGroup type;
	private ImageTextButton back,savefiltre1,savefiltre2,filtre01,filtre02;
	private SearchList searchlist,selectlist;
	private Order orderlist;
	private Sizer sizer;
	private Label titre1_1,titre1_2,titre1_3,titre1_4,titre1_5,titre1_6,titre1_7,titre2_1,titre2_2,titre2_3;
	private Table main,background,search,notation,resultats,result,filtre1,filtre2;
	private TabbedPane tab;
	private Stack stack;
	private float runTime;
	private PatrimoListe resultlist,filtre1list,filtre2list;
	private ScrollPane resultscroll,filtre1scroll,filtre2scroll,searchscroll,selectscroll;
	private Array<Actor> setactors,getactors;
	private Array<Criteria> local;
	private boolean flag=false,update=false;
	
	public SearchScreen(Object argument) {
		int filtre=(int) argument;
		Gdx.app.debug("xplorateur-SearchScreen","Création des elements primordiaux du screen (stage, renderer, stack, table)");
		setactors=new Array<Actor>();
		getactors=new Array<Actor>();
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
		Gdx.app.debug("xplorateur-SearchScreen","Ajout des élements de recherche");
		titre1_1=new Label("Rechercher par titre, mot clé, nom de commune ou code insee...",AssetLoader.Skin_images,"Little");
		main.add(titre1_1).padLeft(25).padTop(25).top().left();
		titre1_2=new Label("Type de patrimoine",AssetLoader.Skin_images,"Little");
		main.add(titre1_2).padLeft(25).padTop(25).top().left().row();
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
		setactors.add(selectlist);
		getactors.add(selectlist);	
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
		selectlist.addListener(new ChangeListener() {
	        @Override
	        public void changed(ChangeEvent event, Actor actor) {
	        	update();
	        }
	    });
		selectscroll=new ScrollPane(selectlist, AssetLoader.Skin_images, "Scroll"); 
		search.add(selectscroll).top().left().size(430f,425f).row();
		notation=new Table();
		titre1_3=new Label("Intérêt du patrimoine",AssetLoader.Skin_images,"Little");
		notation.add(titre1_3).padTop(25).top().left();
		titre1_4=new Label("Durée de visite",AssetLoader.Skin_images,"Little");
		notation.add(titre1_4).padTop(25).top().left().row();
		interet=new NotationGroup(Notationtype.INTERET);
		interet.addListener(new ChangeListener() {
	        @Override
	        public void changed(ChangeEvent event, Actor actor) {
	        	update();
	        }
	    });
		setactors.add(interet);
		getactors.add(interet);	
		notation.add(interet).padTop(5).top().left().size(325f,325f);
		duree=new NotationGroup(Notationtype.TIME);
		duree.addListener(new ChangeListener() {
	        @Override
	        public void changed(ChangeEvent event, Actor actor) {
	        	update();
	        }
	    });
		setactors.add(duree);
		getactors.add(duree);	
		notation.add(duree).padTop(5).padLeft(5f).top().left().size(325f,325f).row();
		search.add(notation).padLeft(225f).top().left().colspan(2).row();
		main.add(search).padLeft(5).padTop(5).top().left();
		type=new TypeGroup();
		type.addListener(new ChangeListener() {
	        @Override
	        public void changed(ChangeEvent event, Actor actor) {
	        	update();
	        }
	    });
		setactors.add(type);
		getactors.add(type);
		main.add(type).padLeft(15).padTop(25).top().left().size(200f,600f).row();
		titre1_5=new Label("Particularités du patrimoine",AssetLoader.Skin_images,"Little");
		main.add(titre1_5).padLeft(225f).padTop(35).top().left().row();	
		particularite=new ParticulariteGroup();
		particularite.addListener(new ChangeListener() {
	        @Override
	        public void changed(ChangeEvent event, Actor actor) {
	        	update();
	        }
	    });
		setactors.add(particularite);
		getactors.add(particularite);	
		main.add(particularite).padLeft(250f).padTop(5).top().left().size(650f,90f);	
		resultats=new Table();
		titre1_6=new Label("Nombre de résultats",AssetLoader.Skin_images,"Little");
		resultats.add(titre1_6).top().left().row();
		sizer=new Sizer();
		sizer.addListener(new ChangeListener() {
	        @Override
	        public void changed(ChangeEvent event, Actor actor) {
	        	update();
	        }
	    });
		setactors.add(sizer);
		getactors.add(sizer);	
		resultats.add(sizer).top().left().row();
		titre1_7=new Label("Trier par",AssetLoader.Skin_images,"Little");
		resultats.add(titre1_7).top().left().row();
		orderlist=new Order();
		orderlist.addListener(new ChangeListener() {
	        @Override
	        public void changed(ChangeEvent event, Actor actor) {
	        	update();
	        }
	    });
		setactors.add(orderlist);
		getactors.add(orderlist);	
		resultats.add(orderlist).top().left().row();
		main.add(resultats).padLeft(15f).padTop(0).top().left().row();	
		Gdx.app.debug("xplorateur-SearchScreen","Ajout des élements de résultat : tabulaire");
		tab.addListener(new ActorGestureListener() { 
			@Override
			public void fling (InputEvent event, float VelocityX, float VelocityY, int button) {
				if (Math.abs(VelocityX)>Math.abs(VelocityY)) {
					int index=tab.getSelectedIndex();
					if (VelocityX<0)
						index++;
					else
						index--;
					if (index>=0 & index<=2)
						tab.setSelectedIndex(index);
				}
			 }
		});
		tab.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				if (tab.getName()!=null)
				{
					if (event.getBubbles()==false)
					{
						if (tab.getSelectedIndex()==1)
							setInfos(Patrimoines.getFilter1());
						else if (tab.getSelectedIndex()==2)
							setInfos(Patrimoines.getFilter2());
						else
							setInfos(local);
						flag=false;
						update();
					}
					else if (flag==false)
					{
						flag=true;
					}
				}
		    }
		});
		result = new Table();
		filtre1 = new Table();
		filtre2 = new Table();
		Patrimoines patrimoines=null;
		tab.addTab("Resultats", result);
		titre2_1 = new Label("Résultat de la recherche", AssetLoader.Skin_images, "Titre2");
		result.add(titre2_1).top().center().expand().row();
		resultlist=new PatrimoListe(patrimoines,null);
		resultlist.addListener(new ActorGestureListener() {
		    @Override
		    public void tap (InputEvent event, float x, float y, int count, int button) {
				if (count>1)
				{
					ScreenManager.setArgument(resultlist.getSelected());
					ScreenManager.callScreen(Screentype.PATRIMOINE);				
				}
			 }
		});
		resultscroll=new ScrollPane(resultlist, AssetLoader.Skin_images, "Scroll"); 
		result.add(resultscroll).top().center().size(710,900).expand().row();
		tab.addTab("Filtre1", filtre1);
		titre2_2 = new Label("Filtre 1", AssetLoader.Skin_images, "Titre2");
		filtre1.add(titre2_2).top().center().expand().row();
		filtre1list=new PatrimoListe(patrimoines,null);
		filtre1list.addListener(new ActorGestureListener() {
		    @Override
		    public void tap (InputEvent event, float x, float y, int count, int button) {
				if (count>1)
				{
					ScreenManager.setArgument(filtre1list.getSelected());
					ScreenManager.callScreen(Screentype.PATRIMOINE);
				}
			 }
		});
		filtre1scroll=new ScrollPane(filtre1list, AssetLoader.Skin_images, "Scroll"); 
		filtre1.add(filtre1scroll).top().center().size(710,900).expand().row();
		tab.addTab("Filtre2", filtre2);
		titre2_3 = new Label("Filtre 2", AssetLoader.Skin_images, "Titre2");
		filtre2.add(titre2_3).top().center().expand().row();
		filtre2list=new PatrimoListe(patrimoines,null);
		filtre2list.addListener(new ActorGestureListener() {
		    @Override
		    public void tap (InputEvent event, float x, float y, int count, int button) {
				if (count>1)
				{
					ScreenManager.setArgument(filtre1list.getSelected());
					ScreenManager.callScreen(Screentype.PATRIMOINE);				
				}
			 }
		});
		filtre2scroll=new ScrollPane(filtre2list, AssetLoader.Skin_images, "Scroll"); 
		filtre2.add(filtre2scroll).top().center().size(710,900).expand().row();
		
		Gdx.app.debug("xplorateur-SearchScreen","Ajout des boutons");
		if (ScreenManager.isCalled())
			back=new ImageTextButton("Retour",AssetLoader.Skin_images,"Back");
		else
			back=new ImageTextButton("Menu",AssetLoader.Skin_images,"Back");
		back.setPosition(71f, 80f);
		back.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				ChangeEvent changeevent=new ChangeEvent();
				changeevent.setBubbles(true);
				tab.fire(changeevent);
				ScreenManager.returnorsetScreen(Screentype.MENU);
			}
		});
		savefiltre1=new ImageTextButton("Sauver vers\nfiltre 1",AssetLoader.Skin_images,"savefiltre1");
		savefiltre1.setPosition(71f, 250f);
		savefiltre1.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Patrimoines.setFilter1(getInfos());
			}
		});
		savefiltre2=new ImageTextButton("Sauver vers\nfiltre 2",AssetLoader.Skin_images,"savefiltre2");
		savefiltre2.setPosition(71f, 420f);
		savefiltre2.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Patrimoines.setFilter2(getInfos());
			}
		});
		filtre01=new ImageTextButton("Effacer\nfiltre 1",AssetLoader.Skin_images,"filtre0");
		filtre01.setPosition(71f, 250f);
		filtre01.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Patrimoines.clearFilter1();
				setInfos(Patrimoines.getFilter1());
				update();
			}
		});
		filtre02=new ImageTextButton("Effacer\nfiltre 2",AssetLoader.Skin_images,"filtre0");
		filtre02.setPosition(71f, 420f);
		filtre02.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Patrimoines.clearFilter2();
				setInfos(Patrimoines.getFilter2());
				update();
			}
		});
		tab.setName("ok");
		update();
		if (filtre>=0)
		{
			tab.setSelectedIndex(filtre);
			tab.setTouchable(Touchable.disabled);
		}
	}
	
	public void update() {
		if (!update)
		{
			update=true;
			Gdx.app.debug("xplorateur-SearchScreen","Changement dans les critères ....");
			Patrimoines patrimoines=null;
			if (tab.getSelectedIndex()==1)
			{
				Patrimoines.setFilter1(getInfos());
				patrimoines=Patrimoines.getNearFiltered(Patrimoines.getFilter1());
				filtre1list.setPatrimoines(patrimoines, null);
				filtre1list.setViewed(orderlist.getField());
				filtre01.setVisible(true);
				filtre02.setVisible(false);
				savefiltre1.setVisible(false);
				savefiltre2.setVisible(true);
			}
			else if (tab.getSelectedIndex()==2)
			{
				Patrimoines.setFilter2(getInfos());
				patrimoines=Patrimoines.getNearFiltered(Patrimoines.getFilter2());
				filtre2list.setPatrimoines(patrimoines, null);
				filtre2list.setViewed(orderlist.getField());
				filtre01.setVisible(false);
				filtre02.setVisible(true);
				savefiltre1.setVisible(true);
				savefiltre2.setVisible(false);
			}
			else
			{
				local=getInfos();
				patrimoines=Patrimoines.getNearFiltered(local);
				resultlist.setPatrimoines(patrimoines, null);
				resultlist.setViewed(orderlist.getField());
				filtre01.setVisible(false);
				filtre02.setVisible(false);
				savefiltre1.setVisible(true);
				savefiltre2.setVisible(true);
			}
			update=false;
		}
	}
	
	public void setInfos(Array<Criteria> criterias)
	{
		for(Actor actor:setactors)
		{
			Method m;
			try {
				m = actor.getClass().getMethod("setCriterias", Array.class);
				Array<Criteria> array=(Array<Criteria>) m.invoke(actor, criterias);
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
	}

	
	public Array<Criteria> getInfos()
	{
		Array<Criteria> result;
		result= new Array<Criteria>();
		for(Actor actor:getactors)
		{
			Method m;
			try {
				m = actor.getClass().getMethod("getCriterias", null);
				Array<Criteria> Criterias=(Array<Criteria>) m.invoke(actor, null);
				if (Criterias!=null)
				{
					for (Criteria criteria:Criterias)
						if (!result.contains(criteria, true))
							result.add(criteria);
				}
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		return result;
	}

	@Override
	public void show() {
		stage.addActor(back);
		stage.addActor(savefiltre1);
		stage.addActor(savefiltre2);
		stage.addActor(filtre01);
		stage.addActor(filtre02);
		stage.addActor(stack);
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
