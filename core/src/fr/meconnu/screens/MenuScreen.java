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
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
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
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import fr.meconnu.assets.AssetLoader;
import fr.meconnu.cache.Filler;
import fr.meconnu.cache.Filler.Cachetype;
import fr.meconnu.cache.Filler.Movetype;
import fr.meconnu.cache.Loader;
import fr.meconnu.cache.Location.Localisationtype;
import fr.meconnu.cache.Patrimoine.FieldType;
import fr.meconnu.cache.Patrimoines;
import fr.meconnu.database.Base.datatype;
import fr.meconnu.renderers.MenuRenderer;

public class MenuScreen implements Screen {
	private MenuRenderer Renderer;
	private ImageButton logo;
	private Image Menu1, Menu2, Menu3, Menu4, Menu5;
	private float runTime;
	private Timer EffectTimer;
	private TimerTask EffectTask;
	private Timer BlinkTimer;
	private TimerTask BlinkTask;
	private int Blinkcounter;
	private Stack stack;
	private Table foreground,background;
	private Stage stage;
	private Group menu;
	private TextArea textarea;
	private ImageButton localizer,compass,gyroscope,moving,network,cache;
	private ImageTextButton back;
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
		Menu1.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				((Game) Gdx.app.getApplicationListener()).setScreen(new CompassScreen());
			}
		});
		Menu2 = new Image(AssetLoader.Skin_images, "rechercher");
		Menu2.setOrigin(Menu2.getWidth() / 2, Menu2.getHeight() / 2);
		Menu2.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				((Game) Gdx.app.getApplicationListener()).setScreen(new SearchScreen());
			}
		});
		Menu3 = new Image(AssetLoader.Skin_images, "consulter");
		Menu3.setOrigin(Menu3.getWidth() / 2, Menu3.getHeight() / 2);
		Menu3.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				//AssetLoader.Datahandler.cache().Eraseall();
				//AssetLoader.Datahandler.cache().init(datatype.cache);
			}
		});
		Menu4 = new Image(AssetLoader.Skin_images, "generer");
		Menu4.setOrigin(Menu4.getWidth() / 2, Menu4.getHeight() / 2);
		Menu4.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				
			}
		});
		Menu5 = new Image(AssetLoader.Skin_images, "A propos");
		Menu5.setOrigin(Menu5.getWidth() / 2, Menu5.getHeight() / 2);
		Menu5.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				//Patrimoines patrimoines = AssetLoader.Datahandler.cache().readPatrimoines(new Vector2(45.041971f , 1.23997f), 0.2f, FieldType.INTERET,2000, false);
				//((Game) Gdx.app.getApplicationListener()).setScreen(new PatrimoineScreen(((Game) Gdx.app.getApplicationListener()).getScreen(),patrimoines.getValue(0)));
			}
		});
		menu.addActor(Menu1);
		menu.addActor(Menu2);
		menu.addActor(Menu3);
		menu.addActor(Menu4);
		menu.addActor(Menu5);
		back=new ImageTextButton("Quitter",AssetLoader.Skin_images,"Back");
		back.setPosition(71f, 80f);
		back.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
			}
		});
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
		logo = new ImageButton(AssetLoader.Skin_images, "logo");
		logo.setPosition(1400,40);
		background.addActor(logo);
		Gdx.app.debug("xplorateur-GameScreen","Ajout du text");
		//textarea = new TextArea("Ceci est un texte de référence pour essayer",AssetLoader.Skin_images,"Transparent");
		//textarea.setBounds(1200, 600, 600, 200);
		//foreground.addActor(textarea);
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
				//if (textarea!=null && AssetLoader.Datahandler.cache()!=null)
				//	textarea.setText("Cache :"+String.valueOf(AssetLoader.Datahandler.cache().getNumCache()));
			}
		};
		BlinkTimer.scheduleAtFixedRate(BlinkTask, 0, 1000);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		Gdx.input.setInputProcessor(stage);
		stage.addActor(stack);
		stage.addActor(menu);
		stage.addActor(back);
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
