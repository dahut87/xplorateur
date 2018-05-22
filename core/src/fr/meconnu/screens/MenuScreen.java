package fr.meconnu.screens;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Orientation;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import fr.meconnu.assets.AssetLoader;
import fr.meconnu.assets.ScreenManager;
import fr.meconnu.assets.ScreenManager.Screentype;
import fr.meconnu.cache.Filler;
import fr.meconnu.cache.Filler.Cachetype;
import fr.meconnu.cache.Filler.Movetype;
import fr.meconnu.cache.Loader;
import fr.meconnu.cache.Location.Localisationtype;
import fr.meconnu.cache.Patrimoine.FieldType;
import fr.meconnu.cache.Patrimoines;
import fr.meconnu.database.Base.datatype;
import fr.meconnu.dialogs.WarningDialog;
import fr.meconnu.renderers.MenuRenderer;

public class MenuScreen implements Screen {
	private MenuRenderer Renderer;
	private Image logo;
	private Image Menu1, Menu2, Menu3, Menu4, Menu5;
	private float runTime;
	private Timer EffectTimer;
	private TimerTask EffectTask,WriteTask;
	private Timer BlinkTimer;
	private TimerTask BlinkTask;
	private int Blinkcounter;
	private Stack stack;
	private Table foreground,background;
	private Stage stage;
	private Group menu;
	private TextArea textarea;
	private final String info="Meconnu.fr - Xplorateur\nL'application mobile qui vous accompagne afin de découvrir le patrimoine environnant.\n\n Développée par Nicolas Hordé pour l'association meconnu.fr.\nAssociation \"meconnu.fr\"\n55 avenue du Berry\n23000 Guéret\n\nRéalisé avec Java 1.8 - LibGDX 9.8\nAvec Eclipse & Android Studio\nEt les librairies mapsforge/vtm & Longri/libgdx_SqLiteDB\n\nVersion Android-1.0Alpha1" + 
			" ";
	private ImageButton localizer,compass,gyroscope,moving,network,cache;
	private ImageTextButton back;
	private WarningDialog dialog;
	public enum quality {
		Bas(AssetLoader.language.get("[quality-gamescreen-low]"), TextureFilter.Nearest), Moyen(AssetLoader.language.get("[quality-gamescreen-medium]"), TextureFilter.MipMap), Eleve(
				AssetLoader.language.get("[quality-gamescreen-high]"), TextureFilter.Linear);
		private final String text;
		private final TextureFilter aquality;

		private quality(final String text, TextureFilter aquality) {
			this.text = text;
			this.aquality = aquality;
		}

		@Override
		public String toString() {
			return text;
		}

		public TextureFilter getQuality() {
			return this.aquality;
		}
	}
	
	public void menu() {
		menu.setVisible(true);
		Menu1.setRotation(0);
		Menu1.setScale(1f);
		Menu1.setColor(1f, 1f, 1f, 1f);
		Menu1.setPosition(1920.0f, AssetLoader.height - 220.0f);
		Menu1.addAction(Actions.sequence(Actions.moveTo(10.0f,AssetLoader.height - 220.0f, 0.25f)));
		Menu2.setRotation(0);
		Menu2.setScale(1f);
		Menu2.setColor(1f, 1f, 1f, 1f);
		Menu2.setPosition(1920, AssetLoader.height * 17 / 20 - 300);
		Menu2.addAction(Actions.sequence(Actions.delay(0.1f),Actions.moveTo(220.0f / 2,	AssetLoader.height - 460.0f, 0.25f)));
		Menu3.setRotation(0);
		Menu3.setScale(1f);
		Menu3.setColor(1f, 1f, 1f, 1f);
		Menu3.setPosition(1920, AssetLoader.height * 14 / 20 - 300);
		Menu3.addAction(Actions.sequence(Actions.delay(0.2f),Actions.moveTo(240.0f,	AssetLoader.height - 670.0f, 0.25f)));
		Menu4.setRotation(0);
		Menu4.setScale(1f);
		Menu4.setColor(1f, 1f, 1f, 1f);
		Menu4.setPosition(1920, AssetLoader.height * 11 / 20 - 300);
		Menu4.addAction(Actions.sequence(Actions.delay(0.3f),Actions.moveTo(380.0f, AssetLoader.height - 920.0f, 0.25f)));
		Menu5.setRotation(0);
		Menu5.setScale(1f);
		Menu5.setColor(1f, 1f, 1f, 1f);
		Menu5.setPosition(30.0f, AssetLoader.height);
		Menu5.addAction(Actions.sequence(Actions.delay(1f),Actions.moveTo(80.0f,250.0f, 0.25f)));
	}
	
	public MenuScreen() {
		Gdx.app.debug("xplorateur-GameScreen","Création des elements primordiaux du screen (stage, renderer, stack, table)");
		dialog = new WarningDialog();
		Renderer = new MenuRenderer(this);
		stack = new Stack();
		background = new Table();
		foreground = new Table();
		stage = new Stage(AssetLoader.viewport);
		stack.setSize(AssetLoader.width, AssetLoader.height);
		stack.add(background);
		stack.add(foreground);		
		menu = new Group();
		Gdx.app.debug("xplorateur-GameScreen","Préparation du menu)");
		Menu1 = new Image(AssetLoader.Skin_images, "naviguer");
		Menu1.setOrigin(Menu1.getWidth() / 2, Menu1.getHeight() / 2);
		Menu1.addListener(new ActorGestureListener() { 
			@Override
			public void tap (InputEvent event, float x, float y, int count, int button) {
				Menu2.addAction(Actions.fadeOut(0.2f));
				Menu3.addAction(Actions.fadeOut(0.2f));
				Menu4.addAction(Actions.fadeOut(0.2f));
				Menu1.addAction(Actions.sequence(Actions.parallel(Actions.rotateBy(360f,0.3f),Actions.scaleTo(0.5f, 0.5f,0.5f)),Actions.run(new Runnable() {
					public void run() {
						ScreenManager.setScreen(Screentype.COMPASS);
					}
				}
			)));
			}
		});
		Menu2 = new Image(AssetLoader.Skin_images, "rechercher");
		Menu2.setOrigin(Menu2.getWidth() / 2, Menu2.getHeight() / 2);
		Menu2.addListener(new ActorGestureListener() { 
			@Override
			public void tap (InputEvent event, float x, float y, int count, int button) {
				Menu1.addAction(Actions.fadeOut(0.2f));
				Menu3.addAction(Actions.fadeOut(0.2f));
				Menu4.addAction(Actions.fadeOut(0.2f));
				Menu2.addAction(Actions.sequence(Actions.parallel(Actions.color(Color.RED, 0.5f),Actions.scaleTo(0.5f, 0.5f,0.5f)),Actions.run(new Runnable() {
					public void run() {
						ScreenManager.setArgument(-1);
						ScreenManager.setScreen(Screentype.SEARCH);
					}
				}
			)));
			}
		});
		Menu3 = new Image(AssetLoader.Skin_images, "consulter");
		Menu3.setOrigin(Menu3.getWidth() / 2, Menu3.getHeight() / 2);
		Menu3.addListener(new ActorGestureListener() { 
			@Override
			public void tap (InputEvent event, float x, float y, int count, int button) {
				Menu1.addAction(Actions.fadeOut(0.2f));
				Menu2.addAction(Actions.fadeOut(0.2f));
				Menu4.addAction(Actions.fadeOut(0.2f));
				Menu3.addAction(Actions.sequence(Actions.parallel(Actions.moveBy(250f, 0f, 0.5f),Actions.scaleTo(0.5f, 0.5f,0.5f)),Actions.run(new Runnable() {
					public void run() {
						ScreenManager.setScreen(Screentype.MAP);
					}
				}
			)));
			}
		});
		Menu4 = new Image(AssetLoader.Skin_images, "generer");
		Menu4.setOrigin(Menu4.getWidth() / 2, Menu4.getHeight() / 2);
		Menu4.addListener(new ActorGestureListener() { 
			@Override
			public void tap (InputEvent event, float x, float y, int count, int button) {
				dialog.show("Fonctionnalité en cours d'implémentation...", stage);
			}
		});
		Menu5 = new Image(AssetLoader.Skin_images, "A propos");
		Menu5.setOrigin(Menu5.getWidth() / 2, Menu5.getHeight() / 2);
		Menu5.addListener(new ActorGestureListener() { 
			@Override
			public void tap (InputEvent event, float x, float y, int count, int button) {
				Menu1.addAction(Actions.fadeOut(0.5f));
				Menu2.addAction(Actions.fadeOut(0.5f));
				Menu3.addAction(Actions.fadeOut(0.5f));
				Menu4.addAction(Actions.fadeOut(0.5f));
				Menu5.addAction(Actions.sequence(Actions.moveTo(AssetLoader.width/2.2f,AssetLoader.height/2f,0.5f),Actions.scaleTo(5f, 5f, 0.5f),Actions.hide()));
				logo.addAction(Actions.parallel(Actions.moveBy(-200, 300f, 1f)));
				textarea.addAction(Actions.parallel(Actions.sizeTo(800f, 850f,1f),Actions.moveTo(40f, 170f, 1f)));
				back.setText("Retour");
				back.setName("Retour");
				textarea.setText("");
				BlinkTimer.scheduleAtFixedRate(WriteTask, 0, 40);
			}
			@Override
			public boolean longPress (Actor actor,float stageX, float stageY) {
				Filler.test();
				textarea.setText("Procédure de test");
				return false;
			}
		});
		menu.addActor(Menu1);
		menu.addActor(Menu2);
		menu.addActor(Menu3);
		menu.addActor(Menu4);
		menu.addActor(Menu5);
		back=new ImageTextButton("Quitter",AssetLoader.Skin_images,"Back");
		back.setPosition(71f, 80f);
		back.setName("Exit");
		back.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (back.getName().contains("Retour"))
					ScreenManager.setScreen(Screentype.MENU);
				else
					Gdx.app.exit();
			}
		});
		foreground.addActor(back);
		Gdx.app.debug("xplorateur-LevelScreen", "Mise en place du timer renderer.");
		EffectTimer = new Timer();
		EffectTask = new TimerTask() {
			@Override
			public void run() {
				Renderer.evolve();
			}
		};
		EffectTimer.scheduleAtFixedRate(EffectTask, 0, 30);
		Gdx.app.debug("xplorateur-GameScreen","Ajout du logo");
		logo = new Image(AssetLoader.Skin_images, "logo");
		logo.setPosition(1400,40);
		background.addActor(logo);
		Gdx.app.debug("xplorateur-GameScreen","Ajout du text");
		textarea = new TextArea(AssetLoader.Datahandler.cache().getInformations(),AssetLoader.Skin_images,"Transparent");
		textarea.setBounds(1150, 350, 700, 500);
		foreground.addActor(textarea);
		Gdx.app.debug("xplorateur-GameScreen","Ajout des indicateurs (Réseau,GPS,Boussole,Gyroscope,Mouvement)");	
		network = new ImageButton(AssetLoader.Skin_images,"net");
		network.setPosition(1300, 980);
		network.setChecked(true);
		foreground.addActor(network);
		cache = new ImageButton(AssetLoader.Skin_images,Filler.getCachelevel().toString());
		cache.setPosition(1400, 980);
		foreground.addActor(cache);
		if (Filler.isRunning())
			localizer = new ImageButton(AssetLoader.Skin_images,Filler.getLocaliser().getLocalisationtype().toString());
		else
			localizer = new ImageButton(AssetLoader.Skin_images,Localisationtype.NONE.toString());
		localizer.setPosition(1500, 980);
		foreground.addActor(localizer);
		moving = new ImageButton(AssetLoader.Skin_images,Movetype.NOSTATUS.toString());
		moving.setPosition(1600, 980);
		foreground.addActor(moving);
		if (AssetLoader.Compass) {
			compass = new ImageButton(AssetLoader.Skin_images,"compass");
			compass.setPosition(1700, 980);
			foreground.addActor(compass);
		}
		if (AssetLoader.Gyroscope) {
			gyroscope = new ImageButton(AssetLoader.Skin_images,"gyroscope");
			gyroscope.setPosition(1800, 980);
			foreground.addActor(gyroscope);
		}
		Gdx.app.debug("xplorateur-LevelScreen", "lancement d'un ping.");
		Filler.ping();
		Gdx.app.debug("xplorateur-LevelScreen", "Mise en place du timer clignotant.");
		BlinkTimer = new Timer();
		BlinkTask = new TimerTask() {
			@Override
			public void run() {
				Blinkcounter++;
				ImageButtonStyle style = cache.getStyle();
				Drawable drawable = AssetLoader.Skin_images.getDrawable(Filler.getCachelevel().toString());
				style.up = drawable;
				style.down = drawable;
				network.setChecked(!Filler.isAccessible());
				if (Filler.isRunning()) {
					if (Filler.getLocaliser().getLocalisationtype()==Localisationtype.GPS)
						if (Filler.getLocaliser().isLocalisable())
						{
							localizer.setChecked(false);
							style = moving.getStyle();
							drawable = AssetLoader.Skin_images.getDrawable(Filler.getMovetype().toString());
							style.up = drawable;
							style.down = drawable;
						}
						else
						{
							localizer.setChecked(Blinkcounter%2==0);
							style = moving.getStyle();
							drawable = AssetLoader.Skin_images.getDrawable(Movetype.NOSTATUS.toString());
							style.up = drawable;
							style.down = drawable;
						}
				}
			}
		};
		WriteTask = new TimerTask() {
			@Override
			public void run() {
				int pointer=textarea.getText().length();
				textarea.setText(info.substring(0,pointer+1));
			}
		};
		BlinkTimer.scheduleAtFixedRate(BlinkTask, 0, 1000);
		ScreenManager.initScreen();
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		Gdx.input.setInputProcessor(stage);
		stage.addActor(stack);
		stage.addActor(menu);
		menu();
	}

	@Override
	public void render(float delta) {
		runTime += delta;
		Renderer.render(delta, runTime);
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
		
	};
}
