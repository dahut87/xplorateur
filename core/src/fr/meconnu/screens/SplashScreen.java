package fr.meconnu.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import fr.meconnu.app.Xplorateur;
import fr.meconnu.assets.AssetLoader;

public class SplashScreen implements Screen {
	private Xplorateur app;
	private Stage stage;
	private Image splashImage;
	private float scale;
	private SpriteBatch batcher;

	public SplashScreen(Xplorateur app) {
		AssetLoader.load();
		this.app = app;
		batcher = new SpriteBatch();
		stage = new Stage(AssetLoader.viewport);
		splashImage = new Image(AssetLoader.Texture_logo);
		AssetLoader.loadall();
	}

	@Override
	public void show() {
		Gdx.app.log("xplorateur-LevelScreen", "***** Affichage du SplashScreen");
		scale = 2;
		splashImage.setScale(scale);
		splashImage.setPosition(
				(AssetLoader.width / 2) - (scale * splashImage.getWidth() / 2),
				(AssetLoader.height / 2)
						- (scale * splashImage.getHeight() / 2) + 100);
		stage.addActor(splashImage);
		splashImage.addAction(Actions.sequence(Actions.alpha(0),
				Actions.fadeIn(3f), Actions.run(new Runnable() {
					@Override
					public void run() {
						AssetLoader.finishall();
					}
				}), Actions.run(new Runnable() {
					@Override
					public void run() {
						((Game) Gdx.app.getApplicationListener()).setScreen(new MenuScreen());
					}
				})));
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(255, 255, 255, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act();
		stage.draw();
		if (AssetLoader.manager != null) {
			batcher.begin();
			batcher.setProjectionMatrix(AssetLoader.Camera.combined);
			AssetLoader.empty.draw(batcher, (AssetLoader.width / 2) - 400f, 280f, 800f, 50f);
			AssetLoader.full.draw(batcher, (AssetLoader.width / 2) - 400f, 280f, AssetLoader.manager.getProgress() * 800f, 50f);
			AssetLoader.manager.update();
			batcher.end();
		}
	}

	@Override
	public void resize(int width, int height) {
		AssetLoader.viewport.update(width, height);
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		stage.dispose();
	}
}
